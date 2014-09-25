package tzatziki.analysis.exec.model;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
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

    public ScenarioOutlineExec recursiveCopy() {
        ScenarioOutlineExec copy = new ScenarioOutlineExec(keyword, name);
        FluentIterable.from(examplesList).allMatch(copyExamplesTo(copy));
        recursiveCopy(copy);
        return copy;
    }

    private static Predicate<? super ExamplesExec> copyExamplesTo(final ScenarioOutlineExec outlineExec) {
        return new Predicate<ExamplesExec>() {
            @Override
            public boolean apply(ExamplesExec examplesExec) {
                outlineExec.declareExamples(examplesExec.recursiveCopy());
                return true;
            }
        };
    }

}
