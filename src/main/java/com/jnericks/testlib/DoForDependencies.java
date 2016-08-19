package com.jnericks.testlib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DoForDependencies<TDoFor> {

    private final List<Dependency> _dependencies;

    DoForDependencies(List<Dependency> dependencies) {
        _dependencies = dependencies;
    }

    public void use(TDoFor... dependencies) {
        use(Arrays.asList(dependencies));
    }

    public void use(List<TDoFor> dependencies) {
        if (_dependencies.size() != dependencies.size())
            throw new IllegalArgumentException(String.format("List of dependencies are not the same size, expecting %d, received %d", _dependencies.size(), dependencies.size()));

        Iterator<TDoFor> iterator = dependencies.iterator();
        for (int i = 0; i < _dependencies.size(); i++) {
            _dependencies.get(i).set(iterator.next());
        }
    }
}
