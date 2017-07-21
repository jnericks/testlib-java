package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.mockito.Mockito;

public class Dependency<T> {

    final TypeToken<T> typeToken;
    T dependency;

    Dependency(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    public T get() {
        try {
            // lazy load uninitialized dependency
            if (dependency == null) {
                dependency = Mockito.mock((Class<T>)typeToken.getRawType());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("%s. Please provide a value for this dependency.", e.getMessage()));
        }

        return dependency;
    }

    public void set(T dependency) {
        this.dependency = dependency;
    }
}