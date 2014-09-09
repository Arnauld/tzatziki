package tzatziki.analysis.exec.tag;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioRef;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagView {
    private final String description;
    private final TagFilter tagFilter;
    //
    private List<ScenarioRef> scenarioMatching = Lists.newArrayList();
    private List<ScenarioRef> scenarioPassed = Lists.newArrayList();
    private List<ScenarioRef> scenarioPending = Lists.newArrayList();
    private List<ScenarioRef> scenarioFailed = Lists.newArrayList();
    private List<ScenarioRef> scenarioUndefined = Lists.newArrayList();
    private List<ScenarioRef> scenarioSkipped = Lists.newArrayList();

    public TagView(String description, TagFilter tagFilter) {
        this.description = description;
        this.tagFilter = tagFilter;
    }

    public String description() {
        return description;
    }

    public int nbPassed() {
        return scenarioPassed.size();
    }

    public int nbTotal() {
        return scenarioMatching.size();
    }

    public FluentIterable<ScenarioRef> scenarioMatching() {
        return FluentIterable.from(scenarioMatching);
    }

    public FluentIterable<ScenarioRef> scenarioPending() {
        return FluentIterable.from(scenarioPending);
    }

    public FluentIterable<ScenarioRef> scenarioFailed() {
        return FluentIterable.from(scenarioFailed);
    }

    public FluentIterable<ScenarioRef> scenarioPassed() {
        return FluentIterable.from(scenarioPassed);
    }

    public FluentIterable<ScenarioRef> scenarioSkipped() {
        return FluentIterable.from(scenarioSkipped);
    }

    public FluentIterable<ScenarioRef> scenarioUndefined() {
        return FluentIterable.from(scenarioUndefined);
    }

    public void consolidateView(FeatureExec featureExec) {
        FluentIterable<String> featureTags = featureExec.tags();

        // TODO: manage background and outline
        // Set<String> backgroundTags = featureExec.background().tags().toSet();

        featureExec.scenario().forEach(consolidateViewFromScenario(featureExec, featureTags));
    }

    private void consolidateViewFromScenario(FeatureExec featureExec,
                                             ScenarioExec scenarioExec,
                                             FluentIterable<String> inheritedTags) {
        Set<String> allTags = Sets.newHashSet();
        inheritedTags.copyInto(allTags);
        scenarioExec.tags().copyInto(allTags);

        Tags tags = Tags.from(allTags);
        if (!tagFilter.apply(tags)) {
            return;
        }

        ScenarioRef scenarioRef = createRef(featureExec, scenarioExec);
        scenarioMatching.add(scenarioRef);

        switch (scenarioExec.status()) {
            case Passed:
                scenarioPassed.add(scenarioRef);
                break;
            case Skipped:
                scenarioSkipped.add(scenarioRef);
                break;
            case Undefined:
                scenarioUndefined.add(scenarioRef);
                break;
            case Failed:
                scenarioFailed.add(scenarioRef);
                break;
            case Pending:
                scenarioPending.add(scenarioRef);
                break;
        }
    }

    private ScenarioRef createRef(FeatureExec featureExec, ScenarioExec scenarioExec) {
        return new ScenarioRef(featureExec.uri(), featureExec.name(), scenarioExec.lineRange(), scenarioExec.name());
    }

    private Consumer<? super ScenarioExec> consolidateViewFromScenario(
            final FeatureExec featureExec, final FluentIterable<String> inheritedTags) {
        return new Consumer<ScenarioExec>() {
            @Override
            public void accept(ScenarioExec scenarioExec) {
                consolidateViewFromScenario(featureExec, scenarioExec, inheritedTags);
            }
        };
    }
}
