package tzatziki.features.sample;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        format = "tzatziki.analysis.exec.JsonEmitterReport:target/report/sample"
)
public class RunCucumberSampleTest {
}
