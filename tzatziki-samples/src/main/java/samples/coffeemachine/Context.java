package samples.coffeemachine;

import org.mockito.Mockito;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Context {

    private DrinkMaker drinkMaker;
    private Gateway gateway;

    public Context() {
        drinkMaker = Mockito.mock(DrinkMaker.class);
    }

    public DrinkMaker getDrinkMaker() {
        return drinkMaker;
    }

    public Gateway getGateway() {
        if (gateway == null)
            gateway = new Gateway(drinkMaker);
        return gateway;
    }

}
