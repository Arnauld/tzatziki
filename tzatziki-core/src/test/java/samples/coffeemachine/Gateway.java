package samples.coffeemachine;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Gateway {

    private DrinkMaker drinkMaker;

    public Gateway(DrinkMaker drinkMaker) {
        this.drinkMaker = drinkMaker;
    }


    public void order(String drinkType, int nbSugar, boolean b) {
        StringBuilder c = new StringBuilder();

        if (drinkType.equalsIgnoreCase("Coffee"))
            c.append("C");
        else if (drinkType.equalsIgnoreCase("Tea"))
            c.append("T");
        else if (drinkType.equalsIgnoreCase("Chocolate"))
            c.append("H");
        else if (drinkType.equalsIgnoreCase("Orange Juice"))
            c.append("O");


        if (nbSugar > 0) {
            c.append(":").append(nbSugar).append(":0");
        } else
            c.append("::");

        drinkMaker.executeCommand(c.toString());
    }

    public void publish(String message) {
        if(message.contains("enough"))
            throw new IllegalArgumentException();
        drinkMaker.executeCommand("M:" + message);
    }
}
