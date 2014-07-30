package tzatziki.analysis.exec;

import com.google.common.base.Optional;
import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.tag.TagFilter;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
            }
        }

        assertThat(scenarioTotal).isEqualTo(22);
        assertThat(scenarioAccepted).isEqualTo(8);
    }

    private List<FeatureExec> loadSample() throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream("/tzatziki/analysis/exec/tag/coffeemachine-exec.json");
        return new JsonIO().load(in);
    }
}