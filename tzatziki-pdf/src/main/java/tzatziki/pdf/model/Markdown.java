package tzatziki.pdf.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Markdown {
    private final String raw;

    public Markdown(String raw) {
        this.raw = raw;
    }

    public String raw() {
        return raw;
    }
}
