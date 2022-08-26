/*
 * Example programs for rho-mu library.
 * Copyright (C) 2017-2022 Vincent A. Cicirello
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

import java.util.concurrent.ThreadLocalRandom;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import org.cicirello.math.stats.Statistics;
import org.cicirello.math.rand.RandomIndexer;

/**
 * Simple program comparing CPU time of RandomIndexer.nextBiasedInt vs ThreadLocalRandom.nextInt
 * to demonstrate the significant time advantage for cases where strict uniformity is not required
 * (nextBiasedInt excludes the rejection sampling necessary for uniformity).
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public class TimingNextBiasedIntMethod {
	
	/* private constructor to prevent instantiation */
	private TimingNextBiasedIntMethod() {}
	
	// number of random numbers to generate for each time point
	// NOTE: This is much higher than runs of basic nextInt timing comparison
	// to get more easily measurable times for the nextBiasedInt method.
	private final static int N = 10000000;
	
	// number of trials to average for each bound
	private final static int TRIALS = 100;
	
	/**
	 * Runs the example program.
	 * @param args No command line arguments
	 */
	public static void main(String[] args) {
		ExamplesShared.printCopyrightAndLicense();
		
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		
		// Attempt to "warm-up" Java's JIT compiler.
		for (int bound = 2; bound <= 512; bound*= 2) {
			for (int j = 0; j < 100000; j++) {
				RandomIndexer.nextBiasedInt(bound-1);
				RandomIndexer.nextBiasedInt(bound);
				RandomIndexer.nextBiasedInt(bound+1);
				ThreadLocalRandom.current().nextInt(bound-1);
				ThreadLocalRandom.current().nextInt(bound);
				ThreadLocalRandom.current().nextInt(bound+1);
			}
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<double[]>[] msLowBound = new ArrayList[2]; 
		msLowBound[0] = new ArrayList<double[]>();
		msLowBound[1] = new ArrayList<double[]>();
		@SuppressWarnings("unchecked")
		ArrayList<double[]>[] msHighBound = new ArrayList[2]; 
		msHighBound[0] = new ArrayList<double[]>();
		msHighBound[1] = new ArrayList<double[]>();
		@SuppressWarnings("unchecked")
		ArrayList<double[]>[] msPow2Bound = new ArrayList[2]; 
		msPow2Bound[0] = new ArrayList<double[]>();
		msPow2Bound[1] = new ArrayList<double[]>();
		
		System.out.printf("%6s\t%10s\t%10s\t%10s\t%10s\n", "Bound", "TLR", "BIASED", "t", "dof");
		for (int bound = 1; bound <= 512; bound++) {
			double[][] ms = new double[2][TRIALS];
			for (int j = 0; j < TRIALS; j++) {
				long start = bean.getCurrentThreadCpuTime();
				for (int i = 0; i < N; i++) {
					ThreadLocalRandom.current().nextInt(bound);
				}
				long middle = bean.getCurrentThreadCpuTime();
				for (int i = 0; i < N; i++) {
					RandomIndexer.nextBiasedInt(bound);
				}
				long end = bean.getCurrentThreadCpuTime();
				// compute elapsed times in milliseconds
				ms[0][j] = (middle-start) / 1000000.0;
				ms[1][j] = (end-middle) / 1000000.0;
			}
			if ((bound & (bound-1)) != 0) {
				if (bound <= 256) {
					msLowBound[0].add(ms[0]);
					msLowBound[1].add(ms[1]);
				} else {
					msHighBound[0].add(ms[0]);
					msHighBound[1].add(ms[1]);
				}
			} else {
				msPow2Bound[0].add(ms[0]);
				msPow2Bound[1].add(ms[1]);
			}
			Number[] tTest = Statistics.tTestWelch(ms[0],ms[1]);
			double t = tTest[0].doubleValue();
			int dof = tTest[1].intValue();
			// times are converted to seconds during output
			System.out.printf("%6d\t%10.7f\t%10.7f\t%10.4f\t%10d\n", bound, Statistics.mean(ms[0])/1000, Statistics.mean(ms[1])/1000, t, dof);
		}
		
		System.out.println();
		System.out.println("SUMMARY RESULTS");
		System.out.printf("%6s\t%10s\t%10s\t%10s\t%10s\n", "Bound", "TLR", "BIASED", "t", "dof");
		// Output means, and results of t-test, for the case of low bounds.
		double[] a0 = toArray(msLowBound[0]);
		double[] a1 = toArray(msLowBound[1]);
		Number[] tTest = Statistics.tTestWelch(a0,a1);
		double t = tTest[0].doubleValue();
		int dof = tTest[1].intValue();
		double apiTime = Statistics.mean(a0);
		double rhomuTime = Statistics.mean(a1);
		// times are converted to seconds during output
		System.out.printf("%6s\t%10.7f\t%10.7f\t%10.4f\t%10d\n", "LOW", apiTime/1000, rhomuTime/1000, t, dof);
		final double LOW_PCT_DIFF = (apiTime - rhomuTime) / apiTime;
		
		// Output means, and results of t-test, for the case of high bounds.
		a0 = toArray(msHighBound[0]);
		a1 = toArray(msHighBound[1]);
		tTest = Statistics.tTestWelch(a0,a1);
		t = tTest[0].doubleValue();
		dof = tTest[1].intValue();
		apiTime = Statistics.mean(a0);
		rhomuTime = Statistics.mean(a1);
		// times are converted to seconds during output
		System.out.printf("%6s\t%10.7f\t%10.7f\t%10.4f\t%10d\n", "HIGH", apiTime/1000, rhomuTime/1000, t, dof);
		final double HIGH_PCT_DIFF = (apiTime - rhomuTime) / apiTime;
		
		// Output means, and results of t-test, for the case of bounds that are powers of 2.
		a0 = toArray(msPow2Bound[0]);
		a1 = toArray(msPow2Bound[1]);
		tTest = Statistics.tTestWelch(a0,a1);
		t = tTest[0].doubleValue();
		dof = tTest[1].intValue();
		apiTime = Statistics.mean(a0);
		rhomuTime = Statistics.mean(a1);
		// times are converted to seconds during output
		System.out.printf("%6s\t%10.7f\t%10.7f\t%10.4f\t%10d\n", "POW2", apiTime/1000, rhomuTime/1000, t, dof);
		final double POW2_PCT_DIFF = (apiTime - rhomuTime) / apiTime;
		
		// Output means, and results of t-test, for all cases combined.
		ArrayList<double[]> allOriginal = new ArrayList<double[]>();
		allOriginal.addAll(msLowBound[0]);
		allOriginal.addAll(msHighBound[0]);
		allOriginal.addAll(msPow2Bound[0]);
		ArrayList<double[]> allRandomIndexer = new ArrayList<double[]>();
		allRandomIndexer.addAll(msLowBound[1]);
		allRandomIndexer.addAll(msHighBound[1]);
		allRandomIndexer.addAll(msPow2Bound[1]);
		a0 = toArray(allOriginal);
		a1 = toArray(allRandomIndexer);
		tTest = Statistics.tTestWelch(a0,a1);
		t = tTest[0].doubleValue();
		dof = tTest[1].intValue();
		apiTime = Statistics.mean(a0);
		rhomuTime = Statistics.mean(a1);
		// times are converted to seconds during output
		System.out.printf("%6s\t%10.7f\t%10.7f\t%10.4f\t%10d\n", "ALL", apiTime/1000, rhomuTime/1000, t, dof);
		final double ALL_PCT_DIFF = (apiTime - rhomuTime) / apiTime;
		
		System.out.println();
		System.out.println("Interpreting Above Results:");
		System.out.println("1) Negative t value implies Java library's nextInt method was faster.");
		System.out.println("2) Positive t value implies rho-mu's nextBiasedInt method was faster.");
		System.out.println("3) Greater absolute value of t value implies more significant time difference.");
		System.out.println("4) You can use a table from any statistics book to map t value to p value,");
		System.out.println("   and the dof column in results provides the degrees of freedom for the test");
		System.out.println("   needed to do so.");
		System.out.println("5) LOW are the results for bounds that are less than 256 and not powers of 2.");
		System.out.println("6) HIGH are the results for bounds that are greater than 256 and not powers of 2.");
		System.out.println("7) POW2 are the results for bounds that are powers of 2.");
		System.out.println("8) ALL are all of the results for all bounds tested.");
		
		System.out.println();
		
		if (LOW_PCT_DIFF >= 0) {
			System.out.printf("For Low bounds, rho-mu's nextBiasedInt spends %.2f%% LESS TIME THAN the Java API's built in nextInt.\n", 100*LOW_PCT_DIFF);
		} else {
			System.out.printf("For Low bounds, rho-mu's nextBiasedInt spends %.2f%% MORE TIME THAN the Java API's built in nextInt.\n", -100*LOW_PCT_DIFF);
		}
		
		if (HIGH_PCT_DIFF >= 0) {
			System.out.printf("For high bounds, rho-mu's nextBiasedInt spends %.2f%% LESS TIME THAN the Java API's built in nextInt.\n", 100*HIGH_PCT_DIFF);
		} else {
			System.out.printf("For high bounds, rho-mu's nextBiasedInt spends %.2f%% MORE TIME THAN the Java API's built in nextInt.\n", -100*HIGH_PCT_DIFF);
		}
		
		if (POW2_PCT_DIFF >= 0) {
			System.out.printf("For powers-of-2 bounds, rho-mu's nextBiasedInt spends %.2f%% LESS TIME THAN the Java API's built in nextInt.\n", 100*POW2_PCT_DIFF);
		} else {
			System.out.printf("For powers-of-2 bounds, rho-mu's nextBiasedInt spends %.2f%% MORE TIME THAN the Java API's built in nextInt.\n", -100*POW2_PCT_DIFF);
		}
		
		if (ALL_PCT_DIFF >= 0) {
			System.out.printf("Overall, rho-mu's nextBiasedInt spends %.2f%% LESS TIME THAN the Java API's built in nextInt.\n", 100*ALL_PCT_DIFF);
		} else {
			System.out.printf("Overall, rho-mu's nextBiasedInt spends %.2f%% MORE TIME THAN the Java API's built in nextInt.\n", -100*ALL_PCT_DIFF);
		}
	}
	
	private static double[] toArray(ArrayList<double[]> from) {
		int n = 0;
		for (double[] e : from) n += e.length;
		double[] a = new double[n];
		int i = 0;
		for (double[] e : from) {
			for (int j = 0; j < e.length; j++) {
				a[i] = e[j];
				i++;
			}
		}
		return a;
	}
}