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

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

/**
 * This utility class provides methods for generating random variates from different distributions.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class RandomVariates {

  /*
   * Utility class of static methods, so constructor is private to prevent
   * instantiation.
   */
  private RandomVariates() {}

  /**
   * Generates a pseudorandom integer from a binomial distribution. The source of randomness is via
   * the {@link ThreadLocalRandom} class, and thus this method is both safe and efficient for use
   * with threads.
   *
   * @param n Number of trials for the binomial distribution.
   * @param p The probability of a successful trial.
   * @return A pseudorandom integer from a binomial distribution.
   */
  public static int nextBinomial(int n, double p) {
    return Binomial.nextBinomial(n, p, ThreadLocalRandom.current());
  }

  /**
   * Generates a pseudorandom integer from a binomial distribution.
   *
   * @param n Number of trials for the binomial distribution.
   * @param p The probability of a successful trial.
   * @param r The source of randomness.
   * @return A pseudorandom integer from a binomial distribution.
   */
  public static int nextBinomial(int n, double p, RandomGenerator r) {
    return Binomial.nextBinomial(n, p, r);
  }

  /**
   * Generates a pseudorandom number from a Cauchy distribution.
   *
   * @param median The median of the Cauchy.
   * @param scale The scale parameter of the Cauchy.
   * @return a pseudorandom number from a Cauchy distribution
   */
  public static double nextCauchy(double median, double scale) {
    return median
        + internalNextCauchy(
            scale,
            internalNextTransformedU(
                ThreadLocalRandom.current(), ThreadLocalRandom.current().nextDouble()));
  }

  /**
   * Generates a pseudorandom number from a Cauchy distribution with median 0 and chosen scale
   * parameter.
   *
   * @param scale The scale parameter of the Cauchy.
   * @return a pseudorandom number from a Cauchy distribution
   */
  public static double nextCauchy(double scale) {
    return internalNextCauchy(
        scale,
        internalNextTransformedU(
            ThreadLocalRandom.current(), ThreadLocalRandom.current().nextDouble()));
  }

  /**
   * Generates a pseudorandom number from a Cauchy distribution.
   *
   * @param median The median of the Cauchy.
   * @param scale The scale parameter of the Cauchy.
   * @param r The source of randomness.
   * @return a pseudorandom number from a Cauchy distribution
   */
  public static double nextCauchy(double median, double scale, RandomGenerator r) {
    return median + internalNextCauchy(scale, internalNextTransformedU(r, r.nextDouble()));
  }

  /**
   * Generates a pseudorandom number from a Cauchy distribution with median 0 and chosen scale
   * parameter.
   *
   * @param scale The scale parameter of the Cauchy.
   * @param r The source of randomness.
   * @return a pseudorandom number from a Cauchy distribution
   */
  public static double nextCauchy(double scale, RandomGenerator r) {
    return internalNextCauchy(scale, internalNextTransformedU(r, r.nextDouble()));
  }

  /**
   * Generates a random number from a Gaussian distribution with mean mu and standard deviation
   * sigma.
   *
   * <p>{@link ThreadLocalRandom} is used as the source of randomness. However, it does not directly
   * use ThreadLocalRandom's nextGaussian, which is the slow polar method. Instead, it uses the
   * approach described in the following paper to use the RandomGenerator interface's default
   * implementation of McFarland's modified ziggurat algorithm, which is much faster.
   *
   * <p>Vincent A. Cicirello. 2024. <a href="https://reports.cicirello.org/24/009/">Fast Gaussian
   * Distributed Pseudorandom Number Generation in Java via the Ziggurat Algorithm</a>. Technical
   * Report ALG-24-009, Cicirello.org, May 2024. <a
   * href="https://reports.cicirello.org/24/009/ALG-24-009.pdf">[PDF]</a>
   *
   * @param mu The mean of the Gaussian.
   * @param sigma The standard deviation of the Gaussian.
   * @return A random number from a Gaussian distribution with mean mu and standard deviation sigma.
   */
  public static double nextGaussian(double mu, double sigma) {
    return gaussianThreadLocalRandom.nextGaussian(mu, sigma);
  }

  /**
   * Generates a random number from a Gaussian distribution with mean mu and standard deviation
   * sigma.
   *
   * <p>If the RandomGenerator r is one of Java's legacy pseudorandom number generators Random or
   * one of its subclasses, then this method uses our implementation of the original ziggurat
   * algorithm, and otherwise it uses the RandomGenerator's default implementation of nextGaussian
   * introduced in Java 17 which is McFarland's modified ziggurat. See the following report for
   * relevant experiments:
   *
   * <p>Vincent A. Cicirello. 2024. <a href="https://reports.cicirello.org/24/009/">Fast Gaussian
   * Distributed Pseudorandom Number Generation in Java via the Ziggurat Algorithm</a>. Technical
   * Report ALG-24-009, Cicirello.org, May 2024. <a
   * href="https://reports.cicirello.org/24/009/ALG-24-009.pdf">[PDF]</a>
   *
   * @param mu The mean of the Gaussian.
   * @param sigma The standard deviation of the Gaussian.
   * @param r The pseudorandom number generator to use for the source of randomness.
   * @return A random number from a Gaussian distribution with mean mu and standard deviation sigma.
   */
  public static double nextGaussian(double mu, double sigma, RandomGenerator r) {
    return r instanceof Random
        ? mu + sigma * ZigguratGaussian.nextGaussian(r)
        : r.nextGaussian(mu, sigma);
  }

  /**
   * Generates a random number from a Gaussian distribution with mean 0 and standard deviation
   * sigma.
   *
   * <p>{@link ThreadLocalRandom} is used as the source of randomness. However, it does not directly
   * use ThreadLocalRandom's nextGaussian, which is the slow polar method. Instead, it uses the
   * approach described in the following paper to use the RandomGenerator interface's default
   * implementation of McFarland's modified ziggurat algorithm, which is much faster.
   *
   * <p>Vincent A. Cicirello. 2024. <a href="https://reports.cicirello.org/24/009/">Fast Gaussian
   * Distributed Pseudorandom Number Generation in Java via the Ziggurat Algorithm</a>. Technical
   * Report ALG-24-009, Cicirello.org, May 2024. <a
   * href="https://reports.cicirello.org/24/009/ALG-24-009.pdf">[PDF]</a>
   *
   * @param sigma The standard deviation of the Gaussian.
   * @return A random number from a Gaussian distribution with mean 0 and standard deviation sigma.
   */
  public static double nextGaussian(double sigma) {
    return gaussianThreadLocalRandom.nextGaussian(0, sigma);
  }

  /**
   * Generates a random number from a Gaussian distribution with mean 0 and standard deviation
   * sigma.
   *
   * <p>If the RandomGenerator r is one of Java's legacy pseudorandom number generators Random or
   * one of its subclasses, then this method uses our implementation of the original ziggurat
   * algorithm, and otherwise it uses the RandomGenerator's default implementation of nextGaussian
   * introduced in Java 17 which is McFarland's modified ziggurat. See the following report for
   * relevant experiments:
   *
   * <p>Vincent A. Cicirello. 2024. <a href="https://reports.cicirello.org/24/009/">Fast Gaussian
   * Distributed Pseudorandom Number Generation in Java via the Ziggurat Algorithm</a>. Technical
   * Report ALG-24-009, Cicirello.org, May 2024. <a
   * href="https://reports.cicirello.org/24/009/ALG-24-009.pdf">[PDF]</a>
   *
   * @param sigma The standard deviation of the Gaussian.
   * @param r The pseudorandom number generator to use for the source of randomness.
   * @return A random number from a Gaussian distribution with mean 0 and standard deviation sigma.
   */
  public static double nextGaussian(double sigma, RandomGenerator r) {
    return r instanceof Random
        ? sigma * ZigguratGaussian.nextGaussian(r)
        : r.nextGaussian(0, sigma);
  }

  /**
   * Generates a random number from a Gaussian distribution with mean 0 and standard deviation 1.
   *
   * <p>{@link ThreadLocalRandom} is used as the source of randomness. However, it does not directly
   * use ThreadLocalRandom's nextGaussian, which is the slow polar method. Instead, it uses the
   * approach described in the following paper to use the RandomGenerator interface's default
   * implementation of McFarland's modified ziggurat algorithm, which is much faster.
   *
   * <p>Vincent A. Cicirello. 2024. <a href="https://reports.cicirello.org/24/009/">Fast Gaussian
   * Distributed Pseudorandom Number Generation in Java via the Ziggurat Algorithm</a>. Technical
   * Report ALG-24-009, Cicirello.org, May 2024. <a
   * href="https://reports.cicirello.org/24/009/ALG-24-009.pdf">[PDF]</a>
   *
   * @return A random number from a Gaussian distribution with mean 0 and standard deviation 1.
   */
  public static double nextGaussian() {
    return gaussianThreadLocalRandom.nextGaussian();
  }

  /**
   * Generates a random number from a Gaussian distribution with mean 0 and standard deviation 1.
   *
   * <p>If the RandomGenerator r is one of Java's legacy pseudorandom number generators Random or
   * one of its subclasses, then this method uses our implementation of the original ziggurat
   * algorithm, and otherwise it uses the RandomGenerator's default implementation of nextGaussian
   * introduced in Java 17 which is McFarland's modified ziggurat. See the following report for
   * relevant experiments:
   *
   * <p>Vincent A. Cicirello. 2024. <a href="https://reports.cicirello.org/24/009/">Fast Gaussian
   * Distributed Pseudorandom Number Generation in Java via the Ziggurat Algorithm</a>. Technical
   * Report ALG-24-009, Cicirello.org, May 2024. <a
   * href="https://reports.cicirello.org/24/009/ALG-24-009.pdf">[PDF]</a>
   *
   * @param r The pseudorandom number generator to use for the source of randomness.
   * @return A random number from a Gaussian distribution with mean 0 and standard deviation 1.
   */
  public static double nextGaussian(RandomGenerator r) {
    return r instanceof Random ? ZigguratGaussian.nextGaussian(r) : r.nextGaussian();
  }

  /*
   * For implementing nextGaussian with ThreadLocalRandom:
   *
   * Beginning in Java 17, the RandomGenerator interface provides a default
   * implementation of nextGaussian that uses the very fast modified ziggurat
   * algorithm. However, ThreadLocalRandom extends the legacy Random class, which
   * instead uses the very slow polar method because the Random class's linear
   * congruential method doesn't meet the Java 17's modified ziggurat's requirements
   * on the quality of the randomness of low-order bits of random longs. The
   * ThreadLocalRandom actually does meet those requirements because it uses a
   * stronger pseudorandom number generator. This static field provides a minimal
   * implementation of the RandomGenerator interface, using ThreadLocalRandom to
   * generate random longs, and otherwise inheriting the default implementations
   * of all other interface methods, including nextGaussian. Use the nextGaussian
   * method on this instance in the methods that generate Gaussians with
   * ThreadLocalRandom.
   */
  private static final RandomGenerator gaussianThreadLocalRandom =
      new RandomGenerator() {
        @Override
        public long nextLong() {
          return ThreadLocalRandom.current().nextLong();
        }
      };

  /*
   * INTERNAL METHODS FOR nextCauchy start here.
   *
   * Inverse Method:
   *      Mathematically, it should be: median + scale * tan(PI * (u - 0.5)),
   * where u is uniformly random from the interval [0, 1].
   * However, since tan goes through one complete cycle every PI,
   * we can replace it with: median + scale * tan(PI * u), going from
   * 0 to PI, rather than from -PI/2 to PI/2.  This is equivalent
   * as far as generating a random Cauchy variate is concerned, but saves
   * one arithmetic operation.
   *     At first glance, it may appear as if we might be
   * doubly sampling u == 0 since tan(0)==tan(PI), however, our uniform
   * random numbers are generated from [0, 1), so that right endpoint will never
   * be sampled.
   *     We have one special case to consider.  When u==0.5, we have tan(PI/2),
   * which is undefined.  In the limit, however, tan(PI/2) is infinity.
   * We could map this to the constant for infinity.  However, this would introduce
   * a very slight bias in favor of positive results since our interval considers
   * from tan(0) through tan(PI-epsilon), which doesn't include tan(-PI/2), though it
   * comes close since tan(PI/2+epsilon)==tan(-PI/2+epsilon).  In the limit, tan(-PI/2)
   * is -infinity.  So mapping tan(PI/2) to infinity would result in one extra value that
   * leads to a positive result relative to the number of values that lead to negative results.
   *     We handle this in the following way.  First, when u==0.5, we generate a
   * random boolean to control whether u==0.5 means PI/2 or -PI/2.  Second, rather than
   * map to the constants for positive and negative infinity from the Double class, we
   * pass these along to the tan method and let it do its thing numerically, which is
   * a value around 1.6ish * 10 to the power 16 (and negative of that in the case of -PI/2).
   *
   */

  /*
   * package-private to facilitate testing.
   */
  static double internalNextCauchy(double scale, double u) {
    return scale * StrictMath.tan(StrictMath.PI * u);
  }

  /*
   * package-private to facilitate testing.
   */
  static double internalNextTransformedU(RandomGenerator r, double u) {
    return u == 0.5 && r.nextBoolean() ? -0.5 : u;
  }
}
