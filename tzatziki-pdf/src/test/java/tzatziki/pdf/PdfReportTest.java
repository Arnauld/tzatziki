package tzatziki.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Section;
import gutenberg.itext.Sections;
import org.junit.Before;
import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ResultExec;
import tzatziki.analysis.exec.model.StepExec;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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
        emitPreamble(report);

        for (FeatureExec feature : loadSample()) {
            report.emit(feature);
        }
        report.endReport();

        System.out.println("PdfReportTest.usecase~~> " + output);
    }

    private void emitPreamble(PdfReport report) {
        report.emit(new PdfEmitter<Object>() {
            @Override
            public void emit(Object value, EmitterContext emitterContext) {
                Sections sections = emitterContext.sections();
                Section section = sections.newSection("Preamble", 1);
                try {
                    section.setNumberDepth(0);
                    //
                    List<StepExec> list = Arrays.asList(
                            new StepExec("Given", "a passed step").declareResult(new ResultExec("passed", null, null, 500L)),
                            new StepExec("And", "a failed step").declareResult(new ResultExec("failed", null, null, 230L)),
                            new StepExec("When", "a pending step").declareResult(new ResultExec("pending", null, null, null)),
                            new StepExec("But", "an undefined step").declareResult(new ResultExec("undefined", null, null, null)),
                            new StepExec("Then", "a skipped step").declareResult(new ResultExec("skipped", null, null, null))
                    );
                    emitterContext.emit(Steps.class, new Steps(list));
                } finally {
                    sections.leaveSection(1);
                }
                try {
                    emitterContext.emit(section);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            }
        }, null);
    }

    private List<FeatureExec> loadSample() throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream("/tzatziki/pdf/coffeemachine-exec.json");
        return new JsonIO().load(in);
    }
}