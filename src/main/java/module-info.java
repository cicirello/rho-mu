/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2022 Vincent A. Cicirello
 *
 * This file is part of the rho mu library (https://rho-mu.cicirello.org/).
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
 *
 * <p>Copyright &copy; 2017-2022 <a href="https://www.cicirello.org/" target=_top>Vincent A. Cicirello</a>.</p>
 *
 * <p><a href="https://search.maven.org/artifact/org.cicirello/rho-mu"><img 
 * src="https://img.shields.io/maven-central/v/org.cicirello/rho-mu.svg?logo=apachemaven" 
 * alt="Maven Central" height="20" width="153"></a>
 * <a href="https://github.com/cicirello/rho-mu/releases"><img 
 * src="https://img.shields.io/github/v/release/cicirello/rho-mu?logo=GitHub" 
 * alt="GitHub release (latest by date)" height="20" width="111"></a>
 * <a href="https://github.com/cicirello/rho-mu"><img 
 * src="https://rho-mu.cicirello.org/images/GitHub.svg" 
 * alt="GitHub Repository" width="68" height="20"></a>
 * <a href="https://github.com/cicirello/rho-mu/blob/main/LICENSE"><img 
 * src="https://img.shields.io/github/license/cicirello/rho-mu" 
 * alt="GNU General Public License Version 3 (GPLv3)" height="20" width="102"></a>
 * </p>
 *
 * <h3>About the &rho;&mu; Library</h3>
 *
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
 * <p>The <a href="https://github.com/cicirello/rho-mu" target=_top>source code repository</a> 
 * is hosted on GitHub.
 * The source code is licensed under the 
 * <a href="https://www.gnu.org/licenses/gpl-3.0.html" target=_top>GNU General Public License Version 3 (GPLv3)</a>.  For more information see the <a href="https://rho-mu.cicirello.org/" target=_top>&rho;&mu; 
 * website</a>.</p>
 *
 * <h3>Support &rho;&mu;</h3>
 *
 * <p><a href="https://github.com/sponsors/cicirello"><img src="https://rho-mu.cicirello.org/images/github-sponsors.svg" alt="GitHub Sponsors" width="107" height="28"></a>
 * <a href="https://liberapay.com/cicirello"><img src="https://rho-mu.cicirello.org/images/Liberapay.svg" alt="Liberapay" width="119" height="28"></a>
 * <a href="https://ko-fi.com/cicirello"><img src="https://rho-mu.cicirello.org/images/ko-fi.svg" alt="Ko-Fi" width="82" height="28"></a></p>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, 
 * <a href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
module org.cicirello.rho_mu {
	exports org.cicirello.math;
	exports org.cicirello.math.la;
	exports org.cicirello.math.rand;
	exports org.cicirello.math.stats;
}
