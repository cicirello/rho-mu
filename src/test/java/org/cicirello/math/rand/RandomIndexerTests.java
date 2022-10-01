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
 *
 */

package org.cicirello.math.rand;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.SplittableRandom;

/**
 * JUnit tests for the methods of the RandomIndexer class.
 */
public class RandomIndexerTests extends SharedTestRandom {
	
	private static double EPSILON = 1e-10;
		
	@Test
	public void testArrayMask() {
		SplittableRandom r1 = new SplittableRandom(42);
		for (int n = 1; n <= 5; n++) {
			for (int k = 0; k <= n; k++) {
				boolean[] mask = RandomIndexer.arrayMask(n, k);
				assertEquals( n, mask.length);
				int count = 0;
				for (int i = 0; i < mask.length; i++) {
					if (mask[i]) count++;
				}
				assertEquals( k, count);
				
				mask = RandomIndexer.arrayMask(n, k, r1);
				assertEquals( n, mask.length);
				count = 0;
				for (int i = 0; i < mask.length; i++) {
					if (mask[i]) count++;
				}
				assertEquals( k, count);
				
				mask = RandomIndexer.arrayMask(n);
				assertEquals( n, mask.length);
				
				mask = RandomIndexer.arrayMask(n, r1);
				assertEquals( n, mask.length);
			}
		}
		
		final int TRIALS = 120000;
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets);
			assertTrue( chi <= 3.841, "Using ThreadLocalRandom, chi square goodness of fit test: " + chi);
		}
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n, r1);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets);
			assertTrue( chi <= 3.841, "Using SplittableRandom, chi square goodness of fit test: " + chi);
		}
	}
	
	@Test
	public void testArrayMaskP05() {
		double p = 0.5;
		SplittableRandom r1 = new SplittableRandom(42);
		for (int n = 1; n <= 5; n++) {
			for (int k = 0; k <= n; k++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p);
				assertEquals( n, mask.length);
				
				mask = RandomIndexer.arrayMask(n, p, r1);
				assertEquals( n, mask.length);
			}
		}
		
		final int TRIALS = 120000;
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets);
			assertTrue( chi <= 3.841, "Using ThreadLocalRandom, chi square goodness of fit test: " + chi);
		}
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p, r1);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets);
			assertTrue( chi <= 3.841, "Using SplittableRandom, chi square goodness of fit test: " + chi);
		}
	}
	
	@Test
	public void testArrayMaskP075() {
		double p = 0.75;
		double[] pa = {0.25, 0.75};
		SplittableRandom r1 = new SplittableRandom(42);
		for (int n = 1; n <= 5; n++) {
			for (int k = 0; k <= n; k++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p);
				assertEquals( n, mask.length);
				
				mask = RandomIndexer.arrayMask(n, p, r1);
				assertEquals( n, mask.length);
			}
		}
		
		final int TRIALS = 120000;
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets, pa);
			assertTrue( chi <= 3.841, "Using ThreadLocalRandom, chi square goodness of fit test: " + chi);
		}
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p, r1);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets, pa);
			assertTrue( chi <= 3.841, "Using SplittableRandom, chi square goodness of fit test: " + chi);
		}
	}
	
	@Test
	public void testArrayMaskP025() {
		double p = 0.25;
		double[] pa = {0.75, 0.25};
		SplittableRandom r1 = new SplittableRandom(42);
		for (int n = 1; n <= 5; n++) {
			for (int k = 0; k <= n; k++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p);
				assertEquals( n, mask.length);
				
				mask = RandomIndexer.arrayMask(n, p, r1);
				assertEquals( n, mask.length);
			}
		}
		
		final int TRIALS = 120000;
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets, pa);
			assertTrue( chi <= 3.841, "Using ThreadLocalRandom, chi square goodness of fit test: " + chi);
		}
		for (int n = 1; n <= 5; n++) {
			final int MAX = TRIALS / n;
			int[] buckets = {0, 0};
			for (int t = 0; t < MAX; t++) {
				boolean[] mask = RandomIndexer.arrayMask(n, p, r1);
				for (int i = 0; i < n; i++) {
					if (mask[i]) buckets[1]++;
					else buckets[0]++;
				}
			}
			double chi = chiSquare(buckets, pa);
			assertTrue( chi <= 3.841, "Using SplittableRandom, chi square goodness of fit test: " + chi);
		}
	}
	
	@Test
	public void testArrayMaskBoundaryCases() {
		SplittableRandom r1 = new SplittableRandom(42);
		boolean[] mask = RandomIndexer.arrayMask(10, 1.0, r1);
		assertEquals(10, mask.length);
		for (int i = 0; i < mask.length; i++) {
			assertTrue(mask[i]);
		}
		mask = RandomIndexer.arrayMask(10, 0.0, r1);
		assertEquals(10, mask.length);
		for (int i = 0; i < mask.length; i++) {
			assertFalse(mask[i]);
		}
	}
	
	@Test
	public void testRandInt_ThreadLocalRandom() {
		final int REPS_PER_BUCKET = 100;
		double[] limit95 = {
			EPSILON, 3.841, 5.991, 7.815, 9.488, 
			11.07, 12.59, 14.07, 15.51, 16.92, 
			18.31, 19.68, 21.03
		};
		int countH = 0;
		for (int i = 1; i <= 13; i++) {
			for (int trial = 0; trial < 100; trial++) {
				int[] a = new int[i];
				for (int j = 0; j < REPS_PER_BUCKET * i; j++) {
					int k = RandomIndexer.nextInt(i);
					assertTrue( k < i && k >= 0, "nextInt outside range for bound="+i);
					a[k] += 1;
				}
				for (int k = 0; k < i; k++) {
					assertTrue(a[k]>0, "failed to generate any samples of "+k);
				}
				double chi = chiSquare(a);
				if (chi > limit95[i-1]) countH++;
			}
		}
		assertTrue( countH <= 130, "chi square too high too often, countHigh=" + countH);
	}
	
	@Test
	public void testRandInt_SplittableRandom() {
		final int REPS_PER_BUCKET = 100;
		SplittableRandom gen = new SplittableRandom(42);
		double[] limit95 = {
			EPSILON, 3.841, 5.991, 7.815, 9.488, 
			11.07, 12.59, 14.07, 15.51, 16.92, 
			18.31, 19.68, 21.03
		};
		int countH = 0;
		for (int i = 1; i <= 13; i++) {
			for (int trial = 0; trial < 100; trial++) {
				int[] a = new int[i];
				for (int j = 0; j < REPS_PER_BUCKET * i; j++) {
					int k = RandomIndexer.nextInt(i, gen);
					assertTrue( k < i && k >= 0, "nextInt outside range for bound="+i);
					a[k] += 1;
				}
				for (int k = 0; k < i; k++) {
					assertTrue(a[k]>0, "failed to generate any samples of "+k);
				}
				double chi = chiSquare(a);
				if (chi > limit95[i-1]) countH++;
			}
		}
		assertTrue( countH <= 130, "chi square too high too often, countHigh=" + countH);
	}
	
	@Test
	public void testRandIntOriginBound_SplittableRandom() {
		final int ORIGIN = 5;
		final int REPS_PER_BUCKET = 100;
		SplittableRandom gen = new SplittableRandom(42);
		double[] limit95 = {
			EPSILON, 3.841, 5.991, 7.815, 9.488, 
			11.07, 12.59, 14.07, 15.51, 16.92, 
			18.31, 19.68, 21.03
		};
		int countH = 0;
		for (int i = 1; i <= 13; i++) {
			for (int trial = 0; trial < 100; trial++) {
				int[] a = new int[i];
				for (int j = 0; j < REPS_PER_BUCKET * i; j++) {
					int k = RandomIndexer.nextInt(ORIGIN, ORIGIN+i, gen);
					assertTrue( k < ORIGIN+i && k >= ORIGIN, "nextInt outside range for (origin, bound)=("+ORIGIN+ ", "+(ORIGIN+i)+")");
					a[k-ORIGIN] += 1;
				}
				for (int k = 0; k < i; k++) {
					assertTrue(a[k]>0, "failed to generate any samples of "+(ORIGIN+k));
				}
				double chi = chiSquare(a);
				if (chi > limit95[i-1]) countH++;
			}
		}
		assertTrue( countH <= 130, "chi square too high too often, countHigh=" + countH);
	}
	
	@Test
	public void testRandIntOriginBound_ThreadLocalRandom() {
		final int ORIGIN = 5;
		for (int bound = ORIGIN + 1; bound <= 10; bound++) {
			for (int i = 0; i < 10; i++) {
				int k = RandomIndexer.nextInt(ORIGIN, bound);
				assertTrue( k < bound && k >= ORIGIN, "nextInt outside range for (origin, bound)=("+ORIGIN+ ", "+(ORIGIN+i)+")");
			}
		}
	}
	
	@Test
	public void testBiasedIntOriginBound_ThreadLocalRandom() {
		final int ORIGIN = 5;
		for (int bound = ORIGIN + 1; bound <= 10; bound++) {
			for (int i = 0; i < 10; i++) {
				int k = RandomIndexer.nextBiasedInt(ORIGIN, bound);
				assertTrue( k < bound && k >= ORIGIN, "nextInt outside range for (origin, bound)=("+ORIGIN+ ", "+(ORIGIN+i)+")");
			}
		}
	}
	
	@Test
	public void testBiasedIntOriginBound_SplittableRandom() {
		final int ORIGIN = 5;
		SplittableRandom gen = new SplittableRandom(42);
		for (int bound = ORIGIN + 1; bound <= 10; bound++) {
			for (int i = 0; i < 10; i++) {
				int k = RandomIndexer.nextBiasedInt(ORIGIN, bound, gen);
				assertTrue( k < bound && k >= ORIGIN, "nextInt outside range for (origin, bound)=("+ORIGIN+ ", "+(ORIGIN+i)+")");
			}
		}
	}
	
	@Test
	public void testRandIntLargeBound_Splittable() {
		SplittableRandom r = new SplittableRandom(42);
		final int N = 5;
		final int HIGH_BOUND = 0x40000001;
		int[] v = new int[N];
		for (int i = 0; i < N; i++) {
			int value = RandomIndexer.nextInt(HIGH_BOUND, r);
			assertTrue(value < HIGH_BOUND);
			v[i] = value;
			for (int j = i-1; j >= 0; j--) {
				// Note that a failure here might be OK, but unlikely 
				// due to small number of random samples from large bound.
				assertNotEquals(v[j], value);
			}
		}
	}
	
	@Test
	public void testRandBiasedInt_ThreadLocalRandom() {
		final int REPS_PER_BUCKET = 20;
		for (int i = 1; i <= 13; i++) {
			for (int trial = 0; trial < 10; trial++) {
				int[] a = new int[i];
				for (int j = 0; j < REPS_PER_BUCKET * i; j++) {
					int k = RandomIndexer.nextBiasedInt(i);
					assertTrue( k < i && k >= 0, "nextInt outside range for bound="+i);
					a[k] += 1;
				}
				for (int k = 0; k < i; k++) {
					assertTrue(a[k]>0, "failed to generate any samples of "+k);
				}
			}
		}
	}
	
	@Test
	public void testRandBiasedInt_SplittableRandom() {
		final int REPS_PER_BUCKET = 20;
		SplittableRandom gen = new SplittableRandom(42);
		for (int i = 1; i <= 13; i++) {
			for (int trial = 0; trial < 10; trial++) {
				int[] a = new int[i];
				for (int j = 0; j < REPS_PER_BUCKET * i; j++) {
					int k = RandomIndexer.nextBiasedInt(i, gen);
					assertTrue( k < i && k >= 0, "nextInt outside range for bound="+i);
					a[k] += 1;
				}
				for (int k = 0; k < i; k++) {
					assertTrue(a[k]>0, "failed to generate any samples of "+k);
				}
			}
		}
	}
	
	@Test
	public void testExceptions() {
		IllegalArgumentException thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> RandomIndexer.nextInt(0)
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> RandomIndexer.nextInt(0, new SplittableRandom())
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> RandomIndexer.nextBiasedInt(0)
		);
		thrown = assertThrows( 
			IllegalArgumentException.class,
			() -> RandomIndexer.nextBiasedInt(0, new SplittableRandom())
		);
	}
}
