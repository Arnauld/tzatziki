package tzatziki.analysis.exec.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
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
    private DataTable dataTable;

    public StepExec(String keyword, String name) {
        this.keyword = keyword;
        this.name = name;
    }

    public String keyword() {
        return keyword;
    }

    public String name() {
        return name;
    }

    public StepExec declareDocString(String docString) {
        this.docString = docString;
        return this;
    }

    public String docString() {
        return docString;
    }

    public boolean hasDocString() {
        return docString != null;
    }

    public StepExec declareTable(DataTable dataTable) {
        this.dataTable = dataTable;
        return this;
    }

    public DataTable table() {
        return dataTable;
    }

    public boolean hasTable() {
        return dataTable != null && !dataTable.isEmpty();
    }

    public ResultExec result() {
        return resultExec;
    }

    public StepExec declareResult(ResultExec resultExec) {
        if (this.resultExec != null)
            throw new IllegalStateException("Result already assigned");
        this.resultExec = resultExec;
        return this;
    }

    public StepExec declareMatch(MatchExec matchExec) {
        if (this.matchExec != null)
            throw new IllegalStateException("Match already assigned");
        this.matchExec = matchExec;
        return this;
    }

    public StepExec declareComments(List<String> comments) {
        this.comments.addAll(comments);
        return this;
    }

    public FluentIterable<String> comments() {
        return FluentIterable.from(comments);
    }

    public boolean isMatching() {
        return matchExec != null && !Strings.isNullOrEmpty(matchExec.getLocation());
    }

    @Override
    public String toString() {
        return "StepExec{" + keyword + "'" + name + "'}";
    }

    public StepExec recursiveCopy() {
        StepExec copy = new StepExec(keyword, name);
        copy.resultExec = resultExec.recursiveCopy();
        copy.matchExec = matchExec.recursiveCopy();
        copy.comments.addAll(comments);
        copy.docString = docString;
        copy.dataTable = dataTable; // clone?
        super.recursiveCopy(copy);
        return copy;
    }

    public static class Tok {
        public final String value;
        public final boolean param;

        public Tok(String value, boolean param) {
            this.value = value;
            this.param = param;
        }
    }

    public List<Tok> tokenizeBody() {
        String full = name();
        int lastIndex = 0;
        List<Tok> toks = Lists.newArrayList();
        for (MatchExec.Arg arg : matchExec.getArgs()) {
            if (arg.getOffset() > lastIndex) {
                toks.add(new Tok(full.substring(lastIndex, arg.getOffset()), false));
            }
            toks.add(new Tok(arg.getVal(), true));
            lastIndex = arg.getOffset() + arg.getVal().length();
        }
        if (lastIndex < full.length())
            toks.add(new Tok(full.substring(lastIndex), false));
        return toks;
    }
}
