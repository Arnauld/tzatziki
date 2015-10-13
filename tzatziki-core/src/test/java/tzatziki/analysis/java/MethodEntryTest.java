package tzatziki.analysis.java;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodEntryTest {

    @Test
    public void should_return_patterns() {
        MethodEntry entry = new MethodEntry("bookADeal", Arrays.asList("java.lang.String"));
        entry.declarePattern("Given", "^a standard deal (.*) with no specifics");
        entry.declarePattern("Given", "^I have booked a standard deal (.*) with no specifics");

        assertThat(entry.patterns().toList()).containsExactly(
                "^a standard deal (.*) with no specifics",
                "^I have booked a standard deal (.*) with no specifics");
    }

}