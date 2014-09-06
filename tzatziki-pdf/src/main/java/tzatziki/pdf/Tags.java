package tzatziki.pdf;

import com.google.common.collect.FluentIterable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Tags {
    private final FluentIterable<String> tags;

    public Tags(FluentIterable<String> tags) {
        this.tags = tags;
    }

    public FluentIterable<String> tags() {
        return tags;
    }
}
