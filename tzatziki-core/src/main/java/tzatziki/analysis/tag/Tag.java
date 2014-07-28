package tzatziki.analysis.tag;


import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Tag {
    private final String tag;
    private String description;
    private final Set<UsedBy> usedBySet = Sets.newHashSet();

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public Tag declareUsedBy(UsedBy usedBy) {
        usedBySet.add(usedBy);
        return this;
    }

    public FluentIterable<UsedBy> usages() {
        return FluentIterable.from(usedBySet);
    }

    public Tag declareDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }
}
