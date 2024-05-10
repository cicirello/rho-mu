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
  public void testNextIntPair_IndexPair() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    IndexPair result = erg.nextIntPair(6);
    validateCombo(6, result);
  }

  @Test
  public void testNextIntPairSorted() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextSortedIntPair(6, null);
    validateCombo(6, 2, result);
    assertTrue(result[0] < result[1]);
    int[] result2 = erg.nextSortedIntPair(6, result);
    assertTrue(result == result2);
    validateCombo(6, 2, result2);
    assertTrue(result2[0] < result2[1]);
  }

  @Test
  public void testNextIntPairSorted_IndexPair() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    IndexPair result = erg.nextSortedIntPair(6);
    validateCombo(6, result);
    assertTrue(result.i() < result.j());
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
    int[] result = erg.nextSortedIntTriple(8, null);
    validateCombo(8, 3, result);
    assertTrue(result[0] < result[1]);
    assertTrue(result[1] < result[2]);
    int[] result2 = erg.nextSortedIntTriple(8, result);
    assertTrue(result == result2);
    validateCombo(8, 3, result);
    assertTrue(result2[0] < result2[1]);
    assertTrue(result2[1] < result2[2]);
  }

  @Test
  public void testNextIntTriple_IndexTriple() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    IndexTriple result = erg.nextIntTriple(8);
    validateCombo(8, result);
  }

  @Test
  public void testNextIntTripleSorted_IndexTriple() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    IndexTriple result = erg.nextSortedIntTriple(8);
    assertTrue(0 <= result.i());
    assertTrue(result.i() < result.j());
    assertTrue(result.j() < result.k());
    assertTrue(result.k() < 8);
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
  public void testNextWindowedIntPair_IndexPair() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    IndexPair result = erg.nextWindowedIntPair(100, 5);
    validateWindowed(100, 5, result);
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
  public void testNextWindowedIntTriple_IndexTriple() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    IndexTriple result = erg.nextWindowedIntTriple(100, 6);
    validateWindowed(100, 6, result);
    IndexTriple result2 = erg.nextWindowedIntTriple(8, 6);
    validateWindowed(8, 6, result2);
  }

  @Test
  public void testNextWindowedIntTripleSorted() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    int[] result = erg.nextSortedWindowedIntTriple(100, 6, null);
    validateWindowed(100, 6, 3, result);
    assertTrue(result[0] < result[1]);
    assertTrue(result[1] < result[2]);
    int[] result2 = erg.nextSortedWindowedIntTriple(100, 6, result);
    assertTrue(result == result2);
    validateWindowed(100, 6, 3, result);
    assertTrue(result2[0] < result2[1]);
    assertTrue(result2[1] < result2[2]);
  }

  @Test
  public void testNextWindowedIntTripleSorted_IndexTriple() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
    IndexTriple result = erg.nextSortedWindowedIntTriple(100, 6);
    validateWindowed(100, 6, result);
    assertTrue(result.i() < result.j());
    assertTrue(result.j() < result.k());
    IndexTriple result2 = erg.nextSortedWindowedIntTriple(100, 6);
    validateWindowed(100, 6, result2);
    assertTrue(result2.i() < result2.j());
    assertTrue(result2.j() < result2.k());
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

  private void validateCombo(int n, IndexTriple result) {
    assertTrue(result.i() < n);
    assertTrue(result.j() < n);
    assertTrue(result.k() < n);
    assertTrue(result.i() >= 0);
    assertTrue(result.j() >= 0);
    assertTrue(result.k() >= 0);
    assertNotEquals(result.i(), result.j());
    assertNotEquals(result.i(), result.k());
    assertNotEquals(result.k(), result.j());
  }

  private void validateCombo(int n, IndexPair result) {
    assertTrue(result.i() < n);
    assertTrue(result.j() < n);
    assertTrue(result.i() >= 0);
    assertTrue(result.j() >= 0);
    assertNotEquals(result.i(), result.j());
  }

  private void validateWindowed(int n, int window, int k, int[] result) {
    validateCombo(n, k, result);
    for (int i = 0; i < result.length; i++) {
      for (int j = i + 1; j < result.length; j++) {
        assertTrue(Math.abs(result[i] - result[j]) <= window);
      }
    }
  }

  private void validateWindowed(int n, int window, IndexTriple result) {
    validateCombo(n, result);
    assertTrue(Math.abs(result.i() - result.j()) <= window);
    assertTrue(Math.abs(result.i() - result.k()) <= window);
    assertTrue(Math.abs(result.j() - result.k()) <= window);
  }

  private void validateWindowed(int n, int window, IndexPair result) {
    validateCombo(n, result);
    assertTrue(Math.abs(result.i() - result.j()) <= window);
  }
}
