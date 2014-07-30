package tzatziki.analysis.exec.model;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.exec.tag.Tags;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureExec {
    private final String uri;
    private final String keyword;
    private final String name;
    private String description;
    private BackgroundExec backgroundExec;
    private List<StepContainer> stepContainerList = Lists.newArrayList();
    private List<String> tags = Lists.newArrayList();
    private List<String> comments = Lists.newArrayList();
    //


    public FeatureExec(String uri, String keyword, String name) {
        this.uri = uri;
        this.keyword = keyword;
        this.name = name;
    }

    public FeatureExec recursiveCopy() {
        return recursiveCopy(TagFilter.AcceptAll);
    }

    public FeatureExec recursiveCopy(Predicate<Tags> acceptedContent) {
        FeatureExec copy = new FeatureExec(uri, keyword, name)
                .declareDescription(description)
                .declareTags(tags)
                .declareComments(comments);
        if (backgroundExec != null)
            copy.background(backgroundExec.recursiveCopy());

        FluentIterable.from(stepContainerList).forEach(copyScenarioTo(copy, acceptedContent));
        return copy;
    }

    public String uri() {
        return uri;
    }

    public String name() {
        return name;
    }

    public FeatureExec background(BackgroundExec backgroundExec) {
        this.backgroundExec = backgroundExec;
        return this;
    }

    public BackgroundExec background() {
        return backgroundExec;
    }

    public FeatureExec declareScenario(ScenarioExec scenarioExec) {
        stepContainerList.add(scenarioExec);
        return this;
    }

    public FeatureExec declareScenarioOutline(ScenarioOutlineExec scenarioOutlineExec) {
        stepContainerList.add(scenarioOutlineExec);
        return this;
    }

    public FeatureExec declareTags(List<String> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public FeatureExec declareComments(List<String> comments) {
        this.comments.addAll(comments);
        return this;
    }

    public FeatureExec declareDescription(String description) {
        this.description = description;
        return this;
    }

    public FluentIterable<String> tags() {
        return FluentIterable.from(tags);
    }

    public FluentIterable<ScenarioExec> scenario() {
        return FluentIterable.from(stepContainerList).filter(ScenarioExec.class);
    }

    private static Consumer<? super StepContainer> copyScenarioTo(final FeatureExec featureExec,
                                                                  final Predicate<Tags> matching) {
        return new Consumer<StepContainer>() {
            @Override
            public void accept(StepContainer stepContainer) {
                if (!matching.apply(Tags.from(stepContainer.tags().toList())))
                    return;

                if (stepContainer instanceof ScenarioExec) {
                    featureExec.declareScenario(((ScenarioExec) stepContainer).recursiveCopy());
                }
                if (stepContainer instanceof ScenarioOutlineExec) {
                    featureExec.declareScenarioOutline(((ScenarioOutlineExec) stepContainer).recursiveCopy());
                }
            }
        };
    }
}
