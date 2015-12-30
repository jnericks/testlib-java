package com.jnericks.testlib;

import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.times;

public abstract class BaseUnitTester
{
    public final VerificationMode NeverReceive = times(0);
    public final VerificationMode ReceivedOnce = times(1);
}
