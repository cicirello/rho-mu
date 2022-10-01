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
 * JUnit tests for the arrayMask methods of the RandomIndexer class.
 */
public class ArrayMaskTests extends SharedTestRandom {
	
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
}
