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
 * JUnit tests for the methods of the EnhancedSplittableGenerator class.
 */
public class EnhancedSplittableGeneratorTests {
	
	@Test
	public void testDefault() {
		EnhancedSplittableGenerator erg = new EnhancedSplittableGenerator();
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
	public void testWithSeed() {
		EnhancedSplittableGenerator erg = new EnhancedSplittableGenerator(42L);
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
	public void testSplit() {
		EnhancedSplittableGenerator esg = new EnhancedSplittableGenerator(new SplittableRandom(42));
		EnhancedSplittableGenerator split = esg.split();
		assertTrue(split.nextInt(5) < 5);
	}
	
	@Test
	public void testSplitSource() {
		EnhancedSplittableGenerator esg = new EnhancedSplittableGenerator(new SplittableRandom(42));
		EnhancedSplittableGenerator split = esg.split(new SplittableRandom(55));
		assertTrue(split.nextInt(5) < 5);
	}
	
	@Test
	public void testUnlimitedStream() {
		EnhancedSplittableGenerator esg = new EnhancedSplittableGenerator(new SplittableRandom(42));
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.splits().limit(10).forEach(gen -> {
				assertTrue(gen instanceof EnhancedSplittableGenerator);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testUnlimitedStreamSource() {
		EnhancedSplittableGenerator esg = new EnhancedSplittableGenerator(new SplittableRandom(42));
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.splits(new SplittableRandom(55)).limit(10).forEach(gen -> {
				assertTrue(gen instanceof EnhancedSplittableGenerator);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStream() {
		EnhancedSplittableGenerator esg = EnhancedSplittableGenerator.of("SplittableRandom");
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.splits(5).forEach(gen -> {
				assertTrue(gen instanceof EnhancedSplittableGenerator);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
	
	@Test
	public void testLimitedStreamSource() {
		EnhancedSplittableGenerator esg = EnhancedSplittableGenerator.of("SplittableRandom");
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.splits(5, new SplittableRandom(55)).forEach(gen -> {
				assertTrue(gen instanceof EnhancedSplittableGenerator);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
	
	@Test
	public void testUnlimitedStreamE() {
		EnhancedSplittableGenerator esg = new EnhancedSplittableGenerator(new SplittableRandom(42));
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.esplits().limit(10).forEach(gen -> {
				// calls a method added by the EnhancedRandomGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testUnlimitedStreamSourceE() {
		EnhancedSplittableGenerator esg = new EnhancedSplittableGenerator(new SplittableRandom(42));
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.esplits(new SplittableRandom(55)).limit(10).forEach(gen -> {
				// calls a method added by the EnhancedRandomGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(10, c.count);
	}
	
	@Test
	public void testLimitedStreamE() {
		EnhancedSplittableGenerator esg = EnhancedSplittableGenerator.of("SplittableRandom");
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.esplits(5).forEach(gen -> {
				// calls a method added by the EnhancedRandomGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
	
	@Test
	public void testLimitedStreamSourceE() {
		EnhancedSplittableGenerator esg = EnhancedSplittableGenerator.of("SplittableRandom");
		class Counter {
			int count;
		}
		final Counter c = new Counter();
		esg.esplits(5, new SplittableRandom(55)).forEach(gen -> {
				// calls a method added by the EnhancedRandomGenerator class
				gen.nextCauchy(1.0);
				c.count++;
			}
		);
		assertEquals(5, c.count);
	}
}
