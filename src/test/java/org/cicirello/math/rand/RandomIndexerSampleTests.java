/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2024 Vincent A. Cicirello, <https://www.cicirello.org/>.
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
 */

package org.cicirello.math.rand;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.SplittableRandom;
import org.junit.jupiter.api.*;

/** JUnit tests for the methods of the RandomIndexer class. */
public class RandomIndexerSampleTests {

  // Part of each test case in this class is a chi square goodness of fit test
  // on a large number of samples to test for uniformity of results.
  // Some of these tests are of methods that rely on ThreadLocalRandom, which
  // doesn't allow setting a seed, so it is impossible to 100% replicate tests
  // from one run of the test set to the next.
  // These test cases fully passed the most recent time run.
  // If you make any changes to the sampling methods that depend upon ThreadLocalRandom,
  // then change this constant to false, run all tests, and switch back to true if
  // they pass.  In the event that the chi square tests "fail" for methods that rely on
  // ThreadLocalRandom, then look carefully at the statistical test results, and rerun
  // since a "failure" is not necessarily an actual failure (statistically, the chi square
  // test statistic can be above the cutoff 5% of the time).
  private static final boolean DISABLE_CHI_SQUARE_TESTS = true;

  @Test
  public void testSamplePSplittable() {
    final int TRIALS = 1000;
    SplittableRandom r = new SplittableRandom(42);
    double[] P = {0.25, 0.5, 0.75};
    int n = 100;
    for (double p : P) {
      int sum = 0;
      for (int i = 0; i < TRIALS; i++) {
        int[] result = RandomSampler.sample(n, p, r);
        assertTrue(validSample(n, result), "verify correct range and no duplicates");
        sum += result.length;
      }
      double ave = 1.0 * sum / TRIALS;
      assertTrue(Math.abs(n * p - ave) <= 5, "verify correct sampling frequency");
    }
    int[] result = RandomSampler.sample(4, 0.0, r);
    assertEquals(0, result.length);
    result = RandomSampler.sample(4, 1.0, r);
    assertEquals(4, result.length);
  }

  @Test
  public void testSamplePThreadLocal() {
    final int TRIALS = 1000;
    double[] P = {0.25, 0.5, 0.75};
    int n = 100;
    for (double p : P) {
      int sum = 0;
      for (int i = 0; i < TRIALS; i++) {
        int[] result = RandomSampler.sample(n, p);
        assertTrue(validSample(n, result), "verify correct range and no duplicates");
        sum += result.length;
      }
      // Don't do this check since we can't seed.
      // double ave = 1.0 * sum / TRIALS;
      // assertTrue("verify correct sampling frequency", Math.abs(n*p-ave) <= 5);
    }
    int[] result = RandomSampler.sample(4, 0.0);
    assertEquals(0, result.length);
    result = RandomSampler.sample(4, 1.0);
    assertEquals(4, result.length);
  }

