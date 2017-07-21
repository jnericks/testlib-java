package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class SystemUnderTestFactory<TSut> {

    final TypeToken<TSut> sutTypeToken;
    volatile Constructor<?> ctor;

    List<Dependency> dependencies;
    TSut systemUnderTest;

    Runnable preProcessor = () -> {};
    Consumer<TSut> postProcessor = sut -> {};

    Supplier<TSut> sutFactory = () -> {
        try {
            return (TSut)ctor.newInstance(dependencies.stream().map(Dependency::get).toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };

    public SystemUnderTestFactory(Class<TSut> type) {
        sutTypeToken = TypeToken.of(type);

        dependencies = new ArrayList<>();
        ctor = TestUtils.getGreediestCtor(sutTypeToken);

        Type[] parameterTypes = ctor.getGenericParameterTypes();
        for (Type t : parameterTypes) {
            TypeToken parameterTypeToken = TypeToken.of(t);
            dependencies.add(new Dependency<>(parameterTypeToken));
        }
    }

    /**
     * Allows you to supply your own factory to create the system under test.
     *
     * @param sutFactory a supplier that will provide your own instance of TSut, bypassing the
     *                   auto-generated one
     */
    public void createSutUsing(Supplier<TSut> sutFactory) {
        this.sutFactory = sutFactory;
    }

    /**
     * Runs just before the system under test is created.
     *
     * @param preProcessor runnable to execute before system under test is created
     */
    public void beforeSutCreated(Runnable preProcessor) {
        this.preProcessor = preProcessor;
    }

    /**
     * Runs right after the system under test is created.
     *
     * @param postProcessor consumer which has access to the system under test just created
     */
    public void afterSutCreated(Consumer<TSut> postProcessor) {
        this.postProcessor = postProcessor;
    }

    /**
     * Initiates the creation of the system under test without returning it.
     */
    public void createSut() {
        // sut already initialized
        if (systemUnderTest != null)
            return;

        preProcessor.run();
        systemUnderTest = sutFactory.get();
        postProcessor.accept(systemUnderTest);
    }

    /**
     * Initiates the creation of the system under test and returns it. Successive calls do not
     * re-create the system under test, it will return the one already created by the first call.
     *
     * @return the system under test.
     */
    public TSut sut() {
        createSut();
        return systemUnderTest;
    }

    /**
     * Gives access to the fake object created for each constructor based dependency of the system
     * under test.
     *
     * @param type          the type of the dependency
     * @param <TDependency> the type of the dependency
     * @return the dependency
     */
    public <TDependency> TDependency dependency(Class<TDependency> type) {
        for (Dependency d : dependencies) {
            if (d.typeToken.isSubtypeOf(type)) {
                return (TDependency)d.get();
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", type.getSimpleName(), sutTypeToken.getRawType().getSimpleName()));
    }

    /**
     * Gives access to the fake object created for each constructor based dependency of the system
     * under test.
     *
     * @param typeToken     the type token of the dependency
     * @param <TDependency> the type of the dependency
     * @return the dependency
     */
    public <TDependency> TDependency dependency(TypeToken<TDependency> typeToken) {
        for (Dependency d : dependencies) {
            if (d.typeToken.equals(typeToken)) {
                return (TDependency)d.get();
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), sutTypeToken.getRawType().getSimpleName()));
    }

    /**
     * Allows you to supply your own dependency for a given type
     *
     * @param type          the type of the dependency
     * @param <TDependency> the type of the dependency
     * @return object that allows you to supply your own dependency
     */
    public <TDependency> DoForDependency<TDependency> forDependency(Class<TDependency> type) {
        return forDependency(TypeToken.of(type));
    }

    /**
     * Allows you to supply your own dependency for a given type token
     *
     * @param typeToken     the type token of the dependency
     * @param <TDependency> the type of the dependency
     * @return object that allows you to supply your own dependency
     */
    public <TDependency> DoForDependency<TDependency> forDependency(TypeToken<TDependency> typeToken) {
        List<Dependency> dependencies = this.dependencies.stream().filter(x -> x.typeToken.equals(typeToken)).collect(Collectors.toList());

        if (dependencies.size() > 0) {
            return new DoForDependency(dependencies);
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), sutTypeToken.getRawType().getSimpleName()));
    }

    /**
     * Allows you to supply your own set of dependencies for a given type. Useful for when there are
     * multiple dependencies with the same type.
     *
     * @param type          the type of the dependencies
     * @param <TDependency> the type of the dependencies
     * @return object that allows you to supply your own dependencies
     */
    public <TDependency> DoForDependencies<TDependency> forDependencies(Class<TDependency> type) {
        return forDependencies(TypeToken.of(type));
    }

    /**
     * Allows you to supply your own set of dependencies for a given type token. Useful for when
     * there are multiple dependencies with the same type.
     *
     * @param typeToken     the type token of the dependencies
     * @param <TDependency> the type of the dependencies
     * @return object that allows you to supply your own dependencies
     */
    public <TDependency> DoForDependencies<TDependency> forDependencies(TypeToken<TDependency> typeToken) {
        List<Dependency> dependencies = this.dependencies.stream().filter(x -> x.typeToken.equals(typeToken)).collect(Collectors.toList());

        if (dependencies.size() > 0) {
            return new DoForDependencies<>(dependencies);
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), sutTypeToken.getRawType().getSimpleName()));
    }
}
