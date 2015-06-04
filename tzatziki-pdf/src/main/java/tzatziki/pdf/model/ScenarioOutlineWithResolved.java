package tzatziki.pdf.model;

import com.google.common.collect.FluentIterable;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioOutlineWithResolved {
    private final ScenarioOutlineExec outlineExec;
    private final List<ScenarioExec> resolvedExec = new ArrayList<ScenarioExec>();

    public ScenarioOutlineWithResolved(ScenarioOutlineExec outlineExec) {
        this.outlineExec = outlineExec;
    }

    public void declareScenario(ScenarioExec scenario) {
        resolvedExec.add(scenario);
    }

    public ScenarioOutlineExec outline() {
        return outlineExec;
    }

    public FluentIterable<ScenarioExec> resolved() {
        return FluentIterable.from(resolvedExec);
    }
}
