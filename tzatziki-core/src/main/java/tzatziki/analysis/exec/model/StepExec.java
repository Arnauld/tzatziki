package tzatziki.analysis.exec.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StepExec extends EmbeddingAndWriteContainer {

    public static Predicate<StepExec> statusSucess() {
        return new Predicate<StepExec>() {
            @Override
            public boolean apply(StepExec input) {
                return false;
            }
        };
    }

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
