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

import java.util.random.RandomGenerator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.SplittableRandom;

/**
 * <p>An EnhancedRandomGenerator is used to wrap an object of any
 * class that implements {@link RandomGenerator} for the purpose
 * of adding all of the functionality of the {@link RandomIndexer}
 * and {@link RandomVariates}. In this way, the EnhancedRandomGenerator
 * can be used as a drop-in replacement for any of Java's 
 * random number generators, while substituting more efficient algorithms
 * in some cases, or adding functionality in others. Methods of the 
 * {@link RandomGenerator} without equivalent replacements in one or the other
 * of {@link RandomIndexer} or {@link RandomVariates} are simply delegated
 * to the wrapped {@link RandomGenerator}.</p>
 *
 * <p>Enhanced Functionality provided by this class includes:</p>
 * <ul>
 * <li>Faster generation of random int values subject to a bound or bound and origin.</li>
 * <li>Faster generation of random int values within an IntStream subject to a bound and origin.</li>
 * <li>Faster generation of Gaussian distributed random doubles.</li>
 * <li>Additional distributions available beyond what is supported by
 *     the Java API's RandomGenerator classes, such as Binomial and Cauchy
 *     random vaiables.</li>
 * <li>An ultrafast, but biased, nextBiasedInt method that sacrifices uniformity
 *     for speed by excluding the rejection sampling necessary to ensure uniformity.</li>
 * <li>Methods for generating random pairs of integers without replacement, and random
 *    triples of integers without replacement.</li>
 * <li>Methods for generating random samples without replacement from a range of integers.</li>
 * </ul>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class EnhancedRandomGenerator implements RandomGenerator {
	
	private final RandomGenerator generator;
	
	/**
	 * Constructs the EnhancedRandomGenerator to wrap an instance of
	 * the default random number generator as obtained via a call to
	 * {@link RandomGenerator#getDefault}.
	 */
	public EnhancedRandomGenerator() {
		this(RandomGenerator.getDefault());
	}
	
	/**
	 * Constructs the EnhancedRandomGenerator to wrap an instance
	 * of a random number generator initialized with a specified seed
	 * to enable replicating the same sequence of random numbers during
	 * subsequent runs.
	 *
	 * @param seed The seed for the random number generator.
	 */
	public EnhancedRandomGenerator(long seed) {
		this(new SplittableRandom(seed));
	}
	
	/**
	 * Constructs the EnhancedRandomGenerator from the RandomGenerator
	 * to wrap.
	 *
	 * @param generator The RandomGenerator to wrap, which serves as the source
	 * of randomness.
	 */
	public EnhancedRandomGenerator(RandomGenerator generator) {
		this.generator = generator;
	}
	
	/**
	 * Constructs the EnhancedRandomGenerator to wrap an instance of
	 * any random number generator supported by your version of Java
	 * as specified by its name, as documented via the {@link RandomGenerator#of}
	 * method.
	 *
	 * @param algorithmName The name of the random number generator as documented
	 * by the {@link RandomGenerator#of} method.
	 *
	 * @throws NullPointerException if algorithmName is null.
	 * @throws IllegalArgumentException if algorithmName is not found.
	 */
	public EnhancedRandomGenerator(String algorithmName) {
		this(RandomGenerator.of(algorithmName));
	}
	
	/**
	 * Gets an EnhancedRandomGenerator wrapping an instance of
	 * the default random number generator as obtained via a call to
	 * {@link RandomGenerator#getDefault}.
	 *
	 * @return an EnhancedRandomGenerator wrapping an instance of
	 * the default random number generator
	 */
	public static EnhancedRandomGenerator getDefault() {
		return new EnhancedRandomGenerator();
	}
	
	/**
	 * Gets an EnhancedRandomGenerator wrapping an instance of
	 * any random number generator supported by your version of Java
	 * as specified by its name, as documented via the {@link RandomGenerator#of}
	 * method.
	 *
	 * @param algorithmName The name of the random number generator as documented
	 * by the {@link RandomGenerator#of} method.
	 *
	 * @return an EnhancedRandomGenerator wrapping an instance of your chosen
	 * random number generator.
	 *
	 * @throws NullPointerException if algorithmName is null.
	 * @throws IllegalArgumentException if algorithmName is not found.
	 */
	public static EnhancedRandomGenerator of(String algorithmName) {
		return new EnhancedRandomGenerator(algorithmName);
	}
	
	
	
	
	// METHODS THAT ADD FUNCTIONALITY:
	
	/**
	 * <p>Generates an "array mask" of a specified length,
	 * where an "array mask" is an array of boolean values of the same length as another array.
	 * Each position in the result is equally likely true or false.
	 * <b>Enhanced Functionality.</b></p>
	 *
	 * <p>Runtime: O(n).</p>
	 *
	 * @param n The length of the array mask.
	 * @return An array of n randomly generated boolean values.
	 */
	public final boolean[] arrayMask(int n) {
		return RandomIndexer.arrayMask(n, generator);
	}
	
	/**
	 * <p>Generates an "array mask" of a specified length and specified number of true values,
	 * where an "array mask" is an array of boolean values of the same length as another array.
	 * <b>Enhanced Functionality.</b></p>
	 *
	 * <p>Runtime: O(min(n, k<sup>2</sup>)), and it uses O(min(k, n-k)) random numbers.</p>
	 *
	 * @param n The length of the array mask.
	 * @param k The desired number of true values, which must be no greater than n.
	 * @return An array of n boolean values, exactly k of which are equal to true.
	 */
	public final boolean[] arrayMask(int n, int k) {
		return RandomIndexer.arrayMask(n, k, generator);
	}
	
	/**
	 * <p>Generates an "array mask" of a specified length,
	 * where an "array mask" is an array of boolean values of the same length as another array.
	 * <b>Enhanced Functionality.</b></p>
	 *
	 * <p>Runtime: O(n), and it uses O(n) random doubles.</p>
	 *
	 * @param n The length of the array mask.
	 * @param p The probability that an element of the result is true.
	 * @return An array of n boolean values, such that each element is true with probability p.
	 */
	public final boolean[] arrayMask(int n, double p) {
		return RandomIndexer.arrayMask(n, p, generator);
	}
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom int values, each value
	 * generated from a binomial distribution. <b>Enhanced Functionality.</b></p> 
	 *
	 * @param n Number of trials for the binomial distribution.
	 * @param p The probability of a successful trial.
	 *
	 * @return an effectively unlimited stream of pseudorandom int values
	 * generated from a binomial distribution.
	 */
	public final IntStream binomials(int n, double p) {
		return IntStream.generate(() -> nextBinomial(n, p)).sequential();
	}
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom int values, each value
	 * generated from a binomial distribution. <b>Enhanced Functionality.</b></p> 
	 *
	 * @param streamSize The number of values in the stream.
	 * @param n Number of trials for the binomial distribution.
	 * @param p The probability of a successful trial.
	 *
	 * @return an effectively unlimited stream of pseudorandom int values
	 * generated from a binomial distribution.
	 */
	public final IntStream binomials(long streamSize, int n, double p) {
		return IntStream.generate(() -> nextBinomial(n, p)).sequential().limit(streamSize);
	}
	
	/**
	 * <p>Generates a random integer in the interval: [0, bound). <b>Enhanced Functionality.</b></p>
	 *
	 * <p>The nextBiasedInt(bound) method computes a random int in the target interval
	 * but faster than nextInt(bound). It does not
	 * correct for bias via rejection sampling, and thus some values in the interval [0, bound)
	 * may be more likely than others. There is no bias for bound values that are powers of 2.
	 * Otherwise, the lower the value of bound, the less bias; and the higher
	 * the value of bound, the more bias. If your bound is relatively low, and if your application
	 * does not require strict uniformity, then this method is significantly faster than any
	 * approach that corrects for bias. We started with  
	 * the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer 
	 * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.
	 * But we removed from it the rejection sampling portion.</p>
	 *
	 * @param bound Upper bound, exclusive, on range of random integers (must be positive).
	 * @return a random integer between 0 (inclusive) and bound (exclusive).
	 * @throws IllegalArgumentException if the bound is not positive
	 */
	public final int nextBiasedInt(int bound) {
		return RandomIndexer.nextBiasedInt(bound, generator);
	}
	
	/**
	 * Generates a pseudorandom integer from a binomial distribution.
	 * <b>Enhanced Functionality.</b>
	 *
	 * @param n Number of trials for the binomial distribution.
	 * @param p The probability of a successful trial.
	 * @return A pseudorandom integer from a binomial distribution.
	 */
	public final int nextBinomial(int n, double p) {
		return RandomVariates.nextBinomial(n, p, generator);
	}
	
	/**
	 * Generates a pseudorandom number from a Cauchy distribution with median 0
	 * and chosen scale parameter. <b>Enhanced Functionality.</b>
	 *
	 * @param scale The scale parameter of the Cauchy.
	 * @return a pseudorandom number from a Cauchy distribution
	 */
	public final double nextCauchy(double scale) {
		return RandomVariates.nextCauchy(scale, generator);
	}
	
	/**
	 * Generates a pseudorandom number from a Cauchy distribution.
	 * <b>Enhanced Functionality.</b>
	 *
	 * @param median The median of the Cauchy.
	 * @param scale The scale parameter of the Cauchy.
	 * @return a pseudorandom number from a Cauchy distribution
	 */
	public final double nextCauchy(double median, double scale) {
		return RandomVariates.nextCauchy(median, scale, generator);
	}
	
	/**
	 * <p>Generates a random number from a Gaussian distribution with
	 * mean 0 and standard deviation, stddev, of your choosing.</p> 
	 *
	 * <p><b>Enhanced Functionality:</b> This method uses
	 * the library's current most-efficient algorithm for Gaussian random number
	 * generation, which may change in future releases. If you require a 
	 * guarantee of the algorithm used, then see the API for the classes that
	 * implement specific Gaussian algorithms.</p>
	 *
	 * @param stddev The standard deviation of the Gaussian.
	 * @return A random number from a Gaussian distribution with mean 0 and
	 * standard deviation stddev.
	 */
	public final double nextGaussian(double stddev) {
		return RandomVariates.nextGaussian(stddev, generator);
	}
	
	/**
	 * <p>Generates a random sample of 2 integers, without replacement, from the
	 * set of integers in the interval [0, n). All n choose 2 combinations are equally
	 * likely. <b>Enhanced Functionality.</b></p>
	 *
	 * <p>The runtime is O(1).</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param result An array to hold the pair that is generated.  If result is null
	 * or if result.length is less than 2, then this method will construct an array for the result. 
	 * @return An array containing the pair of 
	 * randomly chosen integers from the interval [0, n).
	 * @throws IllegalArgumentException if n &lt; 2.
	 */
	public final int[] nextIntPair(int n, int[] result) {
		return RandomIndexer.nextIntPair(n, result, generator);
	}
	
	/**
	 * <p>Generates a random sample of 3 integers, without replacement, from the
	 * set of integers in the interval [0, n). All n choose 3 combinations are equally
	 * likely. <b>Enhanced Functionality.</b></p>
	 *
	 * <p>The runtime is O(1).</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param result An array to hold the pair that is generated.  If result is null
	 * or if result.length is less than 3, then this method will construct an array for the result. 
	 * @return An array containing the pair of 
	 * randomly chosen integers from the interval [0, n).  
	 * @throws IllegalArgumentException if n &lt; 3.
	 */
	public final int[] nextIntTriple(int n, int[] result) {
		return RandomIndexer.nextIntTriple(n, result, generator);
	}
	
	/**
	 * <p>Generates a random sample of 3 integers, without replacement, from the
	 * set of integers in the interval [0, n).  All n choose 3 combinations are equally
	 * likely. <b>Enhanced Functionality.</b></p>
	 *
	 * <p>The runtime is O(1).</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param result An array to hold the pair that is generated.  If result is null
	 * or if result.length is less than 3, then this method will construct an array for the result. 
	 * @param sort If true, the result is sorted in increasing order; otherwise it is in arbitrary order.
	 * @return An array containing the pair of 
	 * randomly chosen integers from the interval [0, n).  
	 * @throws IllegalArgumentException if n &lt; 3.
	 */
	public final int[] nextIntTriple(int n, int[] result, boolean sort) {
		return RandomIndexer.nextIntTriple(n, result, sort, generator);
	}
	
	/**
	 * <p>Generates a random sample of 2 integers, i, j, without replacement, from the
	 * set of integers in the interval [0, n), such that |i-j| &le; window.  
	 * All pairs that satisfy the window constraint are equally likely.
	 * <b>Enhanced Functionality.</b></p>
	 *
	 * <p>The runtime is O(1).</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param window The maximum difference between the integers of the pair.
	 * @param result An array to hold the pair that is generated.  If result is null
	 * or if result.length is less than 2, then this method will construct an array for the result. 
	 * @return An array containing the pair of 
	 * randomly chosen integers, i, j, 
	 * from the interval [0, n), such that |i-j| &le; window.  
	 * @throws IllegalArgumentException if window &lt; 1 or n &lt; 2.
	 */
	public final int[] nextWindowedIntPair(int n, int window, int[] result) {
		return RandomIndexer.nextWindowedIntPair(n, window, result, generator);
	}
	
	/**
	 * <p>Generates a random sample of 3 integers, i, j, k without replacement, from the
	 * set of integers in the interval [0, n), such that |i-j| &le; window, and 
	 * |i-k| &le; window, and |k-j| &le; window.  
	 * All triples that satisfy the window constraint are equally likely.
	 * <b>Enhanced Functionality.</b></p>
	 *
	 * <p>The runtime is O(1).</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param window The maximum difference between the integers of the triple.
	 * @param result An array to hold the pair that is generated.  If result is null
	 * or if result.length is less than 3, then this method will construct an array for the result. 
	 * @return An array containing the triple of 
	 * randomly chosen integers, i, j, k 
	 * from the interval [0, n), such that |i-j| &le; window, and 
	 * |i-k| &le; window, and |k-j| &le; window.  
	 * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
	 */
	public final int[] nextWindowedIntTriple(int n, int window, int[] result) {
		return RandomIndexer.nextWindowedIntTriple(n, window, result, generator);
	}
	
	/**
	 * <p>Generates a random sample of 3 integers, i, j, k without replacement, from the
	 * set of integers in the interval [0, n), such that |i-j| &le; window, and 
	 * |i-k| &le; window, and |k-j| &le; window.  
	 * All triples that satisfy the window constraint are equally likely.
	 * <b>Enhanced Functionality.</b></p>
	 *
	 * <p>The runtime is O(1).</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param window The maximum difference between the integers of the triple.
	 * @param result An array to hold the pair that is generated.  If result is null
	 * or if result.length is less than 3, then this method will construct an array for the result.
	 * @param sort If true, the result is sorted in increasing order, otherwise it is in random order.
	 * @return An array containing the triple of 
	 * randomly chosen integers, i, j, k 
	 * from the interval [0, n), such that |i-j| &le; window, and 
	 * |i-k| &le; window, and |k-j| &le; window.  
	 * @throws IllegalArgumentException if window &lt; 2 or n &lt; 3.
	 */
	public final int[] nextWindowedIntTriple(int n, int window, int[] result, boolean sort) {
		return RandomIndexer.nextWindowedIntTriple(n, window, result, sort, generator);
	}
	
	/**
	 * <p>Generates a random sample, without replacement, from the
	 * set of integers in the interval [0, n).  Each of the n integers 
	 * has a probability p of inclusion in the sample.
	 * <b>Enhanced Functionality.</b></p>
	 *
	 * @param n The number of integers to choose from.
	 * @param p The probability that each of the n integers is included in the sample.
	 * @return An array containing the sample.
	 */
	public final int[] sample(int n, double p) {
		return RandomIndexer.sample(n, p, generator);
	}
	
	/**
	 * <p>Generates a random sample of k integers, without replacement, from the
	 * set of integers in the interval [0, n).  All n choose k combinations are equally
	 * likely. <b>Enhanced Functionality.</b></p>
	 *
	 * <p>This method chooses among the {@link #samplePool}, 
	 * {@link #sampleReservoir}, and {@link #sampleInsertion} 
	 * methods based on the values of n and k.</p>
	 *
	 * <p>The runtime is O(min(n, k<sup>2</sup>))
	 * and it generates O(min(k, n-k)) random numbers.</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param k The size of the desired sample.
	 * @param result An array to hold the sample that is generated.  If result is null
	 * or if result.length is less than k, then this method will construct an array for the result. 
	 * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
	 * @throws IllegalArgumentException if k &gt; n.
	 * @throws NegativeArraySizeException if k &lt; 0.
	 */
	public final int[] sample(int n, int k, int[] result) {
		return RandomIndexer.sample(n, k, result, generator);
	}
	
	/**
	 * <p>Generates a random sample of k integers, without replacement, from the
	 * set of integers in the interval [0, n).  All n choose k combinations are equally
	 * likely. <b>Enhanced Functionality.</b></p>  
	 *
	 * <p>The runtime is O(k<sup>2</sup>)
	 * and it generates O(k) random numbers.  Thus, it is a better 
	 * choice than both sampleReservoir and samplePool when k<sup>2</sup> &lt; n.
	 * Just like sampleReservoir, the sampleInsertion method only requires O(1) extra space,
	 * while samplePool requires O(n) extra space.</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param k The size of the desired sample.
	 * @param result An array to hold the sample that is generated.  If result is null
	 * or if result.length is less than k, then this method will construct an array for the result. 
	 * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
	 * @throws IllegalArgumentException if k &gt; n.
	 * @throws NegativeArraySizeException if k &lt; 0.
	 */
	public final int[] sampleInsertion(int n, int k, int[] result) {
		return RandomIndexer.sampleInsertion(n, k, result, generator);
	}
	
	/**
	 * <p>Generates a random sample of k integers, without replacement, from the
	 * set of integers in the interval [0, n).  All n choose k combinations are equally
	 * likely. <b>Enhanced Functionality.</b></p> 
	 * 
	 * <p>This implements the algorithm SELECT of S. Goodman and S. Hedetniemi, as described in:
	 * J Ernvall, O Nevalainen, "An Algorithm for Unbiased Random Sampling," The 
	 * Computer Journal, 25(1):45-47, 1982.</p>
	 *
	 * <p>The runtime is O(n)
	 * and it generates O(k) random numbers.  Thus, it is a better 
	 * choice than sampleReservoir when k &lt; n-k.
	 * However, this uses O(n) extra space, whereas the reservoir algorithm
	 * uses no extra space.</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param k The size of the desired sample.
	 * @param result An array to hold the sample that is generated.  If result is null
	 * or if result.length is less than k, then this method will construct an array for the result. 
	 * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
	 * @throws IllegalArgumentException if k &gt; n.
	 * @throws NegativeArraySizeException if k &lt; 0.
	 */
	public final int[] samplePool(int n, int k, int[] result) {
		return RandomIndexer.samplePool(n, k, result, generator);
	}
	
	/**
	 * <p>Generates a random sample of k integers, without replacement, from the
	 * set of integers in the interval [0, n).  All n choose k combinations are equally
	 * likely. <b>Enhanced Functionality.</b></p>  
	 *
	 * <p>Uses the reservoir sampling algorithm (Algorithm R) 
	 * from J. Vitter's 1985 article "Random Sampling
	 * with a Reservoir" from ACM Transactions on Mathematical Software.</p> 
	 * <p>The runtime is O(n)
	 * and it generates O(n-k) random numbers.  Thus, it is an 
	 * especially good choice as k
	 * approaches n.  Only constant extra space required.</p>
	 *
	 * @param n The number of integers to choose from.
	 * @param k The size of the desired sample.
	 * @param result An array to hold the sample that is generated.  If result is null
	 * or if result.length is less than k, then this method will construct an array for the result. 
	 * @return An array containing the sample of k randomly chosen integers from the interval [0, n).
	 * @throws IllegalArgumentException if k &gt; n.
	 * @throws NegativeArraySizeException if k &lt; 0.
	 */
	public final int[] sampleReservoir(int n, int k, int[] result) {
		return RandomIndexer.sampleReservoir(n, k, result, generator);
	}
	
	
	
	
	// METHODS THAT CHANGE FUNCTIONALITY:
	
	/**
	 * <p>Generates a random number from a Gaussian distribution with
	 * mean 0 and standard deviation 1.</p> 
	 * <p><b>Enhanced Functionality:</b> This method uses
	 * the library's current most-efficient algorithm for Gaussian random number
	 * generation, which may change in future releases. If you require a 
	 * guarantee of the algorithm used, then see the API for the classes that
	 * implement specific Gaussian algorithms.</p>
	 *
	 * @return A random number from a Gaussian distribution with mean 0 and
	 * standard deviation 1.
	 */
	@Override
	public final double nextGaussian() {
		return RandomVariates.nextGaussian(generator);
	}
	
	/**
	 * <p>Generates a random number from a Gaussian distribution with
	 * mean and standard deviation of your choosing.</p> 
	 * <p><b>Enhanced Functionality:</b> This method uses
	 * the library's current most-efficient algorithm for Gaussian random number
	 * generation, which may change in future releases. If you require a 
	 * guarantee of the algorithm used, then see the API for the classes that
	 * implement specific Gaussian algorithms.</p>
	 *
	 * @param mean The mean of the Gaussian.
	 * @param stddev The standard deviation of the Gaussian.
	 * @return A random number from a Gaussian distribution with mean 0 and
	 * standard deviation stddev.
	 */
	@Override
	public final double nextGaussian(double mean, double stddev) {
		return mean + RandomVariates.nextGaussian(stddev, generator);
	}
	
	/**
	 * <p>Generates a random integer uniformly distributed in the interval: [0, bound).</p>
	 * <p><b>Enhanced Functionality:</b> This method is an implementation of 
	 * the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer 
	 * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.</p>
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
	 * <p>Generates a random integer uniformly distributed in the interval: [origin, bound).</p>
	 * <p><b>Enhanced Functionality:</b> This method is an implementation of 
	 * the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer 
	 * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.</p>
	 *
	 * @param origin The lower bound, inclusive (must be less than bound)
	 * @param bound Upper bound, exclusive, on range of random integers.
	 * @return a random integer between 0 (inclusive) and bound (exclusive).
	 * @throws IllegalArgumentException if the origin is greater than or equal to bound
	 */
	@Override
	public final int nextInt(int origin, int bound) {
		return origin + RandomIndexer.nextInt(bound - origin, generator);
	}
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom int values, each value
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).</p> 
	 *
	 * <p><b>Enhanced Functionality:</b> Each int produced by the stream is generated
	 * by an implementation of the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer 
	 * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.</p>
	 *
	 * @param randomNumberOrigin The lower bound, inclusive (must be less than bound)
	 * @param randomNumberBound Upper bound, exclusive, on range of random integers.
	 *
	 * @return an effectively unlimited stream of pseudorandom int values,
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 *
	 * @throws IllegalArgumentException if the randomNumberOrigin is greater 
	 * than or equal to randomNumberBound
	 */
	@Override
	public final IntStream ints(int randomNumberOrigin, int randomNumberBound) {
		return IntStream.generate(() -> nextInt(randomNumberOrigin, randomNumberBound)).sequential();
	}
	
	/**
	 * <p>Returns a stream of pseudorandom int values, each value
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).</p> 
	 *
	 * <p><b>Enhanced Functionality:</b> Each int produced by the stream is generated
	 * by an implementation of the algorithm proposed in the article: Daniel Lemire, "Fast Random Integer 
	 * Generation in an Interval," ACM Transactions on Modeling and Computer Simulation, 29(1), 2019.</p>
	 *
	 * @param streamSize The number of values in the stream.
	 * @param randomNumberOrigin The lower bound, inclusive (must be less than bound).
	 * @param randomNumberBound Upper bound, exclusive, on range of random integers.
	 *
	 * @return a stream of pseudorandom int values,
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 *
	 * @throws IllegalArgumentException if the randomNumberOrigin is greater 
	 * than or equal to randomNumberBound, or if streamSize is negative.
	 */
	@Override
	public final IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
		return IntStream.generate(() -> nextInt(randomNumberOrigin, randomNumberBound)).sequential().limit(streamSize);
	}
	
	
	
	
	// METHODS THAT DELEGATE TO WRAPPED OBJECT:
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom double values, each value
	 * uniformly random from the interval [0.0, 1.0).
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @return an effectively unlimited stream of pseudorandom double values,
	 * uniformly random from the interval [0.0, 1.0).
	 */
	@Override
	public final DoubleStream doubles() {
		return generator.doubles();
	}
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom double values, each value
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @param randomNumberOrigin The lower bound, inclusive (must be less than bound)
	 * @param randomNumberBound Upper bound, exclusive, on range of random integers.
	 *
	 * @return an effectively unlimited stream of pseudorandom double values,
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 *
	 * @throws IllegalArgumentException if the randomNumberOrigin is greater 
	 * than or equal to randomNumberBound, or if randomNumberOrigin is not finite.
	 * or if randomNumberBound is not finite. 
	 */
	@Override
	public final DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
		return generator.doubles(randomNumberOrigin, randomNumberBound);
	}
	
	/**
	 * <p>Returns a stream of pseudorandom double values, each value
	 * uniformly random from the interval [0.0, 1.0).
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @param streamSize The number of values in the stream.
	 *
	 * @return a stream of pseudorandom double values,
	 * uniformly random from the interval [0.0, 1.0).
	 *
	 * @throws IllegalArgumentException if streamSize is negative.
	 */
	@Override
	public final DoubleStream doubles(long streamSize) {
		return generator.doubles(streamSize);
	}
	
	/**
	 * <p>Returns a stream of pseudorandom double values, each value
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 * <b>Delegates implementation to the wrapped object.</b></p>
	 *
	 * @param streamSize The number of values in the stream.
	 * @param randomNumberOrigin The lower bound, inclusive (must be less than bound).
	 * @param randomNumberBound Upper bound, exclusive, on range of random integers.
	 *
	 * @return a stream of pseudorandom double values,
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 *
	 * @throws IllegalArgumentException if the randomNumberOrigin is greater 
	 * than or equal to randomNumberBound, or if streamSize is negative.
	 */
	@Override
	public final DoubleStream doubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
		return generator.doubles(streamSize, randomNumberOrigin, randomNumberBound);
	}
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom int values.
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @return an effectively unlimited stream of pseudorandom int values.
	 */
	@Override
	public final IntStream ints() {
		return generator.ints();
	}
	
	/**
	 * <p>Returns a stream of pseudorandom int values.
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @param streamSize The number of values in the stream.
	 * 
	 * @return a stream of pseudorandom int values.
	 *
	 * @throws IllegalArgumentException if streamSize is negative.
	 */
	@Override
	public final IntStream ints(long streamSize) {
		return generator.ints(streamSize);
	}
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom long values.
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @return an effectively unlimited stream of pseudorandom long values.
	 */
	@Override
	public final LongStream longs() {
		return generator.longs();
	}
	
	/**
	 * <p>Returns an effectively unlimited stream of pseudorandom long values, each value
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @param randomNumberOrigin The lower bound, inclusive (must be less than bound)
	 * @param randomNumberBound Upper bound, exclusive, on range of random integers.
	 *
	 * @return an effectively unlimited stream of pseudorandom long values,
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 *
	 * @throws IllegalArgumentException if the randomNumberOrigin is greater 
	 * than or equal to randomNumberBound
	 */
	@Override
	public final LongStream longs(long randomNumberOrigin, long randomNumberBound) {
		return generator.longs(randomNumberOrigin, randomNumberBound);
	}
	
	/**
	 * <p>Returns a stream of pseudorandom long values.
	 * <b>Delegates implementation to the wrapped object.</b></p> 
	 *
	 * @param streamSize The number of values in the stream.
	 * 
	 * @return a stream of pseudorandom long values.
	 *
	 * @throws IllegalArgumentException if streamSize is negative.
	 */
	@Override
	public final LongStream longs(long streamSize) {
		return generator.longs(streamSize);
	}
	
	/**
	 * <p>Returns a stream of pseudorandom long values, each value
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 * <b>Delegates implementation to the wrapped object.</b></p>
	 *
	 * @param streamSize The number of values in the stream.
	 * @param randomNumberOrigin The lower bound, inclusive (must be less than bound).
	 * @param randomNumberBound Upper bound, exclusive, on range of random integers.
	 *
	 * @return a stream of pseudorandom long values,
	 * uniformly random from the interval [randomNumberOrigin, randomNumberBound).
	 *
	 * @throws IllegalArgumentException if the randomNumberOrigin is greater 
	 * than or equal to randomNumberBound, or if streamSize is negative.
	 */
	@Override
	public final LongStream longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
		return generator.longs(streamSize, randomNumberOrigin, randomNumberBound);
	}
	
	/**
	 * Generates a pseudorandom boolean value.
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @return a pseudorandom boolean value
	 */
	@Override
	public final boolean nextBoolean() {
		return generator.nextBoolean();
	}
	
	/**
	 * Fills a user-supplied byte array with byte values generated
	 * pseudorandomly uniformly distributed in the interval [-128, 127].
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @param bytes The byte array to fill with random bytes.
	 *
	 * @throws NullPointerException if bytes is null
	 */
	@Override
	public final void nextBytes(byte[] bytes) {
		generator.nextBytes(bytes);
	}
	
	/**
	 * Generates a pseudorandom double value in the interval [0, 1).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @return a pseudorandom double in [0, 1).
	 */
	@Override
	public final double nextDouble() {
		return generator.nextDouble();
	}
	
	/**
	 * Generates a pseudorandom double value in the interval [0, bound).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @param bound The upper bound, exclusive. Must be positive and finite.
	 *
	 * @return a pseudorandom double in [0, bound).
	 *
	 * @throws IllegalArgumentException if bound is not finite or not positive.
	 */
	@Override
	public final double nextDouble(double bound) {
		return generator.nextDouble(bound);
	}
	
	/**
	 * Generates a pseudorandom double value in the interval [origin, bound).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @param origin The lower bound, inclusive.
	 * @param bound The upper bound, exclusive.
	 *
	 * @return a pseudorandom double in [origin, bound).
	 *
	 * @throws IllegalArgumentException if origin is greater than or equal to bound,
	 * or if origin is not finite or if bound is not finite.
	 */
	@Override
	public final double nextDouble(double origin, double bound) {
		return generator.nextDouble(origin, bound);
	}
	
	/**
	 * Generates a non-negative pseudorandom number from an exponential distribution
	 * with mean 1. <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @return a non-negative pseudorandom number from an exponential distribution
	 */
	@Override
	public final double nextExponential() {
		return generator.nextExponential();
	}
	
	/**
	 * Generates a pseudorandom float value in the interval [0, 1).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @return a pseudorandom float in [0, 1).
	 */
	@Override
	public final float nextFloat() {
		return generator.nextFloat();
	}
	
	/**
	 * Generates a pseudorandom float value in the interval [0, bound).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @param bound The upper bound, exclusive. Must be positive and finite.
	 *
	 * @return a pseudorandom float in [0, bound).
	 *
	 * @throws IllegalArgumentException if bound is not finite or not positive.
	 */
	@Override
	public final float nextFloat(float bound) {
		return generator.nextFloat(bound);
	}
	
	/**
	 * Generates a pseudorandom float value in the interval [origin, bound).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @param origin The lower bound, inclusive.
	 * @param bound The upper bound, exclusive.
	 *
	 * @return a pseudorandom float in [origin, bound).
	 *
	 * @throws IllegalArgumentException if origin is greater than or equal to bound,
	 * or if origin is not finite or if bound is not finite.
	 */
	@Override
	public final float nextFloat(float origin, float bound) {
		return generator.nextFloat(origin, bound);
	}
	
	/**
	 * Generates a pseudorandom int value.
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @return a pseudorandom int
	 */
	@Override
	public final int nextInt() {
		return generator.nextInt();
	}
	
	/**
	 * Generates a pseudorandom long value.
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @return a pseudorandom long
	 */
	@Override
	public final long nextLong() {
		return generator.nextLong();
	}
	
	/**
	 * Generates a pseudorandom long value in the interval [0, bound).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @param bound The upper bound, exclusive (must be positive).
	 *
	 * @return a pseudorandom long in [0, bound).
	 *
	 * @throws IllegalArgumentException if bound is not positive.
	 */
	@Override
	public final long nextLong(long bound) {
		return generator.nextLong(bound);
	}
	
	/**
	 * Generates a pseudorandom long value in the interval [origin, bound).
	 * <b>Delegates implementation to the wrapped object.</b>
	 *
	 * @param origin The lower bound, inclusive.
	 * @param bound The upper bound, exclusive.
	 *
	 * @return a pseudorandom long in [origin, bound).
	 *
	 * @throws IllegalArgumentException if origin is greater than or equal to bound.
	 */
	@Override
	public final long nextLong(long origin, long bound) {
		return generator.nextLong(origin, bound);
	}
}
