package tzatziki.pdf.feature;

import com.itextpdf.text.DocumentException;
import gutenberg.itext.FontModifier;
import gutenberg.itext.Styles;
import gutenberg.itext.model.Markdown;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.pdf.TestSettings;
import tzatziki.pdf.support.Configuration;
import tzatziki.pdf.support.DefaultPdfReportBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({RunFeature.class})
public class RunFeatureTest {

    @AfterClass
    public static void generateReport() throws IOException, DocumentException {
        new PdfSimpleReport().generate();
    }


    public static class PdfSimpleReport {
        public void generate() throws IOException, DocumentException {
            List<FeatureExec> execs = loadExec(new File(buildDir(), "tz-pdf/exec.json"));

            File fileOut = new File(buildDir(), "tz-pdf/report.pdf");

            new DefaultPdfReportBuilder()
                    .using(new Configuration()
                                    .displayFeatureTags(true)
                                    .displayScenarioTags(true)
                                    .declareProperty("imageDir",
                                            new File(baseDir(), "/src/test/resources/myapp/feature/images").toURI().toString())
                                    .adjustFont(Styles.TABLE_HEADER_FONT, new FontModifier().size(10.0f))
                    )
                    .title("myapp")
                    .subTitle("Technical & Functional specifications")
                    .features(execs)
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
}
