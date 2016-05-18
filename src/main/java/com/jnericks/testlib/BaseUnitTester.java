package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

public abstract class BaseUnitTester
{
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
     * Creates a fake object that supports stubbing/mocking of final and native methods using PowerMockito.
     *
     * @param type the type of the fake object
     * @param <T>  the type of the fake object
     * @return the fake object.
     */
    protected static <T> T fake(Class<T> type)
    {
        return PowerMockito.mock(type);
    }

    /**
     * Creates a fake object that supports stubbing/mocking of final and native methods using PowerMockito.
     *
     * @param typeToken the type token of the fake object
     * @param <T>       the type of the fake object
     * @return the fake object.
     */
    public static <T> T fake(TypeToken<T> typeToken)
    {
        return PowerMockito.mock((Class<T>) typeToken.getRawType());
    }
}
