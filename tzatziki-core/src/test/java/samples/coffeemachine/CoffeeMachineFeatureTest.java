package samples.coffeemachine;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        format = "tzatziki.analysis.exec.JsonEmitterReport:target/samples/coffeemachine"
)
public class CoffeeMachineFeatureTest {
}
