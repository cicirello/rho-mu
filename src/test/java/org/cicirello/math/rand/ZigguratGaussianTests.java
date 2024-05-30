/*
 * JUnit test cases for ZigguratGaussian.
 *
 * Copyright 2019-2024 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * The JUnit test cases for ZigguratGaussian is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The JUnit test cases for ZigguratGaussian are distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.	See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ZigguratGaussianTests.java.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.cicirello.math.rand;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.SplittableRandom;
import org.junit.jupiter.api.*;

/** JUnit test cases for the methods of the ZigguratGaussian class. */
public class ZigguratGaussianTests {

  // Test cases use chi square goodness of fit.  This constant
  // can be used to adjust the number of samples used for this test.
  private static final int EXPECTED_SAMPLES_PER_BUCKET = 60;

  // Change to true to see extra statistical output not otherwise used
  // by automated tests (e.g., to see the specific chi square statistic value).
  // Extra output sent to standard out.
  private static final boolean VERBOSE_OUTPUT = false;

  @Test
  public void testRandom1() {
    Random r = new Random(42);
    int[] buckets = new int[20];
    final int N = buckets.length * EXPECTED_SAMPLES_PER_BUCKET;
    for (int i = 0; i < N; i++) {
      int j = whichBucket(ZigguratGaussian.nextGaussian(r));
      buckets[j]++;
    }
    double chi = chiSquare(buckets);
    assertTrue(
        chi <= 30.144); // 19 degrees of freedom, 95% percentage point of chi square distribution:
    // 30.144
    if (VERBOSE_OUTPUT) {
      System.out.printf("Random, sigma=1, chi=%5.4f\n", chi);
    }
  }

  @Test
  public void testSplittableRandom1() {
    SplittableRandom r = new SplittableRandom(42);
    int[] buckets = new int[20];
    final int N = buckets.length * EXPECTED_SAMPLES_PER_BUCKET;
    for (int i = 0; i < N; i++) {
      int j = whichBucket(ZigguratGaussian.nextGaussian(r));
      buckets[j]++;
    }
    double chi = chiSquare(buckets);
    assertTrue(
        chi <= 30.144); // 19 degrees of freedom, 95% percentage point of chi square distribution:
    // 30.144
    if (VERBOSE_OUTPUT) {
      System.out.printf("SplittableRandom, sigma=1, chi=%5.4f\n", chi);
    }
  }

  private double chiSquare(int[] buckets) {
    int x = 0;
    for (int e : buckets) {
      x = x + e * e;
    }
    return 1.0 * x / EXPECTED_SAMPLES_PER_BUCKET - buckets.length * EXPECTED_SAMPLES_PER_BUCKET;
  }

  private int whichBucket(double x) {
    final double[] upperBoundaries = {
      -1.644853627,
      -1.281551566,
      -1.036433389,
      -0.841621234,
      -0.67448975,
      -0.524400513,
      -0.385320466,
      -0.253347103,
      -0.125661347,
      0,
      0.125661347,
      0.253347103,
      0.385320466,
      0.524400513,
      0.67448975,
      0.841621234,
      1.036433389,
      1.281551566,
      1.644853627
    };
    for (int i = 0; i < upperBoundaries.length; i++) {
      if (x <= upperBoundaries[i]) return i;
    }
    return upperBoundaries.length;
  }

  private int whichBucket(double x, double sigma) {
    final double[] upperBoundaries = {
      -1.644853627,
      -1.281551566,
      -1.036433389,
      -0.841621234,
      -0.67448975,
      -0.524400513,
      -0.385320466,
      -0.253347103,
      -0.125661347,
      0,
      0.125661347,
      0.253347103,
      0.385320466,
      0.524400513,
      0.67448975,
      0.841621234,
      1.036433389,
      1.281551566,
      1.644853627
    };
    for (int i = 0; i < upperBoundaries.length; i++) {
      upperBoundaries[i] = upperBoundaries[i] * sigma;
    }
    for (int i = 0; i < upperBoundaries.length; i++) {
      if (x <= upperBoundaries[i]) return i;
    }
    return upperBoundaries.length;
  }
}
