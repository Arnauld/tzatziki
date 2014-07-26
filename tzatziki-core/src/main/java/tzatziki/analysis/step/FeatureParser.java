package tzatziki.analysis.step;

import com.google.common.collect.Lists;
import cucumber.runtime.FeatureBuilder;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import gherkin.formatter.Formatter;
import tzatziki.junit.ResourceLoaderWrapper;
import tzatziki.util.Filter;
import tzatziki.util.Filters;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureParser {

    private List<CucumberFeature> cucumberFeatures;
    private Formatter formatter;
    private String suffix = "";
    //
    private List<String> featurePaths = Lists.newArrayList();
    private List<Object> filters = Lists.newArrayList();
    private CucumberConverter converter = new CucumberConverter();

    public FeatureParser() {
        cucumberFeatures = Lists.newArrayList();
        formatter = new FeatureBuilder(cucumberFeatures);
    }

    public FeatureParser usingSourceDirectory(File sourceTree) {
        featurePaths.add(sourceTree.getAbsolutePath());
        return this;
    }

    public Features process() {
        ResourceLoader resourceLoader = createResourceLoader(getClass());

        Features features = new Features();
        for (CucumberFeature cucumberFeature : CucumberFeature.load(resourceLoader, featurePaths, filters)) {
            features.add(converter.convert(cucumberFeature));
        }

        return features;
    }

    protected ResourceLoader createResourceLoader(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        MultiLoader loader = new MultiLoader(classLoader);

        List<Filter<InputStream>> filters = instanciateFilters(clazz);
        if (filters.isEmpty())
            return loader;
        else
            return new ResourceLoaderWrapper(loader, Filters.chain(filters));
    }

    private List<Filter<InputStream>> instanciateFilters(Class<?> clazz) {
        return Lists.newArrayList();
    }


}
