package com.jnericks.testlib;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DoForDependencies<TDoFor>
{
    private final List<Dependency> _dependencies;

    DoForDependencies(List<Dependency> dependencies)
    {
        _dependencies = dependencies;
    }

    public void use(TDoFor... dependencies)
    {
//        if (_dependencies.size() != dependency.length)
//            throw new RuntimeException();

        for (int i = 0; i < _dependencies.size(); i++)
            _dependencies.get(i).set(dependencies[i]);
    }

    public void use(Collection<TDoFor> dependencies)
    {
        Iterator<TDoFor> iterator = dependencies.iterator();

        for (int i = 0; i < _dependencies.size(); i++)
        {
            _dependencies.get(i).set(iterator.next());
        }
    }
}
