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
 * that generate random numbers from distributions other than uniform.
 */
public class ERGNonUniformTests {
	
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
}
