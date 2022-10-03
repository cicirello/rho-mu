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
 * JUnit tests for the transpose methods of the Statistics class.
 */
public class TransposeTests {
	
	@Test
	public void testTransposeInts() {
		for (int n = 0; n < 5; n++) {
			int[][] m = getSquareIntsForTranspose(n);
			int[][] transpose = getSquareIntsForTranspose(n);
			int[][] result = MatrixOps.transposeSquareMatrixInline(transpose);
			assertEquals((Object)transpose, (Object)result, "should return reference to parameter");
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					assertEquals(m[j][i], transpose[i][j], "position [i][j] should be equal to position [j][i] of original");
				}
			}
		}
	}
	
	@Test
	public void testTransposeDoubles() {
		for (int n = 0; n < 5; n++) {
			double[][] m = getSquareDoublesForTranspose(n);
			double[][] transpose = getSquareDoublesForTranspose(n);
			double[][] result = MatrixOps.transposeSquareMatrixInline(transpose);
			assertEquals((Object)transpose, (Object)result, "should return reference to parameter");
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					assertEquals(m[j][i], transpose[i][j], 0.0, "position [i][j] should be equal to position [j][i] of original");
				}
			}
		}
	}
	
	private int[][] getSquareIntsForTranspose(int n) {
		int[][] m = new int[n][n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m[i][j] = k;
				k++;
			}
		}
		return m;
	}
	
	private double[][] getSquareDoublesForTranspose(int n) {
		double[][] m = new double[n][n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m[i][j] = k;
				k++;
			}
		}
		return m;
	}
}
