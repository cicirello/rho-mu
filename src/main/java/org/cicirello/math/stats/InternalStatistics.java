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
 * Internal helper methods for the functionality of the class Statistics, a utility class of basic statistics.
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a> 
 */
final class InternalStatistics {
	
	// Utility class of static methods.  Should not be instantiated.
	private InternalStatistics() {}
	
	/**
	 * Computes variance of the data in a array.
	 *
	 * @param data The array of data.
	 * @param divisor Pass data.length if a population, or data.length - 1 if a sample.
	 *
	 * @return The variance of the data, either population variance or sample variance.
	 */
	static double variance(int[] data, double divisor) {
		if (data.length < 2) return 0.0;
		double mean = Statistics.mean(data);
		double sumSquares = 0;
		double sum = 0;
		for (int e : data) {
			sumSquares = sumSquares + (e-mean)*(e-mean); 
			sum = sum + (e-mean);
		}
		return (sumSquares - sum*sum/data.length)/divisor;
	}
	
	/**
	 * Computes variance of the data in a array.
	 *
	 * @param data The array of data.
	 * @param divisor Pass data.length if a population, or data.length - 1 if a sample.
	 *
	 * @return The variance of the data, either population variance or sample variance.
	 */
	static double variance(double[] data, double divisor) {
		if (data.length < 2) return 0.0;
		double mean = Statistics.mean(data);
		double sumSquares = 0;
		double sum = 0;
		for (double e : data) {
			sumSquares = sumSquares + (e-mean)*(e-mean); 
			sum = sum + (e-mean);
		}
		return (sumSquares - sum*sum/data.length)/divisor;
	}
	
	/**
	 * Computes correlation coefficient.
	 *
	 * @param varX The variance of X
	 * @param varY The variance of Y
	 * @param covar The covariance of X and Y
	 *
	 * @return correlation coefficient
	 */
	static double correlation(double varX, double varY, double covar) {
		if (covar < 0.0) {
			return -Math.exp(Math.log(-covar) - 0.5 * Math.log(varX) - 0.5 * Math.log(varY));
		} else {
			return Math.exp(Math.log(covar) - 0.5 * Math.log(varX) - 0.5 * Math.log(varY));
		}
	}
}
