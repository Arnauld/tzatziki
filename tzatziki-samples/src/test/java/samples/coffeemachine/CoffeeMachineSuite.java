package samples.coffeemachine;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import gutenberg.itext.model.Markdown;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import samples.TestSettings;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.support.TagView;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.java.AvailableStepsXls;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.java.GrammarParser;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.analysis.tag.TagDictionaryLoader;
import tzatziki.pdf.support.Configuration;
import tzatziki.pdf.support.DefaultPdfReportBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static tzatziki.pdf.support.DefaultPdfReportBuilder.Overview.FeatureSummary;
import static tzatziki.pdf.support.DefaultPdfReportBuilder.Overview.TagViews;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CoffeeMachineSuite.ExecuteFeatures.class,
        CoffeeMachineSuite.StaticAnalysis.class})
public class CoffeeMachineSuite {

    @RunWith(Cucumber.class)
    @CucumberOptions(
            format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/samples/coffeemachine/suite"
    )
    public static class ExecuteFeatures {
    }

    public static class StaticAnalysis {
        @Test
        public void generateXlsGrammar() {
            TestSettings settings = new TestSettings();
            String buildDir = settings.getBuildDir();
            String baseDir = settings.getBaseDir();

            File javaSourceTree = new File(baseDir, "src/main/java/samples/coffeemachine/");
            Grammar grammar = new GrammarParser()
                    //.usingListener(new ConsoleOutputListener())
                    .usingSourceDirectory(javaSourceTree)
                    .process();

            assertThat(grammar.hasEntries())
                    .describedAs("No entry found at " + javaSourceTree.getAbsolutePath())
                    .isTrue();

            File fileDst = new File(buildDir, "samples/coffeemachine/suite/grammar.xls");
            AvailableStepsXls xls = new AvailableStepsXls(fileDst);
            xls.emit(grammar);
            xls.close();
        }
    }

    @AfterClass
    public static void generateExecutionReport() throws Exception {
        List<FeatureExec> execs = loadExec(executionResultFile());
        TagDictionary tagDictionary = new TagDictionaryLoader().fromUTF8PropertiesResource("/samples/coffeemachine/tags.properties");

        File fileOut = new File(buildDir(), "samples/coffeemachine/suite/report.pdf");

        new DefaultPdfReportBuilder()
                .using(new Configuration()
                                .displayFeatureTags(false)
                                .displayScenarioTags(false)
                                .declareProperty("imageDir",
                                        new File(baseDir(), "/src/main/resources/samples/coffeemachine/images").toURI().toString())
                )
                .title("Coffee machine")
                .subTitle("Technical & Functional specifications")
                .markup(Markdown.fromUTF8Resource("/samples/coffeemachine/00-preambule.md"))
                .overview(FeatureSummary, TagViews)
                .features(execs)
                .tagDictionary(tagDictionary)
                .tagViewsFromDictionnary()
                .tagViews(
                        new TagView("Payment (non wip)", TagFilter.from("~@wip", "@payment")),
                        new TagView("Non wip about tea", TagFilter.from("~@wip", "@tea"))
                )
                .sampleSteps()
                .generate(fileOut);
    }

    private static File buildDir() {
        String baseDir = new TestSettings().getBuildDir();
        return new File(baseDir);
    }

    private static File baseDir() {
        String baseDir = new TestSettings().getBaseDir();
        return new File(baseDir);
    }

    private static File executionResultFile() {
        return new File(buildDir(), "samples/coffeemachine/suite/exec.json");
    }

    private static File featuresSourceTree() {
        return new File(baseDir(), "src/main/resources/samples/coffeemachine");
    }


    private static List<FeatureExec> loadExec(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return new JsonIO().load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
