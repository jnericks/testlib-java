package com.jnericks.testlib;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
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
        _ctor = getGreediestCtor(_typeToken);

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
            e.printStackTrace();
        }
    }

    private Object[] getCtorArgs()
    {
        return _dependencies.stream().map(x -> x.object).toArray();
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
                return (TDependency)d.object;
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", type.getSimpleName(), _typeToken.getRawType().getSimpleName()));
    }

    public <TDependency> DoForDependency<TDependency> forDependency(Class<TDependency> type)
    {
        for (int i = 0; i < _dependencies.size(); i++)
        {
            Dependency dependency = _dependencies.get(i);
            if (dependency.typeToken.isSubtypeOf(type))
            {
                return new DoForDependency(type, _dependencies, i);
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", type.getSimpleName(), _typeToken.getRawType().getSimpleName()));
    }

    public <TDependency> DoForDependency<TDependency> forDependency(TypeToken<TDependency> typeToken)
    {
        for (int i = 0; i < _dependencies.size(); i++)
        {
            Dependency dependency = _dependencies.get(i);
            if (typeToken.equals(dependency.typeToken))
            {
                return new DoForDependency(typeToken, _dependencies, i);
            }
        }

        throw new UnsupportedOperationException(String.format("%s is not a dependency of %s", typeToken.getRawType().getSimpleName(), _typeToken.getRawType().getSimpleName()));
    }

    Constructor getGreediestCtor(TypeToken<TSut> typeToken)
    {
        Constructor[] ctors = typeToken.getRawType().getConstructors();
        Constructor greediest = ctors[0];
        int count = greediest.getParameterCount();
        int len = ctors.length;
        if (len > 1)
        {
            for (int i = 1; i < len; i++)
            {
                Constructor ctor = ctors[i];
                if (ctor.getParameterCount() > count)
                {
                    greediest = ctor;
                    count = ctor.getParameterCount();
                }
            }
        }

        return greediest;
    }

    public class DoForDependency<TDoFor>
    {
        final TypeToken<TDoFor> _typeToken;
        final List<Dependency> _dependencies;
        int _index;

        protected DoForDependency(Class<TDoFor> type, List<Dependency> dependencies, int index)
        {
            this(TypeToken.of(type), dependencies, index);
        }

        protected DoForDependency(TypeToken<TDoFor> type, List<Dependency> dependencies, int index)
        {
            _typeToken = type;
            _dependencies = dependencies;
            _index = index;
        }

        public void use(TDoFor dependency)
        {
            _dependencies.set(_index, new Dependency(_typeToken, dependency));
        }
    }

    private class Dependency<TDependency>
    {
        public TypeToken<TDependency> typeToken;
        public TDependency object;

        public Dependency(TypeToken<TDependency> typeToken)
        {
            this(typeToken, Mocks.mock(typeToken));
        }

        public Dependency(TypeToken<TDependency> typeToken, TDependency use)
        {
            this.typeToken = typeToken;
            this.object = use;
        }
    }
}