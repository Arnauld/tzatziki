package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureExec {
    private final String uri;
    private final String keyword;
    private final String name;
    private BackgroundExec backgroundExec;
    private List<StepContainer> stepContainerList = Lists.newArrayList();
    private List<String> tags = Lists.newArrayList();
    private List<String> comments = Lists.newArrayList();
    private String description;

    public FeatureExec(String uri, String keyword, String name) {
        this.uri = uri;
        this.keyword = keyword;
        this.name = name;
    }

    public String uri() {
        return uri;
    }

    public String name() {
        return name;
    }

    public void background(BackgroundExec backgroundExec) {
        this.backgroundExec = backgroundExec;
    }

    public BackgroundExec background() {
        return backgroundExec;
    }

    public void declareScenario(ScenarioExec scenarioExec) {
        stepContainerList.add(scenarioExec);
    }

    public void declareScenarioOutline(ScenarioOutlineExec scenarioOutlineExec) {
        stepContainerList.add(scenarioOutlineExec);
    }

    public void declareTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public void declareComments(List<String> comments) {
        this.comments.addAll(comments);
    }

    public void declareDescription(String description) {
        this.description = description;
    }

    public FluentIterable<String> tags() {
        return FluentIterable.from(tags);
    }

    public FluentIterable<ScenarioExec> scenario() {
        return FluentIterable.from(stepContainerList).filter(ScenarioExec.class);
    }
}
