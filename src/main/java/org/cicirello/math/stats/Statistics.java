/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

package org.cicirello.math.stats;

/**
 * Utility class of basic statistics.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a> 
 */
public final class Statistics {
	
	// Utility class of static methods.  Should not be instantiated.
	private Statistics() {}
	
	/**
	 * Computes mean of a dataset.
	 * @param data The dataset.
	 * @return the mean of the data.
	 */
	public static double mean(int[] data) {
		int sum = 0;
		for (int e : data) sum = sum + e;
		return ((double)sum) / data.length;
	}
	
	/**
	 * Computes mean of a dataset.
	 * @param data The dataset.
	 * @return the mean of the data.
	 */
	public static double mean(double[] data) {
		double sum = 0;
		for (double e : data) sum = sum + e;
		return sum / data.length;
	}
	
	/**
	 * Computes variance of a population.
	 * @param data The dataset.
	 * @return the variance of the data.
	 */
	public static double variance(int[] data) {
		return internalVariance(data, data.length);
	}
	
	/**
	 * Computes variance of a population.
	 * @param data The dataset.
	 * @return the variance of the data.
	 */
	public static double variance(double[] data) {
		return internalVariance(data, data.length);
	}
	
	/**
	 * Computes variance of a sample.
	 * @param data The dataset.
	 * @return the variance of the data.
	 */
	public static double varianceSample(int[] data) {
		return internalVariance(data, data.length - 1.0);
	}
	
	
	/**
	 * Computes variance of a sample.
	 * @param data The dataset.
	 * @return the variance of the data.
	 */
	public static double varianceSample(double[] data) {
		return internalVariance(data, data.length - 1.0);
	}
	
	/**
	 * Computes the sample standard deviation.
	 * @param data The dataset.
	 * @return the sample standard deviation.
	 */
	public static double stdev(int[] data) {
		return Math.sqrt(varianceSample(data));
	}
	
	/**
	 * Computes the sample standard deviation.
	 * @param data The dataset.
	 * @return the sample standard deviation.
	 */
	public static double stdev(double[] data) {
		return Math.sqrt(varianceSample(data));
	}
	
	/**
	 * Computes covariance for a pair of random variables.
	 * @param X Array of samples of first variable.
	 * @param Y Array of samples of second variable.
	 * @return the covariance of X and Y.
	 */
	public static double covariance(int[] X, int[] Y) {
		if (X.length < 2) return 0.0;
		if (X.length != Y.length) throw new IllegalArgumentException("Arrays must have same length!");
		double kX = X[0]; 
		double kY = Y[0]; 
		double sumX = 0;
		double sumY = 0;
		double sumProduct = 0;
		for (int i = 0; i < X.length; i++) {
			sumX = sumX + (X[i] - kX);
			sumY = sumY + (Y[i] - kY);
			sumProduct = sumProduct + (X[i] - kX)*(Y[i] - kY);
		}
		return (sumProduct - sumX*sumY/X.length)/X.length;
	}
	
		
	/**
	 * Computes covariance for a pair of random variables.
	 * @param X Array of samples of first variable.
	 * @param Y Array of samples of second variable.
	 * @return the covariance of X and Y.
	 */
	public static double covariance(double[] X, double[] Y) {
		if (X.length < 2) return 0.0;
		if (X.length != Y.length) throw new IllegalArgumentException("Arrays must have same length!");
		double kX = X[0];
		double kY = Y[0];
		double sumX = 0;
		double sumY = 0;
		double sumProduct = 0;
		for (int i = 0; i < X.length; i++) {
			sumX = sumX + (X[i] - kX);
			sumY = sumY + (Y[i] - kY);
			sumProduct = sumProduct + (X[i] - kX)*(Y[i] - kY);
		}
		return (sumProduct - sumX*sumY/X.length)/X.length;
	}
	
	/**
	 * Computes correlation coefficient for a pair of random variables.
	 * @param X Array of samples of first variable.
	 * @param Y Array of samples of second variable.
	 * @return the correlation coefficient of X and Y.
	 */
	public static double correlation(int[] X, int[] Y) {
		double varX = variance(X);
		if (varX == 0.0) return 0.0;
		double varY = variance(Y);
		if (varY == 0.0) return 0.0;
		double covar = covariance(X,Y);
		if (covar == 0.0) return 0.0;
		
		return internalCorrelation(varX, varY, covar);
	}
	
	/**
	 * Computes correlation coefficient for a pair of random variables.
	 * @param X Array of samples of first variable.
	 * @param Y Array of samples of second variable.
	 * @return the correlation coefficient of X and Y.
	 */
	public static double correlation(double[] X, double[] Y) {
		double varX = variance(X);
		if (varX == 0.0) return 0.0;
		double varY = variance(Y);
		if (varY == 0.0) return 0.0;
		double covar = covariance(X,Y);
		if (covar == 0.0) return 0.0;
		
		return internalCorrelation(varX, varY, covar);
	}
	
	/**
	 * Computes correlation matrix.
	 * @param data The data with random variables in rows and samples in columns.
	 * @return the correlation matrix, M, where M[i][j] is the correlation coefficient of data[i] and data[j].
	 */
	public static double[][] correlationMatrix(int[][] data) {
		double[][] corr = new double[data.length][data.length];
		for (int i = 0; i < data.length; i++) {
			corr[i][i] = 1.0;
			for (int j = i+1; j < data.length; j++) {
				corr[i][j] = corr[j][i] = correlation(data[i], data[j]);
			}
		}
		return corr;
	}
	
