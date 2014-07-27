package tzatziki.analysis.exec;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import tzatziki.analysis.exec.model.FeatureExec;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JsonLoader {
    public List<FeatureExec> load(InputStream in) throws UnsupportedEncodingException {
        Features features = new Gson().fromJson(new InputStreamReader(in, "UTF8"), Features.class);
        return features.features;
    }

    public static class Features {
        private List<FeatureExec> features = Lists.newArrayList();
    }
}
