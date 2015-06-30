package tzatziki.analysis.exec;

import com.google.common.base.Optional;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import tzatziki.TestSettings;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;
import tzatziki.analysis.exec.tag.TagFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExecutionFilterITest {
    @Test
    public void should_filter_outline() throws IOException {
        Result result = new JUnitCore().run(RunnerOutline.class);
        assertNoFailures(result);

        List<FeatureExec> featureExecs = readFeatureExecs("target/report/feature-w-outline/exec.json");

        List<FeatureExec> featureSelected = new ArrayList<FeatureExec>();
        ExecutionFilter filter = new ExecutionFilter(TagFilter.from("@wip", "@activated"));
        for (FeatureExec featureExec : featureExecs) {
            Optional<FeatureExec> optional = filter.filter(featureExec);
            if (optional.isPresent())
                featureSelected.add(optional.get());
        }

        assertThat(featureSelected).hasSize(1);

        FeatureExec featureExec = featureSelected.get(0);
        assertThat(featureExec.background()).isNull();
        assertThat(featureExec.lastOutline()).isNotEqualTo(Optional.absent());
        assertThat(featureExec.stepContainers()).hasSize(3);
        assertThat(featureExec.stepContainers().get(0)).isInstanceOf(ScenarioOutlineExec.class);
        assertThat(featureExec.stepContainers().get(1)).isInstanceOf(ScenarioExec.class);
        assertThat(featureExec.stepContainers().get(2)).isInstanceOf(ScenarioExec.class);
    }

    @RunWith(Cucumber.class)
    @CucumberOptions(
            format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/report/feature-w-outline",
            features = "classpath:tzatziki/analysis/exec/feature/feature-w-outline.feature"
    )
    public static class RunnerOutline {
    }

    private static void assertNoFailures(Result result) {
        for (Failure failure : result.getFailures()) {
            failure.getException().printStackTrace();
        }
        assertThat(result.getFailures()).isEmpty();
    }

    private static List<FeatureExec> readFeatureExecs(String pathname) throws IOException {
        TestSettings settings = new TestSettings();
        File input = new File(settings.getBaseDir(), pathname);
        FileInputStream in = new FileInputStream(input);
        try {
            return new JsonIO().load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
