package com.jnericks.tests.testlib.TestObjects;

public class SystemWithMultipleStringDependencies
{
    public String one;
    public String two;
    public String three;
    public DependencyA a;

    public SystemWithMultipleStringDependencies(String one, String two, String three, DependencyA a)
    {
        this.one = one;
        this.two = two;
        this.three = three;
        this.a = a;
    }
}
