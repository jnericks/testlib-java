package com.jnericks.tests.testlib.TestObjects;

import java.util.List;

public class SystemWithGenericDependencies {

    DependencyA a;
    List<DependencyA> as;
    List<DependencyB> bs;

    public SystemWithGenericDependencies(DependencyA a, List<DependencyA> as, List<DependencyB> bs) {
        this.a = a;
        this.as = as;
        this.bs = bs;
    }

    public DependencyA getA() {
        return a;
    }

    public List<DependencyA> getAs() {
        return as;
    }

    public List<DependencyB> getBs() {
        return bs;
    }
}
