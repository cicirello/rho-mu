# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - 2022-05-07

### Added
* Seeded constructors for the EnhancedSplittableGenerator, EnhancedStreamableGenerator, and
  EnhancedRandomGenerator classes.

### Changed

### Deprecated

### Removed

### Fixed

### CI/CD

### Other


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
