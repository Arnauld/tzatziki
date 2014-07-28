package tzatziki.analysis.tag;


/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Tag {
    private final String tag;
    private String description;

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }


    public Tag declareDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }
}
