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

import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

/**
 * Shuffler is a class of utility methods for randomizing the order of elements in arrays and Lists.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
@SuppressWarnings(
    "PMD.GodClass") // PMD is flagging as GodClass due to array.length (foreign data accesses)
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
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
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
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
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
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
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
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
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
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
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
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
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
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param array the array to shuffle
   * @param <T> type of array elements
   */
  public static <T> void shuffle(T[] array) {
    shuffle(array, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of an array. All possible reorderings are equally
   * likely.
   *
   * @param array the array to shuffle
   * @param gen the source of randomness
   * @param <T> type of array elements
   */
  public static <T> void shuffle(T[] array, RandomGenerator gen) {
    for (int bound = array.length;
        bound >= 2;
        swap(array, RandomIndexer.nextInt(bound, gen), --bound)) {}
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
   * @param <T> type of array elements
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static <T> void shuffle(T[] array, int first, int last) {
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
   * @param <T> type of array elements
   * @throws ArrayIndexOutOfBoundsException if first is less than 0 or if last is greater than
   *     array.length
   */
  public static <T> void shuffle(T[] array, int first, int last, RandomGenerator gen) {
    for (final int minBound = first + 2;
        last >= minBound;
        swap(array, RandomIndexer.nextInt(first, last, gen), --last)) {}
  }

  /**
   * Randomizes the ordering of the elements of a List. All possible reorderings are equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param list the List to shuffle
   * @param <T> type of List elements
   */
  public static <T> void shuffle(List<T> list) {
    shuffle(list, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of a List. All possible reorderings are equally likely.
   *
   * @param list the List to shuffle
   * @param gen the source of randomness
   * @param <T> type of List elements
   */
  public static <T> void shuffle(List<T> list, RandomGenerator gen) {
    if (list instanceof RandomAccess) {
      for (int bound = list.size();
          bound >= 2;
          swap(list, RandomIndexer.nextInt(bound, gen), --bound)) {}
    } else {
      Object[] array = list.toArray();
      shuffle(array, gen);
      list.clear();
      for (int i = 0; i < array.length; i++) {
        @SuppressWarnings("unchecked")
        T element = (T) array[i];
        list.add(element);
      }
    }
  }

  /**
   * Randomizes the ordering of the elements of a portion of a List. All possible reorderings are
   * equally likely.
   *
   * <p>This method uses ThreadLocalRandom as the pseudorandom number generator, and is thus safe,
   * and efficient (i.e., non-blocking), for use with threads.
   *
   * @param list the List to shuffle
   * @param first the first element (inclusive) of the part of the List to shuffle
   * @param last the last element (exclusive) of the part of the List to shuffle
   * @param <T> type of List elements
   */
  public static <T> void shuffle(List<T> list, int first, int last) {
    shuffle(list, first, last, ThreadLocalRandom.current());
  }

  /**
   * Randomizes the ordering of the elements of a portion of a List. All possible reorderings are
   * equally likely.
   *
   * @param list the List to shuffle
   * @param first the first element (inclusive) of the part of the List to shuffle
   * @param last the last element (exclusive) of the part of the List to shuffle
   * @param gen the source of randomness
   * @param <T> type of List elements
   */
  public static <T> void shuffle(List<T> list, int first, int last, RandomGenerator gen) {
    if (list instanceof RandomAccess) {
      for (final int minBound = first + 2;
          last >= minBound;
          swap(list, RandomIndexer.nextInt(first, last, gen), --last)) {}
    } else {
      Object[] array = list.toArray();
      shuffle(array, first, last, gen);
      list.clear();
      for (int i = 0; i < array.length; i++) {
        @SuppressWarnings("unchecked")
        T element = (T) array[i];
        list.add(element);
      }
    }
  }
}
