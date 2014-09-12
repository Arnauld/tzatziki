package tzatziki.analysis.exec.support;

import com.google.common.collect.Lists;
import tzatziki.analysis.exec.model.FeatureExec;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagViews implements Iterable<TagView> {
    private List<TagView> tagViews;

    public TagViews() {
        tagViews = Lists.newArrayList();
    }

    public TagViews addAll(Collection<? extends TagView> views) {
        tagViews.addAll(views);
        return this;
    }

    public TagViews addAll(TagView... views) {
        for (TagView view : views) {
            tagViews.add(view);
        }
        return this;
    }

    @Override
    public Iterator<TagView> iterator() {
        return tagViews.iterator();
    }

    public void consolidateView(FeatureExec featureExec) {
        for (TagView tagView : tagViews) {
            tagView.consolidateView(featureExec);
        }
    }
}