package com.jnericks.testlib;

import org.junit.Before;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseUnitTesterWithSut<TSut> extends BaseUnitTester
{
    protected SystemUnderTestFactory<TSut> SutFactory;

    @Before
    public void before()
    {
        Class<TSut> parameterizedType = getParameterizedType();
        SutFactory = new SystemUnderTestFactory<>(parameterizedType);
    }

    protected Class<TSut> getParameterizedType()
    {
        Type type = getClass().getGenericSuperclass();

        while (!(type instanceof ParameterizedType) || ((ParameterizedType)type).getRawType() != BaseUnitTesterWithSut.class)
        {
            if (type instanceof ParameterizedType)
            {
                type = ((Class<?>)((ParameterizedType)type).getRawType()).getGenericSuperclass();
            }
            else
            {
                type = ((Class<?>)type).getGenericSuperclass();
            }
        }

        type = ((ParameterizedType)type).getActualTypeArguments()[0];

        return (Class<TSut>)type;
    }
}
