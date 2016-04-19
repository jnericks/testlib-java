package com.jnericks.tests.testlib;

import com.google.common.reflect.TypeToken;

import com.jnericks.testlib.BaseUnitTester;
import com.jnericks.testlib.BaseUnitTesterWithSut;
import com.jnericks.testlib.SystemUnderTestFactory;
import com.jnericks.tests.testlib.TestObjects.DependencyA;
import com.jnericks.tests.testlib.TestObjects.DependencyAImpl;
import com.jnericks.tests.testlib.TestObjects.DependencyB;
import com.jnericks.tests.testlib.TestObjects.NotADependency;
import com.jnericks.tests.testlib.TestObjects.SystemForTest;
import com.jnericks.tests.testlib.TestObjects.SystemWithGenericDependencies;
import com.jnericks.tests.testlib.TestObjects.SystemWithMultipleStringAndIntDependencies;
import com.jnericks.tests.testlib.TestObjects.SystemWithPrimitives;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mock;

public class SystemUnderTestFactoryTester extends BaseUnitTester
{
    public static class WhenSystemIsBasic extends BaseUnitTesterWithSut<SystemForTest>
    {
        @Before
        public void setup_context()
        {
            sutFactory = new SystemUnderTestFactory<>(SystemForTest.class);
        }

        @Test
        public void should_be_able_to_create_sut()
        {
            SystemForTest sut = sut();

            assertThat(sut).isExactlyInstanceOf(SystemForTest.class);
        }

        @Test
        public void should_have_sut_be_a_singleton()
        {
            SystemForTest sut1 = sut();
            SystemForTest sut2 = sut();

            assertThat(sut1).isSameAs(sut2);
        }

        @Test
        public void should_have_dependency_a()
        {
            DependencyA a = dependency(DependencyA.class);

            assertThat(a).isInstanceOf(DependencyA.class);
        }

        @Test
        public void should_have_dependency_b()
        {
            DependencyB a = dependency(DependencyB.class);

            assertThat(a).isInstanceOf(DependencyB.class);
        }

        @Test
        public void should_be_able_to_do_a_stuff()
        {
            sut().doAStuff();

            BDDMockito.then(dependency(DependencyA.class)).should(ReceivedOnce).aStuff();
        }

        @Test
        public void should_be_able_to_do_b_stuff()
        {
            sut().doBStuff();

            BDDMockito.then(dependency(DependencyB.class)).should(ReceivedOnce).bStuff();
        }

        @Test
        public void should_be_able_to_stub_a_method()
        {
            Object objectPassedToSut = new Object();
            Object objectReturnedFromDependencyA = new Object();

            given(dependency(DependencyA.class).doSomething(objectPassedToSut))
                    .willReturn(objectReturnedFromDependencyA);

            Object actual = sut().passToDependencyA(objectPassedToSut);
            assertThat(actual).isSameAs(objectReturnedFromDependencyA);
        }

        @Test
        public void should_be_able_to_retrieve_injected_substitute()
        {
            DependencyA myDependencyA = mock(DependencyA.class);
            forDependency(DependencyA.class).use(myDependencyA);

            assertThat(dependency(DependencyA.class)).isSameAs(myDependencyA);
        }

        @Test
        public void should_be_able_to_assert_on_injected_substitute()
        {
            DependencyA myDependencyA = mock(DependencyA.class);
            forDependency(DependencyA.class).use(myDependencyA);
            sut().doAStuff();

            BDDMockito.then(myDependencyA).should(ReceivedOnce).aStuff();
        }

