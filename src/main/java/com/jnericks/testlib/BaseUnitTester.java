package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

public abstract class BaseUnitTester {

    /**
     * Verifying exactly 0 invocations
     */
    protected static final VerificationMode NeverReceive = times(0);

    /**
     * Verifying exactly 1 invocations
     */
    protected static final VerificationMode ReceivedOnce = times(1);

    /*
     * Verifying at-least-once invocation
     */
    protected static final VerificationMode ReceiveAtLeastOnce = atLeastOnce();

    /**
     * Creates a fake object that supports stubbing/mocking via Mockito.
     *
     * @param typeToken the type token of the fake object
     * @param <T>       the type of the fake object
     * @return the fake object.
     */
    public static <T> T fake(TypeToken<T> typeToken) {
        return Mockito.mock((Class<T>)typeToken.getRawType());
    }

    /**
     * Creates a fake object that supports stubbing/mocking via Mockito.
     *
     * @param type the type of the fake object
     * @param <T>  the type of the fake object
     * @return the fake object.
     */
    protected static <T> T fake(Class<T> type) {
        return Mockito.mock(type);
    }
}
