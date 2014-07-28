package samples.coffeemachine;

import cucumber.api.java.en.Given;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MoneySteps {


    @Given("^I've inserted (\\d+)€ in the machine$")
    public void I_ve_inserted_€_in_the_machine(BigDecimal amount) throws Throwable {
    }
}
