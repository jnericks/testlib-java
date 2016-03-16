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
import com.jnericks.tests.testlib.TestObjects.SystemWithMultipleStringDependencies;
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
            SutFactory = new SystemUnderTestFactory<>(SystemForTest.class);
        }

        @Test
        public void should_be_able_to_create_sut()
        {
            SystemForTest sut = SutFactory.sut();

            assertThat(sut).isExactlyInstanceOf(SystemForTest.class);
        }

        @Test
        public void should_have_sut_be_a_singleton()
        {
            SystemForTest sut1 = SutFactory.sut();
            SystemForTest sut2 = SutFactory.sut();

            assertThat(sut1).isSameAs(sut2);
        }

        @Test
        public void should_have_dependency_a()
        {
            DependencyA a = SutFactory.dependency(DependencyA.class);

            assertThat(a).isInstanceOf(DependencyA.class);
        }

        @Test
        public void should_have_dependency_b()
        {
            DependencyB a = SutFactory.dependency(DependencyB.class);

            assertThat(a).isInstanceOf(DependencyB.class);
        }

        @Test
        public void should_be_able_to_do_a_stuff()
        {
            SutFactory.sut().doAStuff();

            BDDMockito.then(SutFactory.dependency(DependencyA.class)).should(ReceivedOnce).aStuff();
        }

        @Test
        public void should_be_able_to_do_b_stuff()
        {
            SutFactory.sut().doBStuff();

            BDDMockito.then(SutFactory.dependency(DependencyB.class)).should(ReceivedOnce).bStuff();
        }

        @Test
        public void should_be_able_to_stub_a_method()
        {
            Object objectPassedToSut = new Object();
            Object objectReturnedFromDependencyA = new Object();

            given(SutFactory.dependency(DependencyA.class).doSomething(objectPassedToSut))
                    .willReturn(objectReturnedFromDependencyA);

            Object actual = SutFactory.sut().passToDependencyA(objectPassedToSut);
            assertThat(actual).isSameAs(objectReturnedFromDependencyA);
        }

        @Test
        public void should_be_able_to_retrieve_injected_substitute()
        {
            DependencyA myDependencyA = mock(DependencyA.class);
            SutFactory.forDependency(DependencyA.class).use(myDependencyA);

            assertThat(SutFactory.dependency(DependencyA.class)).isSameAs(myDependencyA);
        }

        @Test
        public void should_be_able_to_assert_on_injected_substitute()
        {
            DependencyA myDependencyA = mock(DependencyA.class);
            SutFactory.forDependency(DependencyA.class).use(myDependencyA);
            SutFactory.sut().doAStuff();

            BDDMockito.then(myDependencyA).should(ReceivedOnce).aStuff();
        }

        @Test
        public void should_throw_exception_when_configuring_an_object_that_is_NOT_a_dependency()
        {
            assertThatThrownBy(() -> SutFactory.forDependency(NotADependency.class).use(mock(NotADependency.class)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_throw_exception_when_configuring_an_object_with_a_TypeToken_that_is_NOT_a_dependency()
        {
            assertThatThrownBy(() -> SutFactory.forDependency(new TypeToken<List<NotADependency>>() { }).use(new ArrayList<>()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_throw_exception_when_retrieving_object_that_is_NOT_a_dependency()
        {
            assertThatThrownBy(() -> SutFactory.dependency(NotADependency.class))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_throw_exception_when_trying_to_retrieve_sut_from_method()
        {
            assertThatThrownBy(() -> SutFactory.dependency(SystemForTest.class))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        public void should_be_able_to_override_internal_sut_factory()
        {
            SystemForTest system = mock(SystemForTest.class);
            SutFactory.createSutUsing(() -> system);
            assertThat(SutFactory.sut()).isSameAs(system);
        }

        @Test
        public void should_be_able_to_add_a_pre_processor()
        {
            Runnable runnable = mock(Runnable.class);

            SutFactory.beforeSutCreated(runnable);
            SutFactory.createSut();

            BDDMockito.then(runnable).should(ReceivedOnce).run();
        }

        @Test
        public void should_be_able_to_add_a_post_processor()
        {
            Consumer<SystemForTest> consumer = mock(Consumer.class);

            SutFactory.afterSutCreated(consumer);
            SutFactory.createSut();

            BDDMockito.then(consumer).should(ReceivedOnce).accept(SutFactory.sut());
        }

        @Test
        public void should_be_able_to_add_pre_and_post_processors_to_custom_sut_creation()
        {
            Runnable runnable = mock(Runnable.class);
            Consumer<SystemForTest> consumer = mock(Consumer.class);

            SutFactory.beforeSutCreated(runnable);
            SutFactory.afterSutCreated(consumer);

            SutFactory.createSutUsing(() -> new SystemForTest(null));
            SutFactory.createSut();

            BDDMockito.then(runnable).should(ReceivedOnce).run();
            BDDMockito.then(consumer).should(ReceivedOnce).accept(SutFactory.sut());
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

            SutFactory.forDependency(DependencyA.class).use(impl);

            SutFactory.sut().doAStuff();
            BDDMockito.then(runnable).should(ReceivedOnce).run();

            Object actual = SutFactory.sut().passToDependencyA(objectPassedIn);
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

            SutFactory.forDependency(new TypeToken<DependencyA>() { }).use(customA);
            SutFactory.forDependency(new TypeToken<List<DependencyB>>() { }).use(customBs);

            then(SutFactory.sut().getA()).isSameAs(customA);
            then(SutFactory.sut().getBs()).isSameAs(customBs);
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
                SutFactory.forDependency(int.class).use(integer);
            }

            @Test
            public void should_be_able_to_mock_and_execute_mockable_dependency()
            {
                Object input = new Object();
                Object expected = new Object();
                given(SutFactory.dependency(DependencyA.class).doSomething(input)).willReturn(expected);

                Object actual = SutFactory.sut().executeA(input);

                then(actual).isSameAs(expected);
            }

            @Test
            public void should_be_able_to_supply_value_for_non_mockable_dependency()
            {
                then(SutFactory.sut().getI()).isEqualTo(integer);
            }
        }

        public static class AndPrimitiveDependencyIsNotSupplied extends WhenSystemHasPrimitiveDependency
        {
            @Test
            public void should_be_able_to_mock_and_execute_mockable_dependency()
            {
                thenThrownBy(() -> SutFactory.createSut()).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    public static class WhenSystemHasMultipleDependenciesOfTheSameType extends BaseUnitTesterWithSut<SystemWithMultipleStringDependencies>
    {
        @Test
        public void should_be_able_to_supply_it_with_a_list_of_values()
        {
            SutFactory.forDependencies(String.class).use("string one", "string two", "string three");
        }
    }
}
