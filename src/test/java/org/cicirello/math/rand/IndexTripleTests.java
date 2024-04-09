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

import org.junit.jupiter.api.*;

/** JUnit tests for IndexTriple. */
public class IndexTripleTests {

  @Test
  public void testSorted() {
    int[] c = {2, 4, 6};
    IndexTriple expected = new IndexTriple(c[0], c[1], c[2]);
    assertEquals(expected, IndexTriple.sorted(c[0], c[1], c[2]));
    assertEquals(expected, IndexTriple.sorted(c[0], c[2], c[1]));
    assertEquals(expected, IndexTriple.sorted(c[1], c[0], c[2]));
    assertEquals(expected, IndexTriple.sorted(c[1], c[2], c[0]));
    assertEquals(expected, IndexTriple.sorted(c[2], c[0], c[1]));
    assertEquals(expected, IndexTriple.sorted(c[2], c[1], c[0]));
    expected = new IndexTriple(c[0], c[0], c[1]);
    assertEquals(expected, IndexTriple.sorted(c[0], c[0], c[1]));
    assertEquals(expected, IndexTriple.sorted(c[0], c[1], c[0]));
    assertEquals(expected, IndexTriple.sorted(c[1], c[0], c[0]));
    expected = new IndexTriple(c[0], c[1], c[1]);
    assertEquals(expected, IndexTriple.sorted(c[0], c[1], c[1]));
    assertEquals(expected, IndexTriple.sorted(c[1], c[1], c[0]));
    assertEquals(expected, IndexTriple.sorted(c[1], c[0], c[1]));
    expected = new IndexTriple(c[0], c[0], c[0]);
    assertEquals(expected, IndexTriple.sorted(c[0], c[0], c[0]));
  }
}
