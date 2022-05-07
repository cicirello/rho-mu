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
 * JUnit tests for the methods of the EnhancedStreamableGenerator class.
 */
public class EnhancedStreamableGeneratorTests {
	
	@Test
	public void testWithSeed() {
		EnhancedStreamableGenerator erg = new EnhancedStreamableGenerator(42L);
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
	public void testUnlimitedStream() {
		EnhancedStreamableGenerator esg = new EnhancedStreamableGenerator(new SplittableRandom(42));
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.rngs().limit(10).forEach(gen -> {
				assertTrue(gen instanceof EnhancedRandomGenerator);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStream() {
		EnhancedStreamableGenerator esg = EnhancedStreamableGenerator.of("SplittableRandom");
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.rngs(5).forEach(gen -> {
				assertTrue(gen instanceof EnhancedRandomGenerator);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
	
	@Test
	public void testUnlimitedStreamE() {
		EnhancedStreamableGenerator esg = new EnhancedStreamableGenerator(new SplittableRandom(42));
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.erngs().limit(10).forEach(gen -> {
				// calls a method added by the EnhancedRandomGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStreamE() {
		EnhancedStreamableGenerator esg = EnhancedStreamableGenerator.of("SplittableRandom");
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.erngs(5).forEach(gen -> {
				// calls a method added by the EnhancedRandomGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
}
