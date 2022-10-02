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

import java.util.SplittableRandom;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the arrayMask methods of the EnhancedRandomGenerator class.
 */
public class ERGArrayMaskTests {
	
	@Test
	public void testArrayMask() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		boolean[] b = erg.arrayMask(100);
		assertEquals(100, b.length);
		int count = countTrue(b);
		assertTrue(count > 0);
		assertTrue(count < 100);
	}
	
	@Test
	public void testArrayMaskNK() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int N = 5;
		for (int k = 0; k <= N; k++) {
			boolean[] b = erg.arrayMask(N, k);
			assertEquals(N, b.length);
			int count = countTrue(b);
			assertEquals(k, count);
		}
	}
	
	@Test
	public void testArrayMaskNP() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int N = 100;
		double p = 0.05;
		boolean[] b = erg.arrayMask(N, p);
		assertEquals(N, b.length);
		int count = countTrue(b);
		assertTrue(count < N);
		p = 0.95;
		b = erg.arrayMask(N, p);
		assertEquals(N, b.length);
		count = countTrue(b);
		assertTrue(count > 0);
		p = 0.5;
		b = erg.arrayMask(N, p);
		assertEquals(N, b.length);
		count = countTrue(b);
		assertTrue(count > 0);
		assertTrue(count < N);
	}
	
	private int countTrue(boolean[] b) {
		int count = 0;
		for (boolean bool : b) {
			if (bool) count++;
		}
		return count;
	}
}
