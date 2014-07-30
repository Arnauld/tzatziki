package tzatziki.analysis.exec.tag;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagFilterTest {


    @Test
    public void simple_tag_filtering() {
        TagFilter tagFilter = TagFilter.from("~@wip", "~@default", "@specs");

        assertThat(tagFilter.apply(Tags.from("@wip"))).isFalse();
        assertThat(tagFilter.apply(Tags.from("@wip", "@option"))).isFalse();
        assertThat(tagFilter.apply(Tags.from("@defaults"))).isFalse();
        assertThat(tagFilter.apply(Tags.from("@defaults", "@option"))).isFalse();
        assertThat(tagFilter.apply(Tags.from("@defaults", "@specs"))).isTrue();
    }
}