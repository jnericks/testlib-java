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
    public void createSutFactory()
    {
        Class<TSut> parameterizedType = getParameterizedType();
        sutFactory = new SystemUnderTestFactory<>(parameterizedType);
    }

    /**
     * Allows you to supply your own factory to create the system under test.
     *
     * @param sutFactory
     */
    protected void createSutUsing(Supplier<TSut> sutFactory)
    {
        this.sutFactory.createSutUsing(sutFactory);
    }

    /**
     * Runs just before the system under test is created.
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
     * @param postProcessor consumer which has access to the system under test just created
     */
    protected void afterSutCreated(Consumer<TSut> postProcessor)
    {
        sutFactory.afterSutCreated(postProcessor);
    }

    /**
     * Initiates the creation of the system under test without returning it.
     */
    protected void createSut()
    {
        sutFactory.createSut();
    }

    /**
     * Initiates the creation of the system under test and returns it.
     *
     * @return the system under test.
     */
    protected TSut sut()
    {
        return sutFactory.sut();
    }

    /**
     * Gives access to the mock object created for each constructor based dependency of the system under test.
     *
     * @param dependencyClass the type of the dependency
     * @param <TDependency>   the type of the dependency
     * @return the dependency
     */
    protected <TDependency> TDependency dependency(Class<TDependency> dependencyClass)
    {
        return sutFactory.dependency(dependencyClass);
    }

    /**
     * Allows you to supply your own dependency for a given type
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
     * @param typeToken     the type token of the dependency
     * @param <TDependency> the type of the dependency
     * @return object that allows you to supply your own dependency
     */
    protected <TDependency> DoForDependency<TDependency> forDependency(TypeToken<TDependency> typeToken)
    {
        return sutFactory.forDependency(typeToken);
    }

    /**
     * Allows you to supply your own set of dependencies for a given type.
     * Use for when there are multiple dependencies with the same type.
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
     * Allows you to supply your own set of dependencies for a given type token.
     * Use for when there are multiple dependencies with the same type.
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
