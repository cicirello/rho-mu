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

/**
 * <p>An EnhancedJumpableGenerator is used to wrap an object of any
 * class that implements {@link RandomGenerator.JumpableGenerator} for the purpose
 * of adding all of the functionality of the {@link RandomIndexer}
 * and {@link RandomVariates}. See the superclass {@link EnhancedRandomGenerator}
 * for documentation of the enhanced functionality that is added to the
 * wrapped object.</p>
 *
 * <p>The methods of the {@link RandomGenerator.JumpableGenerator} interface,
 * such as {@link #jumps}, that return streams of RandomGenerator 
 * are implemented to return streams
 * of EnhancedRandomGenerator objects that wrap the RandomGenerator 
 * objects in the
 * streams returned by the enclosed RandomGenerator.JumpableGenerator. However, additionally
 * this class provides a counterpart, such as {@link #ejumps}, for each of these methods
 * whose return type is explicitly a 
 * stream of EnhancedRandomGenerator objects to simplify usage without the need to cast
 * the objects of the stream in order to utilize the enhancements.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class EnhancedJumpableGenerator extends EnhancedStreamableGenerator implements RandomGenerator.JumpableGenerator {
	
	private final RandomGenerator.JumpableGenerator generator;
	
	/**
	 * Constructs an EnhancedJumpableGenerator to wrap and enhance a given 
	 * {@link RandomGenerator.JumpableGenerator}.
	 *
	 * @param generator the RandomGenerator.JumpableGenerator to wrap and enhance.
	 */
	public EnhancedJumpableGenerator(RandomGenerator.JumpableGenerator generator) {
		super(generator);
		this.generator = generator;
	}
	
	/**
	 * Constructs the EnhancedJumpableGenerator to wrap an instance of
	 * any random number generator supported by your version of Java
	 * as specified by its name, as documented via the 
	 * {@link RandomGenerator.JumpableGenerator#of}
	 * method.
	 *
	 * @param algorithmName The name of the random number generator as documented
	 * by the {@link RandomGenerator.JumpableGenerator#of} method.
	 *
	 * @throws NullPointerException if algorithmName is null.
	 * @throws IllegalArgumentException if algorithmName is not found.
	 */
	public EnhancedJumpableGenerator(String algorithmName) {
		this(RandomGenerator.JumpableGenerator.of(algorithmName));
	}
	
	/**
	 * Gets an EnhancedJumpableGenerator wrapping an instance of
	 * any random number generator supported by your version of Java
	 * as specified by its name, as documented via the 
	 * {@link RandomGenerator.JumpableGenerator#of}
	 * method.
	 *
	 * @param algorithmName The name of the random number generator as documented
	 * by the {@link RandomGenerator.JumpableGenerator#of} method.
	 *
	 * @return an EnhancedJumpableGenerator wrapping an instance of your chosen
	 * random number generator.
	 *
	 * @throws NullPointerException if algorithmName is null.
	 * @throws IllegalArgumentException if algorithmName is not found.
	 */
	public static EnhancedJumpableGenerator of(String algorithmName) {
		return new EnhancedJumpableGenerator(algorithmName);
	}
	
	/**
	 * Creates and returns a new EnhancedJumpableGenerator whose internal
	 * state is an exact copy of this one. Thus, if subjected to the same series
	 * of actions, this EnhancedJumpableGenerator and its copy should behave
	 * identically.
	 *
	 * @return a new EnhancedJumpableGenerator with internal state identical to this one.
	 */
	@Override
	public EnhancedJumpableGenerator copy() {
		return new EnhancedJumpableGenerator(generator.copy());
	}
	
	/**
	 * Creates a copy of this EnhancedJumpableGenerator, jumps this
	 * EnhancedJumpableGenerator forward, and then returns the copy.
	 *
	 * @return a copy of this EnhancedJumpableGenerator before the jump occurred.
	 */
	@Override
	public EnhancedJumpableGenerator copyAndJump() {
		EnhancedJumpableGenerator c = copy();
		jump();
		return c;
	}
	
	/**
	 * Changes the state of this EnhancedJumpableGenerator to jump a large fixed distance,
	 * indicated by the {@link #jumpDistance} method, in its state cycle.
	 */
	@Override
	public final void jump() {
		generator.jump();
	}
	
	/**
	 * Returns the distance by which the {@link #jump} method will jump ahead in this
	 * EnhancedJumpableGenerator instances state cycle.
	 *
	 * @return the jump distance
	 */
	@Override
	public final double jumpDistance() {
		return generator.jumpDistance();
	}
	
	/**
	 * <p>Gets an effectively unlimited stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one. The random number generators in the stream are not also jumpable.</p>
	 *
	 * <p>The behavior of this method is identical to that of {@link #jumps}, but may be
	 * more convenient to use due to explicit return type of a stream of EnhancedRandomGenerator
	 * objects.</p>
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	public final Stream<EnhancedRandomGenerator> ejumps() {
		return generator.jumps().map(gen -> new EnhancedRandomGenerator(gen));
	}
	
	/**
	 * <p>Gets a stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one. The random number generators in the stream are not also jumpable.</p>
	 *
	 * <p>The behavior of this method is identical to that of {@link #jumps(long)}, but may be
	 * more convenient to use due to explicit return type of a stream of EnhancedRandomGenerator
	 * objects.</p>
	 *
	 * @param streamSize the number of EnhancedRandomGenerator objects in the stream
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	public final Stream<EnhancedRandomGenerator> ejumps(long streamSize) {
		return generator.jumps(streamSize).map(gen -> new EnhancedRandomGenerator(gen));
	}
	
	/**
	 * <p>Gets an effectively unlimited stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one. The random number generators in the stream are not also jumpable.</p>
	 *
	 * <p>The implementation of this method from the {@link RandomGenerator.JumpableGenerator}
	 * interface returns a stream of {@link EnhancedRandomGenerator} objects, and is thus
	 * safe to cast the objects of the stream to {@link EnhancedRandomGenerator}. However,
	 * you may find it more convenient to instead utilize the {@link #ejumps} method, whose
	 * behavior is identical aside from the return type.</p>
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	@Override
	public final Stream<RandomGenerator> jumps() {
		return generator.jumps().map(gen -> new EnhancedRandomGenerator(gen));
	}
	
	/**
	 * <p>Gets a stream of {@link EnhancedRandomGenerator} objects,
	 * each wrapping an object of the same random number generator algorithm as this
	 * one. The random number generators in the stream are not also jumpable.</p>
	 *
	 * <p>The implementation of this method from the {@link RandomGenerator.JumpableGenerator}
	 * interface returns a stream of {@link EnhancedRandomGenerator} objects, and is thus
	 * safe to cast the objects of the stream to {@link EnhancedRandomGenerator}. However,
	 * you may find it more convenient to instead utilize the {@link #ejumps(long)} method, whose
	 * behavior is identical aside from the return type.</p>
	 *
	 * @param streamSize the number of EnhancedRandomGenerator objects in the stream
	 *
	 * @return a stream of EnhancedRandomGenerator objects
	 */
	@Override
	public final Stream<RandomGenerator> jumps(long streamSize) {
		return generator.jumps(streamSize).map(gen -> new EnhancedRandomGenerator(gen));
	}
}
