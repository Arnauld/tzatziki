package tzatziki.pdf;

import org.junit.Before;
import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class PdfReportTest {

    private Configuration configuration;
    private TestSettings settings;

    @Before
    public void setUp() {
        configuration = new Configuration();
        settings = new TestSettings();
    }

    @Test
    public void usecase() throws Exception {
        File output = new File(settings.getBuildDir(), getClass().getSimpleName() + "_usecase.pdf");

        PdfReport report = new PdfReport(configuration);
        report.startReport(output);
        for (FeatureExec feature : loadSample()) {
            report.emit(feature);
        }
        report.endReport();

        System.out.println("PdfReportTest.usecase~~> " + output);
    }

    private List<FeatureExec> loadSample() throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream("/tzatziki/pdf/coffeemachine-exec.json");
        return new JsonIO().load(in);
    }
}