        @Test
        public void should_throw_exception_when_configuring_an_object_that_is_NOT_a_dependency()
        {
            assertThatThrownBy(() -> forDependency(NotADependency.class).use(mock(NotADependency.class)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_throw_exception_when_configuring_an_object_with_a_TypeToken_that_is_NOT_a_dependency()
        {
            assertThatThrownBy(() -> forDependency(new TypeToken<List<NotADependency>>() {}).use(new ArrayList<>()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_throw_exception_when_retrieving_object_that_is_NOT_a_dependency()
        {
            assertThatThrownBy(() -> dependency(NotADependency.class))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_throw_exception_when_trying_to_retrieve_sut_from_method()
        {
            assertThatThrownBy(() -> dependency(SystemForTest.class))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_be_able_to_override_internal_sut_factory()
        {
            SystemForTest system = mock(SystemForTest.class);
            createSutUsing(() -> system);
            assertThat(sut()).isSameAs(system);
        }

        @Test
        public void should_be_able_to_add_a_pre_processor()
        {
            Runnable runnable = mock(Runnable.class);

            beforeSutCreated(runnable);
            createSut();

            BDDMockito.then(runnable).should(ReceivedOnce).run();
        }

        @Test
        public void should_be_able_to_add_a_post_processor()
        {
            Consumer<SystemForTest> consumer = mock(Consumer.class);

            afterSutCreated(consumer);
            createSut();

            BDDMockito.then(consumer).should(ReceivedOnce).accept(sut());
        }

        @Test
        public void should_be_able_to_add_pre_and_post_processors_to_custom_sut_creation()
        {
            Runnable runnable = mock(Runnable.class);
            Consumer<SystemForTest> consumer = mock(Consumer.class);

            beforeSutCreated(runnable);
            afterSutCreated(consumer);

            createSutUsing(() -> new SystemForTest(null));
            createSut();

            BDDMockito.then(runnable).should(ReceivedOnce).run();
            BDDMockito.then(consumer).should(ReceivedOnce).accept(sut());
        }

        @Test
        public void should_be_able_to_swap_in_a_concrete_impl_of_a_dependency()
        {
            Object objectPassedIn = new Object();
            Object objectReturned = new Object();

            Runnable runnable = mock(Runnable.class);
            Function<Object, Object> function = mock(Function.class);
            given(function.apply(objectPassedIn)).willReturn(objectReturned);

            DependencyA impl = new DependencyAImpl(runnable, function);

            forDependency(DependencyA.class).use(impl);

            sut().doAStuff();
            BDDMockito.then(runnable).should(ReceivedOnce).run();

            Object actual = sut().passToDependencyA(objectPassedIn);
            BDDMockito.then(function).should(ReceivedOnce).apply(objectPassedIn);
            assertThat(actual).isSameAs(objectReturned);
        }
    }

    public static class WhenSystemHasGenericDependencies extends BaseUnitTesterWithSut<SystemWithGenericDependencies>
    {
        @Test
        public void should_be_able_to_override_generic_dependency()
        {
            DependencyA customA = mock(DependencyA.class);
            List<DependencyB> customBs = new ArrayList<>();

            forDependency(new TypeToken<DependencyA>() {}).use(customA);
            forDependency(new TypeToken<List<DependencyB>>() {}).use(customBs);

            then(sut().getA()).isSameAs(customA);
            then(sut().getBs()).isSameAs(customBs);
        }
    }

    public static class WhenSystemHasPrimitiveDependency extends BaseUnitTesterWithSut<SystemWithPrimitives>
    {
        public static class AndPrimitiveDependencyIsSupplied extends WhenSystemHasPrimitiveDependency
        {
            protected int integer = 12;

            @Before
            public void setup_context()
            {
                forDependency(int.class).use(integer);
            }

            @Test
            public void should_be_able_to_mock_and_execute_mockable_dependency()
            {
                Object input = new Object();
                Object expected = new Object();
                given(dependency(DependencyA.class).doSomething(input)).willReturn(expected);

                Object actual = sut().executeA(input);

                then(actual).isSameAs(expected);
            }

            @Test
            public void should_be_able_to_supply_value_for_non_mockable_dependency()
            {
                then(sut().getI()).isEqualTo(integer);
            }
        }

        public static class AndPrimitiveDependencyIsNotSupplied extends WhenSystemHasPrimitiveDependency
        {
            @Test
            public void should_be_able_to_mock_and_execute_mockable_dependency()
            {
                thenThrownBy(() -> createSut()).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    public static class WhenSystemHasMultipleDependenciesOfTheSameType extends BaseUnitTesterWithSut<SystemWithMultipleStringAndIntDependencies>
    {
        protected String str1 = "one";
        protected String str2 = "two";
        protected String str3 = "three";

        protected int int1 = 1;
        protected int int2 = 2;
        protected int int3 = 3;

        @Test
        public void should_thrown_exception_on_non_dependency()
        {
            thenThrownBy(() -> forDependencies(boolean.class).use(true, false)).isInstanceOf(UnsupportedOperationException.class);
        }

        public static class AndDependencyListIsLessThanCtor extends WhenSystemHasMultipleDependenciesOfTheSameType
        {
            @Test
            public void should_throw_exception_for_String()
            {
                thenThrownBy(() -> forDependencies(String.class).use(str1, str2)).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            public void should_throw_exception_for_int()
            {
                thenThrownBy(() -> forDependencies(int.class).use(int1, int2)).isInstanceOf(IllegalArgumentException.class);
            }
        }

        public static class AndDependencyListIsEqualToCtor extends WhenSystemHasMultipleDependenciesOfTheSameType
        {
            @Before
            public void setup_context()
            {
                forDependencies(String.class).use(str1, str2, str3);
                forDependencies(int.class).use(int1, int2, int3);
            }

            @Test
            public void should_be_able_to_supply_it_with_a_list_of_String_values()
            {
                then(sut().getStrings()).containsExactly(str1, str2, str3);
            }

            @Test
            public void should_be_able_to_supply_it_with_a_list_of_int_values()
            {
                then(sut().getInts()).containsExactly(int1, int2, int3);
            }

            @Test
            public void should_still_have_generated_mock_for_dependency()
            {
                Object input = new Object();
                Object expected = new Object();
                given(dependency(DependencyA.class).doSomething(input)).willReturn(expected);

                then(sut().executeA(input)).isSameAs(expected);
            }
        }

        public static class AndDependencyListIsGreaterThanCtor extends WhenSystemHasMultipleDependenciesOfTheSameType
        {
            @Test
            public void should_throw_exception_for_String()
            {
                thenThrownBy(() -> forDependencies(String.class).use(str1, str2, str3, "four")).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            public void should_throw_exception_for_int()
            {
                thenThrownBy(() -> forDependencies(int.class).use(int1, int2, int3, 4)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
