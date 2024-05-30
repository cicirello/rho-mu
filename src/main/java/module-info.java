/*
 * rho mu - A Java library of randomization enhancements and other math utilities.
 * Copyright 2017-2024 Vincent A. Cicirello
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
 *
 *
 * <h2>&rho;&mu; - A Java library of randomization enhancements and other math utilities.</h2>
 *
 * <p>Copyright &copy; 2017-2024 <a href="https://www.cicirello.org/" target=_top>Vincent A.
 * Cicirello</a>.
 *
 * <p><a href="https://doi.org/10.21105/joss.04663"><img
 * src="https://joss.theoj.org/papers/10.21105/joss.04663/status.svg" alt="DOI:10.21105/joss.04663"
 * height="20" width="168"></a> <a
 * href="https://central.sonatype.com/artifact/org.cicirello/rho-mu/"><img
 * src="https://img.shields.io/maven-central/v/org.cicirello/rho-mu.svg?logo=apachemaven" alt="Maven
 * Central" height="20" width="153"></a> <a href="https://github.com/cicirello/rho-mu/releases"><img
 * src="https://img.shields.io/github/v/release/cicirello/rho-mu?logo=GitHub" alt="GitHub release
 * (latest by date)" height="20" width="111"></a> <a href="https://github.com/cicirello/rho-mu"><img
 * src="https://rho-mu.cicirello.org/images/GitHub.svg" alt="GitHub Repository" width="68"
 * height="20"></a> <a href="https://github.com/cicirello/rho-mu/blob/main/LICENSE"><img
 * src="https://img.shields.io/github/license/cicirello/rho-mu" alt="GNU General Public License
 * Version 3 (GPLv3)" height="20" width="102"></a>
 *
 * <h3>How to Cite</h3>
 *
 * <p>If you use the &rho;&mu; library in your research, please cite the following article which
 * introduces the library:
 *
 * <ul>
 *   <li>Vincent A. Cicirello. <a
 *       href="https://www.cicirello.org/publications/cicirello2022joss.html">&rho;&mu;: A Java
 *       library of randomization enhancements and other math utilities</a>. <i>Journal of Open
 *       Source Software</i>, 7(76), 4663, August 2022. <a
 *       href="https://www.cicirello.org/publications/cicirello2022joss.pdf">[PDF]</a> <a
 *       href="https://www.cicirello.org/publications/cicirello2022joss.bib">[BIB]</a> <a
 *       href="https://doi.org/10.21105/joss.04663">[DOI]</a>
 * </ul>
 *
 * <h3>Support &rho;&mu;</h3>
 *
 * <p><a href="https://github.com/sponsors/cicirello"><img
 * src="https://rho-mu.cicirello.org/images/github-sponsors.svg" alt="GitHub Sponsors" width="107"
 * height="28"></a> <a href="https://liberapay.com/cicirello"><img
 * src="https://rho-mu.cicirello.org/images/Liberapay.svg" alt="Liberapay" width="119"
 * height="28"></a> <a href="https://ko-fi.com/cicirello"><img
 * src="https://rho-mu.cicirello.org/images/ko-fi.svg" alt="Ko-Fi" width="82" height="28"></a>
 *
 * <h3>About the &rho;&mu; Library</h3>
 *
 * <p>The &rho;&mu; library is a library of Randomization enHancements and Other Math Utilities. It
 * includes implementations of various algorithms for randomly sampling indexes into arrays and
 * other sequential structures, randomly sampling pairs and triples of unique indexes, randomly
 * sampling k indexes, etc. It also includes efficient implementations of random number generation
 * from distributions other than uniform, such as Gaussian, Cauchy, etc. Additionally, it includes
 * implementations of other math functions that are either needed by the randomization utilities, or
 * needed by some of our other projects.
 *
 * <p>Much of the core randomization enhancements is in a pair of utility classes: {@link
 * org.cicirello.math.rand.RandomIndexer RandomIndexer} and {@link
 * org.cicirello.math.rand.RandomVariates RandomVariates}. Beginning with v2.0.0, the &rho;&mu;
 * library was revised to utilize Java 17's hierarchy of random number generator interfaces (i.e.,
 * {@link java.util.random.RandomGenerator RandomGenerator} and its subinterfaces). Specifically,
 * &rho;&mu; now provides a class {@link org.cicirello.math.rand.EnhancedRandomGenerator
 * EnhancedRandomGenerator} that wraps an instance of {@link java.util.random.RandomGenerator
 * RandomGenerator} while also implementing {@link java.util.random.RandomGenerator
 * RandomGenerator}, enabling adding the enhanced randomization features to any of Java 17's many
 * random number generators, while also serving as a drop-in replacement. Additionally, &rho;&mu;
 * provides a hierarchy of such wrapper classes, corresponding to Java 17's hierarchy of random
 * number generator interfaces.
 *
 * <p>The randomization enhancements includes:
 *
 * <ul>
 *   <li>Faster generation of random int values subject to a bound or bound and origin.
 *   <li>Faster generation of random int values within an IntStream subject to a bound and origin.
 *   <li>Faster generation of Gaussian distributed random doubles for Java's legacy random number
 *       generators.
 *   <li>Additional distributions available beyond what is supported by the Java API's
 *       RandomGenerator classes, such as Binomial and Cauchy random vaiables.
 *   <li>Ultrafast, but biased, nextBiasedInt methods that sacrifices uniformity for speed by
 *       excluding the rejection sampling necessary to ensure uniformity, as well as a biasedInts
 *       methods for generating streams of such integers.
 *   <li>Methods for generating random pairs of integers without replacement, and random triples of
 *       integers without replacement.
 *   <li>Methods for generating random samples of k integers without replacement from a range of
 *       integers.
 *   <li>Methods to generate streams of numbers from distributions other than uniform, such as
 *       streams of random numbers from binomial distributions, Cauchy distributions, exponential
 *       distributions, and Gaussian distributions.
 *   <li>Methods to generate streams of pairs of distinct integers, and streams of triples of
 *       distinct integers.
 *   <li>Methods for shuffling the elements of arrays and Lists.
 * </ul>
 *
 * <p>The <a href="https://github.com/cicirello/rho-mu" target=_top>source code repository</a> is
 * hosted on GitHub. The source code is licensed under the <a
 * href="https://www.gnu.org/licenses/gpl-3.0.html" target=_top>GNU General Public License Version 3
 * (GPLv3)</a>. For more information see the <a href="https://rho-mu.cicirello.org/"
 * target=_top>&rho;&mu; website</a>.
 *
 * <h3>UML Class Diagram</h3>
 *
 * <p>The following UML class diagram shows the structure of the &rho;&mu; library. Classes and
 * interfaces shown in blue are classes within the &rho;&mu; library, and are clickable links to the
 * page documenting the class. Classes and interfaces shown in gray are in the Java API, and are
 * also clickable links to the relevant page within the Java API documentation. For brevity in the
 * diagram, methods are omitted, and most attributes are omitted, aside from the attributes
 * corresponding to the wrapped random number generator instances.
 *
 * <p><object type="image/svg+xml" data="https://rho-mu.cicirello.org/images/rho-mu-uml.svg"
 * width="1064" height="1151">UML diagram</object>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
module org.cicirello.rho_mu {
  exports org.cicirello.math;
  exports org.cicirello.math.la;
  exports org.cicirello.math.rand;
  exports org.cicirello.math.stats;

  requires org.cicirello.core;
}
