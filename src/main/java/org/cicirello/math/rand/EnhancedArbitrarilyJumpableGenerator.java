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
 * An EnhancedArbitrarilyJumpableGenerator is used to wrap an object of any class that implements
 * {@link RandomGenerator.ArbitrarilyJumpableGenerator} for the purpose of adding all of the
 * functionality of the {@link RandomIndexer}, {@link RandomSampler}, and {@link RandomVariates}.
 * See the superclass {@link EnhancedRandomGenerator} for documentation of the enhanced
 * functionality that is added to the wrapped object.
 *
 * <p>The methods of the {@link RandomGenerator.ArbitrarilyJumpableGenerator} interface, such as
 * {@link #jumps(double)}, that return streams of RandomGenerator.ArbitrarilyJumpableGenerator are
 * implemented to return streams of EnhancedArbitrarilyJumpableGenerator objects that wrap the
 * RandomGenerator.ArbitrarilyJumpableGenerator objects in the streams returned by the enclosed
 * RandomGenerator.ArbitrarilyJumpableGenerator. However, additionally this class provides a
 * counterpart, such as {@link #ejumps(double)}, for each of these methods whose return type is
 * explicitly a stream of EnhancedArbitrarilyJumpableGenerator objects to simplify usage without the
 * need to cast the objects of the stream in order to utilize the enhancements.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class EnhancedArbitrarilyJumpableGenerator extends EnhancedLeapableGenerator
    implements RandomGenerator.ArbitrarilyJumpableGenerator {

  private final RandomGenerator.ArbitrarilyJumpableGenerator generator;

  /**
   * Constructs an EnhancedArbitrarilyJumpableGenerator to wrap and enhance a given {@link
   * RandomGenerator.ArbitrarilyJumpableGenerator}.
   *
   * @param generator the RandomGenerator.ArbitrarilyJumpableGenerator to wrap and enhance.
   */
  public EnhancedArbitrarilyJumpableGenerator(
      RandomGenerator.ArbitrarilyJumpableGenerator generator) {
    super(generator);
    this.generator = generator;
  }

  /**
   * Constructs the EnhancedArbitrarilyJumpableGenerator to wrap an instance of any random number
   * generator supported by your version of Java as specified by its name, as documented via the
   * {@link RandomGenerator.ArbitrarilyJumpableGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator.ArbitrarilyJumpableGenerator#of} method.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public EnhancedArbitrarilyJumpableGenerator(String algorithmName) {
    this(RandomGenerator.ArbitrarilyJumpableGenerator.of(algorithmName));
  }

  /**
   * Gets an EnhancedArbitrarilyJumpableGenerator wrapping an instance of any random number
   * generator supported by your version of Java as specified by its name, as documented via the
   * {@link RandomGenerator.ArbitrarilyJumpableGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator.ArbitrarilyJumpableGenerator#of} method.
   * @return an EnhancedArbitrarilyJumpableGenerator wrapping an instance of your chosen random
   *     number generator.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public static EnhancedArbitrarilyJumpableGenerator of(String algorithmName) {
    return new EnhancedArbitrarilyJumpableGenerator(algorithmName);
  }

  /**
   * Creates and returns a new EnhancedArbitrarilyJumpableGenerator whose internal state is an exact
   * copy of this one. Thus, if subjected to the same series of actions, this
   * EnhancedArbitrarilyJumpableGenerator and its copy should behave identically.
   *
   * @return a new EnhancedArbitrarilyJumpableGenerator with internal state identical to this one.
   */
  @Override
  public EnhancedArbitrarilyJumpableGenerator copy() {
    return new EnhancedArbitrarilyJumpableGenerator(generator.copy());
  }

  /**
   * Creates a copy of this EnhancedArbitrarilyJumpableGenerator, jumps this
   * EnhancedArbitrarilyJumpableGenerator forward, and then returns the copy.
   *
   * @param distance the distance to jump ahead in the state cycle
   * @return a copy of this EnhancedArbitrarilyJumpableGenerator before the jump occurred.
   */
  @Override
  public final EnhancedArbitrarilyJumpableGenerator copyAndJump(double distance) {
    EnhancedArbitrarilyJumpableGenerator c = copy();
    jump(distance);
    return c;
  }

  /**
   * Changes the state of this EnhancedArbitrarilyJumpableGenerator to jump a fixed distance ahead
   * in its state cycle.
   *
   * @param distance the distance to jump ahead in the state cycle
   */
  @Override
  public final void jump(double distance) {
    generator.jump(distance);
  }

  /**
   * Changes the state of this EnhancedArbitrarilyJumpableGenerator to jump a distance of
   * 2<sup>logDistance</sup> ahead in its state cycle.
   *
   * @param logDistance the base-2 logarithm of the distance to jump ahead in the state cycle
   */
  @Override
  public final void jumpPowerOfTwo(int logDistance) {
    generator.jumpPowerOfTwo(logDistance);
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedArbitrarilyJumpableGenerator} objects,
   * each wrapping an object of the same random number generator algorithm as this one.
   *
   * <p>The implementation of this method from the {@link
   * RandomGenerator.ArbitrarilyJumpableGenerator} interface returns a stream of {@link
   * EnhancedArbitrarilyJumpableGenerator} objects, and is thus safe to cast the objects of the
   * stream to {@link EnhancedArbitrarilyJumpableGenerator}. However, you may find it more
   * convenient to instead utilize the {@link #ejumps(double)} method, whose behavior is identical
   * aside from the return type.
   *
   * @param distance the distance to jump ahead in the state cycle
   * @return a stream of EnhancedArbitrarilyJumpableGenerator objects
   */
  public final Stream<EnhancedArbitrarilyJumpableGenerator> ejumps(double distance) {
    return generator.jumps(distance).map(gen -> new EnhancedArbitrarilyJumpableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedArbitrarilyJumpableGenerator} objects, each wrapping an object
   * of the same random number generator algorithm as this one.
   *
   * <p>The implementation of this method from the {@link
   * RandomGenerator.ArbitrarilyJumpableGenerator} interface returns a stream of {@link
   * EnhancedArbitrarilyJumpableGenerator} objects, and is thus safe to cast the objects of the
   * stream to {@link EnhancedArbitrarilyJumpableGenerator}. However, you may find it more
   * convenient to instead utilize the {@link #ejumps(long, double)} method, whose behavior is
   * identical aside from the return type.
   *
   * @param streamSize the number of EnhancedArbitrarilyJumpableGenerator objects in the stream
   * @param distance the distance to jump ahead in the state cycle
   * @return a stream of EnhancedArbitrarilyJumpableGenerator objects
   */
  public final Stream<EnhancedArbitrarilyJumpableGenerator> ejumps(
      long streamSize, double distance) {
    return generator
        .jumps(streamSize, distance)
        .map(gen -> new EnhancedArbitrarilyJumpableGenerator(gen));
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedArbitrarilyJumpableGenerator} objects,
   * each wrapping an object of the same random number generator algorithm as this one.
   *
   * <p>The implementation of this method from the {@link
   * RandomGenerator.ArbitrarilyJumpableGenerator} interface returns a stream of {@link
   * EnhancedArbitrarilyJumpableGenerator} objects, and is thus safe to cast the objects of the
   * stream to {@link EnhancedArbitrarilyJumpableGenerator}. However, you may find it more
   * convenient to instead utilize the {@link #ejumps(double)} method, whose behavior is identical
   * aside from the return type.
   *
   * @param distance the distance to jump ahead in the state cycle
   * @return a stream of EnhancedArbitrarilyJumpableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.ArbitrarilyJumpableGenerator> jumps(double distance) {
    return generator.jumps(distance).map(gen -> new EnhancedArbitrarilyJumpableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedArbitrarilyJumpableGenerator} objects, each wrapping an object
   * of the same random number generator algorithm as this one.
   *
   * <p>The implementation of this method from the {@link
   * RandomGenerator.ArbitrarilyJumpableGenerator} interface returns a stream of {@link
   * EnhancedArbitrarilyJumpableGenerator} objects, and is thus safe to cast the objects of the
   * stream to {@link EnhancedArbitrarilyJumpableGenerator}. However, you may find it more
   * convenient to instead utilize the {@link #ejumps(long, double)} method, whose behavior is
   * identical aside from the return type.
   *
   * @param streamSize the number of EnhancedArbitrarilyJumpableGenerator objects in the stream
   * @param distance the distance to jump ahead in the state cycle
   * @return a stream of EnhancedArbitrarilyJumpableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.ArbitrarilyJumpableGenerator> jumps(
      long streamSize, double distance) {
    return generator
        .jumps(streamSize, distance)
        .map(gen -> new EnhancedArbitrarilyJumpableGenerator(gen));
  }
}
