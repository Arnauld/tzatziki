package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StepContainer extends EmbeddingAndWriteContainer {

    private List<StepExec> steps = Lists.newArrayList();
    private List<String> tags = Lists.newArrayList();
    private List<String> comments = Lists.newArrayList();
    private String description;
    private LineRange lineRange;

    protected void recursiveCopy(final StepContainer copy) {
        steps().forEach(new Consumer<StepExec>() {
            @Override
            public void accept(StepExec stepExec) {
                copy.declareStep(stepExec.recursiveCopy());
            }
        });
        copy.declareTags(tags);
        copy.declareComments(comments);
        copy.declareDescription(description);
        copy.declareLineRange(lineRange);
        super.recursiveCopy(copy);
    }


    public void declareStep(StepExec stepExec) {
        if(stepExec==null)
            throw new IllegalArgumentException("Step cannot be null!");
        steps.add(stepExec);
    }

    public void declareTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public FluentIterable<String> tags() {
        return FluentIterable.from(tags);
    }

    public void declareComments(List<String> comments) {
        this.comments.addAll(comments);
    }

    public void declareDescription(String description) {
        this.description = description;
    }

    public FluentIterable<StepExec> steps() {
        return FluentIterable.from(steps);
    }

    public void declareLineRange(LineRange lineRange) {
        this.lineRange = lineRange;
    }

    public LineRange lineRange() {
        return lineRange;
    }

    public String description() {
        return description;
    }

    public FluentIterable<String> comments() {
        return FluentIterable.from(comments);
    }
}
