package tzatziki.pdf.support;

import org.junit.Before;
import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.support.TagView;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.tag.TagDictionaryLoader;
import tzatziki.pdf.TestSettings;
import tzatziki.pdf.model.Markdown;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static tzatziki.pdf.support.DefaultPdfReportBuilder.Overview.FeatureSummary;
import static tzatziki.pdf.support.DefaultPdfReportBuilder.Overview.TagViews;

public class DefaultPdfReportBuilderTest {
    private TestSettings settings;

    @Before
    public void setUp() {
        settings = new TestSettings();
    }

    @Test
    public void usecase_coffeeMachine() throws Exception {
        File fileOut = new File(settings.getBuildDir(), "usecase_coffeeMachine.pdf");

        List<FeatureExec> features = loadSample("/tzatziki/pdf/coffeemachine-exec.json");
        new DefaultPdfReportBuilder()
                .using(new Configuration()
                                .displayFeatureTags(false)
                                .displayScenarioTags(false)
                )
                .title("Coffee machine")
                .subTitle("Technical & Functional specifications")
                .markup(Markdown.fromUTF8Resource("/tzatziki/pdf/00-preambule.md"))
                .overview(FeatureSummary, TagViews)
                .features(features)
                .tagDictionary(new TagDictionaryLoader().fromUTF8PropertiesResource("/tzatziki/pdf/tags.properties"))
                .tagViews(
                        new TagView("Payment (non wip)", TagFilter.from("~@wip", "@payment")),
                        new TagView("Non wip about tea", TagFilter.from("~@wip", "@tea"))
                )
                .sampleSteps()
                .generate(fileOut);
    }

    private List<FeatureExec> loadSample(String resourcePath) throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream(resourcePath);
        return new JsonIO().load(in);
    }
}