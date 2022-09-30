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
 * JUnit tests for the product methods of the MatrixOps class.
 */
public class MatrixProductTests extends SharedTestMatrixOps {
	
	@Test
	public void testProductIdentityInt() {
		int[][] A = new int[0][0];
		int[][] B = new int[0][0];
		int[][] C = MatrixOps.product(A,B,null);
		assertEquals(0, C.length);
		C = new int[0][0];
		int[][] C2 = MatrixOps.product(A,B,C);
		assertEquals(0, C.length);
		assertEquals((Object)C, (Object)C2);
		C = null;
		C = MatrixOps.product(A,B);
		assertEquals(0, C.length);
		for (int n = 1; n < 5; n++) {
			int[][] I = getI(n);
			for (int m = 1; m < 5; m++) {
				C = C2 = null;
				A = getMatrixAInts(n,m);
				C = MatrixOps.product(I,A,null);
				assertEquals(A.length, C.length);
				assertEquals(A[0].length, C[0].length);
				for (int i = 0; i < A.length; i++) {
					assertArrayEquals(A[i], C[i]);
				}
				C = new int[n][m];
				C2 = MatrixOps.product(I,A,C);
				assertEquals((Object)C, (Object)C2);
				assertEquals(A.length, C2.length);
				assertEquals(A[0].length, C2[0].length);
				for (int i = 0; i < A.length; i++) {
					assertArrayEquals(A[i], C2[i]);
				}
				C = null;
				C = MatrixOps.product(I,A);
				assertEquals(A.length, C.length);
				assertEquals(A[0].length, C[0].length);
				for (int i = 0; i < A.length; i++) {
					assertArrayEquals(A[i], C[i]);
				}
				B = getMatrixAInts(m,n);
				C = MatrixOps.product(B,I,null);
				assertEquals(B.length, C.length);
				assertEquals(B[0].length, C[0].length);
				for (int i = 0; i < B.length; i++) {
					assertArrayEquals(B[i], C[i]);
				}
				C = new int[m][n];
				C2 = MatrixOps.product(B,I,C);
				assertEquals((Object)C, (Object)C2);
				assertEquals(B.length, C2.length);
				assertEquals(B[0].length, C2[0].length);
				for (int i = 0; i < B.length; i++) {
					assertArrayEquals(B[i], C2[i]);
				}
				C = null;
				C = MatrixOps.product(B,I);
				assertEquals(B.length, C.length);
				assertEquals(B[0].length, C[0].length);
				for (int i = 0; i < B.length; i++) {
					assertArrayEquals(B[i], C[i]);
				}
			}
		}
		
	}
	
	@Test
	public void testProductIdentityDouble() {
		double[][] A = new double[0][0];
		double[][] B = new double[0][0];
		double[][] C = MatrixOps.product(A,B,null);
		assertEquals(0, C.length);
		C = new double[0][0];
		double[][] C2 = MatrixOps.product(A,B,C);
		assertEquals(0, C.length);
		assertEquals((Object)C, (Object)C2);
		C = null;
		C = MatrixOps.product(A,B);
		assertEquals(0, C.length);
		for (int n = 1; n < 5; n++) {
			double[][] I = getI_d(n);
			for (int m = 1; m < 5; m++) {
				C = C2 = null;
				A = getMatrixA_D(n,m);
				C = MatrixOps.product(I,A,null);
				assertEquals(A.length, C.length);
				assertEquals(A[0].length, C[0].length);
				for (int i = 0; i < A.length; i++) {
					assertArrayEquals(A[i], C[i]);
				}
				C = new double[n][m];
				C2 = MatrixOps.product(I,A,C);
				assertEquals((Object)C, (Object)C2);
				assertEquals(A.length, C2.length);
				assertEquals(A[0].length, C2[0].length);
				for (int i = 0; i < A.length; i++) {
					assertArrayEquals(A[i], C2[i]);
				}
				C = null;
				C = MatrixOps.product(I,A);
				assertEquals(A.length, C.length);
				assertEquals(A[0].length, C[0].length);
				for (int i = 0; i < A.length; i++) {
					assertArrayEquals(A[i], C[i]);
				}
				B = getMatrixA_D(m,n);
				C = MatrixOps.product(B,I,null);
				assertEquals(B.length, C.length);
				assertEquals(B[0].length, C[0].length);
				for (int i = 0; i < B.length; i++) {
					assertArrayEquals(B[i], C[i]);
				}
				C = new double[m][n];
				C2 = MatrixOps.product(B,I,C);
				assertEquals((Object)C, (Object)C2);
				assertEquals(B.length, C2.length);
				assertEquals(B[0].length, C2[0].length);
				for (int i = 0; i < B.length; i++) {
					assertArrayEquals(B[i], C2[i]);
				}
				C = null;
				C = MatrixOps.product(B,I);
				assertEquals(B.length, C.length);
				assertEquals(B[0].length, C[0].length);
				for (int i = 0; i < B.length; i++) {
					assertArrayEquals(B[i], C[i]);
				}
			}
		}	
	}
	