	/**
	 * Computes correlation matrix.
	 * @param data The data with random variables in rows and samples in columns.
	 * @return the correlation matrix, M, where M[i][j] is the correlation coefficient of data[i] and data[j].
	 */
	public static double[][] correlationMatrix(double[][] data) {
		double[][] corr = new double[data.length][data.length];
		for (int i = 0; i < data.length; i++) {
			corr[i][i] = 1.0;
			for (int j = i+1; j < data.length; j++) {
				corr[i][j] = corr[j][i] = correlation(data[i], data[j]);
			}
		}
		return corr;
	}
	
	/**
	 * Welch's t-test, also known as t-test with unequal variances.
	 * The Welch's t-test can be used when variances are unequal and
	 * is also applicable if sample sizes differ.
	 *
	 * @param data1 First dataset.
	 * @param data2 Second dataset.
	 * @return The t statistic.
	 */
	public static double tTestUnequalVariances(double[] data1, double[] data2) {
		return (mean(data1)-mean(data2)) / Math.sqrt(varianceSample(data1)/data1.length + varianceSample(data2)/data2.length);
	}
	
	/**
	 * Welch's t-test, also known as t-test with unequal variances.
	 * The Welch's t-test can be used when variances are unequal and
	 * is also applicable if sample sizes differ.
	 *
	 * @param data1 First dataset.
	 * @param data2 Second dataset.
	 * @return The t statistic.
	 */
	public static double tTestUnequalVariances(int[] data1, int[] data2) {
		return (mean(data1)-mean(data2)) / Math.sqrt(varianceSample(data1)/data1.length + varianceSample(data2)/data2.length);
	}
	
	/**
	 * Welch's t-test, also known as t-test with unequal variances.
	 * The Welch's t-test can be used when variances are unequal and
	 * is also applicable if sample sizes differ.  This method computes
	 * both the t statistic, as well as the approximate degrees of freedom.
	 *
	 * @param data1 First dataset.
	 * @param data2 Second dataset.
	 * @return An array, a, of length 2 such that a[0] is the t statistic (as a Double object), and
	 * a[1] is the degrees of freedom (as an Integer object).
	 */
	public static Number[] tTestWelch(double[] data1, double[] data2) {
		Number[] result = new Number[2];
		double term1 = varianceSample(data1)/data1.length;
		double term2 = varianceSample(data2)/data2.length;
		double termSum = term1 + term2;
		double vDenom = term1*term1/(data1.length-1) + term2*term2/(data2.length-1);
		double v = termSum*termSum/vDenom; 
		Double d = (mean(data1)-mean(data2)) / Math.sqrt(termSum);
		result[0] = d;
		Integer i = (int)v;
		result[1] = i;
		return result;
	}
	
	/**
	 * Welch's t-test, also known as t-test with unequal variances.
	 * The Welch's t-test can be used when variances are unequal and
	 * is also applicable if sample sizes differ.  This method computes
	 * both the t statistic, as well as the approximate degrees of freedom.
	 *
	 * @param data1 First dataset.
	 * @param data2 Second dataset.
	 * @return An array, a, of length 2 such that a[0] is the t statistic (as a Double object), and
	 * a[1] is the degrees of freedom (as an Integer object).
	 */
	public static Number[] tTestWelch(int[] data1, int[] data2) {
		Number[] result = new Number[2];
		double term1 = varianceSample(data1)/data1.length;
		double term2 = varianceSample(data2)/data2.length;
		double termSum = term1 + term2;
		double vDenom = term1*term1/(data1.length-1) + term2*term2/(data2.length-1);
		double v = termSum*termSum/vDenom; 	
		Double d = (mean(data1)-mean(data2)) / Math.sqrt(termSum);
		result[0] = d;
		Integer i = (int)v;
		result[1] = i;
		return result;
	}
	
	private static double internalVariance(int[] data, double divisor) {
		if (data.length < 2) return 0.0;
		double mean = mean(data);
		double sumSquares = 0;
		double sum = 0;
		for (int e : data) {
			sumSquares = sumSquares + (e-mean)*(e-mean); 
			sum = sum + (e-mean);
		}
		return (sumSquares - sum*sum/data.length)/divisor;
	}
	
	private static double internalVariance(double[] data, double divisor) {
		if (data.length < 2) return 0.0;
		double mean = mean(data);
		double sumSquares = 0;
		double sum = 0;
		for (double e : data) {
			sumSquares = sumSquares + (e-mean)*(e-mean); 
			sum = sum + (e-mean);
		}
		return (sumSquares - sum*sum/data.length)/divisor;
	}
	
	private static double internalCorrelation(double varX, double varY, double covar) {
		boolean negate = false;
		if (covar < 0.0) {
			negate = true;
			covar = -covar;
		}
		double logCovar = Math.log(covar);
		double logVarX = Math.log(varX);
		double logVarY = Math.log(varY);
		double corr = Math.exp(logCovar - 0.5 * logVarX - 0.5 * logVarY);
		if (negate) corr = -corr;
		return corr;
	}
}
