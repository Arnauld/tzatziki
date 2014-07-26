package tzatziki.analysis.exec.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioOutlineExec extends StepContainer {
    private final String keyword;
    private final String name;
    private List<ExamplesExec> examplesList = Lists.newArrayList();

    public ScenarioOutlineExec(String keyword, String name) {

        this.keyword = keyword;
        this.name = name;
    }

    public void declareExamples(ExamplesExec examplesExec) {
        examplesList.add(examplesExec);
    }
}
