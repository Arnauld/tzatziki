package tzatziki.analysis.exec.tag;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;

import java.util.Set;
import java.util.function.Consumer;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagView {
    private final String description;
    private final TagFilter tagFilter;
    //
    private int scenarioTagged = 0;
    private int scenarioPending = 0;
    private int scenarioInError = 0;
    private int scenarioInSucess = 0;

    public TagView(String description, TagFilter tagFilter) {
        this.description = description;
        this.tagFilter = tagFilter;
    }

    public String description() {
        return description;
    }

    public void consolidateView(FeatureExec featureExec) {
        FluentIterable<String> featureTags = featureExec.tags();

        // TODO: manage background and outline
        // Set<String> backgroundTags = featureExec.background().tags().toSet();

        featureExec.scenario().forEach(consolidateViewFromScenario(featureTags));
    }

    private void consolidateViewFromScenario(ScenarioExec scenarioExec, FluentIterable<String> inheritedTags) {
        Set<String> allTags = Sets.newHashSet();
        inheritedTags.copyInto(allTags);
        scenarioExec.tags().copyInto(allTags);

        Tags tags = Tags.from(allTags);
        if(!tagFilter.apply(tags)) {
            return;
        }

        scenarioTagged++;
    }

    private Consumer<? super ScenarioExec> consolidateViewFromScenario(
            final FluentIterable<String> inheritedTags) {
        return new Consumer<ScenarioExec>() {
            @Override
            public void accept(ScenarioExec scenarioExec) {
                consolidateViewFromScenario(scenarioExec, inheritedTags);
            }
        };
    }
}
