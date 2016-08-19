package com.jnericks.tests.testlib.TestObjects;

public class SystemWithPrimitives {

    private int _i;
    private DependencyA _a;

    public SystemWithPrimitives(int i, DependencyA a) {
        _i = i;
        _a = a;
    }

    public int getI() {
        return _i;
    }

    public Object executeA(Object o) {
        return _a.doSomething(o);
    }
}
