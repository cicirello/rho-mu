/*
 * Example programs for rho-mu library.
 * Copyright (C) 2017-2024 Vincent A. Cicirello
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.cicirello.examples.rho_mu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;
import org.cicirello.math.rand.EnhancedRandomGenerator;
import org.cicirello.math.rand.IndexPair;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * Experimental comparison of various approaches to generating random pairs of distinct integers.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class TimeRandomPairs {

  private static final long seed = 42;
  private static final EnhancedRandomGenerator generator =
      new EnhancedRandomGenerator(new SplittableRandom(seed));

  // This is to test cases, where array for result already exists.
  private static final int[] preConstructedArray = new int[2];

  @State(Scope.Benchmark)
  public static class ExecutionPlan {

    @Param({"16", "64", "256", "1024"})
    public int n;
  }

  @Benchmark
  @Fork(value = 1)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public int[] sampleReservoir(ExecutionPlan plan) {
    return generator.sampleReservoir(plan.n, 2, null);
  }

  @Benchmark
  @Fork(value = 1)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public int[] samplePool(ExecutionPlan plan) {
    return generator.samplePool(plan.n, 2, null);
  }

  @Benchmark
  @Fork(value = 1)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public int[] sampleInsertion(ExecutionPlan plan) {
    return generator.sampleInsertion(plan.n, 2, null);
  }

  @Benchmark
  @Fork(value = 1)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public int[] nextIntPairReturningArray(ExecutionPlan plan) {
    return generator.nextIntPair(plan.n, null);
  }

  @Benchmark
  @Fork(value = 1)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public IndexPair nextIntPairReturningIndexPair(ExecutionPlan plan) {
    return generator.nextIntPair(plan.n);
  }

  @Benchmark
  @Fork(value = 1)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public int[] nextIntPairPreconstructedArray(ExecutionPlan plan) {
    return generator.nextIntPair(plan.n, preConstructedArray);
  }

  /**
   * Runs the experiments.
   *
   * @param args Ignored, no command line arguments.
   */
  public static void main(String[] args) throws IOException {
    printCopyrightAndLicense();

    URLClassLoader classLoader = (URLClassLoader) TimeRandomPairs.class.getClassLoader();
    StringBuilder classpath = new StringBuilder();
    for (URL url : classLoader.getURLs()) {
      classpath.append(url.getPath()).append(File.pathSeparator);
    }
    System.setProperty("java.class.path", classpath.toString());
    String[] realArgs = new String[args.length + 1];
    System.arraycopy(args, 0, realArgs, 0, args.length);
    realArgs[args.length] = "TimeRandomPairs";
    org.openjdk.jmh.Main.main(realArgs);
  }

  /** Prints copyright and license notices. */
  private static void printCopyrightAndLicense() {
    System.out.println();
    System.out.println(
        "Experiments comparing different algorithms for generating random pairs of distinct ints.");
    System.out.println("Copyright (C) 2024 Vincent A. Cicirello");
    System.out.println("This program comes with ABSOLUTELY NO WARRANTY.  This is free");
    System.out.println("software, and you are welcome to redistribute it under certain");
    System.out.println("conditions.  See the GNU General Public License for more");
    System.out.println("details: https://www.gnu.org/licenses/gpl-3.0.html");
    System.out.println();
  }
}
