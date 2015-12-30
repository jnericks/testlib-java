package com.jnericks.tests.testlib.TestObjects;

import java.util.function.Function;

public class DependencyAImpl implements DependencyA
{
    private final Runnable _aStuff;
    private final Function<Object, Object> _doSomething;

    public DependencyAImpl(Runnable aStuff, Function<Object, Object> doSomething)
    {
        _aStuff = aStuff != null ? aStuff : () -> {};
        _doSomething = doSomething != null ? doSomething : x -> null;
    }

    @Override
    public void aStuff()
    {
        _aStuff.run();
    }

    @Override
    public Object doSomething(Object o)
    {
        return _doSomething.apply(o);
    }
}
