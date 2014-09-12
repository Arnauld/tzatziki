package tzatziki.analysis.exec.tag;

import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.support.TagView;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TagViewTest {

    @Test
    public void usecase() throws Exception {
        List<FeatureExec> features = loadSample();

        TagView tagView = new TagView("Non wip about tea", TagFilter.from("~@wip", "@tea"));
        for (FeatureExec featureExec : features) {
            tagView.consolidateView(featureExec);
        }

        assertThat(tagView.scenarioMatching().size()).isEqualTo(11);
        assertThat(tagView.scenarioFailed().size()).isEqualTo(1);
        assertThat(tagView.scenarioPassed().size()).isEqualTo(1);
        assertThat(tagView.scenarioPending().size()).isEqualTo(1);
        assertThat(tagView.scenarioUndefined().size()).isEqualTo(8);
        assertThat(tagView.scenarioSkipped().size()).isEqualTo(0);
    }

    private List<FeatureExec> loadSample() throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream("/tzatziki/analysis/exec/tag/coffeemachine-exec.json");
        return new JsonIO().load(in);
    }
}