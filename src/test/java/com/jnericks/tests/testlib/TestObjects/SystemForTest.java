package com.jnericks.tests.testlib.TestObjects;

public class SystemForTest {

    DependencyA dependencyA;
    DependencyB dependencyB;

    public SystemForTest(DependencyA dependencyA, DependencyB dependencyB) {
        this.dependencyA = dependencyA;
        this.dependencyB = dependencyB;
    }

    public SystemForTest(DependencyB dependencyB) {
        this.dependencyB = dependencyB;
    }

    public void doAStuff() {
        dependencyA.aStuff();
    }

    public Object passToDependencyA(Object o) {
        return dependencyA.doSomething(o);
    }

    public void doBStuff() {
        dependencyB.bStuff();
    }
}
