package com.jnericks.tests.testlib;

import com.jnericks.testlib.TestUtils;
import com.jnericks.tests.testlib.TestObjects.SystemWithManyConstructors;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.BDDAssertions.then;

public class TestUtilsTester
{
    @Test
    public void should_retrieve_ctor_with_most_parameters()
    {
        Constructor<SystemWithManyConstructors> ctor = TestUtils.getGreediestCtor(SystemWithManyConstructors.class);

        then(ctor.getParameterCount()).isEqualTo(4);
    }
}
