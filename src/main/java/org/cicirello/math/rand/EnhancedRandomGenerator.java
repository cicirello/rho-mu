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
