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
public class TagFilter implements Predicate<Tags> {

    public TagFilter compile(String... tagExprs) {
        return new TagFilter(Tags.from(tagExprs));
    }

    private TagExpression tagExpression;

    public TagFilter(Tags tags) {
        this.tagExpression = new TagExpression(tags.toList());
    }

    @Override
    public boolean apply(Tags input) {
        return tagExpression.evaluate(toGherkinTags(input.toList()));
    }

    private Collection<Tag> toGherkinTags(List<String> strings) {
        List<Tag> tags = Lists.newArrayList();
        for (String str : strings) {
            tags.add(new Tag(str, -1));
        }
        return tags;
    }
}