  @Test
  public void testSampleReservoir_ThreadLocalRandom() {
    for (int n = 1; n <= 6; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = RandomSampler.sampleReservoir(n, k, null);
        assertEquals(k, result.length, "Length of result should be " + k);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(
              result[i] < n && result[i] >= 0,
              "Each integer should be at least 0 and less than " + k);
          assertFalse(inResult[result[i]], "Result shouldn't contain duplicates");
          inResult[result[i]] = true;
        }
      }
    }
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> RandomSampler.sampleReservoir(1, 2, null));
  }

  @Test
  public void testSampleReservoir_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;

    for (int n = 1; n <= 6; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = RandomSampler.sampleReservoir(n, k, null, gen);
        assertEquals(k, result.length, "Length of result should be " + k);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(
              result[i] < n && result[i] >= 0,
              "Each integer should be at least 0 and less than " + k);
          assertFalse(inResult[result[i]], "Result shouldn't contain duplicates");
          inResult[result[i]] = true;
        }
      }
    }
    for (int n = 1; n <= 5; n++) {
      int m = n < 3 ? n : 3;
      for (int k = 1; k <= m; k++) {
        int countH = 0;
        for (int trial = 0; trial < 100; trial++) {
          int[] buckets1 = new int[n];
          int[][] buckets2 = new int[n][n];
          int[][][] buckets3 = new int[n][n][n];
          int numBuckets = k == 1 ? n : (k == 2 ? n * (n - 1) / 2 : n * (n - 1) * (n - 2) / 6);
          for (int j = 0; j < REPS_PER_BUCKET * numBuckets; j++) {
            int[] result = RandomSampler.sampleReservoir(n, k, null, gen);
            Arrays.sort(result);
            switch (k) {
              case 1:
                buckets1[result[0]] += 1;
                break;
              case 2:
                buckets2[result[0]][result[1]] += 1;
                break;
              case 3:
                buckets3[result[0]][result[1]][result[2]] += 1;
                break;
            }
          }
          switch (k) {
            case 1:
              for (int x = 0; x < n; x++) {
                assertTrue(buckets1[x] > 0, "failed to generate any samples: " + x);
              }
              double chi1 = chiSquare(buckets1, numBuckets);
              if (chi1 > limit95[numBuckets - 1]) countH++;
              break;
            case 2:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  assertTrue(
                      buckets2[x][y] > 0, "failed to generate any samples: (" + x + ", " + y + ")");
                }
              }
              double chi2 = chiSquare(buckets2, numBuckets);
              if (chi2 > limit95[numBuckets - 1]) countH++;
              break;
            case 3:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  for (int z = y + 1; z < n; z++) {
                    assertTrue(
                        buckets3[x][y][z] > 0,
                        "failed to generate any samples: (" + x + ", " + y + ", " + z + ")");
                  }
                }
              }
              double chi3 = chiSquare(buckets3, numBuckets);
              if (chi3 > limit95[numBuckets - 1]) countH++;
              break;
          }
        }
        assertTrue(countH <= 10, "chi square too high too often, countHigh=" + countH);
      }
    }
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> RandomSampler.sampleReservoir(1, 2, null, gen));
    int[] expected = new int[2];
    int[] actual = RandomSampler.sampleReservoir(5, 2, expected, gen);
    assertTrue(expected == actual);
    actual = RandomSampler.sampleReservoir(5, 3, expected, gen);
    assertTrue(expected != actual);
    assertEquals(3, actual.length);
  }

  @Test
  public void testSamplePool_ThreadLocalRandom() {
    for (int n = 1; n <= 6; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = RandomSampler.samplePool(n, k, null);
        assertEquals(k, result.length, "Length of result should be " + k);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(
              result[i] < n && result[i] >= 0,
              "Each integer should be at least 0 and less than " + k);
          assertFalse(inResult[result[i]], "Result shouldn't contain duplicates");
          inResult[result[i]] = true;
        }
      }
    }
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> RandomSampler.samplePool(1, 2, null));
  }

  @Test
  public void testSamplePool_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 100;

    for (int n = 1; n <= 6; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = RandomSampler.samplePool(n, k, null, gen);
        assertEquals(k, result.length, "Length of result should be " + k);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(
              result[i] < n && result[i] >= 0,
              "Each integer should be at least 0 and less than " + k);
          assertFalse(inResult[result[i]], "Result shouldn't contain duplicates");
          inResult[result[i]] = true;
        }
      }
    }
    for (int n = 1; n <= 5; n++) {
      int m = n < 3 ? n : 3;
      for (int k = 1; k <= m; k++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[] buckets1 = new int[n];
          int[][] buckets2 = new int[n][n];
          int[][][] buckets3 = new int[n][n][n];
          int numBuckets = k == 1 ? n : (k == 2 ? n * (n - 1) / 2 : n * (n - 1) * (n - 2) / 6);
          for (int j = 0; j < REPS_PER_BUCKET * numBuckets; j++) {
            int[] result = RandomSampler.samplePool(n, k, null, gen);
            Arrays.sort(result);
            switch (k) {
              case 1:
                buckets1[result[0]] += 1;
                break;
              case 2:
                buckets2[result[0]][result[1]] += 1;
                break;
              case 3:
                buckets3[result[0]][result[1]][result[2]] += 1;
                break;
            }
          }
          switch (k) {
            case 1:
              for (int x = 0; x < n; x++) {
                assertTrue(buckets1[x] > 0, "failed to generate any samples: " + x);
              }
              double chi1 = chiSquare(buckets1, numBuckets);
              if (chi1 > limit95[numBuckets - 1]) countH++;
              break;
            case 2:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  assertTrue(
                      buckets2[x][y] > 0, "failed to generate any samples: (" + x + ", " + y + ")");
                }
              }
              double chi2 = chiSquare(buckets2, numBuckets);
              if (chi2 > limit95[numBuckets - 1]) countH++;
              break;
            case 3:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  for (int z = y + 1; z < n; z++) {
                    assertTrue(
                        buckets3[x][y][z] > 0,
                        "failed to generate any samples: (" + x + ", " + y + ", " + z + ")");
                  }
                }
              }
              double chi3 = chiSquare(buckets3, numBuckets);
              if (chi3 > limit95[numBuckets - 1]) countH++;
              break;
          }
        }
        assertTrue(
            countH <= TRIALS * 0.1,
            "chi square too high too often, countHigh=" + countH + " n=" + n + " k=" + k);
      }
    }
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> RandomSampler.samplePool(1, 2, null, gen));
    int[] expected = new int[2];
    int[] actual = RandomSampler.samplePool(5, 2, expected, gen);
    assertTrue(expected == actual);
    actual = RandomSampler.samplePool(5, 3, expected, gen);
    assertTrue(expected != actual);
    assertEquals(3, actual.length);
  }

  @Test
  public void testSample_ThreadLocalRandom() {
    for (int n = 1; n <= 7; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = null;
        result = RandomSampler.sample(n, k, result);
        assertEquals(k, result.length, "Length of result should be " + k);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(
              result[i] < n && result[i] >= 0,
              "Each integer should be at least 0 and less than " + k);
          assertFalse(inResult[result[i]], "Result shouldn't contain duplicates");
          inResult[result[i]] = true;
        }
      }
    }
  }

  @Test
  public void testSample_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(40);
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 100;

    for (int n = 1; n <= 6; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = RandomSampler.sample(n, k, null, gen);
        assertEquals(k, result.length, "Length of result should be " + k);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(
              result[i] < n && result[i] >= 0,
              "Each integer should be at least 0 and less than " + k);
          assertFalse(inResult[result[i]], "Result shouldn't contain duplicates");
          inResult[result[i]] = true;
        }
      }
    }
    for (int n = 1; n <= 5; n++) {
      int m = n < 3 ? n : 3;
      for (int k = 1; k <= m; k++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[] buckets1 = new int[n];
          int[][] buckets2 = new int[n][n];
          int[][][] buckets3 = new int[n][n][n];
          int numBuckets = k == 1 ? n : (k == 2 ? n * (n - 1) / 2 : n * (n - 1) * (n - 2) / 6);
          for (int j = 0; j < REPS_PER_BUCKET * numBuckets; j++) {
            int[] result = RandomSampler.sample(n, k, null, gen);
            Arrays.sort(result);
            switch (k) {
              case 1:
                buckets1[result[0]] += 1;
                break;
              case 2:
                buckets2[result[0]][result[1]] += 1;
                break;
              case 3:
                buckets3[result[0]][result[1]][result[2]] += 1;
                break;
            }
          }
          switch (k) {
            case 1:
              for (int x = 0; x < n; x++) {
                assertTrue(buckets1[x] > 0, "failed to generate any samples: " + x);
              }
              double chi1 = chiSquare(buckets1, numBuckets);
              if (chi1 > limit95[numBuckets - 1]) countH++;
              break;
            case 2:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  assertTrue(
                      buckets2[x][y] > 0, "failed to generate any samples: (" + x + ", " + y + ")");
                }
              }
              double chi2 = chiSquare(buckets2, numBuckets);
              if (chi2 > limit95[numBuckets - 1]) countH++;
              break;
            case 3:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  for (int z = y + 1; z < n; z++) {
                    assertTrue(
                        buckets3[x][y][z] > 0,
                        "failed to generate any samples: (" + x + ", " + y + ", " + z + ")");
                  }
                }
              }
              double chi3 = chiSquare(buckets3, numBuckets);
              if (chi3 > limit95[numBuckets - 1]) countH++;
              break;
          }
        }
        assertTrue(
            countH <= TRIALS * 0.1,
            "chi square too high too often, countHigh=" + countH + " n=" + n + " k=" + k);
      }
    }
  }

  @Test
  public void testPair_ThreadLocalRandom() {
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 200;

    for (int n = 2; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        int[] result = RandomIndexer.nextIntPair(n, (int[]) null);
        assertEquals(2, result.length, "Length of result should be 2");
        assertNotEquals(result[0], result[1], "integers should be different");
        assertTrue(result[0] >= 0, "integers should be at least 0");
        assertTrue(result[1] < n, "integers should be less than " + n);
        assertTrue(result[1] >= 0, "integers should be at least 0");
        assertTrue(result[0] < n, "integers should be less than " + n);
      }
    }

    int[] expected = new int[2];
    int[] actual = RandomIndexer.nextIntPair(5, expected);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextIntPair(5, new int[1]);
    assertEquals(2, actual.length);

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 2; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][] buckets = new int[n][n];
        int numBuckets = n * (n - 1); // /2;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          int[] result = RandomIndexer.nextIntPair(n, (int[]) null);
          buckets[result[0]][result[1]]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testPair_IndexPair_ThreadLocalRandom() {
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 200;

    for (int n = 2; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        IndexPair result = RandomIndexer.nextIntPair(n);
        assertNotEquals(result.i(), result.j(), "integers should be different");
        assertTrue(result.i() >= 0, "integers should be at least 0");
        assertTrue(result.j() < n, "integers should be less than " + n);
        assertTrue(result.j() >= 0, "integers should be at least 0");
        assertTrue(result.i() < n, "integers should be less than " + n);
      }
    }

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 2; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][] buckets = new int[n][n];
        int numBuckets = n * (n - 1); // /2;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          IndexPair result = RandomIndexer.nextIntPair(n);
          buckets[result.i()][result.j()]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testTriple_ThreadLocalRandom() {
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 200;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        int[] result = RandomIndexer.nextIntTriple(n, (int[]) null);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[2], result[1]);
        assertNotEquals(result[0], result[2]);
        Arrays.sort(result);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
      }
    }

    int[] expected = new int[3];
    int[] actual = RandomIndexer.nextIntTriple(5, expected);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextIntTriple(5, new int[2]);
    assertEquals(3, actual.length);

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int[] sortBuckets = new int[6];
        int numBuckets = n * (n - 1) * (n - 2);
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          int[] result = RandomIndexer.nextIntTriple(n, (int[]) null);
          buckets[result[0]][result[1]][result[2]]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testTripleSorted_ThreadLocalRandom() {
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 200;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        int[] result = RandomIndexer.nextSortedIntTriple(n, (int[]) null);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[2], result[1]);
        assertNotEquals(result[0], result[2]);
        assertTrue(result[0] < result[1]);
        assertTrue(result[1] < result[2]);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
      }
    }

    int[] expected = new int[3];
    int[] actual = RandomIndexer.nextSortedIntTriple(5, expected);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextSortedIntTriple(5, new int[2]);
    assertEquals(3, actual.length);

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int numBuckets = n * (n - 1) * (n - 2) / 6;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          int[] result = RandomIndexer.nextSortedIntTriple(n, (int[]) null);
          buckets[result[0]][result[1]][result[2]]++;
        }
        double chi = chiSquare(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testTriple_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        int[] result = RandomIndexer.nextIntTriple(n, null, gen);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[2], result[1]);
        assertNotEquals(result[0], result[2]);
        Arrays.sort(result);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
      }
    }
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int numBuckets = n * (n - 1) * (n - 2);
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          int[] result = RandomIndexer.nextIntTriple(n, null, gen);
          buckets[result[0]][result[1]][result[2]]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }

    int[] expected = new int[3];
    int[] actual = RandomIndexer.nextIntTriple(5, expected, gen);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextIntTriple(5, new int[2], gen);
    assertEquals(3, actual.length);
  }

  @Test
  public void testTripleSorted_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        int[] result = RandomIndexer.nextSortedIntTriple(n, null, gen);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[2], result[1]);
        assertNotEquals(result[0], result[2]);
        assertTrue(result[0] < result[1]);
        assertTrue(result[1] < result[2]);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
      }
    }
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int numBuckets = n * (n - 1) * (n - 2) / 6;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          int[] result = RandomIndexer.nextSortedIntTriple(n, null, gen);
          buckets[result[0]][result[1]][result[2]]++;
        }
        double chi = chiSquare(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }

    int[] expected = new int[3];
    int[] actual = RandomIndexer.nextSortedIntTriple(5, expected, gen);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextSortedIntTriple(5, new int[2], gen);
    assertEquals(3, actual.length);
  }

  @Test
  public void testPair_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 2; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        int[] result = RandomIndexer.nextIntPair(n, null, gen);
        assertEquals(2, result.length);
        assertNotEquals(result[0], result[1]);
        assertTrue(result[0] >= 0);
        assertTrue(result[1] < n);
        assertTrue(result[1] >= 0);
        assertTrue(result[0] < n);
      }
    }
    for (int n = 2; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][] buckets = new int[n][n];
        int numBuckets = n * (n - 1); // /2;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          int[] result = RandomIndexer.nextIntPair(n, null, gen);
          buckets[result[0]][result[1]]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }

    int[] expected = new int[2];
    int[] actual = RandomIndexer.nextIntPair(5, expected, gen);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextIntPair(5, new int[1], gen);
    assertEquals(2, actual.length);
  }

  @Test
  public void testPair_IndexPair_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 2; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        IndexPair result = RandomIndexer.nextIntPair(n, gen);
        assertNotEquals(result.i(), result.j());
        assertTrue(result.i() >= 0);
        assertTrue(result.j() < n);
        assertTrue(result.j() >= 0);
        assertTrue(result.i() < n);
      }
    }
    for (int n = 2; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][] buckets = new int[n][n];
        int numBuckets = n * (n - 1); // /2;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          IndexPair result = RandomIndexer.nextIntPair(n, gen);
          buckets[result.i()][result.j()]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testTriple_IndexTriple_ThreadLocalRandom() {
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 200;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        IndexTriple result = RandomIndexer.nextIntTriple(n);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.k(), result.j());
        assertNotEquals(result.i(), result.k());
        assertTrue(result.i() >= 0);
        assertTrue(result.i() < n);
        assertTrue(result.j() >= 0);
        assertTrue(result.j() < n);
        assertTrue(result.k() >= 0);
        assertTrue(result.k() < n);
      }
    }

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int[] sortBuckets = new int[6];
        int numBuckets = n * (n - 1) * (n - 2);
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          IndexTriple result = RandomIndexer.nextIntTriple(n);
          buckets[result.i()][result.j()][result.k()]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testTripleSorted_IndexTriple_ThreadLocalRandom() {
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 200;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        IndexTriple result = RandomIndexer.nextSortedIntTriple(n);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.k(), result.j());
        assertNotEquals(result.i(), result.k());
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
        assertTrue(result.i() >= 0);
        assertTrue(result.k() < n);
      }
    }

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int numBuckets = n * (n - 1) * (n - 2) / 6;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          IndexTriple result = RandomIndexer.nextSortedIntTriple(n);
          buckets[result.i()][result.j()][result.k()]++;
        }
        double chi = chiSquare(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testTriple_IndexTriple_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        IndexTriple result = RandomIndexer.nextIntTriple(n, gen);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.k(), result.j());
        assertNotEquals(result.i(), result.k());
        assertTrue(result.i() >= 0);
        assertTrue(result.i() < n);
        assertTrue(result.j() >= 0);
        assertTrue(result.j() < n);
        assertTrue(result.k() >= 0);
        assertTrue(result.k() < n);
      }
    }
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int numBuckets = n * (n - 1) * (n - 2);
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          IndexTriple result = RandomIndexer.nextIntTriple(n, gen);
          buckets[result.i()][result.j()][result.k()]++;
        }
        double chi = chiSquareAll(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testTripleSorted_IndexTriple_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 3; n <= 6; n++) {
      for (int i = 0; i < 10; i++) {
        IndexTriple result = RandomIndexer.nextSortedIntTriple(n, gen);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.k(), result.j());
        assertNotEquals(result.i(), result.k());
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
        assertTrue(result.i() >= 0);
        assertTrue(result.k() < n);
      }
    }
    for (int n = 3; n <= 6; n++) {
      int countH = 0;
      for (int trial = 0; trial < TRIALS; trial++) {
        int[][][] buckets = new int[n][n][n];
        int numBuckets = n * (n - 1) * (n - 2) / 6;
        for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
          IndexTriple result = RandomIndexer.nextSortedIntTriple(n, gen);
          buckets[result.i()][result.j()][result.k()]++;
        }
        double chi = chiSquare(buckets, numBuckets);
        if (chi > limit95[numBuckets - 1]) countH++;
      }
      assertTrue(
          countH <= TRIALS * 0.1, "chi square too high too often, countHigh=" + countH + " n=" + n);
    }
  }

  @Test
  public void testSampleInsertion_ThreadLocalRandom() {
    for (int n = 1; n <= 6; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = RandomSampler.sampleInsertion(n, k, null);
        assertEquals(k, result.length);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(result[i] < n && result[i] >= 0);
          assertFalse(inResult[result[i]]);
          inResult[result[i]] = true;
        }
      }
    }
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> RandomSampler.sampleInsertion(1, 2, null));
  }

  @Test
  public void testSampleInsertion_SplittableRandom() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 200;
    final int TRIALS = 100;

    for (int n = 1; n <= 6; n++) {
      for (int k = 0; k <= n; k++) {
        int[] result = RandomSampler.sampleInsertion(n, k, null, gen);
        assertEquals(k, result.length);
        boolean[] inResult = new boolean[n];
        for (int i = 0; i < k; i++) {
          assertTrue(result[i] < n && result[i] >= 0);
          assertFalse(inResult[result[i]]);
          inResult[result[i]] = true;
        }
      }
    }
    for (int n = 1; n <= 5; n++) {
      int m = n < 3 ? n : 3;
      for (int k = 1; k <= m; k++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[] buckets1 = new int[n];
          int[][] buckets2 = new int[n][n];
          int[][][] buckets3 = new int[n][n][n];
          int numBuckets = k == 1 ? n : (k == 2 ? n * (n - 1) / 2 : n * (n - 1) * (n - 2) / 6);
          for (int j = 0; j < REPS_PER_BUCKET * numBuckets; j++) {
            int[] result = RandomSampler.sampleInsertion(n, k, null, gen);
            Arrays.sort(result);
            switch (k) {
              case 1:
                buckets1[result[0]] += 1;
                break;
              case 2:
                buckets2[result[0]][result[1]] += 1;
                break;
              case 3:
                buckets3[result[0]][result[1]][result[2]] += 1;
                break;
            }
          }
          switch (k) {
            case 1:
              for (int x = 0; x < n; x++) {
                assertTrue(buckets1[x] > 0);
              }
              double chi1 = chiSquare(buckets1, numBuckets);
              if (chi1 > limit95[numBuckets - 1]) countH++;
              break;
            case 2:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  assertTrue(buckets2[x][y] > 0);
                }
              }
              double chi2 = chiSquare(buckets2, numBuckets);
              if (chi2 > limit95[numBuckets - 1]) countH++;
              break;
            case 3:
              for (int x = 0; x < n; x++) {
                for (int y = x + 1; y < n; y++) {
                  for (int z = y + 1; z < n; z++) {
                    assertTrue(buckets3[x][y][z] > 0);
                  }
                }
              }
              double chi3 = chiSquare(buckets3, numBuckets);
              if (chi3 > limit95[numBuckets - 1]) countH++;
              break;
          }
        }
        assertTrue(countH <= TRIALS * 0.1);
      }
    }
    IllegalArgumentException thrown =
        assertThrows(
            IllegalArgumentException.class, () -> RandomSampler.sampleInsertion(1, 2, null, gen));
    int[] expected = new int[2];
    int[] actual = RandomSampler.sampleInsertion(5, 2, expected, gen);
    assertTrue(expected == actual);
    actual = RandomSampler.sampleInsertion(5, 3, expected, gen);
    assertTrue(expected != actual);
    assertEquals(3, actual.length);
  }

  @Test
  public void testNextWindowedIntPair_TLR() {
    final int REPS_PER_BUCKET = 600;
    final int TRIALS = 100;

    for (int n = 2; n <= 10; n++) {
      for (int w = 1; w < n; w++) {
        int[] result = RandomIndexer.nextWindowedIntPair(n, w, (int[]) null);
        assertEquals(2, result.length);
        assertNotEquals(result[0], result[1]);
        if (result[0] > result[1]) {
          int temp = result[0];
          result[0] = result[1];
          result[1] = temp;
        }
        assertTrue(result[0] >= 0);
        assertTrue(result[1] < n);
        assertTrue(result[1] - result[0] <= w);
      }
    }

    int[] expected = new int[2];
    int[] actual = RandomIndexer.nextWindowedIntPair(5, 1, expected);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextWindowedIntPair(5, 1, new int[1]);
    assertEquals(2, actual.length);

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 2; n <= 7; n++) {
      for (int w = 1; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][] buckets = new int[n][n];
          int numBuckets = w * (n - w) + w * (w - 1) / 2;
          numBuckets *= 2;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            int[] result = RandomIndexer.nextWindowedIntPair(n, w, (int[]) null);
            buckets[result[0]][result[1]]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (j > i && j - i > w) continue;
              if (i > j && i - j > w) continue;
              flatBuckets[k] = buckets[i][j];
              k++;
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) countH++;
        }
        assertTrue(countH <= TRIALS * 0.1);
      }
    }
  }

  @Test
  public void testNextWindowedIntPair_SR() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 2; n <= 10; n++) {
      for (int w = 1; w < n; w++) {
        int[] result = RandomIndexer.nextWindowedIntPair(n, w, (int[]) null, gen);
        assertEquals(2, result.length);
        assertNotEquals(result[0], result[1]);
        if (result[0] > result[1]) {
          int temp = result[0];
          result[0] = result[1];
          result[1] = temp;
        }
        assertTrue(result[0] >= 0);
        assertTrue(result[1] < n);
        assertTrue(result[1] - result[0] <= w);
      }
    }
    for (int n = 2; n <= 7; n++) {
      for (int w = 1; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][] buckets = new int[n][n];
          int numBuckets = w * (n - w) + w * (w - 1) / 2;
          numBuckets *= 2;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            int[] result = RandomIndexer.nextWindowedIntPair(n, w, (int[]) null, gen);
            buckets[result[0]][result[1]]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (j > i && j - i > w) continue;
              if (i > j && i - j > w) continue;
              flatBuckets[k] = buckets[i][j];
              k++;
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) countH++;
        }
        assertTrue(countH <= TRIALS * 0.1);
      }
    }

    int[] expected = new int[2];
    int[] actual = RandomIndexer.nextWindowedIntPair(5, 1, expected, gen);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextWindowedIntPair(5, 1, new int[1], gen);
    assertEquals(2, actual.length);
  }

  @Test
  public void testNextWindowedIntPair_IndexPair_TLR() {
    final int REPS_PER_BUCKET = 600;
    final int TRIALS = 100;

    for (int n = 2; n <= 10; n++) {
      for (int w = 1; w < n; w++) {
        IndexPair result = RandomIndexer.nextWindowedIntPair(n, w);
        assertNotEquals(result.i(), result.j());
        int min = Math.min(result.i(), result.j());
        int max = Math.max(result.i(), result.j());
        assertTrue(min >= 0);
        assertTrue(max < n);
        assertTrue(max - min <= w);
      }
    }

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 2; n <= 7; n++) {
      for (int w = 1; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][] buckets = new int[n][n];
          int numBuckets = w * (n - w) + w * (w - 1) / 2;
          numBuckets *= 2;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            IndexPair result = RandomIndexer.nextWindowedIntPair(n, w);
            buckets[result.i()][result.j()]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (j > i && j - i > w) continue;
              if (i > j && i - j > w) continue;
              flatBuckets[k] = buckets[i][j];
              k++;
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) countH++;
        }
        assertTrue(countH <= TRIALS * 0.1);
      }
    }
  }

  @Test
  public void testNextWindowedIntPair_IndexPair_SR() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    for (int n = 2; n <= 10; n++) {
      for (int w = 1; w < n; w++) {
        IndexPair result = RandomIndexer.nextWindowedIntPair(n, w, gen);
        assertNotEquals(result.i(), result.j());
        int min = Math.min(result.i(), result.j());
        int max = Math.max(result.i(), result.j());
        assertTrue(min >= 0);
        assertTrue(max < n);
        assertTrue(max - min <= w);
      }
    }
    for (int n = 2; n <= 7; n++) {
      for (int w = 1; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][] buckets = new int[n][n];
          int numBuckets = w * (n - w) + w * (w - 1) / 2;
          numBuckets *= 2;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            IndexPair result = RandomIndexer.nextWindowedIntPair(n, w, gen);
            buckets[result.i()][result.j()]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (j > i && j - i > w) continue;
              if (i > j && i - j > w) continue;
              flatBuckets[k] = buckets[i][j];
              k++;
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) countH++;
        }
        assertTrue(countH <= TRIALS * 0.1);
      }
    }
  }

  @Test
  public void testNextWindowedIntTriple_TLR() {
    final int REPS_PER_BUCKET = 300;
    final int TRIALS = 100;

    for (int n = 3; n <= 10; n++) {
      for (int w = 2; w < n; w++) {
        int[] result = RandomIndexer.nextWindowedIntTriple(n, w, (int[]) null);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[0], result[2]);
        assertNotEquals(result[2], result[1]);
        Arrays.sort(result);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
        assertTrue(result[2] - result[0] <= w);
        result = RandomIndexer.nextSortedWindowedIntTriple(n, w, (int[]) null);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[0], result[2]);
        assertNotEquals(result[2], result[1]);
        String state =
            "result=(" + result[0] + ", " + result[1] + ", " + result[2] + "), w=" + w + " n=" + n;
        assertTrue(result[0] < result[1], "integers should be sorted: " + state);
        assertTrue(result[1] < result[2], "integers should be sorted: " + state);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
        assertTrue(result[2] - result[0] <= w);
      }
    }

    int[] expected = new int[3];
    int[] actual = RandomIndexer.nextWindowedIntTriple(5, 3, expected);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextWindowedIntTriple(5, 3, new int[2]);
    assertEquals(3, actual.length);

    actual = RandomIndexer.nextSortedWindowedIntTriple(5, 3, expected);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextSortedWindowedIntTriple(5, 3, new int[2]);
    assertEquals(3, actual.length);

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 3; n <= 6; n++) {
      for (int w = 2; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][][] buckets = new int[n][n][n];
          int numBuckets = w * (n - w) * (w - 1) / 2 + w * (w - 1) * (w - 2) / 6;
          numBuckets *= 6;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            int[] result = RandomIndexer.nextWindowedIntTriple(n, w, (int[]) null);
            buckets[result[0]][result[1]][result[2]]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (Math.abs(j - i) > w) continue;
              for (int h = 0; h < n; h++) {
                if (h == i || h == j) continue;
                if (Math.abs(h - i) > w) continue;
                if (Math.abs(j - h) > w) continue;
                flatBuckets[k] = buckets[i][j][h];
                k++;
              }
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) countH++;
        }
        assertTrue(countH <= TRIALS * 0.1);
      }
    }
  }

  @Test
  public void testNextWindowedIntTriple_SR() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    {
      // Make sure sorting covers all cases:
      // This part of the test aims to fully cover the
      // helper method: sortSetAndAdjustWindowedTriple

      // This is the easy path through the method, even one sample
      // should be sufficient with n=3 and w=2.
      for (int i = 0; i < 3; i++) {
        int[] result = RandomIndexer.nextSortedWindowedIntTriple(3, 2, (int[]) null, gen);
        assertEquals(3, result.length);
        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
        assertEquals(2, result[2]);
      }

      // With n=1000 and w=n-1, approximately 99.7% of the following samples should
      // go through the alternate path from above. That alternate path has
      // 3 potential subpaths, all approximately equally likely.
      int n = 1000;
      int w = n - 1;
      for (int i = 0; i < 30; i++) {
        int[] result = RandomIndexer.nextSortedWindowedIntTriple(n, w, (int[]) null, gen);
        assertEquals(3, result.length);
        assertTrue(result[0] < result[1]);
        assertTrue(result[1] < result[2]);
      }
      n = 10000;
      w = n - 1;
      for (int i = 0; i < 30; i++) {
        int[] result = RandomIndexer.nextSortedWindowedIntTriple(n, w, (int[]) null, gen);
        assertEquals(3, result.length);
        assertTrue(result[0] < result[1]);
        assertTrue(result[1] < result[2]);
      }
      n = 100;
      w = n - 1;
      for (int i = 0; i < 30; i++) {
        int[] result = RandomIndexer.nextSortedWindowedIntTriple(n, w, (int[]) null, gen);
        assertEquals(3, result.length);
        assertTrue(result[0] < result[1]);
        assertTrue(result[1] < result[2]);
      }
      n = 10;
      w = n - 1;
      for (int i = 0; i < 30; i++) {
        int[] result = RandomIndexer.nextSortedWindowedIntTriple(n, w, (int[]) null, gen);
        assertEquals(3, result.length);
        assertTrue(result[0] < result[1]);
        assertTrue(result[1] < result[2]);
      }
    }

    for (int n = 3; n <= 10; n++) {
      for (int w = 2; w < n; w++) {
        int[] result = RandomIndexer.nextWindowedIntTriple(n, w, (int[]) null, gen);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[0], result[2]);
        assertNotEquals(result[2], result[1]);
        Arrays.sort(result);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
        assertTrue(result[2] - result[0] <= w);
        result = RandomIndexer.nextSortedWindowedIntTriple(n, w, (int[]) null, gen);
        assertEquals(3, result.length);
        assertNotEquals(result[0], result[1]);
        assertNotEquals(result[0], result[2]);
        assertNotEquals(result[2], result[1]);
        String state =
            "result=(" + result[0] + ", " + result[1] + ", " + result[2] + "), w=" + w + " n=" + n;
        assertTrue(result[0] < result[1], "integers should be sorted: " + state);
        assertTrue(result[1] < result[2], "integers should be sorted: " + state);
        assertTrue(result[0] >= 0);
        assertTrue(result[2] < n);
        assertTrue(result[2] - result[0] <= w);
      }
    }
    for (int n = 3; n <= 6; n++) {
      for (int w = 2; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][][] buckets = new int[n][n][n];
          int numBuckets = w * (n - w) * (w - 1) / 2 + w * (w - 1) * (w - 2) / 6;
          numBuckets *= 6;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            int[] result = RandomIndexer.nextWindowedIntTriple(n, w, (int[]) null, gen);
            buckets[result[0]][result[1]][result[2]]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (Math.abs(j - i) > w) continue;
              for (int h = 0; h < n; h++) {
                if (h == i || h == j) continue;
                if (Math.abs(h - i) > w) continue;
                if (Math.abs(j - h) > w) continue;
                flatBuckets[k] = buckets[i][j][h];
                k++;
              }
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) {
            countH++;
            /*System.out.println(
            "chi="
                + chi
                + " n,w="
                + n
                + ","
                + w
                + "  Buckets: "
                + Arrays.toString(flatBuckets));*/
          }
        }
        assertTrue(countH <= TRIALS * 0.1, "chi too high: " + countH + " n,w=" + n + "," + w);
      }
    }

    int[] expected = new int[3];
    int[] actual = RandomIndexer.nextWindowedIntTriple(5, 3, expected, gen);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextWindowedIntTriple(5, 3, new int[2], gen);
    assertEquals(3, actual.length);

    actual = RandomIndexer.nextSortedWindowedIntTriple(5, 3, expected, gen);
    assertTrue(expected == actual);
    actual = RandomIndexer.nextSortedWindowedIntTriple(5, 3, new int[2], gen);
    assertEquals(3, actual.length);
  }

  @Test
  public void testNextWindowedIntTriple_IndexTriple_TLR() {
    final int REPS_PER_BUCKET = 300;
    final int TRIALS = 100;

    for (int n = 3; n <= 10; n++) {
      for (int w = 2; w < n; w++) {
        IndexTriple result = RandomIndexer.nextWindowedIntTriple(n, w);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.i(), result.k());
        assertNotEquals(result.k(), result.j());
        int min = Math.min(Math.min(result.i(), result.j()), result.k());
        int max = Math.max(Math.max(result.i(), result.j()), result.k());
        assertTrue(min >= 0);
        assertTrue(max < n);
        assertTrue(max - min <= w);
        result = RandomIndexer.nextSortedWindowedIntTriple(n, w);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.i(), result.k());
        assertNotEquals(result.k(), result.j());
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
        assertTrue(result.i() >= 0);
        assertTrue(result.k() < n);
        assertTrue(result.k() - result.i() <= w);
      }
    }

    if (DISABLE_CHI_SQUARE_TESTS) return;
    for (int n = 3; n <= 6; n++) {
      for (int w = 2; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][][] buckets = new int[n][n][n];
          int numBuckets = w * (n - w) * (w - 1) / 2 + w * (w - 1) * (w - 2) / 6;
          numBuckets *= 6;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            IndexTriple result = RandomIndexer.nextWindowedIntTriple(n, w);
            buckets[result.i()][result.j()][result.k()]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (Math.abs(j - i) > w) continue;
              for (int h = 0; h < n; h++) {
                if (h == i || h == j) continue;
                if (Math.abs(h - i) > w) continue;
                if (Math.abs(j - h) > w) continue;
                flatBuckets[k] = buckets[i][j][h];
                k++;
              }
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) countH++;
        }
        assertTrue(countH <= TRIALS * 0.1);
      }
    }
  }

  @Test
  public void testNextWindowedIntTriple_IndexTriple_SR() {
    SplittableRandom gen = new SplittableRandom(42);
    final int REPS_PER_BUCKET = 100;
    final int TRIALS = 100;

    {
      // Make sure sorting covers all cases:
      // This part of the test aims to fully cover the
      // helper method: sortSetAndAdjustWindowedTriple

      // This is the easy path through the method, even one sample
      // should be sufficient with n=3 and w=2.
      for (int i = 0; i < 3; i++) {
        IndexTriple result = RandomIndexer.nextSortedWindowedIntTriple(3, 2, gen);
        assertEquals(0, result.i());
        assertEquals(1, result.j());
        assertEquals(2, result.k());
      }

      // With n=1000 and w=n-1, approximately 99.7% of the following samples should
      // go through the alternate path from above. That alternate path has
      // 3 potential subpaths, all approximately equally likely.
      int n = 1000;
      int w = n - 2;
      for (int i = 0; i < 60; i++) {
        IndexTriple result = RandomIndexer.nextSortedWindowedIntTriple(n, w, gen);
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
      }
      n = 10000;
      w = n - 2;
      for (int i = 0; i < 60; i++) {
        IndexTriple result = RandomIndexer.nextSortedWindowedIntTriple(n, w, gen);
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
      }
      n = 100;
      w = n - 2;
      for (int i = 0; i < 60; i++) {
        IndexTriple result = RandomIndexer.nextSortedWindowedIntTriple(n, w, gen);
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
      }
      n = 10;
      w = n - 2;
      for (int i = 0; i < 60; i++) {
        IndexTriple result = RandomIndexer.nextSortedWindowedIntTriple(n, w, gen);
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
      }
    }

    for (int n = 3; n <= 10; n++) {
      for (int w = 2; w < n; w++) {
        IndexTriple result = RandomIndexer.nextWindowedIntTriple(n, w, gen);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.i(), result.k());
        assertNotEquals(result.k(), result.j());
        int min = Math.min(Math.min(result.i(), result.j()), result.k());
        int max = Math.max(Math.max(result.i(), result.j()), result.k());
        assertTrue(min >= 0);
        assertTrue(max < n);
        assertTrue(max - min <= w);
        result = RandomIndexer.nextSortedWindowedIntTriple(n, w, gen);
        assertNotEquals(result.i(), result.j());
        assertNotEquals(result.i(), result.k());
        assertNotEquals(result.k(), result.j());
        assertTrue(0 <= result.i());
        assertTrue(result.i() < result.j());
        assertTrue(result.j() < result.k());
        assertTrue(result.k() < n);
        assertTrue(result.k() - result.i() <= w);
      }
    }
    for (int n = 3; n <= 6; n++) {
      for (int w = 2; w < n; w++) {
        int countH = 0;
        for (int trial = 0; trial < TRIALS; trial++) {
          int[][][] buckets = new int[n][n][n];
          int numBuckets = w * (n - w) * (w - 1) / 2 + w * (w - 1) * (w - 2) / 6;
          numBuckets *= 6;
          for (int i = 0; i < REPS_PER_BUCKET * numBuckets; i++) {
            IndexTriple result = RandomIndexer.nextWindowedIntTriple(n, w, gen);
            buckets[result.i()][result.j()][result.k()]++;
          }
          int[] flatBuckets = new int[numBuckets];
          int k = 0;
          for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
              if (j == i) continue;
              if (Math.abs(j - i) > w) continue;
              for (int h = 0; h < n; h++) {
                if (h == i || h == j) continue;
                if (Math.abs(h - i) > w) continue;
                if (Math.abs(j - h) > w) continue;
                flatBuckets[k] = buckets[i][j][h];
                k++;
              }
            }
          }
          double chi = chiSquare(flatBuckets, numBuckets);
          if (chi > limit95[numBuckets - 1]) {
            countH++;
            /*System.out.println(
            "chi="
                + chi
                + " n,w="
                + n
                + ","
                + w
                + "  Buckets: "
                + Arrays.toString(flatBuckets));*/
          }
        }
        assertTrue(countH <= TRIALS * 0.1, "chi too high: " + countH + " n,w=" + n + "," + w);
      }
    }
  }

  private boolean validSample(int n, int[] result) {
    boolean[] inResult = new boolean[n];
    for (int i = 0; i < result.length; i++) {
      if (result[i] >= n || result[i] < 0) return false;
      if (inResult[i]) return false;
      inResult[i] = true;
    }
    return true;
  }

  private double chiSquare(int[] buckets, int numBuckets) {
    int x = 0;
    int n = 0;
    for (int e : buckets) {
      x = x + e * e;
      n += e;
    }
    return 1.0 * x / (n / numBuckets) - n;
  }

  private double chiSquare(int[][] buckets, int numBuckets) {
    int m = 0;
    int n = 0;
    for (int x = 0; x < buckets.length; x++) {
      for (int y = x + 1; y < buckets.length; y++) {
        int e = buckets[x][y];
        m = m + e * e;
        n += e;
      }
    }
    return 1.0 * m / (n / numBuckets) - n;
  }

  private double chiSquareAll(int[][] buckets, int numBuckets) {
    int m = 0;
    int n = 0;
    for (int x = 0; x < buckets.length; x++) {
      for (int y = 0; y < buckets.length; y++) {
        if (x == y) continue;
        int e = buckets[x][y];
        m = m + e * e;
        n += e;
      }
    }
    return 1.0 * m / (n / numBuckets) - n;
  }

  private double chiSquare(int[][][] buckets, int numBuckets) {
    int m = 0;
    int n = 0;
    for (int x = 0; x < buckets.length; x++) {
      for (int y = x + 1; y < buckets.length; y++) {
        for (int z = y + 1; z < buckets.length; z++) {
          int e = buckets[x][y][z];
          m = m + e * e;
          n += e;
        }
      }
    }
    return 1.0 * m / (n / numBuckets) - n;
  }

  private double chiSquareAll(int[][][] buckets, int numBuckets) {
    int m = 0;
    int n = 0;
    for (int x = 0; x < buckets.length; x++) {
      for (int y = 0; y < buckets.length; y++) {
        if (y == x) continue;
        for (int z = 0; z < buckets.length; z++) {
          if (z == y || z == x) continue;
          int e = buckets[x][y][z];
          m = m + e * e;
          n += e;
        }
      }
    }
    return 1.0 * m / (n / numBuckets) - n;
  }

  private static double EPSILON = 1e-10;

  private static final double[] limit95 = {
    EPSILON,
    3.841458821,
    5.991464547,
    7.814727903,
    9.487729037,
    11.07049769,
    12.59158724,
    14.06714045,
    15.50731306,
    16.9189776,
    18.30703805,
    19.67513757,
    21.02606982,
    22.36203249,
    23.6847913,
    24.99579014,
    26.2962276,
    27.58711164,
    28.86929943,
    30.14352721,
    31.41043284,
    32.67057334,
    33.92443847,
    35.17246163,
    36.4150285,
    37.65248413,
    38.88513866,
    40.11327207,
    41.33713815,
    42.5569678,
    43.77297183,
    44.98534328,
    46.19425952,
    47.39988392,
    48.60236737,
    49.80184957,
    50.99846017,
    52.19231973,
    53.38354062,
    54.57222776,
    55.75847928,
    56.94238715,
    58.12403768,
    59.30351203,
    60.48088658,
    61.65623338,
    62.82962041,
    64.00111197,
    65.1707689,
    66.33864886,
    67.50480655,
    68.66929391,
    69.83216034,
    70.99345283,
    72.15321617,
    73.31149303,
    74.46832416,
    75.62374847,
    76.77780316,
    77.93052381,
    79.08194449,
    80.23209785,
    81.38101519,
    82.52872654,
    83.67526074,
    84.8206455,
    85.96490744,
    87.1080722,
    88.25016442,
    89.39120787,
    90.53122543,
    91.67023918,
    92.80827038,
    93.9453396,
    95.08146667,
    96.21667075,
    97.35097038,
    98.48438346,
    99.61692732,
    100.7486187,
    101.879474,
    103.0095087,
    104.1387382,
    105.2671773,
    106.3948402,
    107.521741,
    108.647893,
    109.7733094,
    110.8980028,
    112.0219857,
    113.1452701,
    114.2678677,
    115.3897897,
    116.5110473,
    117.6316511,
    118.7516118,
    119.8709393,
    120.9896437,
    122.1077346,
    123.2252215,
    124.3421134,
    125.4584194,
    126.5741482,
    127.6893083,
    128.8039079,
    129.9179553,
    131.0314583,
    132.1444245,
    133.2568617,
    134.3687771,
    135.4801779,
    136.5910712,
    137.7014639,
    138.8113626,
    139.9207739,
    141.0297043,
    142.13816,
    143.2461473,
    144.353672,
    145.4607402,
    146.5673576
  };
}
