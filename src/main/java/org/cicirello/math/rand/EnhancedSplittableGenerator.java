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

import java.util.SplittableRandom;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

/**
 * An EnhancedSplittableGenerator is used to wrap an object of any class that implements {@link
 * RandomGenerator.SplittableGenerator} for the purpose of adding all of the functionality of the
 * {@link RandomIndexer}, {@link RandomSampler}, and {@link RandomVariates}. See the superclass
 * {@link EnhancedRandomGenerator} for documentation of the enhanced functionality that is added to
 * the wrapped object.
 *
 * <p>The methods of the {@link RandomGenerator.SplittableGenerator} interface, such as {@link
 * #splits}, that return streams of RandomGenerator.SplittableGenerator are implemented to return
 * streams of EnhancedSplittableGenerator objects that wrap the RandomGenerator.SplittableGenerator
 * objects in the streams returned by the enclosed RandomGenerator.SplittableGenerator. However,
 * additionally this class provides a counterpart, such as {@link #esplits}, for each of these whose
 * return type is explicitly a stream of EnhancedSplittableGenerator objects to simplify usage
 * without the need to cast the objects of the stream in order to utilize the enhancements.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class EnhancedSplittableGenerator extends EnhancedStreamableGenerator
    implements RandomGenerator.SplittableGenerator {

  private final RandomGenerator.SplittableGenerator generator;

  /**
   * Constructs the EnhancedSplittableGenerator to wrap an instance of a default
   * SplittableGenerator. The library's current default is to wrap an instance of {@link
   * SplittableRandom}, chosen due to Java's designation of that class as a legacy random number
   * generator, and likely longterm existence. However, the rho-mu library makes no guarantee that
   * this choice will remain the default.
   */
  public EnhancedSplittableGenerator() {
    this(new SplittableRandom());
  }

  /**
   * Constructs the EnhancedSplittableGenerator to wrap an instance of a random number generator
   * initialized with a specified seed to enable replicating the same sequence of random numbers
   * during subsequent runs.
   *
   * @param seed The seed for the random number generator.
   */
  public EnhancedSplittableGenerator(long seed) {
    this(new SplittableRandom(seed));
  }

  /**
   * Constructs an EnhancedSplittableGenerator to wrap and enhance a given {@link
   * RandomGenerator.SplittableGenerator}.
   *
   * @param generator the RandomGenerator.SplittableGenerator to wrap and enhance.
   */
  public EnhancedSplittableGenerator(RandomGenerator.SplittableGenerator generator) {
    super(generator);
    this.generator = generator;
  }

  /**
   * Constructs the EnhancedSplittableGenerator to wrap an instance of any random number generator
   * supported by your version of Java as specified by its name, as documented via the {@link
   * RandomGenerator.SplittableGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator.SplittableGenerator#of} method.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public EnhancedSplittableGenerator(String algorithmName) {
    this(RandomGenerator.SplittableGenerator.of(algorithmName));
  }

  /**
   * Gets an EnhancedSplittableGenerator wrapping an instance of any random number generator
   * supported by your version of Java as specified by its name, as documented via the {@link
   * RandomGenerator.SplittableGenerator#of} method.
   *
   * @param algorithmName The name of the random number generator as documented by the {@link
   *     RandomGenerator.SplittableGenerator#of} method.
   * @return an EnhancedSplittableGenerator wrapping an instance of your chosen random number
   *     generator.
   * @throws NullPointerException if algorithmName is null.
   * @throws IllegalArgumentException if algorithmName is not found.
   */
  public static EnhancedSplittableGenerator of(String algorithmName) {
    return new EnhancedSplittableGenerator(algorithmName);
  }

  /**
   * Returns a new EnhancedSplittableGenerator split off from this one, such that the new one wraps
   * a split of the wrapped random number generator.
   *
   * @return a new EnhancedSplittableGenerator split off from this one
   */
  @Override
  public final EnhancedSplittableGenerator split() {
    return new EnhancedSplittableGenerator(generator.split());
  }

  /**
   * Returns a new EnhancedSplittableGenerator split off from this one, such that the new one wraps
   * a split of the wrapped random number generator.
   *
   * @param source The source of randomness used to initialize the split rather than this one.
   * @return a new EnhancedSplittableGenerator split off from this one
   */
  @Override
  public final EnhancedSplittableGenerator split(RandomGenerator.SplittableGenerator source) {
    return new EnhancedSplittableGenerator(generator.split(source));
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The behavior of this method is identical to that of {@link #splits}, but may be more
   * convenient to use due to explicit return type of a stream of EnhancedSplittableGenerator
   * objects.
   *
   * @return a stream of EnhancedSplittableGenerator objects
   */
  public final Stream<EnhancedSplittableGenerator> esplits() {
    return generator.splits().map(gen -> new EnhancedSplittableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The behavior of this method is identical to that of {@link #splits(long)}, but may be more
   * convenient to use due to explicit return type of a stream of EnhancedSplittableGenerator
   * objects.
   *
   * @param streamSize the number of EnhancedSplittableGenerator objects in the stream
   * @return a stream of EnhancedSplittableGenerator objects
   */
  public final Stream<EnhancedSplittableGenerator> esplits(long streamSize) {
    return generator.splits(streamSize).map(gen -> new EnhancedSplittableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The behavior of this method is identical to that of {@link #splits(long,
   * RandomGenerator.SplittableGenerator)}, but may be more convenient to use due to explicit return
   * type of a stream of EnhancedSplittableGenerator objects.
   *
   * @param streamSize the number of EnhancedSplittableGenerator objects in the stream
   * @param source The source of randomness used to initialize the splits rather than this one.
   * @return a stream of EnhancedSplittableGenerator objects
   */
  public final Stream<EnhancedSplittableGenerator> esplits(
      long streamSize, RandomGenerator.SplittableGenerator source) {
    return generator.splits(streamSize, source).map(gen -> new EnhancedSplittableGenerator(gen));
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The behavior of this method is identical to that of {@link
   * #splits(RandomGenerator.SplittableGenerator)}, but may be more convenient to use due to
   * explicit return type of a stream of EnhancedSplittableGenerator objects.
   *
   * @param source The source of randomness used to initialize the splits rather than this one.
   * @return a stream of EnhancedSplittableGenerator objects
   */
  public final Stream<EnhancedSplittableGenerator> esplits(
      RandomGenerator.SplittableGenerator source) {
    return generator.splits(source).map(gen -> new EnhancedSplittableGenerator(gen));
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The implementation of this method from the {@link RandomGenerator.SplittableGenerator}
   * interface returns a stream of {@link EnhancedSplittableGenerator} objects, and is thus safe to
   * cast the objects of the stream to {@link EnhancedSplittableGenerator}. However, you may find it
   * more convenient to instead utilize the {@link #esplits} method, whose behavior is identical
   * aside from the return type.
   *
   * @return a stream of EnhancedSplittableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.SplittableGenerator> splits() {
    return generator.splits().map(gen -> new EnhancedSplittableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The implementation of this method from the {@link RandomGenerator.SplittableGenerator}
   * interface returns a stream of {@link EnhancedSplittableGenerator} objects, and is thus safe to
   * cast the objects of the stream to {@link EnhancedSplittableGenerator}. However, you may find it
   * more convenient to instead utilize the {@link #esplits(long)} method, whose behavior is
   * identical aside from the return type.
   *
   * @param streamSize the number of EnhancedSplittableGenerator objects in the stream
   * @return a stream of EnhancedSplittableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.SplittableGenerator> splits(long streamSize) {
    return generator.splits(streamSize).map(gen -> new EnhancedSplittableGenerator(gen));
  }

  /**
   * Gets a stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The implementation of this method from the {@link RandomGenerator.SplittableGenerator}
   * interface returns a stream of {@link EnhancedSplittableGenerator} objects, and is thus safe to
   * cast the objects of the stream to {@link EnhancedSplittableGenerator}. However, you may find it
   * more convenient to instead utilize the {@link #esplits(long,
   * RandomGenerator.SplittableGenerator)} method, whose behavior is identical aside from the return
   * type.
   *
   * @param streamSize the number of EnhancedSplittableGenerator objects in the stream
   * @param source The source of randomness used to initialize the splits rather than this one.
   * @return a stream of EnhancedSplittableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.SplittableGenerator> splits(
      long streamSize, RandomGenerator.SplittableGenerator source) {
    return generator.splits(streamSize, source).map(gen -> new EnhancedSplittableGenerator(gen));
  }

  /**
   * Gets an effectively unlimited stream of {@link EnhancedSplittableGenerator} objects.
   *
   * <p>The implementation of this method from the {@link RandomGenerator.SplittableGenerator}
   * interface returns a stream of {@link EnhancedSplittableGenerator} objects, and is thus safe to
   * cast the objects of the stream to {@link EnhancedSplittableGenerator}. However, you may find it
   * more convenient to instead utilize the {@link #esplits(RandomGenerator.SplittableGenerator)}
   * method, whose behavior is identical aside from the return type.
   *
   * @param source The source of randomness used to initialize the splits rather than this one.
   * @return a stream of EnhancedSplittableGenerator objects
   */
  @Override
  public final Stream<RandomGenerator.SplittableGenerator> splits(
      RandomGenerator.SplittableGenerator source) {
    return generator.splits(source).map(gen -> new EnhancedSplittableGenerator(gen));
  }

  @Override
  public final Stream<EnhancedRandomGenerator> erngs() {
    return this.esplits().map(x -> x);
  }

  @Override
  public final Stream<EnhancedRandomGenerator> erngs(long streamSize) {
    return this.esplits(streamSize).map(x -> x);
  }

  @Override
  public final Stream<RandomGenerator> rngs() {
    return this.splits().map(x -> x);
  }

  @Override
  public final Stream<RandomGenerator> rngs(long streamSize) {
    return this.splits(streamSize).map(x -> x);
  }
}
