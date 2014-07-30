package tzatziki.analysis.step;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Feature {

    private final String uri;
    private final String name;
    private List<String> tags = Lists.newArrayList();
    private Background background;
    private List<Scenario> scenarios = Lists.newArrayList();
    private List<ScenarioOutline> scenarioOutlines = Lists.newArrayList();

    public Feature(String uri, String name) {
        this.uri = uri;
        this.name = name;
    }

    public Background background() {
        return background;
    }

    public void background(Background background) {
        this.background = background;
    }

    public void add(Scenario scenario) {
        scenarios.add(scenario);
    }

    public void add(ScenarioOutline scenarioOutline) {
        scenarioOutlines.add(scenarioOutline);
    }

    public void traverse(FeatureVisitor visitor) {
        visitor.enterFeature(this);
        for (Scenario scenario : scenarios)
            scenario.traverse(visitor);
        for (ScenarioOutline scenarioOutline : scenarioOutlines)
            scenarioOutline.traverse(visitor);
        visitor.exitFeature(this);
    }

    public String uri() {
        return uri;
    }

    public String name() {
        return name;
    }

    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public List<String> getTags() {
        return tags;
    }

    public FluentIterable<Scenario> scenario() {
        return FluentIterable.from(scenarios);
    }

    public FluentIterable<ScenarioOutline> scenarioOutlines() {
        return FluentIterable.from(scenarioOutlines);
    }
}
