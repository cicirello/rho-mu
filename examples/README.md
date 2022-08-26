# Directory of Example Programs for &rho;&mu; Usage

This directory contains programs demonstrating the usage of some of
the functionality of the &rho;&mu; library. A couple of the example
programs also demonstrate the speed advantage over the Java API's
builtin methods, with output from my runs available in the [data](data)
subdirectory.

## Building the Examples

The `pom.xml` in this directory builds the example programs. To build 
the examples, execute the following at the command line with the examples 
directory as your working directory:

```shell
mvn package
```

The requirements for building the examples are the same as for building
the library itself (e.g., Java 17+).

Building the examples will produce a jar file in a target subdirectory of
this examples directory.

## Available Examples

In order to run any of the example programs, you need to first build them (see above).

### Examples with the EnhancedRandomGenerator Wrapper Class

The EnhancedRandomGenerator wraps an instance of any class that implements RandomGenerator. This
wrapper class then: (a) replaces some functionality of the wrapped instance with faster algorithms
or other enhanced behavior, (b) adds additional functionality not present in Java's built-in random 
number generators, and (c) delegates to the wrapped instance any remaining methods of RandomGenerator.

The example program, `BasicUsageExamples.java`, demonstrates some of the functionality of 
EnhancedRandomGenerator, including both enhanced, as well as additional, functionality over that of 
Java 17's built-in randomization support. It is important that you read through the source code of the
example, rather than just running it. The output of this example program isn't as useful in isolation,
as it is when it is considered within the context of the source code examples and comments.

To run this example program, execute the following with the examples directory as your working
directory.

```Shell
mvn exec:java -q -Dexec.mainClass=org.cicirello.examples.rho_mu.BasicUsageExamples
```

If your application requires splittable random generators, or any other fancier form, such as leapable,
jumpable, etc, then please note that EnhancedRandomGenerator is the base class for a hierarchy of six
wrapper classes, each wrapping one of Java 17's RandomGenerator interfaces, while also implementing
the relevant interface. For example, if you want to wrap one of Java 17's splittable random number
generators, EnhancedSplittableGenerator wraps (as well as implements) RandomGenerator.SplittableGenerator,
enabling you to use it as a drop-in replacement while gaining the enhancements provided by &rho;&mu;.

### Demonstrating Speed Advantage Over Java's Builtin `RandomGenerator.nextInt(bound)`

The example program, `RandomIndexerTimes.java`, demonstrates the speed advantage of &rho;&mu; 
enhanced `nextInt(bound)` method over Java's builtin `RandomGenerator.nextInt(bound)`. It computes
the average CPU time of each for every bound from 1 through 512. For each case it tests the 
significance of the time difference with a t-Test. Finally it summarizes the results in three
broad categories: low bounds (less than 256), high bounds (greater than 256), and the special
case of bounds that are powers of 2. There shouldn't be any advantage in that last case since
no rejection sampling is required of either approach when the bound is a power of 2. 

To run this example program, execute the following with the examples directory as your working
directory.

```Shell
mvn exec:java -q -Dexec.mainClass=org.cicirello.examples.rho_mu.RandomIndexerTimes
```

The output from my runs is available in [data/output.RandomIndexerTimes.txt](data/output.RandomIndexerTimes.txt).
Results on your system may differ due to differences in CPU, OS, etc. The details of my test system are found in
[data/system-stats.txt](data/system-stats.txt).

### Trading Off Strict Uniformity for Enhanced Speed

In addition to an enhanced `nextInt(bound)` method, &rho;&mu; provides a `nextBiasedInt(bound)`
method that excludes the rejection sampling necessary to achieve strict uniformity. The purpose is
to provide an ultrafast method for use-cases where strict uniformity is not required. The example
program, `TimingNextBiasedIntMethod.java`, demonstrates the very significant speed advantage of 
&rho;&mu;'s `nextBiasedInt(bound)` over Java's builtin `RandomGenerator.nextInt(bound)`. It computes
the average CPU time of each for every bound from 1 through 512. For each case it tests the 
significance of the time difference with a t-Test. Finally it summarizes the results in three
broad categories: low bounds (less than 256), high bounds (greater than 256), and the special
case of bounds that are powers of 2. 

To run this example program, execute the following with the examples directory as your working
directory.

```Shell
mvn exec:java -q -Dexec.mainClass=org.cicirello.examples.rho_mu.TimingNextBiasedIntMethod
```

The output from my runs is available in [data/output.TimingNextBiasedIntMethod.txt](data/output.TimingNextBiasedIntMethod.txt).
Results on your system may differ due to differences in CPU, OS, etc. Also note that the times
are not directly comparable to those of the prior example because this example required timing a
much larger number of samples to get sufficiently measurable times (due to how much faster `nextBiasedInt(bound)` is).
The details of my test system are found in [data/system-stats.txt](data/system-stats.txt).
