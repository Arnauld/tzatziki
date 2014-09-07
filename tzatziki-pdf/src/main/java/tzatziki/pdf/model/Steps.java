package tzatziki.pdf.model;

import tzatziki.analysis.exec.model.StepExec;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Steps {
    private final Iterable<? extends StepExec> steps;

    public Steps(Iterable<? extends StepExec> steps) {
        this.steps = steps;
    }

    public Iterable<? extends StepExec> steps() {
        return steps;
    }
}
