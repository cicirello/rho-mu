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
import java.util.stream.Stream;
import java.util.SplittableRandom;

/**
 * <p>An EnhancedStreamableGenerator is used to wrap an object of any
 * class that implements {@link RandomGenerator.StreamableGenerator} for the purpose
 * of adding all of the functionality of the {@link RandomIndexer}
 * and {@link RandomVariates}. See the superclass {@link EnhancedRandomGenerator}
 * for documentation of the enhanced functionality that is added to the
 * wrapped object.</p>
 *
 * <p>The methods of the {@link RandomGenerator.StreamableGenerator} interface,
 * such as {@link #rngs}, that return streams of RandomGenerator are implemented to return streams
 * of EnhancedRandomGenerator objects that wrap the RandomGenerator objects in the
 * streams returned by the enclosed StreamableGenerator. However, additionally
 * this class provides a counterpart, such as {@link #erngs}, for each of these 
 * whose return type is explicitly a 
 * stream of EnhancedRandomGenerator objects to simplify usage without the need to cast
 * the objects of the stream in order to utilize the enhancements.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class EnhancedStreamableGenerator extends EnhancedRandomGenerator implements RandomGenerator.StreamableGenerator {
	
	private final RandomGenerator.StreamableGenerator generator;
	
	/**
	 * Constructs the EnhancedStreamableGenerator to wrap an instance
	 * of a random number generator initialized with a specified seed
	 * to enable replicating the same sequence of random numbers during
	 * subsequent runs.
	 *
	 * @param seed The seed for the random number generator.
	 */
	public EnhancedStreamableGenerator(long seed) {
		this(new SplittableRandom(seed));
	}
	
	/**
	 * Constructs an EnhancedStreamableGenerator to wrap and enhance a given 
	 * {@link RandomGenerator.StreamableGenerator}.
	 *
	 * @param generator the RandomGenerator.StreamableGenerator to wrap and enhance.
	 */
	public EnhancedStreamableGenerator(RandomGenerator.StreamableGenerator generator) {
		super(generator);
		this.generator = generator;
	}
	
	/**
	 * Constructs the EnhancedStreamableGenerator to wrap an instance of
	 * any random number generator supported by your version of Java
	 * as specified by its name, as documented via the 
	 * {@link RandomGenerator.StreamableGenerator#of}
	 * method.
	 *
	 * @param algorithmName The name of the random number generator as documented
	 * by the {@link RandomGenerator.StreamableGenerator#of} method.
	 *
	 * @throws NullPointerException if algorithmName is null.
	 * @throws IllegalArgumentException if algorithmName is not found.
	 */
	public EnhancedStreamableGenerator(String algorithmName) {
		this(RandomGenerator.StreamableGenerator.of(algorithmName));
	}
	
	/**
	 * Gets an EnhancedStreamableGenerator wrapping an instance of
	 * any random number generator supported by your version of Java
	 * as specified by its name, as documented via the 
	 * {@link RandomGenerator.StreamableGenerator#of}
	 * method.
	 *
	 * @param algorithmName The name of the random number generator as documented
	 * by the {@link RandomGenerator.StreamableGenerator#of} method.
	 *
	 * @return an EnhancedStreamableGenerator wrapping an instance of your chosen
	 * random number generator.
	 *
	 * @throws NullPointerException if algorithmName is null.
	 * @throws IllegalArgumentException if algorithmName is not found.
	 */
	public static EnhancedStreamableGenerator of(String algorithmName) {
		return new EnhancedStreamableGenerator(algorithmName);
	}
	
	/**
	 * <p>Gets an effectively unlimited stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one.</p>
	 *
	 * <p>The behavior of this method is identical to that of {@link #rngs}, but may be
	 * more convenient to use due to explicit return type of a stream of EnhancedRandomGenerator
	 * objects.</p>
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	public final Stream<EnhancedRandomGenerator> erngs() {
		return generator.rngs().map(gen -> new EnhancedRandomGenerator(gen));
	}
	
	/**
	 * <p>Gets a stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one.</p>
	 *
	 * <p>The behavior of this method is identical to that of {@link #rngs(long)}, but may be
	 * more convenient to use due to explicit return type of a stream of EnhancedRandomGenerator
	 * objects.</p>
	 *
	 * @param streamSize the number of EnhancedRandomGenerator objects in the stream
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	public final Stream<EnhancedRandomGenerator> erngs(long streamSize) {
		return generator.rngs(streamSize).map(gen -> new EnhancedRandomGenerator(gen));
	}
	
	/**
	 * <p>Gets an effectively unlimited stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one.</p>
	 *
	 * <p>The implementation of this method from the {@link RandomGenerator.StreamableGenerator}
	 * interface returns a stream of {@link EnhancedRandomGenerator} objects, and is thus
	 * safe to cast the objects of the stream to {@link EnhancedRandomGenerator}. However,
	 * you may find it more convenient to instead utilize the {@link #erngs} method, whose
	 * behavior is identical aside from the return type.</p>
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	@Override
	public final Stream<RandomGenerator> rngs() {
		return generator.rngs().map(gen -> new EnhancedRandomGenerator(gen));
	}
	
	/**
	 * <p>Gets a stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one.</p> 
	 *
	 * <p>The implementation of this method from the {@link RandomGenerator.StreamableGenerator}
	 * interface returns a stream of {@link EnhancedRandomGenerator} objects, and is thus
	 * safe to cast the objects of the stream to {@link EnhancedRandomGenerator}. However,
	 * you may find it more convenient to instead utilize the {@link #erngs(long)} method, whose
	 * behavior is identical aside from the return type.</p>
	 *
	 * @param streamSize the number of EnhancedRandomGenerator objects in the stream
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	@Override
	public final Stream<RandomGenerator> rngs(long streamSize) {
		return generator.rngs(streamSize).map(gen -> new EnhancedRandomGenerator(gen));
	}
}
