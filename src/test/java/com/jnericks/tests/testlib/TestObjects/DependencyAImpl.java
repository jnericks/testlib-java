package com.jnericks.tests.testlib.TestObjects;

import java.util.function.Function;

public class DependencyAImpl implements DependencyA {

    Runnable aStuff;
    Function<Object, Object> doSomething;

    public DependencyAImpl(Runnable aStuff, Function<Object, Object> doSomething) {
        this.aStuff = aStuff != null ? aStuff : () -> {};
        this.doSomething = doSomething != null ? doSomething : x -> null;
    }

    @Override
    public void aStuff() {
        aStuff.run();
    }

    @Override
    public Object doSomething(Object o) {
        return doSomething.apply(o);
    }
}
