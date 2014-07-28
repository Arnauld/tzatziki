package tzatziki.analysis.exec;

import com.google.gson.GsonBuilder;
import org.junit.Test;
import tzatziki.analysis.exec.model.FeatureExec;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonLoaderTest {

    @Test
    public void usecase() throws Exception {
        InputStream in = getClass().getResourceAsStream("/tzatziki/analysis/exec/two-features.json");
        List<FeatureExec> featureExecs = new JsonLoader().load(in);
        assertThat(featureExecs).hasSize(2);
        System.out.println("JsonLoaderTest.usecase(" +
                new GsonBuilder().setPrettyPrinting().create().toJson(featureExecs));
    }
}