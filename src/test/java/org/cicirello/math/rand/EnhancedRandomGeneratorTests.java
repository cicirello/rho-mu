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

import java.util.random.RandomGenerator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.SplittableRandom;

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
	
	@Test
	public void testNextIntPair() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextIntPair(6, null);
		validateCombo(6, 2, result);
		int[] result2 = erg.nextIntPair(6, result);
		assertTrue(result == result2);
		validateCombo(6, 2, result);
	}
	
	
	@Test
	public void testNextIntTriple() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextIntTriple(8, null);
		validateCombo(8, 3, result);
		int[] result2 = erg.nextIntTriple(8, result);
		assertTrue(result == result2);
		validateCombo(8, 3, result);
	}
	
	@Test
	public void testNextIntTripleSorted() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextIntTriple(8, null, true);
		validateCombo(8, 3, result);
		assertTrue(result[0] < result[1]);
		assertTrue(result[1] < result[2]);
		int[] result2 = erg.nextIntTriple(8, result, true);
		assertTrue(result == result2);
		validateCombo(8, 3, result);
		assertTrue(result2[0] < result2[1]);
		assertTrue(result2[1] < result2[2]);
	}
	
	@Test
	public void testNextWindowedIntPair() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextWindowedIntPair(100, 5, null);
		validateWindowed(100, 5, 2, result);
		int[] result2 = erg.nextWindowedIntPair(100, 5, result);
		assertTrue(result == result2);
		validateWindowed(100, 5, 2, result);
	}
	
	@Test
	public void testNextWindowedIntTriple() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextWindowedIntTriple(100, 6, null);
		validateWindowed(100, 6, 3, result);
		int[] result2 = erg.nextWindowedIntTriple(8, 6, result);
		assertTrue(result == result2);
		validateWindowed(8, 6, 3, result);
	}
	
	@Test
	public void testNextWindowedIntTripleSorted() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextWindowedIntTriple(100, 6, null, true);
		validateWindowed(100, 6, 3, result);
		assertTrue(result[0] < result[1]);
		assertTrue(result[1] < result[2]);
		int[] result2 = erg.nextWindowedIntTriple(100, 6, result, true);
		assertTrue(result == result2);
		validateWindowed(100, 6, 3, result);
		assertTrue(result2[0] < result2[1]);
		assertTrue(result2[1] < result2[2]);
	}
	
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
	
	private int countTrue(boolean[] b) {
		int count = 0;
		for (boolean bool : b) {
			if (bool) count++;
		}
		return count;
	}
	
	private void validateCombo(int n, int k, int[] result) {
		assertEquals(k, result.length);
		for (int i = 0; i < result.length; i++) {
			assertTrue(result[i] < n);
			for (int j = i+1; j < result.length; j++) {
				assertNotEquals(result[i], result[j]);
			}
		}
	}
	
	private void validateWindowed(int n, int window, int k, int[] result) {
		validateCombo(n, k, result);
		for (int i = 0; i < result.length; i++) {
			for (int j = i+1; j < result.length; j++) {
				assertTrue(Math.abs(result[i]-result[j]) <= window);
			}
		}
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
