package com.jnericks.tests.testlib.TestObjects;

public class SystemWithPrimitives {

    int i;
    DependencyA a;

    public SystemWithPrimitives(int i, DependencyA a) {
        this.i = i;
        this.a = a;
    }

    public int getI() {
        return i;
    }

    public Object executeA(Object o) {
        return a.doSomething(o);
    }
}
