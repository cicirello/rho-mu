/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright (C) 2017-2024 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

import java.util.List;
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * An EnhancedRandomGenerator is used to wrap an object of any class that implements {@link
 * RandomGenerator} for the purpose of adding all of the functionality of the {@link RandomIndexer},
 * {@link RandomSampler}, and {@link RandomVariates}. In this way, the EnhancedRandomGenerator can
 * be used as a drop-in replacement for any of Java's random number generators, while substituting
 * more efficient algorithms in some cases, or adding functionality in others. Methods of the {@link
 * RandomGenerator} without equivalent replacements in one of {@link RandomIndexer}, {@link
 * RandomSampler}, or {@link RandomVariates} are simply delegated to the wrapped {@link
 * RandomGenerator}.
 *
 * <p>Enhanced Functionality provided by this class includes:
 *
 * <ul>
 *   <li>Faster generation of random int values subject to a bound or bound and origin.
 *   <li>Faster generation of random int values within an IntStream subject to a bound and origin.
 *   <li>Faster generation of Gaussian distributed random doubles.
 *   <li>Additional distributions available beyond what is supported by the Java API's
 *       RandomGenerator classes, such as Binomial and Cauchy random vaiables.
 *   <li>Ultrafast, but biased, nextBiasedInt methods that sacrifices uniformity for speed by
 *       excluding the rejection sampling necessary to ensure uniformity, as well as a biasedInts
 *       methods for generating streams of such integers.
 *   <li>Methods for generating random pairs of integers without replacement, and random triples of
 *       integers without replacement.
 *   <li>Methods for generating random samples of k integers without replacement from a range of
 *       integers.
 *   <li>Methods to generate streams of numbers from distributions other than uniform, such as
 *       streams of random numbers from binomial distributions, Cauchy distributions, exponential
 *       distributions, and Gaussian distributions.
 *   <li>Methods to generate streams of pairs of distinct integers, and streams of triples of
 *       distinct integers.
 *   <li>Methods for shuffling the elements of arrays.
 * </ul>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class EnhancedRandomGenerator implements RandomGenerator {

  private final RandomGenerator generator;

  private Binomial binomial;

  /**
   * Constructs the EnhancedRandomGenerator to wrap an instance of the default random number
   * generator as obtained via a call to {@link RandomGenerator#getDefault}.
   */
  public EnhancedRandomGenerator() {
    this(RandomGenerator.getDefault());
  }

  /**
   * Constructs the EnhancedRandomGenerator to wrap an instance of a random number generator
   * initialized with a specified seed to enable replicating the same sequence of random numbers
   * during subsequent runs.
   *
   * @param seed The seed for the random number generator.
   */
  public EnhancedRandomGenerator(long seed) {
    this(new SplittableRandom(seed));
  }

  /**
   * Constructs the EnhancedRandomGenerator from the RandomGenerator to wrap.
   *
   * @param generator The RandomGenerator to wrap, which serves as the source of randomness.
   */
  public EnhancedRandomGenerator(RandomGenerator generator) {
    this.generator = generator;
  }

  /**
   * Constructs the EnhancedRandomGenerator to wrap an instance of any random number generator
   * supported by your version of Java as specified by its name, as documented via the {@link
   * RandomGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator#of} method.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public EnhancedRandomGenerator(String algorithmName) {
    this(RandomGenerator.of(algorithmName));
  }

  /**
   * Gets an EnhancedRandomGenerator wrapping an instance of the default random number generator as
   * obtained via a call to {@link RandomGenerator#getDefault}.
   *
   * @return an EnhancedRandomGenerator wrapping an instance of the default random number generator
   */
  public static EnhancedRandomGenerator getDefault() {
    return new EnhancedRandomGenerator();
  }

  /**
   * Gets an EnhancedRandomGenerator wrapping an instance of any random number generator supported
   * by your version of Java as specified by its name, as documented via the {@link
   * RandomGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator#of} method.
   * @return an EnhancedRandomGenerator wrapping an instance of your chosen random number generator.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public static EnhancedRandomGenerator of(String algorithmName) {
    return new EnhancedRandomGenerator(algorithmName);
  }

  // METHODS THAT ADD FUNCTIONALITY:

  /**
   * Generates an "array mask" of a specified length, where an "array mask" is an array of boolean
   * values of the same length as another array. Each position in the result is equally likely true
   * or false. <b>Enhanced Functionality.</b>
   *
   * <p>Runtime: O(n).
   *
   * @param n The length of the array mask.
   * @return An array of n randomly generated boolean values.
   */
  public final boolean[] arrayMask(int n) {
    return RandomIndexer.arrayMask(n, generator);
  }

  /**
   * Generates an "array mask" of a specified length and specified number of true values, where an
   * "array mask" is an array of boolean values of the same length as another array. <b>Enhanced
   * Functionality.</b>
   *
   * <p>Runtime: O(min(n, k<sup>2</sup>)), and it uses O(min(k, n-k)) random numbers.
   *
   * @param n The length of the array mask.
   * @param k The desired number of true values, which must be no greater than n.
   * @return An array of n boolean values, exactly k of which are equal to true.
   */
  public final boolean[] arrayMask(int n, int k) {
    return RandomIndexer.arrayMask(n, k, generator);
  }

  /**
   * Generates an "array mask" of a specified length, where an "array mask" is an array of boolean
   * values of the same length as another array. <b>Enhanced Functionality.</b>
   *
   * <p>Runtime: O(n), and it uses O(n) random doubles.
   *
   * @param n The length of the array mask.
   * @param p The probability that an element of the result is true.
   * @return An array of n boolean values, such that each element is true with probability p.
   */
  public final boolean[] arrayMask(int n, double p) {
    return RandomIndexer.arrayMask(n, nextBinomial(n, p), generator);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom int values, each value random from the
   * interval [randomNumberOrigin, randomNumberBound).
   *
   * <p><b>Enhanced Functionality:</b> Each int produced by the stream is generated by an
   * implementation of a variation of the algorithm proposed in the article: Daniel Lemire, "Fast
   * Random Integer Generation in an Interval," ACM Transactions on Modeling and Computer
   * Simulation, 29(1), 2019. The difference between this implementation and that algorithm is that
   * we have removed the rejection sampling to create an ultrafast, but not strictly uniform, stream
   * of random integers. If you require strict uniformity, then use the {@link #ints(int, int)}
   * method instead.
   *
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound)
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return an effectively unlimited stream of pseudorandom int values, random from the interval
   *     [randomNumberOrigin, randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound
   */
  public final IntStream biasedInts(int randomNumberOrigin, int randomNumberBound) {
    return IntStream.generate(() -> nextBiasedInt(randomNumberOrigin, randomNumberBound))
        .sequential();
  }

  /**
   * Returns a stream of pseudorandom int values, each value random from the interval
   * [randomNumberOrigin, randomNumberBound).
   *
   * <p><b>Enhanced Functionality:</b> Each int produced by the stream is generated by an
   * implementation of a variation of the algorithm proposed in the article: Daniel Lemire, "Fast
   * Random Integer Generation in an Interval," ACM Transactions on Modeling and Computer
   * Simulation, 29(1), 2019. The difference between this implementation and that algorithm is that
   * we have removed the rejection sampling to create an ultrafast, but not strictly uniform, stream
   * of random integers. If you require strict uniformity, then use the {@link #ints(long, int,
   * int)} method instead.
   *
   * @param streamSize The number of values in the stream.
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound).
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return a stream of pseudorandom int values, random from the interval [randomNumberOrigin,
   *     randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound, or if streamSize is negative.
   */
  public final IntStream biasedInts(
      long streamSize, int randomNumberOrigin, int randomNumberBound) {
    return IntStream.generate(() -> nextBiasedInt(randomNumberOrigin, randomNumberBound))
        .sequential()
        .limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom int values, each value generated from a
   * binomial distribution. <b>Enhanced Functionality.</b>
   *
   * @param n Number of trials for the binomial distribution.
   * @param p The probability of a successful trial.
   * @return an effectively unlimited stream of pseudorandom int values generated from a binomial
   *     distribution.
   */
  public final IntStream binomials(int n, double p) {
    return IntStream.generate(() -> nextBinomial(n, p)).sequential();
  }

  /**
   * Returns a stream of pseudorandom int values, each value generated from a binomial distribution.
   * <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n Number of trials for the binomial distribution.
   * @param p The probability of a successful trial.
   * @return a stream of pseudorandom int values generated from a binomial distribution.
   */
  public final IntStream binomials(long streamSize, int n, double p) {
    return IntStream.generate(() -> nextBinomial(n, p)).sequential().limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom double values from a Cauchy
   * distribution. <b>Enhanced Functionality.</b>
   *
   * @param median The median of the Cauchy distribution.
   * @param scale The scale of the Cauchy distribution.
   * @return an effectively unlimited stream of pseudorandom double values from a Cauchy
   *     distribution.
   */
  public final DoubleStream cauchys(double median, double scale) {
    return DoubleStream.generate(() -> nextCauchy(median, scale)).sequential();
  }

  /**
   * Returns a stream of pseudorandom double values from a Cauchy distribution. <b>Enhanced
   * Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param median The median of the Cauchy distribution.
   * @param scale The scale of the Cauchy distribution.
   * @return a stream of pseudorandom double values from a Cauchy distribution.
   */
  public final DoubleStream cauchys(long streamSize, double median, double scale) {
    return DoubleStream.generate(() -> nextCauchy(median, scale)).sequential().limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom non-negative double values from an
   * exponential distribution with mean 1. <b>Enhanced Functionality.</b>
   *
   * @return an effectively unlimited stream of pseudorandom double values from an exponential
   *     distribution with mean 1.
   */
  public final DoubleStream exponentials() {
    return DoubleStream.generate(() -> nextExponential()).sequential();
  }

  /**
   * Returns a stream of pseudorandom non-negative double values from an exponential distribution
   * with mean 1. <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @return a stream of pseudorandom double values from an exponential distribution with mean 1.
   */
  public final DoubleStream exponentials(long streamSize) {
    return DoubleStream.generate(() -> nextExponential()).sequential().limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom double values from a Gaussian
   * distribution with mean 0 and standard deviation 1. <b>Enhanced Functionality.</b>
   *
   * @return an effectively unlimited stream of pseudorandom double values from a Gaussian
   *     distribution with mean 0 and standard deviation 1.
   */
  public final DoubleStream gaussians() {
    return DoubleStream.generate(() -> nextGaussian()).sequential();
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom double values from a Gaussian
   * distribution with specified mean and standard deviation. <b>Enhanced Functionality.</b>
   *
   * @param mean The mean of the Gaussian.
   * @param stdev The standard deviation of the Gaussian.
   * @return an effectively unlimited stream of pseudorandom double values from a Gaussian
   *     distribution with specified mean and standard deviation.
   */
  public final DoubleStream gaussians(double mean, double stdev) {
    return DoubleStream.generate(() -> nextGaussian(mean, stdev)).sequential();
  }

  /**
   * Returns a stream of pseudorandom double values from a Gaussian distribution with specified mean
   * and standard deviation. <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param mean The mean of the Gaussian.
   * @param stdev The standard deviation of the Gaussian.
   * @return a stream of pseudorandom double values from a Gaussian distribution with specified mean
   *     and standard deviation.
   */
  public final DoubleStream gaussians(long streamSize, double mean, double stdev) {
    return DoubleStream.generate(() -> nextGaussian(mean, stdev)).sequential().limit(streamSize);
  }

  /**
   * Returns a stream of pseudorandom non-negative double values from a Gaussian distribution with
   * mean 0 and standard deviation 1. <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @return a stream of pseudorandom double values from a Gaussian distribution with mean 0 and
   *     standard deviation 1.
   */
  public final DoubleStream gaussians(long streamSize) {
    return DoubleStream.generate(() -> nextGaussian()).sequential().limit(streamSize);
  }

  /**
   * Generates a random integer in the interval: [0, bound). <b>Enhanced Functionality.</b>
   *
   * <p>The nextBiasedInt(bound) method computes a random int in the target interval but faster than
   * nextInt(bound). It does not correct for bias via rejection sampling, and thus some values in
   * the interval [0, bound) may be more likely than others. There is no bias for bound values that
   * are powers of 2. Otherwise, the lower the value of bound, the less bias; and the higher the
   * value of bound, the more bias. If your bound is relatively low, and if your application does
   * not require strict uniformity, then this method is significantly faster than any approach that
   * corrects for bias. We started with the algorithm proposed in the article: Daniel Lemire, "Fast
   * Random Integer Generation in an Interval," ACM Transactions on Modeling and Computer
   * Simulation, 29(1), 2019. But we removed from it the rejection sampling portion.
   *
   * @param bound Upper bound, exclusive, on range of random integers (must be positive).
   * @return a random integer between 0 (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not positive
   */
  public final int nextBiasedInt(int bound) {
    return RandomIndexer.nextBiasedInt(bound, generator);
  }

  /**
   * Generates a random integer in the interval: [origin, bound). <b>Enhanced Functionality.</b>
   *
   * <p>The nextBiasedInt(origin, bound) method computes a random int in the target interval but
   * faster than nextInt(origin, bound). It does not correct for bias via rejection sampling, and
   * thus some values in the interval [origin, bound) may be more likely than others. There is no
   * bias interval width is a power of 2. Otherwise, the smaller the interval, the less bias; and
   * the larger the interval, the more bias. If your interval is relatively low, and if your
   * application does not require strict uniformity, then this method is significantly faster than
   * any approach that corrects for bias. We started with the algorithm proposed in the article:
   * Daniel Lemire, "Fast Random Integer Generation in an Interval," ACM Transactions on Modeling
   * and Computer Simulation, 29(1), 2019. But we removed from it the rejection sampling portion.
   *
   * @param origin Lower bound, inclusive, on range of random integers.
   * @param bound Upper bound, exclusive, on range of random integers (must be greater than origin).
   * @return a random integer between origin (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not greater than origin
   */
  public final int nextBiasedInt(int origin, int bound) {
    return RandomIndexer.nextBiasedInt(origin, bound, generator);
  }

  /**
   * Generates a pseudorandom integer from a binomial distribution. <b>Enhanced Functionality.</b>
   *
   * @param n Number of trials for the binomial distribution.
   * @param p The probability of a successful trial.
   * @return A pseudorandom integer from a binomial distribution.
   */
  public final int nextBinomial(int n, double p) {
    if (binomial == null || !binomial.consistentWith(n, p)) {
      binomial = Binomial.createInstance(n, p);
    }
    return binomial.next(generator);
  }

  /**
   * Generates a pseudorandom number from a Cauchy distribution with median 0 and chosen scale
   * parameter. <b>Enhanced Functionality.</b>
   *
   * @param scale The scale parameter of the Cauchy.
   * @return a pseudorandom number from a Cauchy distribution
   */
  public final double nextCauchy(double scale) {
    return RandomVariates.nextCauchy(scale, generator);
  }

  /**
   * Generates a pseudorandom number from a Cauchy distribution. <b>Enhanced Functionality.</b>
   *
   * @param median The median of the Cauchy.
   * @param scale The scale parameter of the Cauchy.
   * @return a pseudorandom number from a Cauchy distribution
   */
  public final double nextCauchy(double median, double scale) {
    return RandomVariates.nextCauchy(median, scale, generator);
  }

  /**
   * Generates a random number from a Gaussian distribution with mean 0 and standard deviation,
   * stddev, of your choosing.
   *
   * <p><b>Enhanced Functionality:</b> This method uses the library's current most-efficient
   * algorithm for Gaussian random number generation, which may change in future releases. If you
   * require a guarantee of the algorithm used, then see the API for the classes that implement
   * specific Gaussian algorithms.
   *
   * @param stddev The standard deviation of the Gaussian.
   * @return A random number from a Gaussian distribution with mean 0 and standard deviation stddev.
   */
  public final double nextGaussian(double stddev) {
    return RandomVariates.nextGaussian(stddev, generator);
  }

  /**
   * Generates a random sample of 2 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 2 combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 2.
   */
  public final int[] nextIntPair(int n, int[] result) {
    return RandomIndexer.nextIntPair(n, result, generator);
  }

  /**
   * Generates a random sample of 2 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 2 combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @return A pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 2.
   */
  public final IndexPair nextIntPair(int n) {
    return RandomIndexer.nextIntPair(n, generator);
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the triple that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @return An array containing the triple of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public final int[] nextIntTriple(int n, int[] result) {
    return RandomIndexer.nextIntTriple(n, result, generator);
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @return A triple of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public final IndexTriple nextIntTriple(int n) {
    return RandomIndexer.nextIntTriple(n, generator);
  }

  /**
   * Generates a random sample of 2 integers, without replacement, from the set of integers in the
   * interval [0, n). The result is sorted with the minimum first followed by the maximum. All n
   * choose 2 combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @return An array containing the pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 2.
   */
  public final int[] nextSortedIntPair(int n, int[] result) {
    return RandomIndexer.nextSortedIntPair(n, result, generator);
  }

  /**
   * Generates a random sample of 2 integers (i, j) without replacement, from the set of integers in
   * the interval [0, n). The pair is sorted such that i is the minimum and j is the maximum. All n
   * choose 2 combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @return A pair of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 2.
   */
  public final IndexPair nextSortedIntPair(int n) {
    return RandomIndexer.nextSortedIntPair(n, generator);
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely. The result is sorted in
   * increasing order. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param result An array to hold the triple that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @return An array containing the triple of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public final int[] nextSortedIntTriple(int n, int[] result) {
    return RandomIndexer.nextSortedIntTriple(n, result, generator);
  }

  /**
   * Generates a random sample of 3 integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose 3 combinations are equally likely. The result is sorted in
   * increasing order. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @return A triple of randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if n &lt; 3.
   */
  public final IndexTriple nextSortedIntTriple(int n) {
    return RandomIndexer.nextSortedIntTriple(n, generator);
  }

  /**
   * Generates a random sample of 2 integers, i, j, without replacement, from the set of integers in
   * the interval [0, n), such that |i-j| &le; window, and sorted such that i is less than j. All
   * pairs that satisfy the window constraint are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the pair.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @return An array containing the pair of randomly chosen integers, i, j, from the interval [0,
   *     n), such that |i-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 1 or n &lt; 2.
   */
  public final int[] nextSortedWindowedIntPair(int n, int window, int[] result) {
    return RandomIndexer.nextSortedWindowedIntPair(n, window, result, generator);
  }

  /**
   * Generates a random sample of 2 integers, i, j, without replacement, from the set of integers in
   * the interval [0, n), such that |i-j| &le; window, and sorted such that i is less than j. All
   * pairs that satisfy the window constraint are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the pair.
   * @return A pair of randomly chosen integers, i, j, from the interval [0, n), such that |i-j|
   *     &le; window.
   * @throws IllegalArgumentException if window &lt; 1 or n &lt; 2.
   */
  public final IndexPair nextSortedWindowedIntPair(int n, int window) {
    return RandomIndexer.nextSortedWindowedIntPair(n, window, generator);
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely. The result is sorted
   * in increasing order. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @param result An array to hold the triple that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @return An array containing the triple of randomly chosen integers, i, j, k from the interval
   *     [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public final int[] nextSortedWindowedIntTriple(int n, int window, int[] result) {
    return RandomIndexer.nextSortedWindowedIntTriple(n, window, result, generator);
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely. The result is sorted
   * in increasing order. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @return A triple of randomly chosen integers, i, j, k from the interval [0, n), such that |i-j|
   *     &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public final IndexTriple nextSortedWindowedIntTriple(int n, int window) {
    return RandomIndexer.nextSortedWindowedIntTriple(n, window, generator);
  }

  /**
   * Generates a random sample of 2 integers, i, j, without replacement, from the set of integers in
   * the interval [0, n), such that |i-j| &le; window. All pairs that satisfy the window constraint
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the pair.
   * @param result An array to hold the pair that is generated. If result is null or if
   *     result.length is less than 2, then this method will construct an array for the result.
   * @return An array containing the pair of randomly chosen integers, i, j, from the interval [0,
   *     n), such that |i-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 1 or n &lt; 2.
   */
  public final int[] nextWindowedIntPair(int n, int window, int[] result) {
    return RandomIndexer.nextWindowedIntPair(n, window, result, generator);
  }

  /**
   * Generates a random sample of 2 integers, i, j, without replacement, from the set of integers in
   * the interval [0, n), such that |i-j| &le; window. All pairs that satisfy the window constraint
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the pair.
   * @return A pair of randomly chosen integers, i, j, from the interval [0, n), such that |i-j|
   *     &le; window.
   * @throws IllegalArgumentException if window &lt; 1 or n &lt; 2.
   */
  public final IndexPair nextWindowedIntPair(int n, int window) {
    return RandomIndexer.nextWindowedIntPair(n, window, generator);
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely. <b>Enhanced
   * Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @param result An array to hold the triple that is generated. If result is null or if
   *     result.length is less than 3, then this method will construct an array for the result.
   * @return An array containing the triple of randomly chosen integers, i, j, k from the interval
   *     [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public final int[] nextWindowedIntTriple(int n, int window, int[] result) {
    return RandomIndexer.nextWindowedIntTriple(n, window, result, generator);
  }

  /**
   * Generates a random sample of 3 integers, i, j, k without replacement, from the set of integers
   * in the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le;
   * window. All triples that satisfy the window constraint are equally likely. <b>Enhanced
   * Functionality.</b>
   *
   * <p>The runtime is O(1).
   *
   * @param n The number of integers to choose from.
   * @param window The maximum difference between the integers of the triple.
   * @return A triple of randomly chosen integers, i, j, k from the interval [0, n), such that |i-j|
   *     &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
   */
  public final IndexTriple nextWindowedIntTriple(int n, int window) {
    return RandomIndexer.nextWindowedIntTriple(n, window, generator);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom pairs of int values, without
   * replacement, from the interval [0, n). <b>Enhanced Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @return an effectively unlimited stream of pseudorandom pairs of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexPair> pairs(int n) {
    return Stream.generate(() -> nextIntPair(n)).sequential();
  }

  /**
   * Returns a stream of pseudorandom pairs of int values, without replacement, from the interval
   * [0, n). <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @return a stream of pseudorandom pairs of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexPair> pairs(long streamSize, int n) {
    return Stream.generate(() -> nextIntPair(n)).sequential().limit(streamSize);
  }

  /**
   * Generates a random sample, without replacement, from the set of integers in the interval [0,
   * n). Each of the n integers has a probability p of inclusion in the sample. <b>Enhanced
   * Functionality.</b>
   *
   * @param n The number of integers to choose from.
   * @param p The probability that each of the n integers is included in the sample.
   * @return An array containing the sample.
   */
  public final int[] sample(int n, double p) {
    return RandomSampler.sample(n, nextBinomial(n, p), null, generator);
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>This method chooses among the {@link #samplePool}, {@link #sampleReservoir}, and {@link
   * #sampleInsertion} methods based on the values of n and k.
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
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public final int[] sample(int n, int k, int[] result) {
    return RandomSampler.sample(n, k, result, generator);
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely. <b>Enhanced Functionality.</b>
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
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public final int[] sampleInsertion(int n, int k, int[] result) {
    return RandomSampler.sampleInsertion(n, k, result, generator);
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely. <b>Enhanced Functionality.</b>
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
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public final int[] samplePool(int n, int k, int[] result) {
    return RandomSampler.samplePool(n, k, result, generator);
  }

  /**
   * Generates a random sample of k integers, without replacement, from the set of integers in the
   * interval [0, n). All n choose k combinations are equally likely. <b>Enhanced Functionality.</b>
   *
   * <p>Uses the reservoir sampling algorithm (Algorithm R) from J. Vitter's 1985 article "Random
   * Sampling with a Reservoir" from ACM Transactions on Mathematical Software.
   *
   * <p>The runtime is O(n) and it generates O(n-k) random numbers. Thus, it is an especially good
   * choice as k approaches n. Only constant extra space required.
   *
   * @param n The number of integers to choose from.
   * @param k The size of the desired sample.
   * @param result An array to hold the sample that is generated. If result is null or if
   *     result.length is less than k, then this method will construct an array for the result.
   * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
   * @throws IllegalArgumentException if k &gt; n.
   * @throws NegativeArraySizeException if k &lt; 0.
   */
  public final int[] sampleReservoir(int n, int k, int[] result) {
    return RandomSampler.sampleReservoir(n, k, result, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   */
  public final void shuffle(byte[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final void shuffle(byte[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   */
  public final void shuffle(char[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final void shuffle(char[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   */
  public final void shuffle(double[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final void shuffle(double[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   */
  public final void shuffle(float[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final void shuffle(float[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   */
  public final void shuffle(int[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final void shuffle(int[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   */
  public final void shuffle(long[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final void shuffle(long[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   */
  public final void shuffle(short[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final void shuffle(short[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param <T> type of array elements
   */
  public final <T> void shuffle(T[] array) {
    Shuffler.shuffle(array, generator);
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param <T> type of array elements
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public final <T> void shuffle(T[] array, int first, int last) {
    Shuffler.shuffle(array, first, last, generator);
  }

  /**
   * Randomizes the ordering of the elements of a List. All possible reorderings are equally likely.
   *
   * @param list the List to shuffle
   * @param <T> type of List elements
   */
  public final <T> void shuffle(List<T> list) {
    Shuffler.shuffle(list, generator);
  }

  /**
   * Randomizes the ordering of the elements of a portion of a List. All possible reorderings are
   * equally likely.
   *
   * @param list the List to shuffle
   * @param first the first element (inclusive) of the part of the List to shuffle
   * @param last the last element (exclusive) of the part of the List to shuffle
   * @param <T> type of List elements
   */
  public final <T> void shuffle(List<T> list, int first, int last) {
    Shuffler.shuffle(list, first, last, generator);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom pairs of int values, without
   * replacement, from the interval [0, n). Each pair is sorted such that i is the minimum and j is
   * the maximum of the pair. <b>Enhanced Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @return an effectively unlimited stream of pseudorandom pairs of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexPair> sortedPairs(int n) {
    return Stream.generate(() -> nextSortedIntPair(n)).sequential();
  }

  /**
   * Returns a stream of pseudorandom pairs of int values, without replacement, from the interval
   * [0, n). Each pair is sorted such that i is the minimum and j is the maximum of the pair.
   * <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @return a stream of pseudorandom pairs of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexPair> sortedPairs(long streamSize, int n) {
    return Stream.generate(() -> nextSortedIntPair(n)).sequential().limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom triples of int values, without
   * replacement, from the interval [0, n). Each triple is sorted in increasing order. <b>Enhanced
   * Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @return an effectively unlimited stream of pseudorandom triples of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexTriple> sortedTriples(int n) {
    return Stream.generate(() -> nextSortedIntTriple(n)).sequential();
  }

  /**
   * Returns a stream of pseudorandom triples of int values, without replacement, from the interval
   * [0, n). Each triple is sorted in increasing order. <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @return a stream of pseudorandom triples of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexTriple> sortedTriples(long streamSize, int n) {
    return Stream.generate(() -> nextSortedIntTriple(n)).sequential().limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom pairs (i, j) of int values, without
   * replacement, from the interval [0, n), such that |i-j| &le; window, and sorted such that i is
   * less than j. All pairs that satisfy the window constraint are equally likely. <b>Enhanced
   * Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the pair.
   * @return an effectively unlimited stream of pseudorandom pairs of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexPair> sortedWindowedPairs(int n, int window) {
    return Stream.generate(() -> nextSortedWindowedIntPair(n, window)).sequential();
  }

  /**
   * Returns a stream of pseudorandom pairs of int values (i, j), without replacement, from the
   * interval [0, n), such that |i-j| &le; window, and sorted such that i is less than j. All pairs
   * that satisfy the window constraint are equally likely.. <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the pair.
   * @return a stream of pseudorandom pairs of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexPair> sortedWindowedPairs(long streamSize, int n, int window) {
    return Stream.generate(() -> nextSortedWindowedIntPair(n, window))
        .sequential()
        .limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom triples (i, j , k) of int values,
   * without replacement, from the interval [0, n), such that |i-j| &le; window, and |i-k| &le;
   * window, and |k-j| &le; window. Each triple is sorted in increasing order. All triples that
   * satisfy the window constraint are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the triple.
   * @return an effectively unlimited stream of pseudorandom triples of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexTriple> sortedWindowedTriples(int n, int window) {
    return Stream.generate(() -> nextSortedWindowedIntTriple(n, window)).sequential();
  }

  /**
   * Returns a stream of pseudorandom triples (i, j , k) of int values, without replacement, from
   * the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * Each triple is sorted in increasing order. All triples that satisfy the window constraint are
   * equally likely. <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the triple.
   * @return a stream of pseudorandom triples of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexTriple> sortedWindowedTriples(long streamSize, int n, int window) {
    return Stream.generate(() -> nextSortedWindowedIntTriple(n, window))
        .sequential()
        .limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom triples of int values, without
   * replacement, from the interval [0, n). <b>Enhanced Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @return an effectively unlimited stream of pseudorandom triples of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexTriple> triples(int n) {
    return Stream.generate(() -> nextIntTriple(n)).sequential();
  }

  /**
   * Returns a stream of pseudorandom triples of int values, without replacement, from the interval
   * [0, n). <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @return a stream of pseudorandom triples of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexTriple> triples(long streamSize, int n) {
    return Stream.generate(() -> nextIntTriple(n)).sequential().limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom pairs (i, j) of int values, without
   * replacement, from the interval [0, n), such that |i-j| &le; window. All pairs that satisfy the
   * window constraint are equally likely. <b>Enhanced Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the pair.
   * @return an effectively unlimited stream of pseudorandom pairs of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexPair> windowedPairs(int n, int window) {
    return Stream.generate(() -> nextWindowedIntPair(n, window)).sequential();
  }

  /**
   * Returns a stream of pseudorandom pairs of int values (i, j), without replacement, from the
   * interval [0, n), such that |i-j| &le; window. All pairs that satisfy the window constraint are
   * equally likely.. <b>Enhanced Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the pair.
   * @return a stream of pseudorandom pairs of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexPair> windowedPairs(long streamSize, int n, int window) {
    return Stream.generate(() -> nextWindowedIntPair(n, window)).sequential().limit(streamSize);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom triples (i, j , k) of int values,
   * without replacement, from the interval [0, n), such that |i-j| &le; window, and |i-k| &le;
   * window, and |k-j| &le; window. All triples that satisfy the window constraint are equally
   * likely. <b>Enhanced Functionality.</b>
   *
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the triple.
   * @return an effectively unlimited stream of pseudorandom triples of int values, without
   *     replacement, from the interval [0, n).
   */
  public final Stream<IndexTriple> windowedTriples(int n, int window) {
    return Stream.generate(() -> nextWindowedIntTriple(n, window)).sequential();
  }

  /**
   * Returns a stream of pseudorandom triples (i, j , k) of int values, without replacement, from
   * the interval [0, n), such that |i-j| &le; window, and |i-k| &le; window, and |k-j| &le; window.
   * All triples that satisfy the window constraint are equally likely. <b>Enhanced
   * Functionality.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param n bound on random values, exclusive.
   * @param window The maximum difference between the integers of the triple.
   * @return a stream of pseudorandom triples of int values, without replacement, from the interval
   *     [0, n).
   */
  public final Stream<IndexTriple> windowedTriples(long streamSize, int n, int window) {
    return Stream.generate(() -> nextWindowedIntTriple(n, window)).sequential().limit(streamSize);
  }

  // METHODS THAT CHANGE FUNCTIONALITY:

  /**
   * Generates a random number from a Gaussian distribution with mean 0 and standard deviation 1.
   *
   * <p><b>Enhanced Functionality:</b> This method uses the library's current most-efficient
   * algorithm for Gaussian random number generation, which may change in future releases. If you
   * require a guarantee of the algorithm used, then see the API for the classes that implement
   * specific Gaussian algorithms.
   *
   * @return A random number from a Gaussian distribution with mean 0 and standard deviation 1.
   */
  @Override
  public final double nextGaussian() {
    return RandomVariates.nextGaussian(generator);
  }

  /**
   * Generates a random number from a Gaussian distribution with mean and standard deviation of your
   * choosing.
   *
   * <p><b>Enhanced Functionality:</b> This method uses the library's current most-efficient
   * algorithm for Gaussian random number generation, which may change in future releases. If you
   * require a guarantee of the algorithm used, then see the API for the classes that implement
   * specific Gaussian algorithms.
   *
   * @param mean The mean of the Gaussian.
   * @param stddev The standard deviation of the Gaussian.
   * @return A random number from a Gaussian distribution with mean 0 and standard deviation stddev.
   */
  @Override
  public final double nextGaussian(double mean, double stddev) {
    return RandomVariates.nextGaussian(mean, stddev, generator);
  }

  /**
   * Generates a random integer uniformly distributed in the interval: [0, bound).
   *
   * <p><b>Enhanced Functionality:</b> This method is an implementation of the algorithm proposed in
   * the article: Daniel Lemire, "Fast Random Integer Generation in an Interval," ACM Transactions
   * on Modeling and Computer Simulation, 29(1), 2019.
   *
   * @param bound Upper bound, exclusive, on range of random integers (must be positive).
   * @return a random integer between 0 (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the bound is not positive
   */
  @Override
  public final int nextInt(int bound) {
    return RandomIndexer.nextInt(bound, generator);
  }

  /**
   * Generates a random integer uniformly distributed in the interval: [origin, bound).
   *
   * <p><b>Enhanced Functionality:</b> This method is an implementation of the algorithm proposed in
   * the article: Daniel Lemire, "Fast Random Integer Generation in an Interval," ACM Transactions
   * on Modeling and Computer Simulation, 29(1), 2019.
   *
   * @param origin The lower bound, inclusive (must be less than bound)
   * @param bound Upper bound, exclusive, on range of random integers.
   * @return a random integer between 0 (inclusive) and bound (exclusive).
   * @throws IllegalArgumentException if the origin is greater than or equal to bound
   */
  @Override
  public final int nextInt(int origin, int bound) {
    return RandomIndexer.nextInt(origin, bound, generator);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom int values, each value uniformly random
   * from the interval [randomNumberOrigin, randomNumberBound).
   *
   * <p><b>Enhanced Functionality:</b> Each int produced by the stream is generated by an
   * implementation of the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer
   * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.
   *
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound)
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return an effectively unlimited stream of pseudorandom int values, uniformly random from the
   *     interval [randomNumberOrigin, randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound
   */
  @Override
  public final IntStream ints(int randomNumberOrigin, int randomNumberBound) {
    return IntStream.generate(() -> nextInt(randomNumberOrigin, randomNumberBound)).sequential();
  }

  /**
   * Returns a stream of pseudorandom int values, each value uniformly random from the interval
   * [randomNumberOrigin, randomNumberBound).
   *
   * <p><b>Enhanced Functionality:</b> Each int produced by the stream is generated by an
   * implementation of the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer
   * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.
   *
   * @param streamSize The number of values in the stream.
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound).
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return a stream of pseudorandom int values, uniformly random from the interval
   *     [randomNumberOrigin, randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound, or if streamSize is negative.
   */
  @Override
  public final IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
    return IntStream.generate(() -> nextInt(randomNumberOrigin, randomNumberBound))
        .sequential()
        .limit(streamSize);
  }

  // METHODS THAT DELEGATE TO WRAPPED OBJECT:

  /**
   * Returns an effectively unlimited stream of pseudorandom double values, each value uniformly
   * random from the interval [0.0, 1.0). <b>Delegates implementation to the wrapped object.</b>
   *
   * @return an effectively unlimited stream of pseudorandom double values, uniformly random from
   *     the interval [0.0, 1.0).
   */
  @Override
  public final DoubleStream doubles() {
    return generator.doubles();
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom double values, each value uniformly
   * random from the interval [randomNumberOrigin, randomNumberBound). <b>Delegates implementation
   * to the wrapped object.</b>
   *
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound)
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return an effectively unlimited stream of pseudorandom double values, uniformly random from
   *     the interval [randomNumberOrigin, randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound, or if randomNumberOrigin is not finite. or if randomNumberBound is not
   *     finite.
   */
  @Override
  public final DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
    return generator.doubles(randomNumberOrigin, randomNumberBound);
  }

  /**
   * Returns a stream of pseudorandom double values, each value uniformly random from the interval
   * [0.0, 1.0). <b>Delegates implementation to the wrapped object.</b>
   *
   * @param streamSize The number of values in the stream.
   * @return a stream of pseudorandom double values, uniformly random from the interval [0.0, 1.0).
   * @throws IllegalArgumentException if streamSize is negative.
   */
  @Override
  public final DoubleStream doubles(long streamSize) {
    return generator.doubles(streamSize);
  }

  /**
   * Returns a stream of pseudorandom double values, each value uniformly random from the interval
   * [randomNumberOrigin, randomNumberBound). <b>Delegates implementation to the wrapped object.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound).
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return a stream of pseudorandom double values, uniformly random from the interval
   *     [randomNumberOrigin, randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound, or if streamSize is negative.
   */
  @Override
  public final DoubleStream doubles(
      long streamSize, double randomNumberOrigin, double randomNumberBound) {
    return generator.doubles(streamSize, randomNumberOrigin, randomNumberBound);
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom int values. <b>Delegates implementation
   * to the wrapped object.</b>
   *
   * @return an effectively unlimited stream of pseudorandom int values.
   */
  @Override
  public final IntStream ints() {
    return generator.ints();
  }

  /**
   * Returns a stream of pseudorandom int values. <b>Delegates implementation to the wrapped
   * object.</b>
   *
   * @param streamSize The number of values in the stream.
   * @return a stream of pseudorandom int values.
   * @throws IllegalArgumentException if streamSize is negative.
   */
  @Override
  public final IntStream ints(long streamSize) {
    return generator.ints(streamSize);
  }

  /**
   * Return true if the implementation of the RandomGenerator (algorithm) that is wrapped by this
   * EnhancedRandomGenerator has been marked for deprecation.
   *
   * @return true if the implementation of the RandomGenerator (algorithm) that is wrapped by this
   *     EnhancedRandomGenerator has been marked for deprecation
   */
  @Override
  public final boolean isDeprecated() {
    return generator.isDeprecated();
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom long values. <b>Delegates
   * implementation to the wrapped object.</b>
   *
   * @return an effectively unlimited stream of pseudorandom long values.
   */
  @Override
  public final LongStream longs() {
    return generator.longs();
  }

  /**
   * Returns an effectively unlimited stream of pseudorandom long values, each value uniformly
   * random from the interval [randomNumberOrigin, randomNumberBound). <b>Delegates implementation
   * to the wrapped object.</b>
   *
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound)
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return an effectively unlimited stream of pseudorandom long values, uniformly random from the
   *     interval [randomNumberOrigin, randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound
   */
  @Override
  public final LongStream longs(long randomNumberOrigin, long randomNumberBound) {
    return generator.longs(randomNumberOrigin, randomNumberBound);
  }

  /**
   * Returns a stream of pseudorandom long values. <b>Delegates implementation to the wrapped
   * object.</b>
   *
   * @param streamSize The number of values in the stream.
   * @return a stream of pseudorandom long values.
   * @throws IllegalArgumentException if streamSize is negative.
   */
  @Override
  public final LongStream longs(long streamSize) {
    return generator.longs(streamSize);
  }

  /**
   * Returns a stream of pseudorandom long values, each value uniformly random from the interval
   * [randomNumberOrigin, randomNumberBound). <b>Delegates implementation to the wrapped object.</b>
   *
   * @param streamSize The number of values in the stream.
   * @param randomNumberOrigin The lower bound, inclusive (must be less than bound).
   * @param randomNumberBound Upper bound, exclusive, on range of random integers.
   * @return a stream of pseudorandom long values, uniformly random from the interval
   *     [randomNumberOrigin, randomNumberBound).
   * @throws IllegalArgumentException if the randomNumberOrigin is greater than or equal to
   *     randomNumberBound, or if streamSize is negative.
   */
  @Override
  public final LongStream longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
    return generator.longs(streamSize, randomNumberOrigin, randomNumberBound);
  }

  /**
   * Generates a pseudorandom boolean value. <b>Delegates implementation to the wrapped object.</b>
   *
   * @return a pseudorandom boolean value
   */
  @Override
  public final boolean nextBoolean() {
    return generator.nextBoolean();
  }

  /**
   * Fills a user-supplied byte array with byte values generated pseudorandomly uniformly
   * distributed in the interval [-128, 127]. <b>Delegates implementation to the wrapped object.</b>
   *
   * @param bytes The byte array to fill with random bytes.
   * @throws NullPointerException if bytes is null
   */
  @Override
  public final void nextBytes(byte[] bytes) {
    generator.nextBytes(bytes);
  }

  /**
   * Generates a pseudorandom double value in the interval [0, 1). <b>Delegates implementation to
   * the wrapped object.</b>
   *
   * @return a pseudorandom double in [0, 1).
   */
  @Override
  public final double nextDouble() {
    return generator.nextDouble();
  }

  /**
   * Generates a pseudorandom double value in the interval [0, bound). <b>Delegates implementation
   * to the wrapped object.</b>
   *
   * @param bound The upper bound, exclusive. Must be positive and finite.
   * @return a pseudorandom double in [0, bound).
   * @throws IllegalArgumentException if bound is not finite or not positive.
   */
  @Override
  public final double nextDouble(double bound) {
    return generator.nextDouble(bound);
  }

  /**
   * Generates a pseudorandom double value in the interval [origin, bound). <b>Delegates
   * implementation to the wrapped object.</b>
   *
   * @param origin The lower bound, inclusive.
   * @param bound The upper bound, exclusive.
   * @return a pseudorandom double in [origin, bound).
   * @throws IllegalArgumentException if origin is greater than or equal to bound, or if origin is
   *     not finite or if bound is not finite.
   */
  @Override
  public final double nextDouble(double origin, double bound) {
    return generator.nextDouble(origin, bound);
  }

  /**
   * Generates a non-negative pseudorandom number from an exponential distribution with mean 1.
   * <b>Delegates implementation to the wrapped object.</b>
   *
   * @return a non-negative pseudorandom number from an exponential distribution
   */
  @Override
  public final double nextExponential() {
    return generator.nextExponential();
  }

  /**
   * Generates a pseudorandom float value in the interval [0, 1). <b>Delegates implementation to the
   * wrapped object.</b>
   *
   * @return a pseudorandom float in [0, 1).
   */
  @Override
  public final float nextFloat() {
    return generator.nextFloat();
  }

  /**
   * Generates a pseudorandom float value in the interval [0, bound). <b>Delegates implementation to
   * the wrapped object.</b>
   *
   * @param bound The upper bound, exclusive. Must be positive and finite.
   * @return a pseudorandom float in [0, bound).
   * @throws IllegalArgumentException if bound is not finite or not positive.
   */
  @Override
  public final float nextFloat(float bound) {
    return generator.nextFloat(bound);
  }

  /**
   * Generates a pseudorandom float value in the interval [origin, bound). <b>Delegates
   * implementation to the wrapped object.</b>
   *
   * @param origin The lower bound, inclusive.
   * @param bound The upper bound, exclusive.
   * @return a pseudorandom float in [origin, bound).
   * @throws IllegalArgumentException if origin is greater than or equal to bound, or if origin is
   *     not finite or if bound is not finite.
   */
  @Override
  public final float nextFloat(float origin, float bound) {
    return generator.nextFloat(origin, bound);
  }

  /**
   * Generates a pseudorandom int value. <b>Delegates implementation to the wrapped object.</b>
   *
   * @return a pseudorandom int
   */
  @Override
  public final int nextInt() {
    return generator.nextInt();
  }

  /**
   * Generates a pseudorandom long value. <b>Delegates implementation to the wrapped object.</b>
   *
   * @return a pseudorandom long
   */
  @Override
  public final long nextLong() {
    return generator.nextLong();
  }

  /**
   * Generates a pseudorandom long value in the interval [0, bound). <b>Delegates implementation to
   * the wrapped object.</b>
   *
   * @param bound The upper bound, exclusive (must be positive).
   * @return a pseudorandom long in [0, bound).
   * @throws IllegalArgumentException if bound is not positive.
   */
  @Override
  public final long nextLong(long bound) {
    return generator.nextLong(bound);
  }

  /**
   * Generates a pseudorandom long value in the interval [origin, bound). <b>Delegates
   * implementation to the wrapped object.</b>
   *
   * @param origin The lower bound, inclusive.
   * @param bound The upper bound, exclusive.
   * @return a pseudorandom long in [origin, bound).
   * @throws IllegalArgumentException if origin is greater than or equal to bound.
   */
  @Override
  public final long nextLong(long origin, long bound) {
    return generator.nextLong(origin, bound);
  }
}
