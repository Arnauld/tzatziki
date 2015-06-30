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
import gutenberg.itext.model.SourceCode;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

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

        String pathname = "target/report/jsonEmitterReport/embedding-triggered/exec.json";
        JsonNode jsonNode = readJsonNode(pathname);
        JsonNode whenNode = jsonNode.get("features").get(0).get("stepContainerList").get(0).get("steps").get(1);

        assertThat(whenNode.get("keyword").asText()).isEqualTo("When ");
        assertThat(whenNode.get("embeddedList")).isNotNull();
        assertThat(whenNode.get("embeddedList").size()).isEqualTo(1);

    }

    private JsonNode readJsonNode(String pathname) throws IOException {
        File output = new File(pathname);
        Reader reader = new InputStreamReader(new FileInputStream(output), "UTF8");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reader, JsonNode.class);
    }

    @RunWith(Cucumber.class)
    @CucumberOptions(
            format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/report/jsonEmitterReport/embedding-triggered",
            features = "classpath:tzatziki/analysis/exec/gson/steps-w-embedding.feature"
    )
    public static class Runner1 {
    }

    @Test
    public void interleaved_comments_within_scenario_should_be_handled_correctly() throws IOException {
        Result result = new JUnitCore().run(Runner2.class);
        for (Failure failure : result.getFailures()) {
            failure.getException().printStackTrace();
        }
        assertThat(result.getFailures()).isEmpty();

        String pathname = "target/report/jsonEmitterReport/comments-description/exec.json";
        JsonNode jsonNode = readJsonNode(pathname);

        JsonNode whenNode = jsonNode.get("features").get(0).get("stepContainerList").get(0).get("steps").get(0).get("comments").get(0);
    }

    @RunWith(Cucumber.class)
    @CucumberOptions(
            format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/report/jsonEmitterReport/comments-description",
            features = "classpath:tzatziki/analysis/exec/gson/comments-n-description-interleaved.feature"
    )
    public static class Runner2 {
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
            scenario.embed(new SourceCode("java", formula).toBytes(), SourceCode.MIME_TYPE);
        }

        @Then("^the result should be ((?:greater|lower)(?: or equal)?) to the (highest|lowest) value of 'xs'$")
        public void result_comparaison(String comparaisonOperator, String selector) throws Throwable {
        }
    }
}
