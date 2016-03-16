package com.jnericks.testlib;

import java.util.List;

public class DoForDependency<TDoFor>
{
    private final List<Dependency> _dependencies;

    DoForDependency(List<Dependency> dependencies)
    {
        _dependencies = dependencies;
    }

    public void use(TDoFor dependency)
    {
        _dependencies.forEach(d -> d.set(dependency));
    }
}
