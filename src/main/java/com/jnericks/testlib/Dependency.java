package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.powermock.api.mockito.PowerMockito;

public class Dependency<T>
{
    public final TypeToken<T> typeToken;
    private T object;

    Dependency(TypeToken<T> typeToken)
    {
        this.typeToken = typeToken;
    }

    public T get()
    {
        try
        {
            if (object == null)
                object = PowerMockito.mock((Class<T>) typeToken.getRawType());
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(String.format("%s. Please provide a value for this dependency.", e.getMessage()));
        }

        return object;
    }

    public void set(T dependency)
    {
        this.object = dependency;
    }
}