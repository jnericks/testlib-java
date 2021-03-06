package com.jnericks.tests.testlib;

import com.jnericks.testlib.BaseUnitTesterWithSut;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class BaseUnitTesterWithSutTester extends BaseUnitTesterWithSut<BaseUnitTesterWithSutTester.TheClassToTest> {

    @Test
    public void should_be_able_to_mock_and_assert_from_outer_class() {
        int input = 22;
        String output = "hello";

        given(dependency(Dependency.class).toString(input)).willReturn(output);

        String actual = sut().execute(input);

        assertThat(actual).isEqualTo(output);
    }

    public interface Dependency {

        String toString(int i);
    }

    public static class InheritedInnerTester extends BaseUnitTesterWithSutTester {

        @Test
        public void should_be_able_to_mock_and_assert_from_inner_class() throws Exception {
            int input = 22;
            String output = "hello";

            given(dependency(Dependency.class).toString(input)).willReturn(output);

            String actual = sut().execute(input);

            assertThat(actual).isEqualTo(output);
        }

        public static class InheritedInheritedInnerTester extends InheritedInnerTester {

            @Test
            public void should_be_able_to_mock_and_assert_from_inner_class() throws Exception {
                int input = 22;
                String output = "hello";

                given(dependency(Dependency.class).toString(input)).willReturn(output);

                String actual = sut().execute(input);

                assertThat(actual).isEqualTo(output);
            }
        }
    }

    public static class NotInheritedInnerTester extends BaseUnitTesterWithSut<BaseUnitTesterWithSutTester.TheClassToTest> {

        @Test
        public void should_be_able_to_mock_and_assert_from_inner_class() throws Exception {
            int input = 22;
            String output = "hello";

            given(dependency(Dependency.class).toString(input)).willReturn(output);

            String actual = sut().execute(input);

            assertThat(actual).isEqualTo(output);
        }

        public static class NotInheritedNotInheritedInnerTester extends BaseUnitTesterWithSut<BaseUnitTesterWithSutTester.TheClassToTest> {

            @Test
            public void should_be_able_to_mock_and_assert_from_inner_class() throws Exception {
                int input = 22;
                String output = "hello";

                given(dependency(Dependency.class).toString(input)).willReturn(output);

                String actual = sut().execute(input);

                assertThat(actual).isEqualTo(output);
            }
        }
    }

    public class TheClassToTest {

        Dependency dependency;

        public TheClassToTest(Dependency dependency) {
            this.dependency = dependency;
        }

        public String execute(int i) {
            return dependency.toString(i);
        }
    }
}