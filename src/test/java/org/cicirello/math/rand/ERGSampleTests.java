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
 * JUnit tests for the methods of the EnhancedRandomGenerator class
 * that implement various algorithms for randomly sampling without replacement.
 */
public class ERGSampleTests {
	
	@Test
	public void testSample() {
		validateSample((erg, n, k) -> erg.sample(n, k, null));
	}
	
	@Test
	public void testSamplePool() {
		validateSample((erg, n, k) -> erg.samplePool(n, k, null));
	}
	
	@Test
	public void testSampleReservoir() {
		validateSample((erg, n, k) -> erg.sampleReservoir(n, k, null));
	}
	
	@Test
	public void testSampleInsertion() {
		validateSample((erg, n, k) -> erg.sampleInsertion(n, k, null));
	}
	
	@Test
	public void testSamplePoolProbability() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		int n = 100;
		int[] result = erg.sample(n, 0.05);
		validateNoRepeats(n, result);
		assertTrue(result.length < n);
		
		result = erg.sample(n, 0.95);
		validateNoRepeats(n, result);
		assertTrue(result.length > 0);
		
		result = erg.sample(n, 0.5);
		validateNoRepeats(n, result);
		assertTrue(result.length > 0);
		assertTrue(result.length < n);
	}
	
	private void validateNoRepeats(int n, int[] result) {
		boolean[] in = new boolean[n];
		for (int e : result) {
			assertFalse(in[e]);
			in[e] = true;
		}
	}
	
	private void validateSample(SampleNK sampler) {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int n = 6;
		int[] result = null;
		for (int k = 1; k <= 6; k++) {
			result = sampler.apply(erg, n, k);
			assertEquals(k, result.length);
			validateNoRepeats(n, result);
		}
	}
	
	@FunctionalInterface
	private static interface SampleNK {
		int[] apply(EnhancedRandomGenerator erg, int n, int k);
	}
}
