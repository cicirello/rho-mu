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

/**
 * Utility class of basic linear algebra matrix operations, where matrices are 
 * represented as 2-D Java arrays.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>  
 */
public final class MatrixOps {
	
	// Utility class of static methods.  Should not be instantiated.
	private MatrixOps() {}
	
	/**
	 * Transpose a square matrix inline.
	 * @param matrix the matrix to transpose, with result replacing contents of matrix.
	 * @return The matrix is returned for convenience for passing to other 
	 * methods requiring a matrix as parameter.  It is the same
	 *         object reference that was passed as a parameter.
	 *
	 * @throws NullPointerException if matrix is null.
	 * @throws ArrayIndexOutOfBoundsException if matrix is not square.
	 */
	public static int[][] transposeSquareMatrixInline(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i+1; j < matrix[i].length; j++) {
				int temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
		return matrix;
	}
	
	/**
	 * Transpose a square matrix inline.
	 * @param matrix the matrix to transpose, with result replacing contents of matrix.
	 * @return The matrix is returned for convenience for passing to other methods requiring a matrix as parameter.  It is the same
	 *         object reference that was passed as a parameter.
	 *
	 * @throws NullPointerException if matrix is null.
	 * @throws ArrayIndexOutOfBoundsException if matrix is not square.
	 */
	public static double[][] transposeSquareMatrixInline(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = i+1; j < matrix[i].length; j++) {
				double temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
		return matrix;
	}
	
	/**
	 * Computes C = A + B.
	 * @param A matrix
	 * @param B matrix
	 * @param C if C is null then a new matrix is constructed for result, otherwise C is used for result.
	 * @return A reference to the C matrix.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 * @throws ArrayIndexOutOfBoundsException if C is non-null, but different dimensions than A and B.
	 */
	public static int[][] sum(int[][] A, int[][] B, int[][] C) {
		if (C==null) {
			C = A.length > 0 ? new int[A.length][A[0].length] : new int[0][0];
		}
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C[i].length; j++) {
				C[i][j] = A[i][j] + B[i][j];
			}
		}
		return C;
	}
	
	/**
	 * Computes C = A + B.
	 * @param A matrix
	 * @param B matrix
	 * @param C if C is null then a new matrix is constructed for result, otherwise C is used for result.
	 * @return A reference to the C matrix.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 * @throws ArrayIndexOutOfBoundsException if C is non-null, but different dimensions than A and B.
	 */
	public static double[][] sum(double[][] A, double[][] B, double[][] C) {
		if (C==null) {
			C = A.length > 0 ? new double[A.length][A[0].length] : new double[0][0];
		}
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C[i].length; j++) {
				C[i][j] = A[i][j] + B[i][j];
			}
		}
		return C;
	}
	
	/**
	 * Computes C = A + B.
	 * @param A matrix
	 * @param B matrix
	 * @return A reference to a new matrix C containing the sum.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 */
	public static int[][] sum(int[][] A, int[][] B) {
		return sum(A, B, null);
	}
	
	/**
	 * Computes C = A + B.
	 * @param A matrix
	 * @param B matrix
	 * @return A reference to a new matrix C containing the sum.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 */
	public static double[][] sum(double[][] A, double[][] B) {
		return sum(A, B, null);
	}
	
	/**
	 * Computes C = A - B.
	 * @param A matrix
	 * @param B matrix
	 * @param C if C is null then a new matrix is constructed for result, otherwise C is used for result.
	 * @return A reference to the C matrix.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 * @throws ArrayIndexOutOfBoundsException if C is non-null, but different dimensions than A and B.
	 */
	public static int[][] difference(int[][] A, int[][] B, int[][] C) {
		if (C==null) {
			C = A.length > 0 ? new int[A.length][A[0].length] : new int[0][0];
		}
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C[i].length; j++) {
				C[i][j] = A[i][j] - B[i][j];
			}
		}
		return C;
	}
	
	/**
	 * Computes C = A - B.
	 * @param A matrix
	 * @param B matrix
	 * @param C if C is null then a new matrix is constructed for result, otherwise C is used for result.
	 * @return A reference to the C matrix.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 * @throws ArrayIndexOutOfBoundsException if C is non-null, but different dimensions than A and B.
	 */
	public static double[][] difference(double[][] A, double[][] B, double[][] C) {
		if (C==null) {
			C = A.length > 0 ? new double[A.length][A[0].length] : new double[0][0];
		}
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C[i].length; j++) {
				C[i][j] = A[i][j] - B[i][j];
			}
		}
		return C;
	}
	
	/**
	 * Computes C = A - B.
	 * @param A matrix
	 * @param B matrix
	 * @return A reference to a new matrix C containing the difference.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 */
	public static int[][] difference(int[][] A, int[][] B) {
		return difference(A, B, null);
	}
	
	/**
	 * Computes C = A - B.
	 * @param A matrix
	 * @param B matrix
	 * @return A reference to a new matrix C containing the difference.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if A and B are of differing dimensions.
	 */
	public static double[][] difference(double[][] A, double[][] B) {
		return difference(A, B, null);
	}
	
	/**
	 * Computes C = A * B.
	 * @param A matrix
	 * @param B matrix
	 * @param C if C is null then a new matrix is constructed for result, otherwise C is used for result.
	 * @return A reference to the C matrix.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if number of columns of A is not equal to number of rows of B.
	 * @throws ArrayIndexOutOfBoundsException if C is non-null, but dimensions inconsistent with A and B (must have
	 * same number of rows of A and same number of columns of B).
	 */
	public static int[][] product(int[][] A, int[][] B, int[][] C) {
		if (C==null) {
			C = B.length > 0 ? new int[A.length][B[0].length] : new int[0][0];
		}
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				C[i][j] = 0;
				for (int k = 0; k < B.length; k++) {
					C[i][j] = C[i][j] + A[i][k]*B[k][j];
				}
			}
		}
		return C;
	}
	
	/**
	 * Computes C = A * B.
	 * @param A matrix
	 * @param B matrix
	 * @param C if C is null then a new matrix is constructed for result, otherwise C is used for result.
	 * @return A reference to the C matrix.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if number of columns of A is not equal to number of rows of B.
	 * @throws ArrayIndexOutOfBoundsException if C is non-null, but dimensions inconsistent with A and B (must have
	 * same number of rows of A and same number of columns of B).
	 */
	public static double[][] product(double[][] A, double[][] B, double[][] C) {
		if (C==null) {
			C = B.length > 0 ? new double[A.length][B[0].length] : new double[0][0];
		}
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				C[i][j] = 0;
				for (int k = 0; k < B.length; k++) {
					C[i][j] = C[i][j] + A[i][k]*B[k][j];
				}
			}
		}
		return C;
	}
	
	/**
	 * Computes C = A * B.
	 * @param A matrix
	 * @param B matrix
	 * @return A reference to a new matrix C containing the product.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if number of columns of A is not equal to number of rows of B.
	 */
	public static int[][] product(int[][] A, int[][] B) {
		return product(A,B,null);
	}
	
	/**
	 * Computes C = A * B.
	 * @param A matrix
	 * @param B matrix
	 * @return A reference to a new matrix C containing the product.
	 *
	 * @throws NullPointerException if either A or B is null.
	 * @throws ArrayIndexOutOfBoundsException if number of columns of A is not equal to number of rows of B.
	 */
	public static double[][] product(double[][] A, double[][] B) {
		return product(A,B,null);
	}
}
