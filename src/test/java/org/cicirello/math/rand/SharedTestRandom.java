/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

/** Code shared by the tests of multiple test classes for the RandomVariates class. */
public class SharedTestRandom {

  double chiSquare(int[] buckets) {
    int x = 0;
    int n = 0;
    for (int e : buckets) {
      x = x + e * e;
      n += e;
    }
    return 1.0 * x / (n / buckets.length) - n;
  }

  double chiSquare(int[] buckets, double[] p) {
    double x = 0;
    int n = 0;
    for (int i = 0; i < buckets.length; i++) {
      int e = buckets[i];
      x = x + e * e / p[i];
      n += e;
    }
    return x / n - n;
  }
}
