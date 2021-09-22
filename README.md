# rho-mu

## Overview

&rho;&mu; is a library of Randomization enHancements and Other Math Utilities
(rho mu). It includes implementations of various algorithms for efficiently 
randomly sampling combinations of indexes into arrays and other sequential
structures. It also includes efficient implementations of random number generation from
distributions other than uniform, such as Gaussian, Cauchy, etc. Additionally, it 
includes implementations of other math functions that are either needed by the randomization 
utilities, or which are needed by some of our other projects.

All of the initial functionality of &rho;&mu; originated in some of our other
libraries, including [JavaPermutationTools](https://github.com/cicirello/JavaPermutationTools)
and [Chips-n-Salsa](https://github.com/cicirello/Chips-n-Salsa). However, we found ourselves 
beginning to add one or the other of those libraries as dependencies in some other projects
strictly for the randomization utilities, which is certainly less than ideal. Therefore, we 
have extracted &rho;&mu; from those libraries.

