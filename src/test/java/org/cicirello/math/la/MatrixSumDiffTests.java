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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the sum and difference methods of the MatrixOps class.
 */
public class MatrixSumDiffTests extends SharedTestMatrixOps {
	
	@Test
	public void testSumInts() {
		for (int n = 0; n < 5; n++) {
			for (int m = 0; m < 5; m++) {
				int[][] a = getMatrixAInts(n,m);
				int[][] b = getMatrixBInts(n,m);
				int[][] c = MatrixOps.sum(a,b,null);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals(m, c[0].length, "result cols");
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(n*m+1, c[i][j], "sum");
					}
				}
				c = null;
				c = MatrixOps.sum(a,b);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals(m, c[0].length, "result cols");
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(n*m+1, c[i][j], "sum");
					}
				}
				int[][] c2 = new int[n][m];
				int[][] c3 = MatrixOps.sum(a,b,c2);
				assertEquals((Object)c2, (Object)c3);
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(n*m+1, c2[i][j], "sum");
					}
				}
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new int[1][2], new int[2][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new int[1][2], new int[1][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new int[1][1], new int[1][1], new int[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new int[1][1], new int[1][1], new int[2][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new int[0][0], new int[0][0], new int[1][2])
		);
	}
	
	@Test
	public void testDifferenceInts() {
		for (int n = 0; n < 5; n++) {
			for (int m = 0; m < 5; m++) {
				int[][] a = fillMatrix(n,m,n*m+1);
				int[][] b = getMatrixBInts(n,m);
				int[][] c = MatrixOps.difference(a,b,null);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals(m, c[0].length, "result cols");
				int k = 1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(k, c[i][j], "diff");
						k++;
					}
				}
				c = null;
				c = MatrixOps.difference(a,b);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals(m, c[0].length, "result cols");
				k = 1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(k, c[i][j], "diff");
						k++;
					}
				}
				int[][] c2 = new int[n][m];
				int[][] c3 = MatrixOps.difference(a,b,c2);
				assertEquals((Object)c2, (Object)c3);
				k = 1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(k, c2[i][j], "diff");
						k++;
					}
				}
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new int[1][2], new int[2][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new int[1][2], new int[1][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new int[1][1], new int[1][1], new int[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new int[1][1], new int[1][1], new int[2][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new int[0][0], new int[0][0], new int[1][2])
		);
	}
	
	@Test
	public void testSumDoubles() {
		for (int n = 0; n < 5; n++) {
			for (int m = 0; m < 5; m++) {
				double[][] a = getMatrixA_D(n,m);
				double[][] b = getMatrixB_D(n,m);
				double[][] c = MatrixOps.sum(a,b,null);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals(m, c[0].length, "result cols");
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(n*m+1, c[i][j], "sum");
					}
				}
				c = null;
				c = MatrixOps.sum(a,b);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals( m, c[0].length, "result cols");
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(n*m+1, c[i][j], "sum");
					}
				}
				double[][] c2 = new double[n][m];
				double[][] c3 = MatrixOps.sum(a,b,c2);
				assertEquals((Object)c2, (Object)c3);
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(n*m+1, c2[i][j], "sum");
					}
				}
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new double[1][2], new double[2][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new double[1][2], new double[1][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new double[1][1], new double[1][1], new double[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new double[1][1], new double[1][1], new double[2][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.sum(new double[0][0], new double[0][0], new double[1][2])
		);
	}
	
	@Test
	public void testDifferenceDoubles() {
		for (int n = 0; n < 5; n++) {
			for (int m = 0; m < 5; m++) {
				double[][] a = fillMatrix(n,m,n*m+1.0);
				double[][] b = getMatrixB_D(n,m);
				double[][] c = MatrixOps.difference(a,b,null);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals( m, c[0].length, "result cols");
				int k = 1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(k, c[i][j], "diff");
						k++;
					}
				}
				c = null;
				c = MatrixOps.difference(a,b);
				assertEquals(n, c.length, "result rows");
				if (n > 0) assertEquals( m, c[0].length, "result cols");
				k = 1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(k, c[i][j], "diff");
						k++;
					}
				}
				double[][] c2 = new double[n][m];
				double[][] c3 = MatrixOps.difference(a,b,c2);
				assertEquals((Object)c2, (Object)c3);
				k = 1;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						assertEquals(k, c2[i][j], "diff");
						k++;
					}
				}
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new double[1][2], new double[2][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new double[1][2], new double[1][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new double[1][1], new double[1][1], new double[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new double[1][1], new double[1][1], new double[2][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.difference(new double[0][0], new double[0][0], new double[1][2])
		);
	}
	
	private int[][] fillMatrix(int n, int m, int value) {
		if (n==0) m = 0;
		int[][] M = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				M[i][j] = value;
			}
		}
		return M;
	}
	
	private double[][] fillMatrix(int n, int m, double value) {
		if (n==0) m = 0;
		double[][] M = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				M[i][j] = value;
			}
		}
		return M;
	}
}
