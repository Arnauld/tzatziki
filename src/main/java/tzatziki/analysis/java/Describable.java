package tzatziki.analysis.java;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Describable {
    private String comment;

    public void describeWith(String comment) {
        this.comment = comment;
    }

    public String comment() {
        return comment;
    }
}
