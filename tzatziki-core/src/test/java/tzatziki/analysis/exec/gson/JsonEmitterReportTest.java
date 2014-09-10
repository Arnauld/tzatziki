package tzatziki.analysis.exec.gson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JsonEmitterReportTest {

    @Test
    public void embedding_triggered_in_step_should_appear_in_the_corresponding_json_step_node() throws IOException {
        Result result = new JUnitCore().run(Runner1.class);
        assertThat(result.getFailures()).isEmpty();

        File output = new File("target/report/jsonEmitterReport/exec.json");
        Reader reader = new InputStreamReader(new FileInputStream(output), "UTF8");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(reader, JsonNode.class);
        JsonNode whenNode = jsonNode.get("features").get(0).get("stepContainerList").get(0).get("steps").get(1);

        assertThat(whenNode.get("keyword").asText()).isEqualTo("When ");
        assertThat(whenNode.get("embeddedList")).isNotNull();
        assertThat(whenNode.get("embeddedList").size()).isEqualTo(1);

    }

    @RunWith(Cucumber.class)
    @CucumberOptions(format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/report/jsonEmitterReport")
    public static class Runner1 {
    }

    public static class Steps {
        private float[] values;
        private Scenario scenario;

        @Before
        public void referenceScenario(Scenario scenario) {
            this.scenario = scenario;
        }

        @Given("^a series of (\\d+) values named '([a-z]+)'$")
        public void a_series_of_values_named_xs(int nb, String varName) throws Throwable {
            values = new float[nb];
        }

        @When("^I apply the following formula:$")
        public void I_apply_the_following_formula(String formula) throws Throwable {
            scenario.embed(formula.getBytes(), "plain/text+java");
        }

        @Then("^the result should be ((?:greater|lower)(?: or equal)?) to the (highest|lowest) value of 'xs'$")
        public void result_comparaison(String comparaisonOperator, String selector) throws Throwable {
        }
    }
}
