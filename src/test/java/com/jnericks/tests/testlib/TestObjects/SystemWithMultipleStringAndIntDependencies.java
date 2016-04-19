package com.jnericks.tests.testlib.TestObjects;

public class SystemWithMultipleStringAndIntDependencies
{
    private DependencyA a;
    private String[] strings;
    private int[] ints;

    public SystemWithMultipleStringAndIntDependencies(String s1, int i1, String s2, DependencyA a, int i2, String s3, int i3)
    {
        this.strings = new String[]{s1, s2, s3};
        this.a = a;
        this.ints = new int[]{i1, i2, i3};
    }

    public String[] getStrings()
    {
        return strings;
    }

    public int[] getInts()
    {
        return ints;
    }

    public Object executeA(Object input)
    {
        return a.doSomething(input);
    }
}
