package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mock;

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
     * Creates a mock object that supports mocking of final and native methods.
     *
     * @param type the type of the mock object
     * @param <T>  the type of the mock object
     * @return the mock object.
     */
    protected static <T> T an(Class<T> type)
    {
        return mock(type);
    }

    /**
     * Creates a mock object that supports mocking of final and native methods.
     *
     * @param typeToken the type token of the mock object
     * @param <T>       the type of the mock object
     * @return the mock object.
     */
    public static <T> T an(TypeToken<T> typeToken)
    {
        return mock((Class<T>) typeToken.getRawType());
    }
}
