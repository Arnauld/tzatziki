package tzatziki.analysis.exec.tag;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

import static java.util.Arrays.asList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagFilter implements Predicate<Tags> {

    public static TagFilter AcceptAll = new TagFilter(Predicates.<Tags>alwaysTrue());

    public static TagFilter from(String... tagExprs) {
        return new TagFilter(new TagExpressionPredicate(asList(tagExprs)));
    }

    private final Predicate<Tags> delegate;

    private TagFilter(Predicate<Tags> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean apply(Tags tags) {
        return delegate.apply(tags);
    }

    public TagFilter and(TagFilter... tagFilters) {
        Iterable<Predicate<Tags>> ands = extractPredicates(tagFilters);
        return new TagFilter(Predicates.and(ands));
    }

    public TagFilter or(TagFilter... tagFilters) {
        Iterable<Predicate<Tags>> ors = extractPredicates(tagFilters);
        return new TagFilter(Predicates.or(ors));
    }

    private static FluentIterable<Predicate<Tags>> extractPredicates(TagFilter[] tagFilters) {
        return FluentIterable.from(asList(tagFilters)).transform(tagFilterPredicateLens);
    }

    private static final Function<TagFilter, Predicate<Tags>> tagFilterPredicateLens = new Function<TagFilter, Predicate<Tags>>() {
        @Override
        public Predicate<Tags> apply(TagFilter filter) {
            return filter.delegate;
        }
    };

}
