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

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import org.cicirello.util.ArrayFiller;
import org.cicirello.util.ArrayMinimumLengthEnforcer;

/**
 * RandomSampler is a class of utility methods related to efficiently sampling integers without
 * replacement.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class RandomSampler {

  /** Class of static utility methods, so instantiation is not allowed. */
  private RandomSampler() {}

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>Uses the reservoir sampling algorithm (Algorithm R) from J. Vitter's 1985 article "Random
   * Sampling with a Reservoir" from ACM Transactions on Mathematical Software. The runtime is O(n)
   * and it generates O(n-k) random numbers. Thus, it is an especially good choice as k approaches
   * n. Only constant extra space required.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] sampleReservoir(int n, int k, int[] result) {
    return sampleReservoir(n, k, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>Uses the reservoir sampling algorithm (Algorithm R) from J. Vitter's 1985 article "Random
   * Sampling with a Reservoir" from ACM Transactions on Mathematical Software. The runtime is O(n)
   * and it generates O(n-k) random numbers. Thus, it is an especially good choice as k approaches
   * n. Only constant extra space required.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @param gen Source of randomness.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] sampleReservoir(int n, int k, int[] result, RandomGenerator gen) {
    if (k > n) throw new IllegalArgumentException("k must be no greater than n");
    result = ArrayMinimumLengthEnforcer.enforce(result, k);
    ArrayFiller.fillPartial(result, k);
    for (int i = k; i < n; i++) {
      int j = RandomIndexer.nextInt(i + 1, gen);
      if (j < k) {
        result[j] = i;
      }
    }
    return result;
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>This implements the algorithm SELECT of S. Goodman and S. Hedetniemi, as described in: J
   * Ernvall, O Nevalainen, "An Algorithm for Unbiased Random Sampling," The Computer Journal,
   * 25(1):45-47, 1982.
   *
   * <p>The runtime is O(n) and it generates O(k) random numbers. Thus, it is a better choice than
   * sampleReservoir when k &lt; n-k. However, this uses O(n) extra space, whereas the reservoir
   * algorithm uses no extra space.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] samplePool(int n, int k, int[] result) {
    return samplePool(n, k, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>This implements the algorithm SELECT of S. Goodman and S. Hedetniemi, as described in: J
   * Ernvall, O Nevalainen, "An Algorithm for Unbiased Random Sampling," The Computer Journal,
   * 25(1):45-47, 1982.
   *
   * <p>The runtime is O(n) and it generates O(k) random numbers. Thus, it is a better choice than
   * sampleReservoir when k &lt; n-k. However, this uses O(n) extra space, whereas the reservoir
   * algorithm uses no extra space.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @param gen Source of randomness.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] samplePool(int n, int k, int[] result, RandomGenerator gen) {
    if (k > n) throw new IllegalArgumentException("k must be no greater than n");
    result = ArrayMinimumLengthEnforcer.enforce(result, k);
    int[] pool = ArrayFiller.create(n);
    int remaining = n;
    for (int i = 0; i < k; i++) {
      int temp = RandomIndexer.nextInt(remaining, gen);
      result[i] = pool[temp];
      remaining--;
      pool[temp] = pool[remaining];
    }
    return result;
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>This implements the insertion sampling algorithm described in:
   *
   * <p>Vincent A. Cicirello. 2022. <a
   * href="https://www.cicirello.org/publications/applsci-12-05506.pdf">Cycle Mutation: Evolving
   * Permutations via Cycle Induction</a>, <i>Applied Sciences</i>, 12(11), Article 5506 (June
   * 2022). doi:<a href="https://doi.org/10.3390/app12115506">10.3390/app12115506</a>
   *
   * <p>The runtime is O(k<sup>2</sup>) and it generates O(k) random numbers. Thus, it is a better
   * choice than both sampleReservoir and samplePool when k<sup>2</sup> &lt; n. Just like
   * sampleReservoir, the sampleInsertion method only requires O(1) extra space, while samplePool
   * requires O(n) extra space.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] sampleInsertion(int n, int k, int[] result) {
    return sampleInsertion(n, k, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>This implements the insertion sampling algorithm described in:
   *
   * <p>Vincent A. Cicirello. 2022. <a
   * href="https://www.cicirello.org/publications/applsci-12-05506.pdf">Cycle Mutation: Evolving
   * Permutations via Cycle Induction</a>, <i>Applied Sciences</i>, 12(11), Article 5506 (June
   * 2022). doi:<a href="https://doi.org/10.3390/app12115506">10.3390/app12115506</a>
   *
   * <p>The runtime is O(k<sup>2</sup>) and it generates O(k) random numbers. Thus, it is a better
   * choice than both sampleReservoir and samplePool when k<sup>2</sup> &lt; n. Just like
   * sampleReservoir, the sampleInsertion method only requires O(1) extra space, while samplePool
   * requires O(n) extra space.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @param gen The source of randomness.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] sampleInsertion(int n, int k, int[] result, RandomGenerator gen) {
    if (k > n) throw new IllegalArgumentException("k must be no greater than n");
    result = ArrayMinimumLengthEnforcer.enforce(result, k);
    for (int i = 0; i < k; i++) {
      int temp = RandomIndexer.nextInt(n - i, gen);
      int j = k - i;
      for (; j < k; j++) {
        if (temp >= result[j]) {
          temp++;
          result[j - 1] = result[j];
        } else break;
      }
      result[j - 1] = temp;
    }
    return result;
  }

  /**
   * Generates a random sample, without replacement, from the set of integers in the interval [0,
   * n). Each of the n integers has a probability p of inclusion in the sample.
   *
   * <p>This method uses ThreadLocalRandom as the source of randomness, and is thus safe, and
   * efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param p The probability that each of the n integers is included in the sample.
   * @return An array containing the sample.
   */
  public static int[] sample(int n, double p) {
    return sample(n, p, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample, without replacement, from the set of integers in the interval [0,
   * n). Each of the n integers has a probability p of inclusion in the sample.
   *
   * @param n The number of integers to choose from.
   * @param p The probability that each of the n integers is included in the sample.
   * @param r The source of randomness.
   * @return An array containing the sample.
   */
  public static int[] sample(int n, double p, RandomGenerator r) {
    if (p <= 0) {
      return new int[0];
    } else if (p >= 1) {
      return ArrayFiller.create(n);
    } else {
      return sample(n, RandomVariates.nextBinomial(n, p, r), null, r);
    }
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>This method chooses among the RandomSampler.samplePool, RandomSampler.sampleReservoir, and
   * RandomSampler.sampleInsertion methods based on the values of n and k.
   *
   * <p>This approach combining reservoir sampling, pool sampling, and insertion sampling was
   * described in: Vincent A. Cicirello. 2022. <a
   * href="https://www.cicirello.org/publications/applsci-12-05506.pdf">Cycle Mutation: Evolving
   * Permutations via Cycle Induction</a>, <i>Applied Sciences</i>, 12(11), Article 5506 (June
   * 2022). doi:<a href="https://doi.org/10.3390/app12115506">10.3390/app12115506</a>
   *
   * <p>The runtime is O(min(n, k<sup>2</sup>)) and it generates O(min(k, n-k)) random numbers.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] sample(int n, int k, int[] result) {
    if (k + k < n) {
      if (k * k < n) return sampleInsertion(n, k, result, ThreadLocalRandom.current());
      else return samplePool(n, k, result, ThreadLocalRandom.current());
    } else return sampleReservoir(n, k, result, ThreadLocalRandom.current());
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely.
   *
   * <p>This method chooses among the RandomSampler.samplePool, RandomSampler.sampleReservoir, and
   * RandomSampler.sampleInsertion methods based on the values of n and k.
   *
   * <p>This approach combining reservoir sampling, pool sampling, and insertion sampling was
   * described in: Vincent A. Cicirello. 2022. <a
   * href="https://www.cicirello.org/publications/applsci-12-05506.pdf">Cycle Mutation: Evolving
   * Permutations via Cycle Induction</a>, <i>Applied Sciences</i>, 12(11), Article 5506 (June
   * 2022). doi:<a href="https://doi.org/10.3390/app12115506">10.3390/app12115506</a>
   *
   * <p>The runtime is O(min(n, k<sup>2</sup>)) and it generates O(min(k, n-k)) random numbers.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @param gen Source of randomness.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public static int[] sample(int n, int k, int[] result, RandomGenerator gen) {
    if (k + k < n) {
      if (k * k < n) return sampleInsertion(n, k, result, gen);
      else return samplePool(n, k, result, gen);
    } else return sampleReservoir(n, k, result, gen);
  }
}
