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
 */

package org.cicirello.math.la;

/** Code shared by tests cases for multiple test classes of matrix ops. */
class SharedTestMatrixOps {

  double[][] getMatrixAB_D(int n, int m, boolean backwards) {
    if (n == 0) m = 0;
    double[][] M = new double[n][m];
    int k = backwards ? n * m : 1;
    int inc = backwards ? -1 : 1;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        M[i][j] = k;
        k += inc;
      }
    }
    return M;
  }

  double[][] getMatrixA_D(int n, int m) {
    return getMatrixAB_D(n, m, false);
  }

  double[][] getMatrixB_D(int n, int m) {
    return getMatrixAB_D(n, m, true);
  }

  int[][] getMatrixABInts(int n, int m, boolean backwards) {
    if (n == 0) m = 0;
    int[][] M = new int[n][m];
    int k = backwards ? n * m : 1;
    int inc = backwards ? -1 : 1;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        M[i][j] = k;
        k += inc;
      }
    }
    return M;
  }

  int[][] getMatrixAInts(int n, int m) {
    return getMatrixABInts(n, m, false);
  }

  int[][] getMatrixBInts(int n, int m) {
    return getMatrixABInts(n, m, true);
  }
}