	@Test
	public void testProductInt() {
		for (int n = 1; n < 5; n++) {
			for (int m = 1; m < 5; m++) {
				int[][] A = getMultA(n,m);
				int[][] B = getMultB(m,n);
				int[][] C = MatrixOps.product(A, B);
				assertEquals(A.length, C.length);
				assertEquals(B[0].length, C[0].length);
				assertEquals((m)*(m+1)*(2*m+1)/6, C[0][0], "[0][0]");
				int sum = (m+1)*m/2;
				for (int i = 1; i < C[0].length; i++) {
					assertEquals(i*sum, C[0][i], "[0][i]");
				}
				for (int i = 1; i < C.length; i++) {
					assertArrayEquals(C[0], C[i]);
				}
				C = null;
				C = MatrixOps.product(A, B, null);
				assertEquals(A.length, C.length);
				assertEquals(B[0].length, C[0].length);
				assertEquals((m)*(m+1)*(2*m+1)/6, C[0][0], "[0][0]");
				for (int i = 1; i < C[0].length; i++) {
					assertEquals(i*sum, C[0][i], "[0][i]");
				}
				for (int i = 1; i < C.length; i++) {
					assertArrayEquals(C[0], C[i]);
				}
				int[][] C2 = new int[n][n];
				C = null;
				C = MatrixOps.product(A, B, C2);
				assertEquals((Object)C, (Object)C2);
				assertEquals(A.length, C.length);
				assertEquals(B[0].length, C[0].length);
				assertEquals((m)*(m+1)*(2*m+1)/6, C[0][0], "[0][0]");
				for (int i = 1; i < C[0].length; i++) {
					assertEquals(i*sum, C[0][i], "[0][i]");
				}
				for (int i = 1; i < C.length; i++) {
					assertArrayEquals(C[0], C[i]);
				}
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new int[1][2], new int[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new int[0][1], new int[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new int[1][1], new int[0][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new int[1][1], new int[1][1], new int[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new int[1][1], new int[1][1], new int[2][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new int[0][0], new int[0][0], new int[1][2])
		);
	}
	
	@Test
	public void testProductDouble() {
		for (int n = 1; n < 5; n++) {
			for (int m = 1; m < 5; m++) {
				double[][] A = getMultA_d(n,m);
				double[][] B = getMultB_d(m,n);
				double[][] C = MatrixOps.product(A, B);
				assertEquals(A.length, C.length);
				assertEquals(B[0].length, C[0].length);
				assertEquals((m)*(m+1)*(2*m+1)/6.0, C[0][0], "[0][0]");
				double sum = (m+1)*m/2;
				for (int i = 1; i < C[0].length; i++) {
					assertEquals(i*sum, C[0][i], "[0][i]");
				}
				for (int i = 1; i < C.length; i++) {
					assertArrayEquals(C[0], C[i]);
				}
				C = null;
				C = MatrixOps.product(A, B, null);
				assertEquals(A.length, C.length);
				assertEquals(B[0].length, C[0].length);
				assertEquals( (m)*(m+1)*(2*m+1)/6.0, C[0][0], "[0][0]");
				for (int i = 1; i < C[0].length; i++) {
					assertEquals(i*sum, C[0][i], "[0][i]");
				}
				for (int i = 1; i < C.length; i++) {
					assertArrayEquals(C[0], C[i]);
				}
				double[][] C2 = new double[n][n];
				C = null;
				C = MatrixOps.product(A, B, C2);
				assertEquals((Object)C, (Object)C2);
				assertEquals(A.length, C.length);
				assertEquals(B[0].length, C[0].length);
				assertEquals((m)*(m+1)*(2*m+1)/6.0, C[0][0], "[0][0]");
				for (int i = 1; i < C[0].length; i++) {
					assertEquals(i*sum, C[0][i], "[0][i]");
				}
				for (int i = 1; i < C.length; i++) {
					assertArrayEquals(C[0], C[i]);
				}
			}
		}
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new double[1][2], new double[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new double[0][1], new double[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new double[1][1], new double[0][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new double[1][1], new double[1][1], new double[1][2])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new double[1][1], new double[1][1], new double[2][1])
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> MatrixOps.product(new double[0][0], new double[0][0], new double[1][2])
		);
	}
	
	private int[][] getI(int n) {
		int[][] I = new int[n][n];
		for (int i = 0; i < n; i++) {
			I[i][i] = 1;
		}
		return I;
	}
	
	private double[][] getI_d(int n) {
		double[][] I = new double[n][n];
		for (int i = 0; i < n; i++) {
			I[i][i] = 1.0;
		}
		return I;
	}
	
	private double[][] getMultA_d(int n, int m) {
		double[][] A = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				A[i][j] = j+1;
			}
		}
		return A;
	}
	
	private double[][] getMultB_d(int n, int m) {
		double[][] B = new double[n][m];
		for (int i = 0; i < n; i++) {
			B[i][0] = i+1;
			for (int j = 1; j < m; j++) {
				B[i][j] = j;
			}
		}
		return B;
	}
	
	private int[][] getMultA(int n, int m) {
		int[][] A = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				A[i][j] = j+1;
			}
		}
		return A;
	}
	
	private int[][] getMultB(int n, int m) {
		int[][] B = new int[n][m];
		for (int i = 0; i < n; i++) {
			B[i][0] = i+1;
			for (int j = 1; j < m; j++) {
				B[i][j] = j;
			}
		}
		return B;
	}
}
