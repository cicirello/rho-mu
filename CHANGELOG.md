# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2022-11-13

### Added

### Changed
* Refactored RandomIndexer and RandomSampler to use new dependency org.cicirello.core.

### Deprecated

### Removed

### Fixed

### Dependencies
* Added dependency org.cicirello.core 2.4.2.

### CI/CD

### Other


## [2.5.0] - 2022-10-05

### Added
* RandomSampler: sampling without replacement from set of integers. This class has been
  extracted from the RandomIndexer class.

### Changed
* Refactored RandomIndexer class to decrease cyclomatic complexity, improve maintainability and reduce redundancy.
* Refactored Statistics class, extracting an internal helper class InternalStatistics (motivated by 
  Sonatype Lift's technical debt scan).
* Refactored MatrixOps class (motivated by Sonatype Lift's technical debt scan).
* Refactored test cases based on suggestion from Sonatype Lift's technical debt scan.

### Deprecated
* The sample, samplePool, sampleReservoir, and sampleInsertion methods of the RandomIndexer class. The equivalents
  of all of these are available in the new RandomSampler class.


## [2.4.2] - 2022-09-02

### Other
* Release to push out documentation fix. No functional changes in this release to the library itself.


## [2.4.1] - 2022-08-29

### Other
* No functional changes in this release: 2.4.1 is functionality equivalent to 2.4.0.
* Release to push update to metadata out to Zenodo archive with citation details of JOSS article.
* Documentation update to module page with citation details of JOSS article.


## [2.4.0] - 2022-08-26

### Added
* Directory of example programs added to the GitHub repository, including:
  * Examples of the basic usage of the EnhancedRandomGenerator class, including potential use
    as a drop-in replacement, as well as demonstrations of enhancements to existing methods of
    Java's RandomGenerators, as well as functionality added by the class.
  * Demonstration of speed advantage of enhanced nextInt(bound) over Java API's builtin method.
  * Demonstration of ultrafast nextBiasedInt(bound) for cases where strict uniformity not needed.


## [2.3.2] - 2022-07-15

### Other
* First release available via JitPack after configuring builds. No actual changes to library. In addition
  to Maven Central and GitHub Packages, the library can now be imported from JitPack as a fall-back option,
  as well as a source of snapshot artifacts built from the current default branch or specific commit hashes.


## [2.3.1] - 2022-06-21

### Documentation
* Updated documentation on the module page.


## [2.3.0] - 2022-05-09

### Added
* Streams of integers from binomial distributions.
* Streams of doubles from Cauchy distributions.
* Streams of doubles from exponential distributions.
* Streams of doubles from Gaussian distributions.
* Streams of not strictly uniform random integers.
* Seeded constructors for the EnhancedSplittableGenerator, EnhancedStreamableGenerator, and
  EnhancedRandomGenerator classes.
* Default constructors for the EnhancedStreamableGenerator and EnhancedSplittableGenerator classes.


## [2.2.0] - 2022-04-30

### Added
* Subclasses of EnhancedRandomGenerator as wrappers for objects that implement the subinterfaces 
  of RandomGenerator enabling adding to those objects all of the functionality of the 
  RandomIndexer and RandomVariates classes. The new subclasses of EnhancedRandomGenerator include:
  * EnhancedSplittableGenerator, which is a wrapper for RandomGenerator.SplittableGenerator,
  * EnhancedJumpableGenerator, which is a wrapper for RandomGenerator.JumpableGenerator,
  * EnhancedLeapableGenerator, which is a wrapper for RandomGenerator.LeapableGenerator,
  * EnhancedStreamableGenerator, which is a wrapper for RandomGenerator.StreamableGenerator, and
  * EnhancedArbitrarilyJumpableGenerator, which is a wrapper for RandomGenerator.ArbitrarilyJumpableGenerator.


## [2.1.0] - 2022-04-26

### Added
* EnhancedRandomGenerator class: wrapper for objects of classes that implement RandomGenerator,
  adding to those objects all of the functionality of the RandomIndexer and RandomVariates classes.
  

## [2.0.0] - 2022-03-21

**Breaking Changes**
Contains breaking changes, including increasing minimum supported Java version to Java 17.

### Changed
* Minimum supported Java version is now Java 17.
* Refactored all classes in package org.cicirello.math.rand to use Java 17's new
  RandomGenerator interface.
* Migrated test cases to JUnit Jupiter 5.8.2.


## [1.2.0] - 2022-02-11

### Added
* Statistics.stdev(int[]) and Statistics.stdev(double[]) methods for sample standard deviation.

### CI/CD
* Added automated commenting of test coverage percentages to CI/CD workflow.


## [1.1.0] - 2021-9-22

### Added
* Gaussian random number generators, specifically integrated the implementations
  from the [Chips-n-Salsa](https://github.com/cicirello/Chips-n-Salsa) library.


## [1.0.0] - 2021-9-22

### Added
* This library provides the Java module org.cicirello.rho_mu, which includes the
  following packages in this initial release, all of which originated with the
  [JavaPermutationTools](https://github.com/cicirello/JavaPermutationTools) library:
  * org.cicirello.math: Math functions needed by other functionality.
  * org.cicirello.math.la: Linear algebra related.
  * org.cicirello.math.rand: Efficient random sampling of single, pair, and triples of array
    indexes, as well as other randomization related functionality such as distributions other
	than random.
  * org.cicirello.math.stats: Statistics related functionality.
