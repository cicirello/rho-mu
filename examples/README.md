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
Results on your system may differ due to differences in CPU, OS, etc.

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
