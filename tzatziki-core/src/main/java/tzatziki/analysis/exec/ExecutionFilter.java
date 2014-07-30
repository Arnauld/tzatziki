package tzatziki.analysis.exec;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.exec.tag.Tags;

import java.util.Set;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExecutionFilter {

    private final TagFilter tagFilter;

    public ExecutionFilter(TagFilter tagFilter) {
        this.tagFilter = tagFilter;
    }

    public Optional<FeatureExec> filter(FeatureExec feature) {
        FluentIterable<String> inheritedTags = feature.tags();
        FeatureExec copy = feature.recursiveCopy(matching(inheritedTags.toSet(), tagFilter));

        if (copy.scenario().isEmpty())
            return Optional.absent();
        else
            return Optional.of(copy);
    }

    private static Predicate<Tags> matching(final Set<String> inheritedTags, final TagFilter tagFilter) {
        return new Predicate<Tags>() {
            @Override
            public boolean apply(Tags tags) {
                return tagFilter.apply(tags.completeWith(inheritedTags));
            }
        };
    }
}
