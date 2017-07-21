package com.jnericks.testlib;

import java.util.List;

public class DoForDependency<TDoFor> {

    final List<Dependency> dependencies;

    DoForDependency(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void use(TDoFor dependency) {
        for (Dependency d : dependencies) {
            d.set(dependency);
        }
    }
}
