package tzatziki.analysis.step;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Scenario {
    private String visualName;
    private List<String> tags = Lists.newArrayList();
    private List<Step> stepList = Lists.newArrayList();

    public void setVisualName(String visualName) {
        this.visualName = visualName;
    }

    public String getVisualName() {
        return visualName;
    }

    public void add(Step step) {
        stepList.add(step);
    }

    public void traverse(FeatureVisitor visitor) {
        visitor.enterScenario(this);
        for (Step step : stepList)
            step.traverse(visitor);
        visitor.exitScenario(this);
    }

    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public List<String> getTags() {
        return tags;
    }
}
