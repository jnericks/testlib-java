package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;
import org.powermock.api.mockito.PowerMockito;

public final class Mocks
{
    private Mocks() { }

    public static <T> T mock(TypeToken<T> typeTokenToMock)
    {
        return PowerMockito.mock((Class<T>)typeTokenToMock.getRawType());
    }
}
