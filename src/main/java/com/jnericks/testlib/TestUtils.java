package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Constructor;

public final class TestUtils {

    TestUtils() { }

    public static <T> Constructor<T> getGreediestCtor(Class<T> aClass) {
        return getGreediestCtor(TypeToken.of(aClass));
    }

    public static <T> Constructor<T> getGreediestCtor(TypeToken<T> typeToken) {
        Constructor[] ctors = typeToken.getRawType().getConstructors();
        Constructor greediest = ctors[0];
        int count = greediest.getParameterCount();
        int len = ctors.length;
        if (len > 1) {
            for (int i = 1; i < len; i++) {
                Constructor ctor = ctors[i];
                if (ctor.getParameterCount() > count) {
                    greediest = ctor;
                    count = ctor.getParameterCount();
                }
            }
        }

        return greediest;
    }
}
