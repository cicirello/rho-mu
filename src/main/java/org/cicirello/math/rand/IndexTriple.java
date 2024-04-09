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

/**
 * A triple of indexes. Instances of IndexTriple are returned by some methods of other classes, such
 * as {@link RandomIndexer} and {@link EnhancedRandomGenerator}, that generate random triples of
 * indexes.
 *
 * @param i an index of the triple
 * @param j another index of the triple
 * @param k and another index of the triple
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public record IndexTriple(int i, int j, int k) {

  /**
   * Factory method to initialize an IndexTriple with sorted indexes.
   *
   * @param i an index of the triple
   * @param j another index of the triple
   * @param k and another index of the triple
   * @return an IndexTriple t, such that t.i() == min(i, j, k), t.j() == median(i, j, k), and t.k()
   *     == max(i, j, k)
   */
  public static IndexTriple sorted(int i, int j, int k) {
    return i < j ? insert(i, j, k) : insert(j, i, k);
  }

  private static IndexTriple insert(int smaller, int larger, int other) {
    if (larger < other) {
      return new IndexTriple(smaller, larger, other);
    }
    return other < smaller
        ? new IndexTriple(other, smaller, larger)
        : new IndexTriple(smaller, other, larger);
  }
}
