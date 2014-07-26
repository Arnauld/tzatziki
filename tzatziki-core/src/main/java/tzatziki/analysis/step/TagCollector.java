package tzatziki.analysis.step;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagCollector extends FeatureVisitorAdapter {

    private Map<String, Stats> allTags = Maps.newConcurrentMap();
    private Feature feature;

    @Override
    public void enterFeature(Feature feature) {
        this.feature = feature;
        for(String tag : feature.getTags()) {
            Stats stats = statsFor(tag);
            stats.featureTagged++;
        }
    }

    @Override
    public void enterScenario(Scenario scenario) {
        for(String tag : scenario.getTags()) {
            Stats stats = statsFor(tag);
            stats.scenarioTagged++;
        }
        for(String tag : feature.getTags()) {
            Stats stats = statsFor(tag);
            stats.scenarioTaggedByInheritance++;
        }
    }

    @Override
    public void enterScenarioOutline(ScenarioOutline scenario) {
        for(String tag : scenario.getTags()) {
            Stats stats = statsFor(tag);
            stats.scenarioOutlineTagged++;
        }
        for(String tag : feature.getTags()) {
            Stats stats = statsFor(tag);
            stats.scenarioOutlineTaggedByInheritance++;
        }
    }

    private Stats statsFor(String tag) {
        Stats stats = allTags.get(tag);
        if(stats == null) {
            stats = new Stats();
            allTags.put(tag, stats);
        }
        return stats;
    }

    public static class Stats {
        public int featureTagged = 0;
        public int scenarioTagged = 0;
        public int scenarioTaggedByInheritance = 0;
        public int scenarioOutlineTagged = 0;
        public int scenarioOutlineTaggedByInheritance = 0;
    }
}
