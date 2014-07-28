package tzatziki.analysis.exec.gson;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;
import tzatziki.analysis.exec.model.StepContainer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JsonIO {

    public Gson createGson() {
        Gson delegate = new GsonBuilder().setPrettyPrinting().create();
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ScenarioExec.class, new ScenarioExecSerializer(delegate))
                .registerTypeAdapter(ScenarioOutlineExec.class, new ScenarioOutlineExecSerializer(delegate))
                .registerTypeAdapter(StepContainer.class, new StepContainerDeserializer(delegate))
                .create();
    }

    public List<FeatureExec> load(InputStream in) throws UnsupportedEncodingException {
        Features features = createGson().fromJson(new InputStreamReader(in, "UTF8"), Features.class);
        return features.features;
    }

    public static class Features {
        private List<FeatureExec> features = Lists.newArrayList();
    }
}
