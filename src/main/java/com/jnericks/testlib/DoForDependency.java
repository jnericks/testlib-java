package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import java.util.List;

public class DoForDependency<TDoFor>
{
    private final TypeToken<TDoFor> _typeToken;
    private final List<Dependency> _dependencies;

    DoForDependency(Class<TDoFor> type, List<Dependency> dependencies)
    {
        this(TypeToken.of(type), dependencies);
    }

    DoForDependency(TypeToken<TDoFor> type, List<Dependency> dependencies)
    {
        _typeToken = type;
        _dependencies = dependencies;
    }

    public void use(TDoFor dependency)
    {
        _dependencies.stream()
                     .filter(d -> d.typeToken.equals(_typeToken))
                     .forEach(d -> d.set(dependency));
    }
}
