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
 * JUnit tests for Cauchy random variables.
 */
public class CauchyTests extends SharedTestRandomVariates {
	
	@Test
	public void testNextCauchyThreadLocalRandom() {
		// Since we cannot seed ThreadLocalRandom, this test case is
		// not 100% replicable.  Additionally, we know that this version of
		// the method simply calls the version that takes a Random as a parameter.
		// Since we did test that version for goodness of fit,
		// and since replication is not possible without a seed, we simply verify
		// that over a large number of trials that samples are generated in each of the buckets
		// used for the goodness of fit tests in the class Random case.
		final int TRIALS = 800;
		double[] scale = {1, 2, 4};
		final double sqrt2 = Math.sqrt(2);
		for (double s : scale) {
			double[] boundaries = { -s*(sqrt2+1), -s, -s*(sqrt2-1), 0 , s*(sqrt2-1), s, s*(sqrt2+1)};
			int[] buckets = new int[8];
			for (int j = 0; j < TRIALS; j++) {
				double v = RandomVariates.nextCauchy(s);
				int i = 0;
				for (; i < boundaries.length; i++) {
					if (v < boundaries[i]) break;
				}
				buckets[i]++;
			}
			for (int i = 0; i < buckets.length; i++) {
				assertTrue( buckets[i] > 0, "verifying at least 1 sample in each bucket, scale="+s + " bucket="+i);
			}
		}
		double median = 5.0;
		double s = 1.0;
		double[] boundaries = { median - s*(sqrt2+1), median - s, median - s*(sqrt2-1), median , median + s*(sqrt2-1), median + s, median + s*(sqrt2+1)};
		int[] buckets = new int[8];
		for (int j = 0; j < TRIALS; j++) {
			double v = RandomVariates.nextCauchy(median, s);
			int i = 0;
			for (; i < boundaries.length; i++) {
				if (v < boundaries[i]) break;
			}
			buckets[i]++;
		}
		for (int i = 0; i < buckets.length; i++) {
			assertTrue( buckets[i] > 0, "verifying at least 1 sample in each bucket, median=5 bucket="+i);
		}
	}
	
	@Test
	public void testNextCauchySplittableRandom() {
		final int TRIALS = 8000;
		SplittableRandom r = new SplittableRandom(53);
		double[] scale = {1, 2, 4};
		final double sqrt2 = Math.sqrt(2);
		for (double s : scale) {
			double[] boundaries = { -s*(sqrt2+1), -s, -s*(sqrt2-1), 0 , s*(sqrt2-1), s, s*(sqrt2+1)};
			int[] buckets = new int[8];
			for (int j = 0; j < TRIALS; j++) {
				double v = RandomVariates.nextCauchy(s, r);
				int i = 0;
				for (; i < boundaries.length; i++) {
					if (v < boundaries[i]) break;
				}
				buckets[i]++;
			}
			double chi = chiSquare(buckets);
			// critical value for 8-1=7 degrees of freedom is 14.07 (at the .95 level).
			assertTrue( chi <= 14.07, "Verifying chi square statistic below .95 critical value: chi="+chi+" crit=14.07");
		}
		double median = 5.0;
		double s = 1.0;
		double[] boundaries = { median - s*(sqrt2+1), median - s, median - s*(sqrt2-1), median , median + s*(sqrt2-1), median + s, median + s*(sqrt2+1)};
		int[] buckets = new int[8];
		for (int j = 0; j < TRIALS; j++) {
			double v = RandomVariates.nextCauchy(median, s, r);
			int i = 0;
			for (; i < boundaries.length; i++) {
				if (v < boundaries[i]) break;
			}
			buckets[i]++;
		}
		double chi = chiSquare(buckets);
		// critical value for 8-1=7 degrees of freedom is 14.07 (at the .95 level).
		assertTrue( chi <= 14.07, "Verifying chi square statistic below .95 critical value: chi="+chi+" crit=14.07");
	}
	
	@Test
	public void testNextCauchyHelpersEdgeCase() {
		SplittableRandom r = new SplittableRandom(53);
		boolean foundPositive = false;
		boolean foundNegative = false;
		for (int i = 0; i < 4; i++) {
			double u = RandomVariates.internalNextTransformedU(r, 0.5);
			assertEquals(0.5, Math.abs(u), 0.0);
			if (u > 0) foundPositive = true;
			else foundNegative = true;
		}
		assertTrue(foundPositive);
		assertTrue(foundNegative);
	}
}
