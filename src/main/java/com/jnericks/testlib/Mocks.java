package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;
import org.powermock.api.mockito.PowerMockito;

@SuppressWarnings("unchecked")
public class Mocks
{
    public static <T> T mock(TypeToken<T> typeTokenToMock)
    {
        return PowerMockito.mock((Class<T>)typeTokenToMock.getRawType());
    }
}
