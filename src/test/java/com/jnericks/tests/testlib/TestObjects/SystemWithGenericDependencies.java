package com.jnericks.tests.testlib.TestObjects;

import java.util.List;

public class SystemWithGenericDependencies
{
    DependencyA _depDependencyA;
    List<DependencyB> _depDependencyBs;

    public SystemWithGenericDependencies(DependencyA _depDependencyA, List<DependencyB> _depDependencyBs)
    {
        this._depDependencyA = _depDependencyA;
        this._depDependencyBs = _depDependencyBs;
    }

    public DependencyA getA()
    {
        return _depDependencyA;
    }

    public List<DependencyB> getBs()
    {
        return _depDependencyBs;
    }
}
