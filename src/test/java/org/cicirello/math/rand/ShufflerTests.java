/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2024 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.SplittableRandom;
import org.junit.jupiter.api.*;

/** JUnit tests for Shuffler. */
public final class ShufflerTests {

  @Nested
  public final class ShufflerEnhancedRandomGeneratorTests extends AbstractShufflerTests {

    public ShufflerEnhancedRandomGeneratorTests() {
      super(
          new Randomizer() {

            private final EnhancedRandomGenerator gen =
                new EnhancedRandomGenerator(new SplittableRandom(42));

            @Override
            public void shuffle(byte[] array) {
              gen.shuffle(array);
            }

            @Override
            public void shuffle(char[] array) {
              gen.shuffle(array);
            }

            @Override
            public void shuffle(double[] array) {
              gen.shuffle(array);
            }

            @Override
            public void shuffle(float[] array) {
              gen.shuffle(array);
            }

            @Override
            public void shuffle(int[] array) {
              gen.shuffle(array);
            }

            @Override
            public void shuffle(long[] array) {
              gen.shuffle(array);
            }

            @Override
            public void shuffle(short[] array) {
              gen.shuffle(array);
            }

            @Override
            public <T> void shuffle(T[] array) {
              gen.shuffle(array);
            }

            @Override
            public <T> void shuffle(List<T> list) {
              gen.shuffle(list);
            }
          });
    }
  }

  @Nested
  public final class ShufflerFullRangeEnhancedRandomGeneratorTests extends AbstractShufflerTests {

    public ShufflerFullRangeEnhancedRandomGeneratorTests() {
      super(
          new Randomizer() {

            private final EnhancedRandomGenerator gen =
                new EnhancedRandomGenerator(new SplittableRandom(42));

            @Override
            public void shuffle(byte[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(char[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(double[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(float[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(int[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(long[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(short[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public <T> void shuffle(T[] array) {
              gen.shuffle(array, 0, array.length);
            }

            @Override
            public <T> void shuffle(List<T> list) {
              gen.shuffle(list, 0, list.size());
            }
          });
    }
  }

  @Nested
  public final class ShufflerPartialRangeEnhancedRandomGeneratorTests
      extends AbstractPartialShufflerTests {

    public ShufflerPartialRangeEnhancedRandomGeneratorTests() {
      super(
          new Randomizer() {

            private final EnhancedRandomGenerator gen =
                new EnhancedRandomGenerator(new SplittableRandom(42));

            @Override
            public void shuffle(byte[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public void shuffle(char[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public void shuffle(double[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public void shuffle(float[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public void shuffle(int[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public void shuffle(long[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public void shuffle(short[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public <T> void shuffle(T[] array, int first, int last) {
              gen.shuffle(array, first, last);
            }

            @Override
            public <T> void shuffle(List<T> list, int first, int last) {
              gen.shuffle(list, first, last);
            }
          });
    }
  }

  @Nested
  public final class ShufflerSpecificRandomGeneratorTests extends AbstractShufflerTests {

    public ShufflerSpecificRandomGeneratorTests() {
      super(
          new Randomizer() {

            private final SplittableRandom gen = new SplittableRandom(42);

            @Override
            public void shuffle(byte[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public void shuffle(char[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public void shuffle(double[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public void shuffle(float[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public void shuffle(int[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public void shuffle(long[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public void shuffle(short[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public <T> void shuffle(T[] array) {
              Shuffler.shuffle(array, gen);
            }

            @Override
            public <T> void shuffle(List<T> list) {
              Shuffler.shuffle(list, gen);
            }
          });
    }
  }

  @Nested
  public final class ShufflerFullRangeWithSpecificRandomGeneratorTests
      extends AbstractShufflerTests {

    public ShufflerFullRangeWithSpecificRandomGeneratorTests() {
      super(
          new Randomizer() {

            private final SplittableRandom gen = new SplittableRandom(42);

            @Override
            public void shuffle(byte[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public void shuffle(char[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public void shuffle(double[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public void shuffle(float[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public void shuffle(int[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public void shuffle(long[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public void shuffle(short[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public <T> void shuffle(T[] array) {
              Shuffler.shuffle(array, 0, array.length, gen);
            }

            @Override
            public <T> void shuffle(List<T> list) {
              Shuffler.shuffle(list, 0, list.size(), gen);
            }
          });
    }
  }

  @Nested
  public final class ShufflerPartialRangeSpecificRandomGeneratorTests
      extends AbstractPartialShufflerTests {

    public ShufflerPartialRangeSpecificRandomGeneratorTests() {
      super(
          new Randomizer() {

            private final SplittableRandom gen = new SplittableRandom(42);

            @Override
            public void shuffle(byte[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public void shuffle(char[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public void shuffle(double[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public void shuffle(float[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public void shuffle(int[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public void shuffle(long[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public void shuffle(short[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public <T> void shuffle(T[] array, int first, int last) {
              Shuffler.shuffle(array, first, last, gen);
            }

            @Override
            public <T> void shuffle(List<T> list, int first, int last) {
              Shuffler.shuffle(list, first, last, gen);
            }
          });
    }
  }

  @Nested
  public final class ShufflerThreadLocalRandomTests extends AbstractShufflerTests {

    public ShufflerThreadLocalRandomTests() {
      super(
          new Randomizer() {

            @Override
            public void shuffle(byte[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public void shuffle(char[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public void shuffle(double[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public void shuffle(float[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public void shuffle(int[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public void shuffle(long[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public void shuffle(short[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public <T> void shuffle(T[] array) {
              Shuffler.shuffle(array);
            }

            @Override
            public <T> void shuffle(List<T> list) {
              Shuffler.shuffle(list);
            }
          });
    }
  }

  @Nested
  public final class ShufflerFullRangeThreadLocalRandomTests extends AbstractShufflerTests {

    public ShufflerFullRangeThreadLocalRandomTests() {
      super(
          new Randomizer() {

            @Override
            public void shuffle(byte[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(char[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(double[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(float[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(int[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(long[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public void shuffle(short[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public <T> void shuffle(T[] array) {
              Shuffler.shuffle(array, 0, array.length);
            }

            @Override
            public <T> void shuffle(List<T> list) {
              Shuffler.shuffle(list, 0, list.size());
            }
          });
    }
  }

  @Nested
  public final class ShufflerPartialRangeThreadLocalRandomTests
      extends AbstractPartialShufflerTests {

    public ShufflerPartialRangeThreadLocalRandomTests() {
      super(
          new Randomizer() {

            @Override
            public void shuffle(byte[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public void shuffle(char[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public void shuffle(double[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public void shuffle(float[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public void shuffle(int[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public void shuffle(long[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public void shuffle(short[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public <T> void shuffle(T[] array, int first, int last) {
              Shuffler.shuffle(array, first, last);
            }

            @Override
            public <T> void shuffle(List<T> list, int first, int last) {
              Shuffler.shuffle(list, first, last);
            }
          });
    }
  }
}
