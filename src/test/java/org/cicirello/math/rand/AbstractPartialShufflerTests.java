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
public abstract class AbstractPartialShufflerTests {

  private final Randomizer gen;
  private static final int MAX_TRIALS = 100;

  AbstractPartialShufflerTests(Randomizer gen) {
    this.gen = gen;
  }

  static interface Randomizer {
    void shuffle(byte[] array, int first, int last);

    void shuffle(char[] array, int first, int last);

    void shuffle(double[] array, int first, int last);

    void shuffle(float[] array, int first, int last);

    void shuffle(int[] array, int first, int last);

    void shuffle(long[] array, int first, int last);

    void shuffle(short[] array, int first, int last);

    void shuffle(Object[] array, int first, int last);
  }

  @Test
  public final void testByte() {
    byte[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (byte[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        byte[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      String state = "START:";
      for (int i = 0; i < elementAtIndex.length; i++) {
        state += "\n" + Arrays.toString(elementAtIndex[i]);
      }
      assertEquals(expectedCaseCount, caseCount, state);
    }
  }

  @Test
  public final void testChar() {
    char[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (char[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        char[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
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
    double[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (double[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        double[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = (int) Math.round(testCase[j] - 42);
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
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
    float[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (float[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        float[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = (int) Math.round(testCase[j] - 42);
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
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
    int[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (int[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        int[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
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
    long[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (long[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        long[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = (int) (testCase[j] - 42);
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
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
    short[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (short[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        short[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
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
    Integer[][] cases = {
      {5, 55}, {5, 42, 55}, {5, 42, 43, 55}, {5, 42, 43, 44, 55}, {5, 42, 43, 44, 45, 55}
    };
    for (Integer[] c : cases) {
      boolean[][] elementAtIndex = new boolean[c.length - 2][c.length - 2];
      int expectedCaseCount = (c.length - 2) * (c.length - 2);
      int caseCount = 0;
      for (int i = 0; i < MAX_TRIALS && caseCount < expectedCaseCount; i++) {
        Integer[] testCase = c.clone();
        gen.shuffle(testCase, 1, c.length - 1);
        assertEquals(5, testCase[0]);
        assertEquals(55, testCase[testCase.length - 1]);
        for (int j = 1; j < c.length - 1; j++) {
          int k = testCase[j] - 42;
          if (!elementAtIndex[j - 1][k]) {
            caseCount++;
            elementAtIndex[j - 1][k] = true;
          }
        }
        Arrays.sort(testCase);
        assertArrayEquals(c, testCase);
      }
      assertEquals(expectedCaseCount, caseCount);
    }
  }
}
