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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * JUnit tests for the methods of the EnhancedRandomGenerator class for generating random pairs and
 * triples of indexes into arrays.
 */
public class ERGPairsTriplesTests {

  @Test
  public void testNextIntPair() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextIntPair(6, null);
    validateCombo(6, 2, result);
    int[] result2 = erg.nextIntPair(6, result);
    assertTrue(result == result2);
    validateCombo(6, 2, result);
  }

  @Test
  public void testNextIntTriple() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextIntTriple(8, null);
    validateCombo(8, 3, result);
    int[] result2 = erg.nextIntTriple(8, result);
    assertTrue(result == result2);
    validateCombo(8, 3, result);
  }

  @Test
  public void testNextIntTripleSorted() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextIntTriple(8, null, true);
    validateCombo(8, 3, result);
    assertTrue(result[0] < result[1]);
    assertTrue(result[1] < result[2]);
    int[] result2 = erg.nextIntTriple(8, result, true);
    assertTrue(result == result2);
    validateCombo(8, 3, result);
    assertTrue(result2[0] < result2[1]);
    assertTrue(result2[1] < result2[2]);
  }

  @Test
  public void testNextWindowedIntPair() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextWindowedIntPair(100, 5, null);
    validateWindowed(100, 5, 2, result);
    int[] result2 = erg.nextWindowedIntPair(100, 5, result);
    assertTrue(result == result2);
    validateWindowed(100, 5, 2, result);
  }

  @Test
  public void testNextWindowedIntTriple() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextWindowedIntTriple(100, 6, null);
    validateWindowed(100, 6, 3, result);
    int[] result2 = erg.nextWindowedIntTriple(8, 6, result);
    assertTrue(result == result2);
    validateWindowed(8, 6, 3, result);
  }

  @Test
  public void testNextWindowedIntTripleSorted() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextWindowedIntTriple(100, 6, null, true);
    validateWindowed(100, 6, 3, result);
    assertTrue(result[0] < result[1]);
    assertTrue(result[1] < result[2]);
    int[] result2 = erg.nextWindowedIntTriple(100, 6, result, true);
    assertTrue(result == result2);
    validateWindowed(100, 6, 3, result);
    assertTrue(result2[0] < result2[1]);
    assertTrue(result2[1] < result2[2]);
  }

  private void validateCombo(int n, int k, int[] result) {
    assertEquals(k, result.length);
    for (int i = 0; i < result.length; i++) {
      assertTrue(result[i] < n);
      for (int j = i + 1; j < result.length; j++) {
        assertNotEquals(result[i], result[j]);
      }
    }
  }

  private void validateWindowed(int n, int window, int k, int[] result) {
    validateCombo(n, k, result);
    for (int i = 0; i < result.length; i++) {
      for (int j = i + 1; j < result.length; j++) {
        assertTrue(Math.abs(result[i] - result[j]) <= window);
      }
    }
  }
}
