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
 * JUnit tests for the methods of the EnhancedRandomGenerator class
 * that produce streams of non-uniform random numbers.
 */
public class ERGStreamsNonUniformTests {
	
	@Test
	public void testBinomials() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final Wrapper w = new Wrapper();
		erg.binomials(100, 0.5).limit(10).forEach(
			x -> {
				assertTrue(x < 100);
				assertTrue(x > 0);
				if (w.last >= 0 && w.last != x) {
					w.different = true;
				}
				w.last = x;
			}
		);
		assertTrue(w.different);
	}
	
	@Test
	public void testBinomialsLimited() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final Wrapper w = new Wrapper();
		erg.binomials(5L, 100, 0.5).forEach(
			x -> {
				assertTrue(x < 100);
				assertTrue(x > 0);
				if (w.last >= 0 && w.last != x) {
					w.different = true;
				}
				w.last = x;
				w.count++;
			}
		);
		assertTrue(w.different);
		assertEquals(5, w.count);
	}
	
	@Test
	public void testExponentials() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.exponentials().limit(10).forEach(
			x -> {
				assertTrue(x >= 0);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
			}
		);
		assertTrue(w.different);
	}
	
	@Test
	public void testExponentialsLimited() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.exponentials(5L).forEach(
			x -> {
				assertTrue(x >= 0);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
				w.count++;
			}
		);
		assertTrue(w.different);
		assertEquals(5, w.count);
	}
	
	@Test
	public void testCauchys() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.cauchys(500.0, 0.01).limit(10).forEach(
			x -> {
				assertTrue(x < 501.0);
				assertTrue(x > 499.0);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
			}
		);
		assertTrue(w.different);
	}
	
	@Test
	public void testCauchysLimited() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.cauchys(5L, 500.0, 0.01).forEach(
			x -> {
				assertTrue(x < 501.0);
				assertTrue(x > 499.0);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
				w.count++;
			}
		);
		assertTrue(w.different);
		assertEquals(5, w.count);
	}
	
	@Test
	public void testGaussians() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.gaussians().limit(10).forEach(
			x -> {
				assertTrue(x < 5.0);
				assertTrue(x > -5.0);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
			}
		);
		assertTrue(w.different);
	}
	
	@Test
	public void testGaussiansMuSigma() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.gaussians(500.0, 0.01).limit(10).forEach(
			x -> {
				assertTrue(x < 500.05);
				assertTrue(x > 499.95);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
			}
		);
		assertTrue(w.different);
	}
	
	@Test
	public void testGaussiansMuSigmaLimited() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.gaussians(5L, 500.0, 0.01).forEach(
			x -> {
				assertTrue(x < 500.05);
				assertTrue(x > 499.95);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
				w.count++;
			}
		);
		assertTrue(w.different);
		assertEquals(5, w.count);
	}
	
	@Test
	public void testGaussiansLimited() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(42L);
		final WrapperNaN w = new WrapperNaN();
		erg.gaussians(5L).forEach(
			x -> {
				assertTrue(x < 5.0);
				assertTrue(x > -5.0);
				if (w.last != Double.NaN && w.last != x) {
					w.different = true;
				}
				w.last = x;
				w.count++;
			}
		);
		assertTrue(w.different);
		assertEquals(5, w.count);
	}
	
	private static class WrapperNaN {
		boolean different;
		double last = Double.NaN;
		int count;
	}
	
	private static class Wrapper {
		boolean different;
		int last = -1;
		int count;
	}
}
