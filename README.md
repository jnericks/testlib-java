TestLib-Java
================================

[![Build Status](https://travis-ci.org/jnericks/testlib-java.svg?branch=master)](https://travis-ci.org/jnericks/testlib-java)
[![Code Coverage](https://img.shields.io/codecov/c/github/jnericks/testlib-java/master.svg)](https://codecov.io/github/jnericks/testlib-java?branch=master)
[![Download](https://api.bintray.com/packages/jnericks/testlib-java/testlib-java/images/download.svg)](https://bintray.com/jnericks/testlib-java/testlib-java/_latestVersion)

Dependencies
--------------------------------
* [AssertJ](http://joel-costigliola.github.io/assertj/index.html)
* [Guava](https://github.com/google/guava)
* [JUnit](http://junit.org/)
* [Mockito](http://mockito.org/)

What is TestLib-Java?
--------------------------------
TestLib-Java is the base package that we utilize in our Java 8 unit test projects. We leverage JUnit because it is consistently stable, actively developed and is the easiest to grasp for engineers when learning to write testable code (utilizing TDD or not). Although we use JUnit, We wanted to be able to adopt a BDD style of testing where we can so this led to the `SystemUnderTestFactory` that will auto-generate an object pre-filled with mocks for your constructor based dependencies. This [blog post](http://blog.ploeh.dk/2009/02/13/SUTFactory/) by Mark Seeman explains the benefits for a SUTFactory, basically it allows your tests to be resilient to changes in the signatures of the constructors of the objects they are testing.
