package tzatziki.analysis.exec;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.analysis.exec.tag.TagFilter;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExecutionFilterTest {

    @Test
    public void filter_exec() throws Exception {
        List<FeatureExec> features = loadSample();

        TagFilter tagFilter = TagFilter.from("~@wip", "@chocolate");
        ExecutionFilter executionFilter = new ExecutionFilter(tagFilter);

        int scenarioTotal = 0;
        int scenarioAccepted = 0;
        for (FeatureExec featureExec : features) {
            scenarioTotal += featureExec.scenario().size();

            Optional<FeatureExec> filteredOpt = executionFilter.filter(featureExec);
            if (filteredOpt.isPresent()) {
                FeatureExec filtered = filteredOpt.get();
                scenarioAccepted += filtered.scenario().size();
                assertThatScenarioAreValid(filtered.scenario(), featureExec.scenario());
            }
        }

        assertThat(scenarioTotal).isEqualTo(22);
        assertThat(scenarioAccepted).isEqualTo(8);
    }

    private static void assertThatScenarioAreValid(FluentIterable<ScenarioExec> filtered, FluentIterable<ScenarioExec> ref) {
        for (ScenarioExec scenarioExec : filtered) {
            assertThat(ref.anyMatch(sameAs(scenarioExec))).isTrue();
        }
    }

    private static Predicate<? super ScenarioExec> sameAs(final ScenarioExec wanted) {
        return new Predicate<ScenarioExec>() {
            @Override
            public boolean apply(ScenarioExec input) {
                if (wanted.name().equals(input.name())) {
                    assertThat(areEqual(wanted.steps(), input.steps()))
                            .describedAs("Same scenario name but different steps... got:" + wanted.steps() + ", expected: " + input.steps())
                            .isTrue();
                    return true;
                }
                return false;
            }
        };
    }

    private static boolean areEqual(FluentIterable<StepExec> steps1, FluentIterable<StepExec> steps2) {
        assertThat(steps1).hasSameSizeAs(steps2);

        Iterator<StepExec> iterator1 = steps1.iterator();
        Iterator<StepExec> iterator2 = steps2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            StepExec stepExec1 = iterator1.next();
            StepExec stepExec2 = iterator2.next();
            assertThat(stepExec1.name()).isEqualTo(stepExec2.name());
            assertThat(stepExec1.result()).isEqualTo(stepExec2.result());
        }
        return iterator1.hasNext() == iterator2.hasNext();
    }

    private List<FeatureExec> loadSample() throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream("/tzatziki/analysis/exec/tag/coffeemachine-exec.json");
        return new JsonIO().load(in);
    }
}