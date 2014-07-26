package tzatziki.analysis.step;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Feature {

    private final String uri;
    private List<String> tags = Lists.newArrayList();
    private List<Scenario> scenarios = Lists.newArrayList();
    private List<ScenarioOutline> scenarioOutlines = Lists.newArrayList();

    public Feature(String uri) {
        this.uri = uri;
    }

    public void add(Scenario scenario) {
        scenarios.add(scenario);
    }

    public void add(ScenarioOutline scenarioOutline) {
        scenarioOutlines.add(scenarioOutline);
    }

    public void traverse(FeatureVisitor visitor) {
        visitor.enterFeature(this);
        for(Scenario scenario : scenarios)
            scenario.traverse(visitor);
        for(ScenarioOutline scenarioOutline : scenarioOutlines)
            scenarioOutline.traverse(visitor);
        visitor.exitFeature(this);
    }

    public String uri() {
        return uri;
    }

    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public List<String> getTags() {
        return tags;
    }
}
