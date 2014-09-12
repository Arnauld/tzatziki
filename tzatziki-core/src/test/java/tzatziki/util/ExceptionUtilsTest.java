package tzatziki.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionUtilsTest {

    @Test
    public void toString_should_return_null__when_null_is_provided() {
        assertThat(ExceptionUtils.toString(null)).isNull();
    }

    @Test
    public void toString_should_return_the_stacktrace() {
        Exception ex = new Exception("Erf");
        String str = ExceptionUtils.toString(ex);
        assertThat(str).startsWith("" +
                "java.lang.Exception: Erf\n" +
                "\tat tzatziki.util.ExceptionUtilsTest.toString_should_return_the_stacktrace(ExceptionUtilsTest.java:");
    }

}