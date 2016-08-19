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

public class SystemUnderTestFactory<TSut> {

    private final TypeToken<TSut> _typeToken;
    private volatile Constructor<?> _ctor;

    private List<Dependency> _dependencies;
    private TSut _sut;

    private Runnable _preProcessor = () -> {};
    private Consumer<TSut> _postProcessor = sut -> {};

    private Supplier<TSut> _sutFactory = () -> {
        try {
            return (TSut)_ctor.newInstance(_dependencies.stream().map(Dependency::get).toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };

    public SystemUnderTestFactory(Class<TSut> type) {
        _typeToken = TypeToken.of(type);

        _dependencies = new ArrayList<>();
        _ctor = TestUtils.getGreediestCtor(_typeToken);

        Type[] parameterTypes = _ctor.getGenericParameterTypes();
        for (Type t : parameterTypes) {
            TypeToken parameterTypeToken = TypeToken.of(t);
            _dependencies.add(new Dependency<>(parameterTypeToken));
        }
    }

    /**
     * Allows you to supply your own factory to create the system under test.
     * @param sutFactory a supplier that will provide your own instance of TSut, bypassing the auto-generated one
     */
    public void createSutUsing(Supplier<TSut> sutFactory) {
        _sutFactory = sutFactory;
    }

    /**
     * Runs just before the system under test is created.
     *
     * @param preProcessor runnable to execute before system under test is created
     */
    public void beforeSutCreated(Runnable preProcessor) {
        _preProcessor = preProcessor;
    }

    /**
     * Runs right after the system under test is created.
     *
     * @param postProcessor consumer which has access to the system under test just created
     */
    public void afterSutCreated(Consumer<TSut> postProcessor) {
        _postProcessor = postProcessor;
    }

    /**
     * Initiates the creation of the system under test without returning it.
     */
    public void createSut() {
        // sut already initialized
        if (_sut != null)
            return;

        _preProcessor.run();
        _sut = _sutFactory.get();
        _postProcessor.accept(_sut);
    }

    /**
     * Initiates the creation of the system under test and returns it. Successive calls do not
     * re-create the system under test, it will return the one already created by the first call.
     *
     * @return the system under test.
     */
    public TSut sut() {
        createSut();
        return _sut;
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
        for (Dependency d : _dependencies) {
            if (d.typeToken.isSubtypeOf(type)) {
                return (TDependency)d.get();
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", type.getSimpleName(), _typeToken.getRawType().getSimpleName()));
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
        for (Dependency d : _dependencies) {
            if (d.typeToken.equals(typeToken)) {
                return (TDependency)d.get();
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), _typeToken.getRawType().getSimpleName()));
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
        List<Dependency> dependencies = _dependencies.stream().filter(x -> x.typeToken.equals(typeToken)).collect(Collectors.toList());

        if (dependencies.size() > 0)
            return new DoForDependency(dependencies);

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), _typeToken.getRawType().getSimpleName()));
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
        List<Dependency> dependencies = _dependencies.stream().filter(x -> x.typeToken.equals(typeToken)).collect(Collectors.toList());

        if (dependencies.size() > 0)
            return new DoForDependencies<>(dependencies);

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), _typeToken.getRawType().getSimpleName()));
    }
}
