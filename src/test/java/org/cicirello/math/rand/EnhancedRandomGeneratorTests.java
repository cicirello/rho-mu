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
	public void testBinomials() {
		class Wrapper {
			boolean different;
			int last = -1;
		}
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
		class Wrapper {
			boolean different;
			int last = -1;
			int count;
		}
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
	public void testBinomial() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		for (int i = 0; i < 10; i++) {
			int lowB = erg.nextBinomial(100, 0.05);
			assertTrue(lowB < 100);
			int highB = erg.nextBinomial(100, 0.95);
			assertTrue(highB > 0);
			int midB = erg.nextBinomial(100, 0.5);
			assertTrue(midB < 100);
			assertTrue(midB > 0);
		}
	}
	
	@Test
	public void testCauchyScale() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		int positive = 0;
		int negative = 0;
		double scale = 0.5;
		for (int i = 0; i < 20; i++) {
			double g = erg.nextCauchy(scale);
			if (g > 0) positive++;
			if (g < 0) negative++;
			assertTrue(Math.abs(g) < 100.0*scale);
		}
		assertTrue(positive > 0);
		assertTrue(negative > 0);
	}
	
	@Test
	public void testCauchyMedianScale() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		int greater = 0;
		int lesser = 0;
		double scale = 0.05;
		double median = 42.0;
		for (int i = 0; i < 20; i++) {
			double g = erg.nextCauchy(median, scale);
			if (g > median) greater++;
			if (g < median) lesser++;
			assertTrue(Math.abs(g-median) < 100.0*scale);
		}
		assertTrue(greater > 0);
		assertTrue(lesser > 0);
		median = -42.0;
		greater = 0;
		lesser = 0;
		for (int i = 0; i < 20; i++) {
			double g = erg.nextCauchy(median, scale);
			if (g > median) greater++;
			if (g < median) lesser++;
			assertTrue(Math.abs(g-median) < 100.0*scale);
		}
		assertTrue(greater > 0);
		assertTrue(lesser > 0);
	}
	
	@Test
	public void testGaussian() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		int positive = 0;
		int negative = 0;
		for (int i = 0; i < 20; i++) {
			double g = erg.nextGaussian();
			if (g > 0) positive++;
			if (g < 0) negative++;
			assertTrue(Math.abs(g) < 5.0);
		}
		assertTrue(positive > 0);
		assertTrue(negative > 0);
	}
	
	@Test
	public void testGaussianStDev() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		int positive = 0;
		int negative = 0;
		double stdev = 0.05;
		for (int i = 0; i < 20; i++) {
			double g = erg.nextGaussian(stdev);
			if (g > 0) positive++;
			if (g < 0) negative++;
			assertTrue(Math.abs(g) < 5.0*stdev);
		}
		assertTrue(positive > 0);
		assertTrue(negative > 0);
	}
	
	@Test
	public void testGaussianMeanStDev() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		int greater = 0;
		int lesser = 0;
		double stdev = 0.05;
		double mean = 42.0;
		for (int i = 0; i < 20; i++) {
			double g = erg.nextGaussian(mean, stdev);
			if (g > mean) greater++;
			if (g < mean) lesser++;
			assertTrue(Math.abs(g-mean) < 5.0*stdev);
		}
		assertTrue(greater > 0);
		assertTrue(lesser > 0);
		mean = -42.0;
		greater = 0;
		lesser = 0;
		for (int i = 0; i < 20; i++) {
			double g = erg.nextGaussian(mean, stdev);
			if (g > mean) greater++;
			if (g < mean) lesser++;
			assertTrue(Math.abs(g-mean) < 5.0*stdev);
		}
		assertTrue(greater > 0);
		assertTrue(lesser > 0);
	}
	
	@Test
	public void testArrayMask() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		boolean[] b = erg.arrayMask(100);
		assertEquals(100, b.length);
		int count = 0;
		for (boolean bool : b) {
			if (bool) count++;
		}
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
			int count = 0;
			for (int i = 0; i < N; i++) {
				if (b[i]) count++;
			}
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
		int count = 0;
		for (int i = 0; i < N; i++) {
			if (b[i]) count++;
		}
		assertTrue(count < N);
		p = 0.95;
		b = erg.arrayMask(N, p);
		assertEquals(N, b.length);
		count = 0;
		for (int i = 0; i < N; i++) {
			if (b[i]) count++;
		}
		assertTrue(count > 0);
		p = 0.5;
		b = erg.arrayMask(N, p);
		assertEquals(N, b.length);
		count = 0;
		for (int i = 0; i < N; i++) {
			if (b[i]) count++;
		}
		assertTrue(count > 0);
		assertTrue(count < N);
	}
	
	@Test
	public void testNextIntPair() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextIntPair(6, null);
		assertEquals(2, result.length);
		assertNotEquals(result[0], result[1]);
		assertTrue(result[0] < 6);
		assertTrue(result[1] < 6);
		int[] result2 = erg.nextIntPair(6, result);
		assertTrue(result == result2);
		assertEquals(2, result2.length);
		assertNotEquals(result2[0], result2[1]);
		assertTrue(result2[0] < 6);
		assertTrue(result2[1] < 6);
	}
	
	@Test
	public void testNextIntTriple() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextIntTriple(8, null);
		assertEquals(3, result.length);
		assertNotEquals(result[0], result[1]);
		assertNotEquals(result[0], result[2]);
		assertNotEquals(result[2], result[1]);
		assertTrue(result[0] < 8);
		assertTrue(result[1] < 8);
		assertTrue(result[2] < 8);
		int[] result2 = erg.nextIntTriple(8, result);
		assertTrue(result == result2);
		assertEquals(3, result2.length);
		assertNotEquals(result2[0], result2[1]);
		assertNotEquals(result2[0], result2[2]);
		assertNotEquals(result2[2], result2[1]);
		assertTrue(result2[0] < 8);
		assertTrue(result2[1] < 8);
		assertTrue(result2[2] < 8);
	}
	
	@Test
	public void testNextIntTripleSorted() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextIntTriple(8, null, true);
		assertEquals(3, result.length);
		assertTrue(result[0] < result[1]);
		assertTrue(result[1] < result[2]);
		assertTrue(result[0] < 8);
		assertTrue(result[1] < 8);
		assertTrue(result[2] < 8);
		int[] result2 = erg.nextIntTriple(8, result, true);
		assertTrue(result == result2);
		assertEquals(3, result2.length);
		assertTrue(result2[0] < result2[1]);
		assertTrue(result2[1] < result2[2]);
		assertTrue(result2[0] < 8);
		assertTrue(result2[1] < 8);
		assertTrue(result2[2] < 8);
	}
	
	@Test
	public void testNextWindowedIntPair() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextWindowedIntPair(100, 5, null);
		assertEquals(2, result.length);
		assertNotEquals(result[0], result[1]);
		assertTrue(result[0] < 100);
		assertTrue(result[1] < 100);
		assertTrue(Math.abs(result[0]-result[1]) <= 5);
		int[] result2 = erg.nextWindowedIntPair(100, 5, result);
		assertTrue(result == result2);
		assertEquals(2, result2.length);
		assertNotEquals(result2[0], result2[1]);
		assertTrue(result2[0] < 100);
		assertTrue(result2[1] < 100);
		assertTrue(Math.abs(result[0]-result[1]) <= 5);
	}
	
	@Test
	public void testNextWindowedIntTriple() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextWindowedIntTriple(100, 6, null);
		assertEquals(3, result.length);
		assertNotEquals(result[0], result[1]);
		assertNotEquals(result[0], result[2]);
		assertNotEquals(result[2], result[1]);
		assertTrue(result[0] < 100);
		assertTrue(result[1] < 100);
		assertTrue(result[2] < 100);
		assertTrue(Math.abs(result[0]-result[1]) <= 6);
		assertTrue(Math.abs(result[0]-result[2]) <= 6);
		assertTrue(Math.abs(result[2]-result[1]) <= 6);
		int[] result2 = erg.nextWindowedIntTriple(8, 6, result);
		assertTrue(result == result2);
		assertEquals(3, result2.length);
		assertNotEquals(result2[0], result2[1]);
		assertNotEquals(result2[0], result2[2]);
		assertNotEquals(result2[2], result2[1]);
		assertTrue(result2[0] < 100);
		assertTrue(result2[1] < 100);
		assertTrue(result2[2] < 100);
		assertTrue(Math.abs(result2[0]-result2[1]) <= 6);
		assertTrue(Math.abs(result2[0]-result2[2]) <= 6);
		assertTrue(Math.abs(result2[2]-result2[1]) <= 6);
	}
	
	@Test
	public void testNextWindowedIntTripleSorted() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int[] result = erg.nextWindowedIntTriple(100, 6, null, true);
		assertEquals(3, result.length);
		assertNotEquals(result[0], result[1]);
		assertNotEquals(result[0], result[2]);
		assertNotEquals(result[2], result[1]);
		assertTrue(result[0] < 100);
		assertTrue(result[1] < 100);
		assertTrue(result[2] < 100);
		assertTrue(Math.abs(result[0]-result[1]) <= 6);
		assertTrue(Math.abs(result[0]-result[2]) <= 6);
		assertTrue(Math.abs(result[2]-result[1]) <= 6);
		assertTrue(result[0] < result[1]);
		assertTrue(result[1] < result[2]);
		int[] result2 = erg.nextWindowedIntTriple(100, 6, result, true);
		assertTrue(result == result2);
		assertEquals(3, result2.length);
		assertNotEquals(result2[0], result2[1]);
		assertNotEquals(result2[0], result2[2]);
		assertNotEquals(result2[2], result2[1]);
		assertTrue(result2[0] < 100);
		assertTrue(result2[1] < 100);
		assertTrue(result2[2] < 100);
		assertTrue(Math.abs(result2[0]-result2[1]) <= 6);
		assertTrue(Math.abs(result2[0]-result2[2]) <= 6);
		assertTrue(Math.abs(result2[2]-result2[1]) <= 6);
		assertTrue(result2[0] < result2[1]);
		assertTrue(result2[1] < result2[2]);
	}
	
	@Test
	public void testSample() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int n = 6;
		int[] result = null;
		for (int k = 1; k <= 6; k++) {
			boolean[] in = new boolean[n];
			result = erg.sample(n, k, null);
			assertEquals(k, result.length);
			for (int e : result) {
				assertFalse(in[e]);
				in[e] = true;
			}
		}
	}
	
	@Test
	public void testSamplePool() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int n = 6;
		int[] result = null;
		for (int k = 1; k <= 6; k++) {
			boolean[] in = new boolean[n];
			result = erg.samplePool(n, k, null);
			assertEquals(k, result.length);
			for (int e : result) {
				assertFalse(in[e]);
				in[e] = true;
			}
		}
	}
	
	@Test
	public void testSampleReservoir() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int n = 6;
		int[] result = null;
		for (int k = 1; k <= 6; k++) {
			boolean[] in = new boolean[n];
			result = erg.sampleReservoir(n, k, null);
			assertEquals(k, result.length);
			for (int e : result) {
				assertFalse(in[e]);
				in[e] = true;
			}
		}
	}
	
	@Test
	public void testSampleInsertion() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator();
		int n = 6;
		int[] result = null;
		for (int k = 1; k <= 6; k++) {
			boolean[] in = new boolean[n];
			result = erg.sampleInsertion(n, k, null);
			assertEquals(k, result.length);
			for (int e : result) {
				assertFalse(in[e]);
				in[e] = true;
			}
		}
	}
	
	@Test
	public void testSamplePoolProbability() {
		EnhancedRandomGenerator erg = new EnhancedRandomGenerator(new SplittableRandom(42));
		int n = 100;
		int[] result = erg.sample(n, 0.05);
		boolean[] in = new boolean[n];
		for (int e : result) {
			assertFalse(in[e]);
			in[e] = true;
		}
		assertTrue(result.length < n);
		
		result = erg.sample(n, 0.95);
		in = new boolean[n];
		for (int e : result) {
			assertFalse(in[e]);
			in[e] = true;
		}
		assertTrue(result.length > 0);
		
		result = erg.sample(n, 0.5);
		in = new boolean[n];
		for (int e : result) {
			assertFalse(in[e]);
			in[e] = true;
		}
		assertTrue(result.length > 0);
		assertTrue(result.length < n);
	}
}
