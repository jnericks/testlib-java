package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

public class Dependency<TDependency>
{
    public final TypeToken<TDependency> typeToken;
    private TDependency object;

    Dependency(TypeToken<TDependency> typeToken)
    {
        this.typeToken = typeToken;
    }

    public TDependency get()
    {
        try
        {
            if (object == null)
                object = Mocks.mock(typeToken);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(String.format("%s. Please provide a value for this dependency.", e.getMessage()));
        }

        return object;
    }

    public void set(TDependency dependency)
    {
        this.object = dependency;
    }
}