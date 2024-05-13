/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright (C) 2017-2024 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

import static org.cicirello.util.SimpleSwapper.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

/**
 * Shuffler is a class of utility methods for randomizing the order of elements in an array.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class Shuffler {

  /** Class of static utility methods, so instantiation is not allowed. */
  private Shuffler() {}

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(byte[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(byte[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(byte[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(byte[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(char[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(char[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(char[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(char[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(double[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(double[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(double[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(double[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(float[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(float[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(float[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(float[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(int[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(int[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(int[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(int[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(long[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(long[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(long[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(long[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(short[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(short[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(short[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(short[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   */
  public static void shuffle(Object[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   */
  public static void shuffle(Object[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound))
      ;
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(Object[] array, int first, int last) {
    shuffle(array, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements within a portion of an array. All possible reorderings
   * are equally likely.
   *
   * @param array the array to shuffle
   * @param first the first element (inclusive) of the part of the array to shuffle
   * @param last the last element (exclusive) of the part of the array to shuffle
   * @param gen the source of randomness
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static void shuffle(Object[] array, int first, int last, RandomGenerator gen) {
    for (int bound = last - first;
        bound >= 2;
        swap(array, first + RandomIndexer.nextInt(bound, gen), --bound + first))
      ;
  }
}
