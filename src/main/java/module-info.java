/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2019 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

/**
 * <h2>&rho;&mu; - A Java library of randomization enhancements and other math utilities.</h2>
 * <p>The &rho;&mu; library is a library of
 * Randomization enHancements and Other Math Utilities.
 * It includes implementations of various algorithms for
 * randomly sampling indexes into arrays and other sequential
 * structures, randomly sampling pairs and triples of unique
 * indexes, randomly sampling k indexes, etc. It also includes
 * efficient implementations of random number generation from
 * distributions other than uniform, such as Gaussian, Cauchy, 
 * etc. Additionally, it includes implementations of other
 * math functions that are either needed by the randomization 
 * utilities, or needed by some of our other projects.</p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
module rho_mu {
	exports org.cicirello.math;
	exports org.cicirello.math.la;
	exports org.cicirello.math.rand;
	exports org.cicirello.math.stats;
	requires java.base;
}
