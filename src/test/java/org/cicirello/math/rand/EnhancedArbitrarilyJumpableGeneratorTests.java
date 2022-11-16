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

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import org.junit.jupiter.api.*;

/** JUnit tests for the methods of the EnhancedArbitrarilyJumpableGenerator class. */
public class EnhancedArbitrarilyJumpableGeneratorTests {

  private static final String arbitrarilyJumpableAlgorithmName = "Xoroshiro128PlusPlus";

  @Test
  public void testWillFailOnceJavaIncludesArbitrarilyJumpableGeneratorAlgorithm() {
    // When this begins failing:
    // 1) Remove fake rng internal class,
    // 2) Insert relevant algorithm name above for arbitrarilyJumpableAlgorithmName,
    // 3) Replace with commented out calls to of in other test methods,
    // 4) Remove unnecessary exception tests,
    // 5 ) Remove this test method.
    final StringBuilder sb = new StringBuilder("ARBITRARILY JUMPABLE:");
    RandomGeneratorFactory.all()
        .forEach(
            gen -> {
              if (gen.isArbitrarilyJumpable()) {
                System.out.println("ARBITRARILY JUMPABLE: " + gen.name());
                sb.append(" ");
                sb.append(gen.name());
              }
            });
    assertFalse(
        RandomGeneratorFactory.all().anyMatch(gen -> gen.isArbitrarilyJumpable()), sb.toString());

    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName));
    thrown =
        assertThrows(
            IllegalArgumentException.class,
            () -> new EnhancedArbitrarilyJumpableGenerator(arbitrarilyJumpableAlgorithmName));
  }

  @Test
  public void testCopy() {
    // EnhancedArbitrarilyJumpableGenerator ejg1 =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator ejg1 =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
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
    // EnhancedArbitrarilyJumpableGenerator ejg1 =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator ejg1 =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
    EnhancedArbitrarilyJumpableGenerator ejg2 = ejg1.copyAndJump((double) (1 << 17));
    assertFalse(ejg1 == ejg2);
    assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
    ejg2.jump((double) (1 << 17));
    assertEquals(ejg1.nextInt(), ejg2.nextInt());
    assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
    assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
    assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
  }

  @Test
  public void testJump() {
    // EnhancedArbitrarilyJumpableGenerator ejg1 =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator ejg1 =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
    EnhancedArbitrarilyJumpableGenerator ejg2 = ejg1.copy();
    assertFalse(ejg1 == ejg2);
    for (int i = 0; i < 3; i++) {
      ejg1.jump(((double) (1 << 17)) * (i + 5));
      ejg2.jump(((double) (1 << 17)) * (i + 5));
      assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
      assertEquals(ejg1.nextInt(), ejg2.nextInt());
      assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
      assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
      assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
    }
  }

  @Test
  public void testJumpPowerOfTwo() {
    // EnhancedArbitrarilyJumpableGenerator ejg1 =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator ejg1 =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
    EnhancedArbitrarilyJumpableGenerator ejg2 = ejg1.copy();
    assertFalse(ejg1 == ejg2);
    for (int i = 0; i < 3; i++) {
      ejg1.jumpPowerOfTwo(17);
      ejg2.jump(((double) (1 << 17)));
      assertEquals(ejg1.jumpDistance(), ejg2.jumpDistance(), 0.0);
      assertEquals(ejg1.nextInt(), ejg2.nextInt());
      assertEquals(ejg1.nextDouble(), ejg2.nextDouble(), 0.0);
      assertEquals(ejg1.nextInt(10), ejg2.nextInt(10));
      assertEquals(ejg1.nextCauchy(1.0), ejg2.nextCauchy(1.0), 0.0);
    }
  }

  @Test
  public void testUnlimitedStream() {
    // EnhancedArbitrarilyJumpableGenerator esg =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator esg =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.jumps((double) (1 << 17))
        .limit(10)
        .forEach(
            gen -> {
              assertTrue(gen instanceof EnhancedArbitrarilyJumpableGenerator);
              c.count++;
            });
    assertEquals(10, c.count);
  }

  @Test
  public void testLimitedStream() {
    // EnhancedArbitrarilyJumpableGenerator esg =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator esg =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.jumps(5, (double) (1 << 17))
        .forEach(
            gen -> {
              assertTrue(gen instanceof EnhancedArbitrarilyJumpableGenerator);
              c.count++;
            });
    assertEquals(5, c.count);
  }

  @Test
  public void testUnlimitedStreamE() {
    // EnhancedArbitrarilyJumpableGenerator esg =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator esg =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.ejumps((double) (1 << 17))
        .limit(10)
        .forEach(
            gen -> {
              // calls a method added by the EnhancedArbitrarilyJumpableGenerator class
              gen.nextCauchy(1.0);
              c.count++;
            });
    assertEquals(10, c.count);
  }

  @Test
  public void testLimitedStreamE() {
    // EnhancedArbitrarilyJumpableGenerator esg =
    // EnhancedArbitrarilyJumpableGenerator.of(arbitrarilyJumpableAlgorithmName);
    EnhancedArbitrarilyJumpableGenerator esg =
        new EnhancedArbitrarilyJumpableGenerator(new FakeArbitrarilyJumpableRNG());
    class Counter {
      int count;
    }
    final Counter c = new Counter();
    esg.ejumps(5, (double) (1 << 17))
        .forEach(
            gen -> {
              // calls a method added by the EnhancedArbitrarilyJumpableGenerator class
              gen.nextCauchy(1.0);
              c.count++;
            });
    assertEquals(5, c.count);
  }

  /*
   * Java 17 doesn't have any RandomGenerator.ArbitrarilyJumpableGenerator in the API.
   * This class is a fake one for testing purposes only. It is not real. Just serves
   * testing purposes. Do not use in any real application.
   */
  private static class FakeArbitrarilyJumpableRNG
      implements RandomGenerator.ArbitrarilyJumpableGenerator {

    private RandomGenerator.LeapableGenerator generator;

    private FakeArbitrarilyJumpableRNG() {
      generator = RandomGenerator.LeapableGenerator.of("Xoroshiro128PlusPlus");
    }

    private FakeArbitrarilyJumpableRNG(RandomGenerator.LeapableGenerator gen) {
      generator = gen;
    }

    @Override
    public long nextLong() {
      return generator.nextLong();
    }

    @Override
    public double jumpDistance() {
      return generator.jumpDistance();
    }

    @Override
    public FakeArbitrarilyJumpableRNG copy() {
      return new FakeArbitrarilyJumpableRNG(generator.copy());
    }

    @Override
    public double leapDistance() {
      return generator.leapDistance();
    }

    @Override
    public void jump(double distance) {
      int count = (int) Math.round(Math.log(distance) / Math.log(2.0));
      for (int i = 0; i < count; i++) {
        generator.jump();
      }
    }

    @Override
    public void jumpPowerOfTwo(int logDistance) {
      jump(Math.pow(2, logDistance));
    }
  }
}
