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

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the methods of the EnhancedRandomGenerator class.
 */
public class EnhancedRandomGeneratorTests {
	
	@Test
	public void testNextBiasedInt() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		assertEquals(0, erg.nextBiasedInt(1));
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextBiasedInt(128);
			assertTrue(x < 128);
			assertTrue(x >= 0);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}

	@Test
	public void testNextIntBound() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		assertEquals(0, erg.nextInt(1));
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextInt(100);
			assertTrue(x < 100);
			assertTrue(x >= 0);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}
	
	@Test
	public void testNextIntBoundWithSeed() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		assertEquals(0, erg.nextInt(1));
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextInt(100);
			assertTrue(x < 100);
			assertTrue(x >= 0);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}
	
	@Test
	public void testNextIntBoundWithNamedRNG() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator("SplittableRandom");
		assertEquals(0, erg.nextInt(1));
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextInt(100);
			assertTrue(x < 100);
			assertTrue(x >= 0);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}
	
	@Test
	public void testNextIntBoundOfMethod() {
		EnhancedRandomGenerator erg = EnhancedRandomGenerator.of("SplittableRandom");
		assertEquals(0, erg.nextInt(1));
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextInt(100);
			assertTrue(x < 100);
			assertTrue(x >= 0);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}
	
	@Test
	public void testNextIntBoundGetDefaultMethod() {
		EnhancedRandomGenerator erg = EnhancedRandomGenerator.getDefault();
		assertEquals(0, erg.nextInt(1));
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextInt(100);
			assertTrue(x < 100);
			assertTrue(x >= 0);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}
	
	@Test
	public void testNextIntOriginBound() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		for (int low = 0; low <= 5; low++) {
			assertEquals(low, erg.nextInt(low,low+1));
		}
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextInt(100, 200);
			assertTrue(x < 200);
			assertTrue(x >= 100);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}
	
	@Test
	public void testNextBiasedIntOriginBound() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42);
		for (int low = 0; low <= 5; low++) {
			assertEquals(low, erg.nextBiasedInt(low,low+1));
		}
		boolean different = false;
		int last = -1;
		for (int i = 0; i < 10; i++) {
			int x = erg.nextBiasedInt(100, 200);
			assertTrue(x < 200);
			assertTrue(x >= 100);
			if (last >= 0 && last != x) {
				different = true;
			} 
			last = x;
		}
		assertTrue(different);
	}
	
	@Test
	public void testIntsOriginBound() {
		class Wrapper {
			boolean different;
			int last = -1;
		}
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		final Wrapper w = new Wrapper();
		erg.ints(100, 200).limit(10).forEach(
			x -> {
				assertTrue(x < 200);
				assertTrue(x >= 100);
				if (w.last >= 0 && w.last != x) {
					w.different = true;
				}
				w.last = x;
			}
		);
		assertTrue(w.different);
	}
	
	@Test
	public void testIntsSizeOriginBound() {
		class Wrapper {
			int count;
			boolean different;
			int last = -1;
		}
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		final Wrapper w = new Wrapper();
		erg.ints(5, 100, 200).forEach(
			x -> {
				assertTrue(x < 200);
				assertTrue(x >= 100);
				if (w.last >= 0 && w.last != x) {
					w.different = true;
				} 
				w.last = x;
				w.count++;
			}
		);
		assertEquals(5, w.count);
		assertTrue(w.different);
	}
	
	@Test
	public void testBiasedIntsOriginBound() {
		class Wrapper {
			boolean different;
			int last = -1;
		}
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42);
		final Wrapper w = new Wrapper();
		erg.biasedInts(100, 200).limit(10).forEach(
			x -> {
				assertTrue(x < 200);
				assertTrue(x >= 100);
				if (w.last >= 0 && w.last != x) {
					w.different = true;
				}
				w.last = x;
			}
		);
		assertTrue(w.different);
	}
	
	@Test
	public void testBiasedIntsSizeOriginBound() {
		class Wrapper {
			int count;
			boolean different;
			int last = -1;
		}
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42);
		final Wrapper w = new Wrapper();
		erg.biasedInts(5, 100, 200).forEach(
			x -> {
				assertTrue(x < 200);
				assertTrue(x >= 100);
				if (w.last >= 0 && w.last != x) {
					w.different = true;
				} 
				w.last = x;
				w.count++;
			}
		);
		assertEquals(5, w.count);
		assertTrue(w.different);
	}
	
	
}
