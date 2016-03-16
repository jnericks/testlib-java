package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SystemUnderTestFactory<TSut>
{
    private final TypeToken<TSut> _typeToken;
    private volatile Constructor<?> _ctor;

    private List<Dependency> _dependencies;
    private TSut _sut;

    private Runnable _preProcessor;
    private Supplier<TSut> _sutFactory;
    private Consumer<TSut> _postProcessor;

    public SystemUnderTestFactory(Class<TSut> type)
    {
        _typeToken = TypeToken.of(type);

        _dependencies = new ArrayList<>();
        _ctor = TestUtils.getGreediestCtor(_typeToken);

        Type[] parameterTypes = _ctor.getGenericParameterTypes();
        for (Type t : parameterTypes)
        {
            TypeToken parameterTypeToken = TypeToken.of(t);
            _dependencies.add(new Dependency<>(parameterTypeToken));
        }
    }

    public void createSutUsing(Supplier<TSut> sutFactory)
    {
        _sutFactory = sutFactory;
    }

    public void beforeSutCreated(Runnable preProcessor)
    {
        _preProcessor = preProcessor;
    }

    public void afterSutCreated(Consumer<TSut> postProcessor)
    {
        _postProcessor = postProcessor;
    }

    public void createSut()
    {
        try
        {
            if (_sut == null)
            {
                if (_preProcessor != null)
                    _preProcessor.run();

                if (_sutFactory == null)
                    _sut = (TSut)_ctor.newInstance(getCtorArgs());
                else
                    _sut = _sutFactory.get();

                if (_postProcessor != null)
                    _postProcessor.accept(_sut);
            }
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Object[] getCtorArgs()
    {
        return _dependencies.stream().map(Dependency::get).toArray();
    }

    public TSut sut()
    {
        createSut();
        return _sut;
    }

    public <TDependency> TDependency dependency(Class<TDependency> type)
    {
        for (Dependency d : _dependencies)
        {
            if (d.typeToken.isSubtypeOf(type))
            {
                return (TDependency)d.get();
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", type.getSimpleName(), _typeToken.getRawType().getSimpleName()));
    }

    public <TDependency> DoForDependency<TDependency> forDependency(Class<TDependency> type)
    {
        if (_dependencies.stream().anyMatch(x -> x.typeToken.isSubtypeOf(type)))
            return new DoForDependency(type, _dependencies);

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", type.getSimpleName(), _typeToken.getRawType().getSimpleName()));
    }

    public <TDependency> DoForDependency<TDependency> forDependency(TypeToken<TDependency> typeToken)
    {
        if (_dependencies.stream().anyMatch(x -> x.typeToken.equals(typeToken)))
            return new DoForDependency(typeToken, _dependencies);

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), _typeToken.getRawType().getSimpleName()));
    }
}
