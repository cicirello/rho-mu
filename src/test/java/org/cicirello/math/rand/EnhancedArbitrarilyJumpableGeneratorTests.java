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
 * JUnit tests for the methods of the EnhancedArbitrarilyJumpableGenerator class.
 */
public class EnhancedArbitrarilyJumpableGeneratorTests {
	
	private static final String jumpableAlgorithmName = "Xoshiro256PlusPlus";
	
	@Test
	public void testCopy() {
		EnhancedArbitrarilyJumpableGenerator ejg1 = EnhancedArbitrarilyJumpableGenerator.of(jumpableAlgorithmName);
		EnhancedArbitrarilyJumpableGenerator ejg2 = ejg1.copy();
		assertFalse(ejg1 == ejg2);
		assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
		assertEquals(ejg1.nextInt(), ejg2.nextInt());
		assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
		assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
		assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
	}
	
	@Test
	public void testCopyAndJump() {
		EnhancedArbitrarilyJumpableGenerator ejg1 = EnhancedArbitrarilyJumpableGenerator.of(jumpableAlgorithmName);
		EnhancedArbitrarilyJumpableGenerator ejg2 = ejg1.copyAndJump(10000.5);
		assertFalse(ejg1 == ejg2);
		assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
		ejg2.jump(10000.5);
		assertEquals(ejg1.nextInt(), ejg2.nextInt());
		assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
		assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
		assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
	}
	
	@Test
	public void testJump() {
		EnhancedArbitrarilyJumpableGenerator ejg1 = EnhancedArbitrarilyJumpableGenerator.of(jumpableAlgorithmName);
		EnhancedArbitrarilyJumpableGenerator ejg2 = ejg1.copy();
		assertFalse(ejg1 == ejg2);
		for (int i = 0; i < 3; i++) {
			ejg1.jump(10000.5 * (i+5));
			ejg2.jump(10000.5 * (i+5));
			assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
			assertEquals(ejg1.nextInt(), ejg2.nextInt());
			assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
			assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
			assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
		}
	}
	
	@Test
	public void testUnlimitedStream() {
		EnhancedArbitrarilyJumpableGenerator esg = EnhancedArbitrarilyJumpableGenerator.of(jumpableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.jumps(10000.5).limit(10).forEach(gen -> {
				assertTrue(gen instanceof EnhancedArbitrarilyJumpableGenerator);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStream() {
		EnhancedArbitrarilyJumpableGenerator esg = EnhancedArbitrarilyJumpableGenerator.of(jumpableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.jumps(5, 10000.5).forEach(gen -> {
				assertTrue(gen instanceof EnhancedArbitrarilyJumpableGenerator);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
	
	@Test
	public void testUnlimitedStreamE() {
		EnhancedArbitrarilyJumpableGenerator esg = EnhancedArbitrarilyJumpableGenerator.of(jumpableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.ejumps(10000.5).limit(10).forEach(gen -> {
				// calls a method added by the EnhancedArbitrarilyJumpableGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStreamE() {
		EnhancedArbitrarilyJumpableGenerator esg = EnhancedArbitrarilyJumpableGenerator.of(jumpableAlgorithmName);
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.ejumps(5, 10000.5).forEach(gen -> {
				// calls a method added by the EnhancedArbitrarilyJumpableGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
}
