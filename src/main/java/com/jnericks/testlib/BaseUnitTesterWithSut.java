package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import org.junit.Before;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseUnitTesterWithSut<TSut> extends BaseUnitTester
{
    private SystemUnderTestFactory<TSut> sutFactory;

    @Before
    public void createSutFactory()
    {
        Class<TSut> parameterizedType = getParameterizedType();
        sutFactory = new SystemUnderTestFactory<>(parameterizedType);
    }

    /**
     * Allows you to supply your own factory to create the system under test.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     */
    protected void createSutUsing(Supplier<TSut> sutFactory)
    {
        this.sutFactory.createSutUsing(sutFactory);
    }

    /**
     * Runs just before the system under test is created.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @param preProcessor runnable to execute before system under test is created
     */
    protected void beforeSutCreated(Runnable preProcessor)
    {
        sutFactory.beforeSutCreated(preProcessor);
    }

    /**
     * Runs right after the system under test is created.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @param postProcessor consumer which has access to the system under test just created
     */
    protected void afterSutCreated(Consumer<TSut> postProcessor)
    {
        sutFactory.afterSutCreated(postProcessor);
    }

    /**
     * Initiates the creation of the system under test without returning it.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     */
    protected void createSut()
    {
        sutFactory.createSut();
    }

    /**
     * Initiates the creation of the system under test and returns it. Successive calls do not
     * re-create the system under test, it will return the one already created by the first call.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @return the system under test.
     */
    protected TSut sut()
    {
        return sutFactory.sut();
    }

    /**
     * Gives access to the fake object created for each constructor based dependency of the system
     * under test.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @param type            the type of the dependency
     * @param <TDependency>   the type of the dependency
     * @return the dependency
     */
    protected <TDependency> TDependency dependency(Class<TDependency> type)
    {
        return sutFactory.dependency(type);
    }

    /**
     * Allows you to supply your own dependency for a given type
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @param type          the type of the dependency
     * @param <TDependency> the type of the dependency
     * @return object that allows you to supply your own dependency
     */
    protected <TDependency> DoForDependency<TDependency> forDependency(Class<TDependency> type)
    {
        return sutFactory.forDependency(type);
    }

    /**
     * Allows you to supply your own dependency for a given type token
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @param typeToken     the type token of the dependency
     * @param <TDependency> the type of the dependency
     * @return object that allows you to supply your own dependency
     */
    protected <TDependency> DoForDependency<TDependency> forDependency(TypeToken<TDependency> typeToken)
    {
        return sutFactory.forDependency(typeToken);
    }

    /**
     * Allows you to supply your own set of dependencies for a given type. Useful for when there are
     * multiple dependencies with the same type.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @param type          the type of the dependencies
     * @param <TDependency> the type of the dependencies
     * @return object that allows you to supply your own dependencies
     */
    protected <TDependency> DoForDependencies<TDependency> forDependencies(Class<TDependency> type)
    {
        return sutFactory.forDependencies(type);
    }

    /**
     * Allows you to supply your own set of dependencies for a given type token. Useful for when
     * there are multiple dependencies with the same type.
     *
     * Note: redirects call to underlying SystemUnderTestFactory
     *
     * @param typeToken     the type token of the dependencies
     * @param <TDependency> the type of the dependencies
     * @return object that allows you to supply your own dependencies
     */
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
