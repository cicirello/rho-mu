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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import org.junit.jupiter.api.*;

/** JUnit tests for JacobiDiagonalization. */
public class JacobiDiagonalizationTests {

  @Test
  public void testJacobiExceptions() {
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> new JacobiDiagonalization(new double[2][3]));
    thrown =
        assertThrows(
            IllegalArgumentException.class, () -> new JacobiDiagonalization(new int[2][3]));
  }

  @Test
  public void testJacobiTooManyIterations() {
    double[][] A = getSymmetric(5);
    JacobiDiagonalization jd = new JacobiDiagonalization(A);
    assertFalse(jd.compute(1));
  }

  @Test
  public void testJacobiDiagonal() {
    for (int n = 1; n <= 10; n++) {
      double[][] A = getDiagonal(n);
      JacobiDiagonalization jd = new JacobiDiagonalization(A);
      assertNull(jd.eigenvectors());
      assertNull(jd.eigenvalues());
      assertTrue(jd.compute());
      validateDiagonalResult(jd, A);
    }
  }

  @Test
  public void testJacobiDiagonalInt() {
    for (int n = 1; n <= 5; n++) {
      int[][] Aint = getDiagonalInt(n);
      double[][] A = toDoubleMatrix(Aint, true);
      JacobiDiagonalization jd = new JacobiDiagonalization(Aint);
      assertNull(jd.eigenvectors());
      assertNull(jd.eigenvalues());
      assertTrue(jd.compute(JacobiDiagonalization.EPSILON));
      validateDiagonalResult(jd, A);
    }
  }

  @Test
  public void testJacobiSymmetric() {
    for (int n = 1; n <= 5; n++) {
      double[][] A = getSymmetric(n);
      JacobiDiagonalization jd = new JacobiDiagonalization(A);
      assertNull(jd.eigenvectors(), "eigvectors should be null");
      assertNull(jd.eigenvalues(), "eigvalues should be null");
      assertTrue(jd.compute(), "should converge");
      validateSymmetricResult(jd, A, n);
    }
  }

  @Test
  public void testJacobiSymmetricInt() {
    for (int n = 1; n <= 5; n++) {
      int[][] Aint = getSymmetricInt(n);
      double[][] A = toDoubleMatrix(Aint, false);
      JacobiDiagonalization jd = new JacobiDiagonalization(A);
      assertNull(jd.eigenvectors(), "eigvectors should be null");
      assertNull(jd.eigenvalues(), "eigvalues should be null");
      assertTrue(jd.compute(JacobiDiagonalization.MAX_ITERATIONS / 10), "should converge");
      validateSymmetricResult(jd, A, n);
    }
  }

  private void validateDiagonalResult(JacobiDiagonalization jd, double[][] A) {
    double[][] P = jd.eigenvectors();
    double[] eig = jd.eigenvalues();
    double[][] lambda = new double[eig.length][eig.length];
    for (int i = 0; i < lambda.length; i++) {
      lambda[i][i] = eig[i];
    }
    double[][] left = MatrixOps.product(A, P);
    double[][] right = MatrixOps.product(P, lambda);
    assertEquals(left.length, right.length);
    assertEquals(left[0].length, right[0].length);
    for (int i = 0; i < left.length; i++) {
      assertArrayEquals(left[i], right[i], JacobiDiagonalization.EPSILON, "diagonal already");
    }
  }

  private void validateSymmetricResult(JacobiDiagonalization jd, double[][] A, int n) {
    double[][] P = jd.eigenvectors();
    double[] eig = jd.eigenvalues();
    double[][] lambda = new double[eig.length][eig.length];
    for (int i = 0; i < lambda.length; i++) {
      lambda[i][i] = eig[i];
    }
    double[][] left = MatrixOps.product(A, P);
    double[][] right = MatrixOps.product(P, lambda);
    assertEquals(left.length, right.length);
    assertEquals(left[0].length, right[0].length);
    for (int i = 0; i < left.length; i++) {
      assertArrayEquals(
          left[i], right[i], 10 * n * JacobiDiagonalization.EPSILON, "symmetric: " + n);
    }
  }

  private double[][] getSymmetric(int n) {
    double[][] A = new double[n][n];
    Random gen = new Random(42);
    for (int i = 0; i < n; i++) {
      A[i][i] = gen.nextDouble() + 1;
      for (int j = i + 1; j < n; j++) {
        A[i][j] = A[j][i] = gen.nextDouble() + 1;
      }
    }
    return A;
  }

  private int[][] getSymmetricInt(int n) {
    int[][] A = new int[n][n];
    Random gen = new Random(42);
    for (int i = 0; i < n; i++) {
      A[i][i] = gen.nextInt(2) + 1;
      for (int j = i + 1; j < n; j++) {
        A[i][j] = A[j][i] = gen.nextInt(2) + 1;
      }
    }
    return A;
  }

  private double[][] getDiagonal(int n) {
    double[][] A = new double[n][n];
    Random gen = new Random(42);
    for (int i = 0; i < n; i++) {
      A[i][i] = 99 * gen.nextDouble() + 1;
    }
    return A;
  }

  private int[][] getDiagonalInt(int n) {
    int[][] A = new int[n][n];
    Random gen = new Random(42);
    for (int i = 0; i < n; i++) {
      A[i][i] = gen.nextInt(99) + 1;
    }
    return A;
  }

  private double[][] toDoubleMatrix(int[][] M, boolean diag) {
    double[][] A = new double[M.length][M[0].length];
    for (int i = 0; i < A.length; i++) {
      if (diag) {
        A[i][i] = M[i][i];
      } else {
        for (int j = 0; j < A[i].length; j++) {
          A[i][j] = M[i][j];
        }
      }
    }
    return A;
  }
}
