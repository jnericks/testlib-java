package com.jnericks.testlib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DoForDependencies<TDoFor> {

    final List<Dependency> dependencies;

    DoForDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void use(TDoFor... dependencies) {
        use(Arrays.asList(dependencies));
    }

    public void use(List<TDoFor> dependencies) {
        if (this.dependencies.size() != dependencies.size()) {
            throw new IllegalArgumentException(String.format("List of dependencies are not the same size, expecting %d, received %d", this.dependencies.size(), dependencies.size()));
        }

        Iterator<TDoFor> iterator = dependencies.iterator();
        for (int i = 0; i < this.dependencies.size(); i++) {
            this.dependencies.get(i).set(iterator.next());
        }
    }
}
