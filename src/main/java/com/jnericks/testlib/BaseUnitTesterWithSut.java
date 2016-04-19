package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.junit.Before;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseUnitTesterWithSut<TSut> extends BaseUnitTester
{
    protected SystemUnderTestFactory<TSut> sutFactory;

    @Before
    public void before()
    {
        Class<TSut> parameterizedType = getParameterizedType();
        sutFactory = new SystemUnderTestFactory<>(parameterizedType);
    }

    protected void createSutUsing(Supplier<TSut> sutFactory)
    {
        this.sutFactory.createSutUsing(sutFactory);
    }

    protected void beforeSutCreated(Runnable preProcessor)
    {
        sutFactory.beforeSutCreated(preProcessor);
    }

    protected void afterSutCreated(Consumer<TSut> postProcessor)
    {
        sutFactory.afterSutCreated(postProcessor);
    }

    protected void createSut()
    {
        sutFactory.createSut();
    }

    protected TSut sut()
    {
        return sutFactory.sut();
    }

    protected <TDependency> TDependency dependency(Class<TDependency> type)
    {
        return sutFactory.dependency(type);
    }

    protected <TDependency> DoForDependency<TDependency> forDependency(Class<TDependency> type)
    {
        return sutFactory.forDependency(type);
    }

    protected <TDependency> DoForDependency<TDependency> forDependency(TypeToken<TDependency> typeToken)
    {
        return sutFactory.forDependency(typeToken);
    }

    protected <TDependency> DoForDependencies<TDependency> forDependencies(Class<TDependency> type)
    {
        return sutFactory.forDependencies(type);
    }

    protected <TDependency> DoForDependencies<TDependency> forDependencies(TypeToken<TDependency> typeToken)
    {
        return sutFactory.forDependencies(typeToken);
    }

    private Class<TSut> getParameterizedType()
    {
        Type type = getClass().getGenericSuperclass();

        while (!(type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(BaseUnitTesterWithSut.class)))
        {
            type = ((Class<?>) type).getGenericSuperclass();
        }

        type = ((ParameterizedType) type).getActualTypeArguments()[0];

        return (Class<TSut>) type;
    }
}
