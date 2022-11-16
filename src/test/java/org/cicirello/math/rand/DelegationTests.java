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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.junit.jupiter.api.*;

/**
 * JUnit tests for the methods of the EnhancedRandomGenerator class. The tests in this class
 * specifically verify that methods that should delegate behavior to the wrapped object delegate to
 * the correct method, and those that should not don't.
 */
public class DelegationTests {

  @Test
  public void testDelegation() {
    EnhancedRandomGenerator e = new EnhancedRandomGenerator(new FakeRNG());
    assertEquals(4201, e.nextLong());
    assertEquals(4202, e.nextLong(5000));
    assertEquals(4203, e.nextLong(4000, 5000));
    assertEquals(32, e.nextInt());
    assertEquals(4204.0f, e.nextFloat(), 0.0f);
    assertEquals(4205.0f, e.nextFloat(5000), 0.0f);
    assertEquals(4206.0f, e.nextFloat(4000, 5000), 0.0f);
    assertEquals(4207.0, e.nextExponential(), 0.0);
    assertEquals(0.0, e.nextDouble(), 0.0);
    assertEquals(Math.ulp(0.0), e.nextDouble(5000), 0.0);
    assertEquals(2.0 * Math.ulp(0.0), e.nextDouble(0, 5000), 0.0);
    byte[] b = new byte[2];
    e.nextBytes(b);
    assertEquals(0x42, b[0]);
    assertEquals(0x43, b[1]);
    assertTrue(e.nextBoolean());
    LongStream longStream = e.longs();
    assertEquals(4208, longStream.findFirst().getAsLong());
    longStream.close();
    longStream = e.longs(5);
    assertEquals(4209, longStream.findFirst().getAsLong());
    longStream.close();
    longStream = e.longs(4000, 5000);
    assertEquals(4210, longStream.findFirst().getAsLong());
    longStream.close();
    longStream = e.longs(5, 4000, 5000);
    assertEquals(4211, longStream.findFirst().getAsLong());
    longStream.close();
    IntStream intStream = e.ints();
    assertEquals(4212, intStream.findFirst().getAsInt());
    intStream.close();
    intStream = e.ints(5);
    assertEquals(4213, intStream.findFirst().getAsInt());
    intStream.close();
    DoubleStream dStream = e.doubles();
    assertEquals(4214.0, dStream.findFirst().getAsDouble(), 0.0);
    dStream.close();
    dStream = e.doubles(5);
    assertEquals(4215.0, dStream.findFirst().getAsDouble(), 0.0);
    dStream.close();
    dStream = e.doubles(4000, 5000);
    assertEquals(4216.0, dStream.findFirst().getAsDouble(), 0.0);
    dStream.close();
    dStream = e.doubles(5, 4000, 5000);
    assertEquals(4217.0, dStream.findFirst().getAsDouble(), 0.0);
    dStream.close();
  }

  @Test
  public void testNoDelegation() {
    EnhancedRandomGenerator e = new EnhancedRandomGenerator(new FakeRNG());
    e.nextInt(128);
    e.nextInt(0, 128);
    e.nextGaussian();
    e.nextGaussian(10, 1);
    IntStream intStream = e.ints(10, 100);
    intStream.close();
    intStream = e.ints(5, 10, 100);
    intStream.close();
  }

  private static class FakeRNG implements RandomGenerator {

    @Override
    public long nextLong() {
      return 4201;
    }

    @Override
    public long nextLong(long bound) {
      return 4202;
    }

    @Override
    public long nextLong(long origin, long bound) {
      return 4203;
    }

    @Override
    public int nextInt() {
      return 32;
    }

    @Override
    public int nextInt(int bound) {
      fail("Should not delegate nextInt(bound)");
      return 32;
    }

    @Override
    public int nextInt(int origin, int bound) {
      fail("Should not delegate nextInt(origin, bound)");
      return 32;
    }

    @Override
    public double nextGaussian() {
      fail("Should not delegate nextGaussian()");
      return 0.0;
    }

    @Override
    public double nextGaussian(double mean, double stdev) {
      fail("Should not delegate nextGaussian(mean, stdev)");
      return mean;
    }

    @Override
    public float nextFloat() {
      return 4204.0f;
    }

    @Override
    public float nextFloat(float bound) {
      return 4205.0f;
    }

    @Override
    public float nextFloat(float origin, float bound) {
      return 4206.0f;
    }

    @Override
    public double nextExponential() {
      return 4207.0;
    }

    @Override
    public double nextDouble() {
      return 0.0;
    }

    @Override
    public double nextDouble(double bound) {
      return Math.ulp(0.0);
    }

    @Override
    public double nextDouble(double origin, double bound) {
      return 2 * Math.ulp(0.0);
    }

    @Override
    public void nextBytes(byte[] bytes) {
      bytes[0] = 0x42;
      bytes[1] = 0x43;
    }

    @Override
    public boolean nextBoolean() {
      return true;
    }

    @Override
    public LongStream longs() {
      return LongStream.generate(() -> 4208).sequential();
    }

    @Override
    public LongStream longs(long streamSize) {
      return LongStream.generate(() -> 4209).sequential().limit(streamSize);
    }

    @Override
    public LongStream longs(long origin, long bound) {
      return LongStream.generate(() -> 4210).sequential();
    }

    @Override
    public LongStream longs(long streamSize, long origin, long bound) {
      return LongStream.generate(() -> 4211).sequential().limit(streamSize);
    }

    @Override
    public IntStream ints() {
      return IntStream.generate(() -> 4212).sequential();
    }

    @Override
    public IntStream ints(long streamSize) {
      return IntStream.generate(() -> 4213).sequential().limit(streamSize);
    }

    @Override
    public IntStream ints(int origin, int bound) {
      fail("Should not delegate int(origin, bound)");
      return IntStream.generate(() -> 0).sequential();
    }

    @Override
    public IntStream ints(long streamSize, int origin, int bound) {
      fail("Should not delegate int(origin, bound)");
      return IntStream.generate(() -> origin).sequential().limit(streamSize);
    }

    @Override
    public DoubleStream doubles() {
      return DoubleStream.generate(() -> 4214.0).sequential();
    }

    @Override
    public DoubleStream doubles(long streamSize) {
      return DoubleStream.generate(() -> 4215.0).sequential().limit(streamSize);
    }

    @Override
    public DoubleStream doubles(double origin, double bound) {
      return DoubleStream.generate(() -> 4216.0).sequential();
    }

    @Override
    public DoubleStream doubles(long streamSize, double origin, double bound) {
      return DoubleStream.generate(() -> 4217.0).sequential().limit(streamSize);
    }
  }
}
