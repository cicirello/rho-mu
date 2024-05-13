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

import java.util.Arrays;
import org.junit.jupiter.api.*;

/** JUnit tests for Shuffler. */
public abstract class AbstractShufflerTests {

  private final Randomizer gen;
  private static final int MAX_TRIALS = 100;

  AbstractShufflerTests(Randomizer gen) {
    this.gen = gen;
  }

  static interface Randomizer {
    void shuffle(byte[] array);

    void shuffle(char[] array);

    void shuffle(double[] array);

    void shuffle(float[] array);

    void shuffle(int[] array);

    void shuffle(long[] array);

    void shuffle(short[] array);

    void shuffle(Object[] array);
  }

  @Test
  public final void testByte() {
    byte[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (byte[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        byte[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }

  @Test
  public final void testChar() {
    char[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (char[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        char[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }

  @Test
  public final void testDouble() {
    double[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (double[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        double[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = (int) Math.round(testCase[j] - 42);
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }

  @Test
  public final void testFloat() {
    float[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (float[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        float[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = (int) Math.round(testCase[j] - 42);
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }

  @Test
  public final void testInt() {
    int[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (int[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        int[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }

  @Test
  public final void testLong() {
    long[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (long[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        long[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = (int) (testCase[j] - 42);
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }

  @Test
  public final void testShort() {
    short[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (short[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        short[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }

  @Test
  public final void testObject() {
    Integer[][] cases = {{}, {42}, {42, 43}, {42, 43, 44}, {42, 43, 44, 45}};
    for (Integer[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length][c.length];
      int expectedCaseCount = c.length * c.length;
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        Integer[] testCase = c.clone();
        gen.shuffle(testCase);
        for (int j = 0; j < c.length; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j][k]) {
            caseCount++;
            elementAtIndex[j][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }
}
