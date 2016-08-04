package tzatziki.analysis.exec.model;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import tzatziki.analysis.exec.tag.Tags;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static tzatziki.analysis.exec.model.StepExec.statusPassed;

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

    public String name() {
        return name;
    }

    public boolean isSucess() {
        return steps().allMatch(statusPassed);
    }

    public void declareExamples(ExamplesExec examplesExec) {
        examplesList.add(examplesExec);
    }

    public FluentIterable<ExamplesExec> examples() {
        return FluentIterable.from(examplesList);
    }

    private static int HEADER_SZ = 1;

    public FluentIterable<ExamplesExec> exampleRows() {
        return FluentIterable.from(examplesList).skip(HEADER_SZ);
    }

    public ScenarioOutlineExec recursiveCopy() {
        return recursiveCopy(Predicates.<Tags>alwaysTrue()).get();
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

    public int exampleRowCount() {
        final AtomicInteger sum = new AtomicInteger();
        for (ExamplesExec examples : examples()) {
            sum.addAndGet(examples.rowCount() - HEADER_SZ);
        }
        return sum.get();
    }

    public Optional<ScenarioOutlineExec> recursiveCopy(final Predicate<Tags> matching) {

        ScenarioOutlineExec copy = new ScenarioOutlineExec(keyword, name);
        examples()
                .filter(new Predicate<ExamplesExec>() {
                    @Override
                    public boolean apply(ExamplesExec input) {
                        return matching.apply(combineTags(input));
                    }
                })
                .allMatch(copyExamplesTo(copy));

        if (copy.examples().isEmpty())
            return Optional.absent();

        super.recursiveCopy(copy);
        return Optional.of(copy);
    }

    private Tags combineTags(ExamplesExec input) {
        return Tags.from(input.tags().toList()).completeWith(tags().toList());
    }
}
