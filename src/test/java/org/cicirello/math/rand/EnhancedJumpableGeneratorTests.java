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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/** JUnit tests for the methods of the EnhancedJumpableGenerator class. */
public class EnhancedJumpableGeneratorTests {

  private static final String jumpableAlgorithmName = "Xoroshiro128PlusPlus";

  @Test
  public void testCopy() {
    EnhancedJumpableGenerator ejg1 = EnhancedJumpableGenerator.of(jumpableAlgorithmName);
    EnhancedJumpableGenerator ejg2 = ejg1.copy();
    assertFalse(ejg1 == ejg2);
    assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
    assertEquals(ejg1.nextInt(), ejg2.nextInt());
    assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
    assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
    assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
  }

  @Test
  public void testCopyAndJump() {
    EnhancedJumpableGenerator ejg1 = EnhancedJumpableGenerator.of(jumpableAlgorithmName);
    EnhancedJumpableGenerator ejg2 = ejg1.copyAndJump();
    assertFalse(ejg1 == ejg2);
    assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
    ejg2.jump();
    assertEquals(ejg1.nextInt(), ejg2.nextInt());
    assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
    assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
    assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
  }

  @Test
  public void testJump() {
    EnhancedJumpableGenerator ejg1 = EnhancedJumpableGenerator.of(jumpableAlgorithmName);
    EnhancedJumpableGenerator ejg2 = ejg1.copy();
    assertFalse(ejg1 == ejg2);
    for (int i = 0; i < 3; i++) {
      ejg1.jump();
      ejg2.jump();
      assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
      assertEquals(ejg1.nextInt(), ejg2.nextInt());
      assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
      assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
      assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
    }
  }

  @Test
  public void testUnlimitedStream() {
    EnhancedJumpableGenerator esg = EnhancedJumpableGenerator.of(jumpableAlgorithmName);
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.jumps()
        .limit(10)
        .forEach(
            gen -> {
              assertTrue(gen instanceof EnhancedRandomGenerator);
              c.count++;
            });
    assertEquals(10, c.count);
  }

  @Test
  public void testLimitedStream() {
    EnhancedJumpableGenerator esg = EnhancedJumpableGenerator.of(jumpableAlgorithmName);
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.jumps(5)
        .forEach(
            gen -> {
              assertTrue(gen instanceof EnhancedRandomGenerator);
              c.count++;
            });
    assertEquals(5, c.count);
  }

  @Test
  public void testUnlimitedStreamE() {
    EnhancedJumpableGenerator esg = EnhancedJumpableGenerator.of(jumpableAlgorithmName);
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.ejumps()
        .limit(10)
        .forEach(
            gen -> {
              // calls a method added by the EnhancedRandomGenerator class
              gen.nextCauchy(1.0);
              c.count++;
            });
    assertEquals(10, c.count);
  }

  @Test
  public void testLimitedStreamE() {
    EnhancedJumpableGenerator esg = EnhancedJumpableGenerator.of(jumpableAlgorithmName);
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.ejumps(5)
        .forEach(
            gen -> {
              // calls a method added by the EnhancedRandomGenerator class
              gen.nextCauchy(1.0);
              c.count++;
            });
    assertEquals(5, c.count);
  }
}
