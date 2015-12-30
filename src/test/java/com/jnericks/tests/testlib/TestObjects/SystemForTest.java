package com.jnericks.tests.testlib.TestObjects;

public class SystemForTest
{
    DependencyA _dependencyA;
    DependencyB _dependencyB;

    public SystemForTest(DependencyB dependencyB)
    {
        _dependencyB = dependencyB;
    }

    public SystemForTest(DependencyA dependencyA, DependencyB dependencyB)
    {
        _dependencyA = dependencyA;
        _dependencyB = dependencyB;
    }

    public void doAStuff()
    {
        _dependencyA.aStuff();
    }

    public Object passToDependencyA(Object o)
    {
        return _dependencyA.doSomething(o);
    }

    public void doBStuff()
    {
        _dependencyB.bStuff();
    }
}
