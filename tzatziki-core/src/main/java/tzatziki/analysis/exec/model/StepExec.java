package tzatziki.analysis.exec.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import java.util.List;

import static tzatziki.analysis.exec.model.ResultExec.*;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StepExec extends EmbeddingAndWriteContainer {

    public static Function<StepExec, ResultExec> stepResultLens = new Function<StepExec, ResultExec>() {
        @Override
        public ResultExec apply(StepExec step) {
            return step.resultExec;
        }
    };

    public static Predicate<StepExec> statusPassed = Predicates.compose(resultPassed, stepResultLens);
    public static Predicate<StepExec> statusPending = Predicates.compose(resultPending, stepResultLens);
    public static Predicate<StepExec> statusFailed = Predicates.compose(resultFailed, stepResultLens);
    public static Predicate<StepExec> statusSkipped = Predicates.compose(resultSkipped, stepResultLens);
    public static Predicate<StepExec> statusUndefined = Predicates.compose(resultUndefined, stepResultLens);

    private final String keyword;
    private final String name;
    private ResultExec resultExec;
    private MatchExec matchExec;
    private List<String> comments = Lists.newArrayList();
    private String docString;

    public StepExec(String keyword, String name) {
        this.keyword = keyword;
        this.name = name;
    }

    public void declareResult(ResultExec resultExec) {
        if (this.resultExec != null)
            throw new IllegalStateException("Result already assigned");
        this.resultExec = resultExec;
    }

    public void declareMatch(MatchExec matchExec) {
        if (this.matchExec != null)
            throw new IllegalStateException("Match already assigned");
        this.matchExec = matchExec;
    }

    public void declareComments(List<String> comments) {
        this.comments.addAll(comments);
    }

    public void declareDocString(String docString) {
        this.docString = docString;
    }
}
