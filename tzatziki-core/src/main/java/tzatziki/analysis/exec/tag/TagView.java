package tzatziki.analysis.exec.tag;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioRef;
import tzatziki.analysis.exec.model.StepExec;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static tzatziki.analysis.exec.model.StepExec.*;

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

        if (scenarioExec.isSucess()) {
            scenarioPassed.add(scenarioRef);
        } else {
            FluentIterable<StepExec> steps = scenarioExec.steps();
            Optional<StepExec> firstMatch = steps.firstMatch(Predicates.not(statusPassed));
            if (!firstMatch.isPresent()) {
                return;
            }
            StepExec step = firstMatch.get();
            if (statusFailed.apply(step)) {
                scenarioFailed.add(scenarioRef);
            } else if (statusPending.apply(step)) {
                scenarioPending.add(scenarioRef);
            } else if (statusSkipped.apply(step)) {
                scenarioSkipped.add(scenarioRef);
            } else if (statusUndefined.apply(step)) {
                scenarioUndefined.add(scenarioRef);
            }
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
