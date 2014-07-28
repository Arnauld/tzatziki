package samples.coffeemachine;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TakeOrderSteps {

    private Context context;

    public TakeOrderSteps(Context context) {
        this.context = context;
    }

    @Given("^the following orders:$")
    public void the_following_orders(DataTable arg1) throws Throwable {
        throw new PendingException();
    }

    @When("^I order an? \"([^\"]*)\"$")
    public void I_order_an(String drink) throws Throwable {
        context.getGateway().order(drink, 0, false);
    }

    @When("^I order an? \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_a_with_sugar(String drink, int nbSugar) throws Throwable {
        context.getGateway().order(drink, nbSugar, false);
    }

    @When("^I order an? extra hot \"([^\"]*)\" with (\\d+) sugar$")
    public void I_order_an_extra_hot_with_sugar(String arg1, int arg2) throws Throwable {
        Assertions.fail("Extra hot is forbidden in summer!");
    }

    @When("^the message \"([^\"]*)\" is sent$")
    public void the_message_is_sent(String message) throws Throwable {
        context.getGateway().publish(message);
    }

    @Then("^the instruction generated should be \"([^\"]*)\"$")
    public void the_instruction_generated_should_be(String command) throws Throwable {
        verify(context.getDrinkMaker()).executeCommand(eq(command));
    }

}
