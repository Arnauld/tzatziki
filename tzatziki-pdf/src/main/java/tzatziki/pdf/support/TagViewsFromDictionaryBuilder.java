package tzatziki.pdf.support;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.lang3.StringUtils;
import tzatziki.analysis.exec.support.TagView;
import tzatziki.analysis.exec.support.TagViews;
import tzatziki.analysis.tag.Tag;
import tzatziki.analysis.tag.TagDictionary;

import java.util.Comparator;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagViewsFromDictionaryBuilder {

    public static Function<Tag, String> DefaultTagViewLabelRenderer = new Function<Tag, String>() {
        @Override
        public String apply(Tag tag) {
            String description = tag.getDescription();
            if (StringUtils.isBlank(description))
                return tag.getTag();
            else
                return tag.getTag() + " - " + description;
        }
    };


    private Predicate<Tag> tagFilter = Predicates.alwaysTrue();
    private Function<Tag, String> tagRenderer = DefaultTagViewLabelRenderer;
    private Comparator<TagView> sorter = TagViews.TagViewPerLabelSorter;
    private boolean displayEvenIfEmpty = true;

    /**
     * If no scenario is marked by this tag, when <code>hideWhenEmpty</code> is <code>true</code>
     * then the tag is not displayed in the summary, otherwise the tag is visible indicating that
     * it is not used.
     *
     * @return this for method chaining
     * @see #displayEvenIfEmpty()
     */
    public TagViewsFromDictionaryBuilder hideWhenEmpty() {
        this.displayEvenIfEmpty = true;
        return this;
    }

    /**
     * @return this for method chaining
     * @see #hideWhenEmpty()
     */
    public TagViewsFromDictionaryBuilder displayEvenIfEmpty() {
        this.displayEvenIfEmpty = false;
        return this;
    }

    /**
     * indicate how to format the tag label within the summary e.g. "@wip - Work in progress"
     *
     * @return this for method chaining
     * @see #DefaultTagViewLabelRenderer
     */
    public TagViewsFromDictionaryBuilder tagRenderer(Function<Tag, String> tagRenderer) {
        this.tagRenderer = tagRenderer;
        return this;
    }

    /**
     * default implementation rely on {@link #tagRenderer}
     *
     * @param tag
     * @return the label for the specified tag
     * @see #tagRenderer(com.google.common.base.Function)
     * @see #DefaultTagViewLabelRenderer
     */
    protected String format(Tag tag) {
        return tagRenderer.apply(tag);
    }


    /**
     * indicate whether or not a Tag should be displayed.
     *
     * @return this for method chaining
     */
    public TagViewsFromDictionaryBuilder tagFilter(Predicate<Tag> tagFilter) {
        this.tagFilter = tagFilter;
        return this;
    }

    /**
     * default implementation rely on {@link #tagFilter}
     *
     * @param tag
     * @return <code>true</code> if the tag should be displayed, <code>false</code> otherwise
     * @see #tagFilter(com.google.common.base.Predicate)
     */
    protected boolean filter(Tag tag) {
        return tagFilter.apply(tag);
    }

    protected Optional<TagView> createTagView(Tag tag) {
        if (filter(tag)) {
            return Optional.of(new TagView(format(tag), tag.getTag()));
        }
        return Optional.absent();
    }

    /**
     * provide a way to sort tag view within the summary, this is usually done through the <code>TagView</code>
     * summary.
     *
     * @return this for method chaining
     */
    public TagViewsFromDictionaryBuilder sorter(Comparator<TagView> sorter) {
        this.sorter = sorter;
        return this;
    }

    public Optional<TagViews> build(TagDictionary dictionary) {
        TagViews tagViews = new TagViews(displayEvenIfEmpty);
        for (Tag tag : dictionary.tags()) {
            Optional<TagView> tagViewOpt = createTagView(tag);
            if (tagViewOpt.isPresent())
                tagViews.addAll(tagViewOpt.get());
        }
        tagViews.sortViews(sorter);
        return Optional.of(tagViews);
    }
}
