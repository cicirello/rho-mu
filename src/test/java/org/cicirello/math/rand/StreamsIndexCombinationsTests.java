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
 * JUnit tests for the methods of the EnhancedRandomGenerator class that produce streams of
 * IndexPairs and IndexTriples.
 */
public class StreamsIndexCombinationsTests {

  @Test
  public void testPairs() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
    erg.pairs(10)
        .limit(10)
        .forEach(
            x -> {
              assertTrue(x.i() < 10);
              assertTrue(x.i() >= 0);
              assertTrue(x.j() < 10);
              assertTrue(x.j() >= 0);
              assertNotEquals(x.j(), x.i());
            });
  }

  @Test
  public void testPairsLimited() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
    final Wrapper w = new Wrapper();
    erg.pairs(10, 6)
        .forEach(
            x -> {
              assertTrue(x.i() < 6);
              assertTrue(x.i() >= 0);
              assertTrue(x.j() < 6);
              assertTrue(x.j() >= 0);
              assertNotEquals(x.j(), x.i());
              w.count++;
            });
    assertEquals(10, w.count);
  }

  @Test
  public void testTriples() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
    erg.triples(10)
        .limit(10)
        .forEach(
            x -> {
              assertTrue(x.i() < 10);
              assertTrue(x.i() >= 0);
              assertTrue(x.j() < 10);
              assertTrue(x.j() >= 0);
              assertTrue(x.k() < 10);
              assertTrue(x.k() >= 0);
              assertNotEquals(x.j(), x.i());
              assertNotEquals(x.k(), x.i());
              assertNotEquals(x.j(), x.k());
            });
  }

  @Test
  public void testTriplesLimited() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
    final Wrapper w = new Wrapper();
    erg.triples(10, 6)
        .forEach(
            x -> {
              assertTrue(x.i() < 6);
              assertTrue(x.i() >= 0);
              assertTrue(x.j() < 6);
              assertTrue(x.j() >= 0);
              assertTrue(x.k() < 6);
              assertTrue(x.k() >= 0);
              assertNotEquals(x.j(), x.i());
              assertNotEquals(x.k(), x.i());
              assertNotEquals(x.j(), x.k());
              w.count++;
            });
    assertEquals(10, w.count);
  }

  @Test
  public void testTriplesSorted() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
    erg.sortedTriples(10)
        .limit(10)
        .forEach(
            x -> {
              assertTrue(x.i() >= 0);
              assertTrue(x.i() < x.j());
              assertTrue(x.j() < x.k());
              assertTrue(x.k() < 10);
            });
  }

  @Test
  public void testTriplesSortedLimited() {
    EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
    final Wrapper w = new Wrapper();
    erg.sortedTriples(10, 6)
        .forEach(
            x -> {
              assertTrue(x.i() >= 0);
              assertTrue(x.i() < x.j());
              assertTrue(x.j() < x.k());
              assertTrue(x.k() < 6);
              w.count++;
            });
    assertEquals(10, w.count);
  }

  private static class Wrapper {
    int count;
  }
}
