package tzatziki.exploratory.cucumber;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import gherkin.TagExpression;
import gherkin.formatter.model.Tag;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagExpressionTest {

    @Test
    public void sampleCase_AND() {
        TagExpression e = new TagExpression(asList("~@wip", "~@defaults"));
        assertThat(e.evaluate(tags("@bar"))).isTrue();
        assertThat(e.evaluate(tags("@specs"))).isTrue();
        assertThat(e.evaluate(tags("@specs", "@defaults"))).isFalse();
    }

    @Test
    public void sampleCase_OR() {
        TagExpression e = new TagExpression(asList("~@wip", "@option", "@modify,@insert"));
        assertThat(e.evaluate(tags("@wip", "@option", "@modify", "@otc"))).isFalse();
        assertThat(e.evaluate(tags("@specs", "@option", "@modify", "@otc"))).isTrue();
        assertThat(e.evaluate(tags("@acceptance", "@option", "@insert", "@modify", "@otc"))).isTrue();
    }

    @Test
    public void sampleCase_OR_withNegation() {
        TagExpression e = new TagExpression(asList("~@wip, ~@defaults"));
        assertThat(e.evaluate(tags("@bar"))).isTrue();
        assertThat(e.evaluate(tags("@specs"))).isTrue();
        assertThat(e.evaluate(tags("@specs", "@defaults"))).isTrue();
        assertThat(e.evaluate(tags("@wip", "@defaults"))).isFalse();
        assertThat(e.evaluate(tags("@wip"))).isTrue();
        assertThat(e.evaluate(tags("@defaults"))).isTrue();
    }

    private static List<Tag> tags(String... names) {
        return FluentIterable.from(asList(names)).transform(new Function<String, Tag>() {
            @Override
            public Tag apply(String input) {
                return tag(input);
            }
        }).toList();
    }

    private static Tag tag(String name) {
        return new Tag(name, 1);
    }

}
