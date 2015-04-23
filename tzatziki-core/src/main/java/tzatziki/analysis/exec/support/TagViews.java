package tzatziki.analysis.exec.support;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import tzatziki.analysis.exec.model.FeatureExec;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagViews implements Iterable<TagView> {
    public static final Predicate<TagView> FilterWhenEmpty = new Predicate<TagView>() {
        @Override
        public boolean apply(TagView input) {
            return input.nbTotal() > 0;
        }
    };
    public static final Predicate<TagView> AcceptAll = new Predicate<TagView>() {
        @Override
        public boolean apply(TagView input) {
            return true;
        }
    };
    public static final Comparator<TagView> TagViewPerLabelSorter = new Comparator<TagView>() {
        @Override
        public int compare(TagView o1, TagView o2) {
            return ObjectUtils.compare(o1.description(), o2.description());
        }
    };


    private final boolean displayEvenIfEmpty;
    private List<TagView> tagViews;
    private Comparator<TagView> sorter = null;

    public TagViews() {
        this(false);
    }

    public TagViews(boolean displayEvenIfEmpty) {
        this.displayEvenIfEmpty = displayEvenIfEmpty;
        this.tagViews = Lists.newArrayList();
    }

    public TagViews addAll(Collection<? extends TagView> views) {
        tagViews.addAll(views);
        return this;
    }

    public TagViews addAll(TagView... views) {
        Collections.addAll(tagViews, views);
        return this;
    }

    @Override
    public Iterator<TagView> iterator() {
        FluentIterable<TagView> iterable = FluentIterable
                .from(tagViews)
                .filter(displayEvenIfEmpty ? AcceptAll : FilterWhenEmpty);

        if (sorter != null)
            return iterable.toSortedList(sorter).iterator();
        else
            return iterable.iterator();
    }

    public void consolidateView(FeatureExec featureExec) {
        for (TagView tagView : tagViews) {
            tagView.consolidateView(featureExec);
        }
    }

    public void clear() {
        for (TagView tagView : tagViews) {
            tagView.clear();
        }
    }

    public void sortViews(Comparator<TagView> sorter) {
        this.sorter = sorter;
    }
}