package com.jnericks.tests.testlib.TestObjects;

public class SystemWithManyConstructors
{
    public SystemWithManyConstructors(DependencyA a, DependencyA b, DependencyA c, DependencyA d) {}

    public SystemWithManyConstructors(DependencyA a) {}

    public SystemWithManyConstructors(DependencyA a, DependencyA b, DependencyA c) {}

    public SystemWithManyConstructors(DependencyA a, DependencyA b) {}
}
