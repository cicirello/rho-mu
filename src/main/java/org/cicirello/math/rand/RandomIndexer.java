/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright (C) 2017-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * This file is part of the rho mu library.
 *
 * The rho mu library is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The rho mu library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the rho mu library.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.cicirello.math.rand;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import org.cicirello.util.ArrayMinimumLengthEnforcer;

/**
 * RandomIndexer is a class of utility methods related to efficiently generating random indexes, and
 * combination of indexes, into arrays. The methods of this class neither directly operate, nor
 * depend, on arrays, and can thus be used wherever you need random integer values. The name of the
 * class is derived from the motivating case, the case of efficiently generating random indexes into
 * an array.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class RandomIndexer {

  /** Class of static utility methods, so instantiation is not allowed. */
  private RandomIndexer() {}

  /**
   * Generates a random integer uniformly distributed in the interval: [0, bound).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads. However, it does not use
   * ThreadLocalRandom.nextInt(int bound) method. Instead, our nextInt(int bound) method is an
   * implementation of the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer
   * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.
   *
   * <p>This method is significantly faster than ThreadLocalRandom.nextInt(int bound).
   *
   * @param bound Upper bound, exclusive, on range of random integers (must be positive).
   * @return a random integer between 0 (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not positive
   */
  public static int nextInt(int bound) {
    return nextInt(bound, ThreadLocalRandom.current());
  }

  /**
   * Generates a random integer uniformly distributed in the interval: [origin, bound).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads. However, it does not use
   * ThreadLocalRandom.nextInt(int origin, int bound) method. Instead, our nextInt(int origin, int
   * bound) method is an implementation of the algorithm proposed in the article: Daniel Lemire,
   * "Fast Random Integer Generation in an Interval," ACM Transactions on Modeling and Computer
   * Simulation, 29(1), 2019.
   *
   * <p>This method is significantly faster than ThreadLocalRandom.nextInt(int bound).
   *
   * @param origin Lower bound, inclusive, on range of random integers.
   * @param bound Upper bound, exclusive, on range of random integers (must be greater than origin).
   * @return a random integer between origin (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not greater than origin
   */
  public static int nextInt(int origin, int bound) {
    return nextInt(origin, bound, ThreadLocalRandom.current());
  }

  /**
   * Generates a random integer uniformly distributed in the interval: [origin, bound).
   *
   * <p>This method uses a RandomGenerator passed as a parameter as the pseudorandom number
   * generator. However, it does not use RandomGenerator.nextInt(int origin, int bound) method.
   * Instead, our nextInt(int origin, int bound) method is an implementation of the algorithm
   * proposed in the article: Daniel Lemire, "Fast Random Integer Generation in an Interval," ACM
   * Transactions on Modeling and Computer Simulation, 29(1), 2019.
   *
   * @param origin Lower bound, inclusive, on range of random integers.
   * @param bound Upper bound, exclusive, on range of random integers (must be greater than origin).
   * @param gen A source of randomness.
   * @return a random integer between origin (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not greater than origin
   */
  public static int nextInt(int origin, int bound, RandomGenerator gen) {
    return origin + nextInt(bound - origin, gen);
  }

  /**
   * Generates a random integer uniformly distributed in the interval: [0, bound).
   *
   * <p>This method uses a RandomGenerator passed as a parameter as the pseudorandom number
   * generator. However, it does not use RandomGenerator.nextInt(int bound) method. Instead, our
   * nextInt(int bound) method is an implementation of the algorithm proposed in the article: Daniel
   * Lemire, "Fast Random Integer Generation in an Interval," ACM Transactions on Modeling and
   * Computer Simulation, 29(1), 2019.
   *
   * @param bound Upper bound, exclusive, on range of random integers (must be positive).
   * @param gen A source of randomness.
   * @return a random integer between 0 (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not positive
   */
  public static int nextInt(int bound, RandomGenerator gen) {
    if (bound < 1) throw new IllegalArgumentException("bound must be positive");
    // Commented out lines handle bound that is a power of 2 as a special case
    // Seems to only marginally speed computation in special case while adding to cost of
    // general case.
    // int b1 = bound - 1;
    // if ((bound & b1) == 0) return gen.nextInt() & b1;
    long product = (long) (gen.nextInt() & 0x7fffffff) * (long) bound;
    int low31 = (int) product & 0x7fffffff;
    if (low31 < bound) {
      int threshold = (0x80000000 - bound) % bound;
      while (low31 < threshold) {
        product = (long) (gen.nextInt() & 0x7fffffff) * (long) bound;
        low31 = (int) product & 0x7fffffff;
      }
    }
    return (int) (product >> 31);
  }

  /**
   * Generates a random integer in the interval: [0, bound).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads. However, it does not use
   * ThreadLocalRandom.nextInt(int bound) method. Instead, our nextBiasedInt(int bound) method
   * computes a random int in the target interval via a multiplication and a shift, rather than the
   * more common mod. This method does not correct for bias via rejection sampling, and thus some
   * values in the interval [0, bound) may be more likely than others. There is no bias for bound
   * values that are powers of 2. Otherwise, the lower the value of bound, the less bias; and the
   * higher the value of bound, the more bias. If your bound is relatively low, and if your
   * application does not require strict uniformity, then this method is significantly faster than
   * any approach that corrects for bias. We started with the algorithm proposed in the article:
   * Daniel Lemire, "Fast Random Integer Generation in an Interval," ACM Transactions on Modeling
   * and Computer Simulation, 29(1), 2019. But we removed from it the rejection sampling portion.
   *
   * @param bound Upper bound, exclusive, on range of random integers (must be positive).
   * @return a random integer between 0 (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not positive
   */
  public static int nextBiasedInt(int bound) {
    if (bound < 1) throw new IllegalArgumentException("bound must be positive");
    return (int)
        (((long) (ThreadLocalRandom.current().nextInt() & 0x7fffffff) * (long) bound) >> 31);
  }

  /**
   * Generates a random integer in the interval: [origin, bound).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads. However, it does not use
   * ThreadLocalRandom.nextInt(int origin, int bound) method. Instead, our nextBiasedInt(int origin,
   * int bound) method computes a random int in the target interval via a multiplication and a
   * shift, rather than the more common mod. This method does not correct for bias via rejection
   * sampling, and thus some values in the interval [origin, bound) may be more likely than others.
   * There is no bias if the width of the interval is a power of 2. Otherwise, the smaller the
   * interval, the less bias; and the larger the interval, the more bias. If your interval is
   * relatively small, and if your application does not require strict uniformity, then this method
   * is significantly faster than any approach that corrects for bias. We started with the algorithm
   * proposed in the article: Daniel Lemire, "Fast Random Integer Generation in an Interval," ACM
   * Transactions on Modeling and Computer Simulation, 29(1), 2019. But we removed from it the
   * rejection sampling portion.
   *
   * @param origin Lower bound, inclusive, on range of random integers.
   * @param bound Upper bound, exclusive, on range of random integers (must be greater than origin).
   * @return a random integer between origin (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not greater than the origin
   */
  public static int nextBiasedInt(int origin, int bound) {
    return origin + nextBiasedInt(bound - origin);
  }

  /**
   * Generates a random integer in the interval: [origin, bound).
   *
   * <p>This method does not use RandomGenerator.nextInt(int origin, int bound) method. Instead, our
   * nextBiasedInt(int origin, int bound) method computes a random int in the target interval via a
   * multiplication and a shift, rather than the more common mod. This method does not correct for
   * bias via rejection sampling, and thus some values in the interval [origin, bound) may be more
   * likely than others. There is no bias if the width of the interval is a power of 2. Otherwise,
   * the smaller the interval, the less bias; and the larger the interval, the more bias. If your
   * interval is relatively small, and if your application does not require strict uniformity, then
   * this method is significantly faster than any approach that corrects for bias. We started with
   * the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer Generation in an
   * Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019. But we removed
   * from it the rejection sampling portion.
   *
   * @param origin Lower bound, inclusive, on range of random integers.
   * @param bound Upper bound, exclusive, on range of random integers (must be greater than origin).
   * @param gen A source of randomness.
   * @return a random integer between origin (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not greater than the origin
   */
  public static int nextBiasedInt(int origin, int bound, RandomGenerator gen) {
    return origin + nextBiasedInt(bound - origin, gen);
  }

  /**
   * Generates a random integer in the interval: [0, bound).
   *
   * <p>This method does not use RandomGenerator.nextInt(int bound) method. Instead, our
   * nextBiasedInt(int bound) method computes a random int in the target interval via a
   * multiplication and a shift, rather than the more common mod. This method does not correct for
   * bias via rejection sampling, and thus some values in the interval [0, bound) may be more likely
   * than others. There is no bias for bound values that are powers of 2. Otherwise, the lower the
   * value of bound, the less bias; and the higher the value of bound, the more bias. If your bound
   * is relatively low, and if your application does not require strict uniformity, then this method
   * is significantly faster than any approach that corrects for bias. We started with the algorithm
   * proposed in the article: Daniel Lemire, "Fast Random Integer Generation in an Interval," ACM
   * Transactions on Modeling and Computer Simulation, 29(1), 2019. But we removed from it the
   * rejection sampling portion.
   *
   * @param bound Upper bound, exclusive, on range of random integers (must be positive).
   * @param gen A source of randomness.
   * @return a random integer between 0 (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not positive
   */
  public static int nextBiasedInt(int bound, RandomGenerator gen) {
    if (bound < 1) throw new IllegalArgumentException("bound must be positive");
    return (int) (((long) (gen.nextInt() & 0x7fffffff) * (long) bound) >> 31);
  }

  /**
   * Generates a random sample of 2 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 2 combinations are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 2.
   */
  public static int[] nextIntPair(int n, int[] result) {
    return nextIntPair(n, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of 2 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 2 combinations are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @param gen Source of randomness.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 2.
   */
  public static int[] nextIntPair(int n, int[] result, RandomGenerator gen) {
    result = ArrayMinimumLengthEnforcer.enforce(result, 2);
    result[0] = nextInt(n, gen);
    result[1] = nextInt(n - 1, gen);
    if (result[1] >= result[0]) {
      result[1]++;
    }
    return result;
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public static int[] nextIntTriple(int n, int[] result) {
    return nextIntTriple(n, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @param sort If true, the result is sorted in increasing order; otherwise it is in arbitrary
   *     order.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public static int[] nextIntTriple(int n, int[] result, boolean sort) {
    return nextIntTriple(n, result, sort, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @param sort If true, the result is sorted in increasing order; otherwise it is in arbitrary
   *     order.
   * @param gen The source of randomness.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public static int[] nextIntTriple(int n, int[] result, boolean sort, RandomGenerator gen) {
    result = ArrayMinimumLengthEnforcer.enforce(result, 3);
    result[0] = nextInt(n, gen);
    result[1] = nextInt(n - 1, gen);
    result[2] = nextInt(n - 2, gen);
    if (sort) adjustSortTriple(result);
    else adjustTriple(result);
    return result;
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @param gen Source of randomness.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public static int[] nextIntTriple(int n, int[] result, RandomGenerator gen) {
    result = ArrayMinimumLengthEnforcer.enforce(result, 3);
    result[0] = nextInt(n, gen);
    result[1] = nextInt(n - 1, gen);
    result[2] = nextInt(n - 2, gen);
    adjustTriple(result);
    return result;
  }

  /**
   * Generates an "array mask" of a specified length, where an "array mask" is an array of boolean
   * values of the same length as another array. Each position in the result is equally likely true
   * or false.
   *
   * <p>Runtime: O(n).
   *
   * <p>This method uses ThreadLocalRandom as the source of randomness, and is thus safe, and
   * efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The length of the array mask.
   * @return An array of n randomly generated boolean values.
   */
  public static boolean[] arrayMask(int n) {
    return arrayMask(n, ThreadLocalRandom.current());
  }

  /**
   * Generates an "array mask" of a specified length, where an "array mask" is an array of boolean
   * values of the same length as another array. Each position in the result is equally likely true
   * or false.
   *
   * <p>Runtime: O(n).
   *
   * @param n The length of the array mask.
   * @param gen The source of randomness.
   * @return An array of n randomly generated boolean values.
   */
  public static boolean[] arrayMask(int n, RandomGenerator gen) {
    boolean[] result = new boolean[n];
    for (int i = 0; i < n; i++) {
      result[i] = gen.nextBoolean();
    }
    return result;
  }

  /**
   * Generates an "array mask" of a specified length and specified number of true values, where an
   * "array mask" is an array of boolean values of the same length as another array.
   *
   * <p>Runtime: O(min(n, k<sup>2</sup>)), and it uses O(min(k, n-k)) random numbers.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The length of the array mask.
   * @param k The desired number of true values, which must be no greater than n.
   * @return An array of n boolean values, exactly k of which are equal to true.
   */
  public static boolean[] arrayMask(int n, int k) {
    return arrayMask(n, k, ThreadLocalRandom.current());
  }

  /**
   * Generates an "array mask" of a specified length and specified number of true values, where an
   * "array mask" is an array of boolean values of the same length as another array.
   *
   * <p>Runtime: O(min(n, k<sup>2</sup>)), and it uses O(min(k, n-k)) random numbers.
   *
   * @param n The length of the array mask.
   * @param k The desired number of true values, which must be no greater than n.
   * @param gen The source of randomness.
   * @return An array of n boolean values, exactly k of which are equal to true.
   */
  public static boolean[] arrayMask(int n, int k, RandomGenerator gen) {
    boolean[] result = new boolean[n];
    if (k >= n) {
      Arrays.fill(result, true);
    } else {
      int[] indexes = RandomSampler.sample(n, k, null, gen);
      for (int i = 0; i < k; i++) {
        result[indexes[i]] = true;
      }
    }
    return result;
  }

  /**
   * Generates an "array mask" of a specified length, where an "array mask" is an array of boolean
   * values of the same length as another array.
   *
   * <p>Runtime: O(n), and it uses O(n) random doubles.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The length of the array mask.
   * @param p The probability that an element of the result is true.
   * @return An array of n boolean values, such that each element is true with probability p.
   */
  public static boolean[] arrayMask(int n, double p) {
    return arrayMask(n, p, ThreadLocalRandom.current());
  }

  /**
   * Generates an "array mask" of a specified length, where an "array mask" is an array of boolean
   * values of the same length as another array.
   *
   * <p>Runtime: O(n), and it uses O(n) random doubles.
   *
   * @param n The length of the array mask.
   * @param p The probability that an element of the result is true.
   * @param gen The source of randomness.
   * @return An array of n boolean values, such that each element is true with probability p.
   */
  public static boolean[] arrayMask(int n, double p, RandomGenerator gen) {
    boolean[] result = new boolean[n];
    if (p >= 1) {
      Arrays.fill(result, true);
    } else {
      int[] s = RandomSampler.sample(n, p, gen);
      for (int i = 0; i < s.length; i++) {
        result[s[i]] = true;
      }
    }
    return result;
  }

  /**
   * Generates a random sample of 2 integers, i, j, without replacement, from the set of integers in
   * the interval [0, n), such that |i-j| &le; window. All pairs that satisfy the window constraint
   * are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the pair.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @return An array containing the pair of randomly chosen integers, i, j, from the interval [0,
   *     n), such that |i-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 1 or n &lt; 2.
   */
  public static int[] nextWindowedIntPair(int n, int window, int[] result) {
    return nextWindowedIntPair(n, window, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of 2 integers, i, j, without replacement, from the set of integers in
   * the interval [0, n), such that |i-j| &le; window. All pairs that satisfy the window constraint
   * are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the pair.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @param gen Source of randomness.
   * @return An array containing the pair of randomly chosen integers, i, j, from the interval [0,
   *     n), such that |i-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 1 or n &lt; 2.
   */
  public static int[] nextWindowedIntPair(int n, int window, int[] result, RandomGenerator gen) {
    if (window >= n - 1) return nextIntPair(n, result, gen);
    result = ArrayMinimumLengthEnforcer.enforce(result, 2);
    final int z1 = n - window;
    final int z2 = z1 + z1;
    int i = nextInt(z2 + window - 1, gen);
    int j = nextInt(window, gen);
    setAndAdjustWindowedPair(result, i, j, z1, z2);
    return result;
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @return An array containing the triple of randomly chosen integers, i, j, k from the interval
   *     [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public static int[] nextWindowedIntTriple(int n, int window, int[] result) {
    return nextWindowedIntTriple(n, window, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @param sort If true, the result is sorted in increasing order, otherwise it is in random order.
   * @return An array containing the triple of randomly chosen integers, i, j, k from the interval
   *     [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public static int[] nextWindowedIntTriple(int n, int window, int[] result, boolean sort) {
    return nextWindowedIntTriple(n, window, result, sort, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @param gen The source of randomness.
   * @return An array containing the triple of randomly chosen integers, i, j, k from the interval
   *     [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public static int[] nextWindowedIntTriple(int n, int window, int[] result, RandomGenerator gen) {
    if (window >= n - 1) return nextIntTriple(n, result, gen);
    result = ArrayMinimumLengthEnforcer.enforce(result, 3);
    final int z1 = n - window;
    final int z3 = 3 * z1;
    int i = nextInt(z3 + window - 2, gen);
    int j = nextInt(window, gen);
    int k = nextInt(window - 1, gen);
    setAndAdjustWindowedTriple(result, i, j, k, z1, z3);
    return result;
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely.
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @param sort If true, the result is sorted in increasing order, otherwise it is in random order.
   * @param gen The source of randomness.
   * @return An array containing the triple of randomly chosen integers, i, j, k from the interval
   *     [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public static int[] nextWindowedIntTriple(
      int n, int window, int[] result, boolean sort, RandomGenerator gen) {
    if (window >= n - 1) return nextIntTriple(n, result, sort, gen);
    result = ArrayMinimumLengthEnforcer.enforce(result, 3);
    final int z1 = n - window;
    final int z3 = 3 * z1;
    int i = nextInt(z3 + window - 2, gen);
    int j = nextInt(window, gen);
    int k = nextInt(window - 1, gen);
    if (sort) sortSetAndAdjustWindowedTriple(result, i, j, k, z1, z3);
    else setAndAdjustWindowedTriple(result, i, j, k, z1, z3);
    return result;
  }

  private static boolean adjustIfNecessary(int[] result, int upper, int lower) {
    if (result[upper] >= result[lower]) {
      result[upper]++;
      return true;
    }
    return false;
  }

  private static void adjustTriple(int[] result) {
    if (adjustIfNecessary(result, 1, 0)) {
      adjustIfNecessary(result, 2, 0);
      adjustIfNecessary(result, 2, 1);
    } else {
      adjustIfNecessary(result, 2, 1);
      adjustIfNecessary(result, 2, 0);
    }
  }

  private static void adjustSortTriple(int[] result) {
    if (!adjustIfNecessary(result, 1, 0)) {
      int temp = result[0];
      result[0] = result[1];
      result[1] = temp;
    }
    if (adjustIfNecessary(result, 2, 0)) {
      if (!adjustIfNecessary(result, 2, 1)) {
        int temp = result[1];
        result[1] = result[2];
        result[2] = temp;
      }
    } else {
      int temp = result[2];
      result[2] = result[1];
      result[1] = result[0];
      result[0] = temp;
    }
  }

  private static void setAndAdjustWindowedPair(
      int[] result, int i, int j, final int z1, final int z2) {
    if (i < z2) {
      int x = i & 1;
      result[x] = i >> 1;
      result[x ^ 1] = result[x] + 1 + j;
    } else {
      i -= z1;
      j += z1;
      result[0] = i >= j ? i + 1 : i;
      result[1] = j;
    }
  }

  private static int iAdjustmentWindowedTriple(int i, int lower, int higher) {
    if (i >= lower) {
      i++;
      if (i >= higher) {
        i++;
      }
    }
    return i;
  }

  private static void setAndAdjustWindowedTriple(
      int[] result, int i, int j, int k, final int z1, final int z3) {
    if (k >= j) {
      k++;
    }
    if (i < z3) {
      int q = i / 3;
      int r = i % 3;
      result[r] = q;
      if (r < 2) {
        result[r ^ 1] = q + 1 + j;
        result[2] = q + 1 + k;
      } else {
        result[0] = q + 1 + j;
        result[1] = q + 1 + k;
      }
    } else {
      i = i - z3 + z1;
      j += z1;
      k += z1;
      i = j < k ? iAdjustmentWindowedTriple(i, j, k) : iAdjustmentWindowedTriple(i, k, j);
      result[0] = i;
      result[1] = j;
      result[2] = k;
    }
  }

  private static void sortSetAndAdjustWindowedTriple(
      int[] result, int i, int j, int k, final int z1, final int z3) {
    if (k >= j) {
      k++;
    } else {
      int t = j;
      j = k;
      k = t;
    }
    if (i < z3) {
      int q = i / 3;
      result[0] = q;
      result[1] = q + 1 + j;
      result[2] = q + 1 + k;
    } else {
      i = i - z3 + z1;
      j += z1;
      k += z1;
      if (i >= j) {
        i++;
        result[0] = j;
        if (i >= k) {
          i++;
          result[1] = k;
          result[2] = i;
        } else {
          result[1] = i;
          result[2] = k;
        }
      } else {
        result[0] = i;
        result[1] = j;
        result[2] = k;
      }
    }
  }
}
