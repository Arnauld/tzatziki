package tzatziki.analysis.exec.gson;

import com.google.gson.Gson;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import tzatziki.analysis.exec.model.FeatureExec;

import java.io.InputStream;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class JsonIOTest {

    private JsonIO jsonIO;
    private Gson gson;

    @Before
    public void setUp() {
        jsonIO = new JsonIO();
        gson = jsonIO.createGson();
    }

    @Test
    @Parameters({
            "/tzatziki/analysis/exec/two-features.json",
            "/samples/coffeemachine/results/exec-all.json"})
    public void readAndWrite_should_lead_to_identity(String resource) throws Exception {
        InputStream in = getClass().getResourceAsStream(resource);
        String origin = IOUtils.toString(in, "UTF8");

        in = getClass().getResourceAsStream(resource);
        List<FeatureExec> featureExecs = jsonIO.load(in);

        String json = toJson(gson, featureExecs).toString();
        JSONAssert.assertEquals(origin, json, false);
    }

    private static StringBuilder toJson(Gson gson, List<FeatureExec> featureExecs) {
        StringBuilder json = new StringBuilder();
        json.append("{\"features\": [\n");
        for (int i = 0; i < featureExecs.size(); i++) {
            if (i > 0)
                json.append(",\n");
            FeatureExec f = featureExecs.get(i);
            json.append(gson.toJson(f));
        }
        json.append("]\n}");
        return json;
    }
}