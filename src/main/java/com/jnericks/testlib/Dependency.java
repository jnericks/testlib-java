package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.mockito.Mockito;

public class Dependency<T> {

    public final TypeToken<T> typeToken;
    private T object;

    Dependency(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    public T get() {
        try {
            if (object == null) {
                Class<T> c = (Class<T>)typeToken.getRawType();
                object = Mockito.mock(c);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("%s. Please provide a value for this dependency.", e.getMessage()));
        }

        return object;
    }

    public void set(T dependency) {
        this.object = dependency;
    }
}