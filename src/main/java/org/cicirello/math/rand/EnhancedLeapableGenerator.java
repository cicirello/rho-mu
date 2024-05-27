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

import java.util.random.RandomGenerator;
import java.util.stream.Stream;

/**
 * An EnhancedLeapableGenerator is used to wrap an object of any class that implements {@link
 * RandomGenerator.LeapableGenerator} for the purpose of adding all of the functionality of the
 * {@link RandomIndexer} and {@link RandomVariates}. See the superclass {@link
 * EnhancedRandomGenerator} for documentation of the enhanced functionality that is added to the
 * wrapped object.
 *
 * <p>The methods of the {@link RandomGenerator.LeapableGenerator} interface, such as {@link
 * #leaps}, that return streams of RandomGenerator.JumpableGenerator are implemented to return
 * streams of EnhancedJumpableGenerator objects that wrap the RandomGenerator.JumpableGenerator
 * objects in the streams returned by the enclosed RandomGenerator.LeapableGenerator. However,
 * additionally this class provides a counterpart, such as {@link #eleaps}, for each of these
 * methods whose return type is explicitly a stream of EnhancedJumpableGenerator objects to simplify
 * usage without the need to cast the objects of the stream in order to utilize the enhancements.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class EnhancedLeapableGenerator extends EnhancedJumpableGenerator
    implements RandomGenerator.LeapableGenerator {

  private final RandomGenerator.LeapableGenerator generator;

  /**
   * Constructs an EnhancedLeapableGenerator to wrap and enhance a given {@link
   * RandomGenerator.LeapableGenerator}.
   *
   * @param generator the RandomGenerator.LeapableGenerator to wrap and enhance.
   */
  public EnhancedLeapableGenerator(RandomGenerator.LeapableGenerator generator) {
    super(generator);
    this.generator = generator;
  }

  /**
   * Constructs the EnhancedLeapableGenerator to wrap an instance of any random number generator
   * supported by your version of Java as specified by its name, as documented via the {@link
   * RandomGenerator.LeapableGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator.LeapableGenerator#of} method.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public EnhancedLeapableGenerator(String algorithmName) {
    this(RandomGenerator.LeapableGenerator.of(algorithmName));
  }

  /**
   * Gets an EnhancedLeapableGenerator wrapping an instance of any random number generator supported
   * by your version of Java as specified by its name, as documented via the {@link
   * RandomGenerator.LeapableGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator.LeapableGenerator#of} method.
   * @return an EnhancedLeapableGenerator wrapping an instance of your chosen random number
   *     generator.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public static EnhancedLeapableGenerator of(String algorithmName) {
    return new EnhancedLeapableGenerator(algorithmName);
  }

  /**
   * Creates and returns a new EnhancedLeapableGenerator whose internal state is an exact copy of
   * this one. Thus, if subjected to the same series of actions, this EnhancedLeapableGenerator and
   * its copy should behave identically.
   *
   * @return a new EnhancedLeapableGenerator with internal state identical to this one.
   */
  @Override
  public EnhancedLeapableGenerator copy() {
    return new EnhancedLeapableGenerator(generator.copy());
  }

  /**
   * Creates a copy of this EnhancedLeapableGenerator, leaps this EnhancedLeapableGenerator forward,
   * and then returns the copy.
   *
   * @return a copy of this EnhancedLeapableGenerator before the leap occurred.
   */
  @Override
  public final EnhancedLeapableGenerator copyAndLeap() {
    EnhancedLeapableGenerator c = copy();
    leap();
    return c;
  }

  /**
   * Changes the state of this EnhancedLeapableGenerator to leap a large fixed distance, indicated
   * by the {@link #leapDistance} method, in its state cycle.
   */
  @Override
  public final void leap() {
    generator.leap();
  }

  /**
   * Returns the distance by which the {@link #leap} method will leap ahead in this
   * EnhancedLeapableGenerator instance's state cycle.
   *
   * @return the leap distance
   */
  @Override
  public final double leapDistance() {
    return generator.leapDistance();
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedJumpableGenerator} objects, each
   * wrapping an object of the same random number generator algorithm as this one. Although
   * jumpable, the random number generators in the stream are not also leapable.
   *
   * <p>The behavior of this method is identical to that of {@link #leaps}, but may be more
   * convenient to use due to explicit return type of a stream of EnhancedJumpableGenerator objects.
   *
   * @return a stream of EnhancedJumpableGenerator objects
   */
  public final Stream<EnhancedJumpableGenerator> eleaps() {
    return generator.leaps().map(gen -> new EnhancedJumpableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedJumpableGenerator} objects, each wrapping an object of the same
   * random number generator algorithm as this one. Although jumpable, the random number generators
   * in the stream are not also leapable.
   *
   * <p>The behavior of this method is identical to that of {@link #leaps(long)}, but may be more
   * convenient to use due to explicit return type of a stream of EnhancedJumpableGenerator objects.
   *
   * @param streamSize the number of EnhancedJumpableGenerator objects in the stream
   * @return a stream of EnhancedJumpableGenerator objects
   */
  public final Stream<EnhancedJumpableGenerator> eleaps(long streamSize) {
    return generator.leaps(streamSize).map(gen -> new EnhancedJumpableGenerator(gen));
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedJumpableGenerator} objects, each
   * wrapping an object of the same random number generator algorithm as this one. Although
   * jumpable, the random number generators in the stream are not also leapable.
   *
   * <p>The implementation of this method from the {@link RandomGenerator.LeapableGenerator}
   * interface returns a stream of {@link EnhancedJumpableGenerator} objects, and is thus safe to
   * cast the objects of the stream to {@link EnhancedJumpableGenerator}. However, you may find it
   * more convenient to instead utilize the {@link #eleaps} method, whose behavior is identical
   * aside from the return type.
   *
   * @return a stream of EnhancedJumpableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.JumpableGenerator> leaps() {
    return generator.leaps().map(gen -> new EnhancedJumpableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedJumpableGenerator} objects, each wrapping an object of the same
   * random number generator algorithm as this one. Although jumpable, the random number generators
   * in the stream are not also leapable.
   *
   * <p>The implementation of this method from the {@link RandomGenerator.JumpableGenerator}
   * interface returns a stream of {@link EnhancedJumpableGenerator} objects, and is thus safe to
   * cast the objects of the stream to {@link EnhancedJumpableGenerator}. However, you may find it
   * more convenient to instead utilize the {@link #eleaps(long)} method, whose behavior is
   * identical aside from the return type.
   *
   * @param streamSize the number of EnhancedJumpableGenerator objects in the stream
   * @return a stream of EnhancedJumpableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.JumpableGenerator> leaps(long streamSize) {
    return generator.leaps(streamSize).map(gen -> new EnhancedJumpableGenerator(gen));
  }
}
