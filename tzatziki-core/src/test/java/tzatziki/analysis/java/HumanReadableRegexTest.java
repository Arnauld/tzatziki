package tzatziki.analysis.java;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HumanReadableRegexTest {

    @Test
    public void should_discard_start_operator() {
        String pattern = "^I order something";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order something");
    }

    @Test
    public void should_discard_end_operator() {
        String pattern = "I order something$";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order something");
    }

    @Test
    public void should_surround_optional_character_with_parenthesis() {
        String pattern = "I order an? object";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order a(n) object");
    }

    @Test
    public void should_replace_any_matcher() {
        String pattern = "I order (.*)";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order <anything>");
    }

    @Test
    public void should_replace_digit_matcher() {
        String pattern = "I order (\\d+)";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order <integer>");
    }

    @Test
    public void should_replace_decimal_digit_matcher() {
        String pattern = "I order (\\d+|\\d*\\.\\d+)";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order <decimal>");
    }


    @Test
    public void should_replace_any_non_double_quote() {
        String pattern = "I order ([^\"]*)";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order <anything>");
    }

    @Test
    public void should_replace_anything_and_number_and_startAndEnd_operator() {
        String pattern = "^I order an? \"([^\"]*)\" with (\\d+) sugars?$";
        HumanReadableRegex regex = new HumanReadableRegex(pattern);
        assertThat(regex.humanReadable()).isEqualTo("I order a(n) \"<anything>\" with <integer> sugar(s)");
    }
}
