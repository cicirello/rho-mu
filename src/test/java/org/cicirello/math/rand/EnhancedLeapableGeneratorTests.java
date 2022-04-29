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
 * JUnit tests for the methods of the EnhancedLeapableGenerator class.
 */
public class EnhancedLeapableGeneratorTests {
	
	private static final String leapableAlgorithmName = "Xoroshiro128PlusPlus";
	
	@Test
	public void testCopy() {
		EnhancedLeapableGenerator ejg1 = EnhancedLeapableGenerator.of(leapableAlgorithmName);
		EnhancedLeapableGenerator ejg2 = ejg1.copy();
		assertFalse(ejg1 == ejg2);
		assertEquals(ejg1.leapDistance(), ejg2.leapDistance(), 0.0);
		assertEquals(ejg1.nextInt(), ejg2.nextInt());
		assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
		assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
		assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
	}
	
	@Test
	public void testCopyAndLeap() {
		EnhancedLeapableGenerator ejg1 = EnhancedLeapableGenerator.of(leapableAlgorithmName);
		EnhancedLeapableGenerator ejg2 = ejg1.copyAndLeap();
		assertFalse(ejg1 == ejg2);
		assertEquals(ejg1.leapDistance(), ejg2.leapDistance(), 0.0);
		ejg2.leap();
		assertEquals(ejg1.nextInt(), ejg2.nextInt());
		assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
		assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
		assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
	}
	
	@Test
	public void testLeap() {
		EnhancedLeapableGenerator ejg1 = EnhancedLeapableGenerator.of(leapableAlgorithmName);
		EnhancedLeapableGenerator ejg2 = ejg1.copy();
		assertFalse(ejg1 == ejg2);
		for (int i = 0; i < 3; i++) {
			ejg1.leap();
			ejg2.leap();
			assertEquals(ejg1.leapDistance(), ejg2.leapDistance(), 0.0);
			assertEquals(ejg1.nextInt(), ejg2.nextInt());
			assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
			assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
			assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
		}
	}
	
	@Test
	public void testUnlimitedStream() {
		EnhancedLeapableGenerator esg = EnhancedLeapableGenerator.of(leapableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.leaps().limit(10).forEach(gen -> {
				assertTrue(gen instanceof EnhancedJumpableGenerator);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStream() {
		EnhancedLeapableGenerator esg = EnhancedLeapableGenerator.of(leapableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.leaps(5).forEach(gen -> {
				assertTrue(gen instanceof EnhancedJumpableGenerator);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
	
	@Test
	public void testUnlimitedStreamE() {
		EnhancedLeapableGenerator esg = EnhancedLeapableGenerator.of(leapableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.eleaps().limit(10).forEach(gen -> {
				// calls a method added by the EnhancedJumpableGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStreamE() {
		EnhancedLeapableGenerator esg = EnhancedLeapableGenerator.of(leapableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.eleaps(5).forEach(gen -> {
				// calls a method added by the EnhancedJumpableGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
}
