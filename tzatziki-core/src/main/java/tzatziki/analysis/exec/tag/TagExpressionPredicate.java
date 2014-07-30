package tzatziki.analysis.exec.tag;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import gherkin.TagExpression;
import gherkin.formatter.model.Tag;

import java.util.Collection;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagExpressionPredicate implements Predicate<Tags> {
    private final TagExpression tagExpression;

    public TagExpressionPredicate(List<String> tagExprs) {
        this.tagExpression = new TagExpression(tagExprs);
    }

    @Override
    public boolean apply(Tags input) {
        return tagExpression.evaluate(toGherkinTags(input.toList()));
    }

    private static Collection<Tag> toGherkinTags(List<String> strings) {
        List<Tag> tags = Lists.newArrayList();
        for (String str : strings) {
            tags.add(new Tag(str, -1));
        }
        return tags;
    }
}
