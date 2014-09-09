package tzatziki.pdf.support;

import com.itextpdf.text.Section;
import gutenberg.itext.Sections;
import org.junit.Before;
import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ResultExec;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.exec.tag.TagView;
import tzatziki.analysis.exec.tag.TagViews;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.analysis.tag.TagDictionaryLoader;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfSimpleEmitter;
import tzatziki.pdf.TestSettings;
import tzatziki.pdf.model.Markdown;
import tzatziki.pdf.model.Steps;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class PdfReportTest {

    private TestSettings settings;
    private Configuration configuration;

    @Before
    public void setUp() {
        settings = new TestSettings();
        configuration = new Configuration();
    }

    @Test
    public void usecase() throws Exception {
        File output = new File(settings.getBuildDir(), getClass().getSimpleName() + "_usecase.pdf");

        List<FeatureExec> features = loadSample();
        TagDictionary tagDictionary = new TagDictionaryLoader().loadTagsFromUTF8PropertiesResources("/tzatziki/pdf/tags.properties");

        TagViews tagViews = new TagViews().addAll(
                new TagView("Payment (non wip)", TagFilter.from("~@wip", "@payment")),
                new TagView("Non wip about tea", TagFilter.from("~@wip", "@tea")));
        for (FeatureExec featureExec : features) {
            tagViews.consolidateView(featureExec);
        }

        PdfReport report = new PdfReport(configuration);
        report.startReport(output);
        emitSampleStepsPreamble(report);
        emitMarkdownPreamble(report);
        emitOverview(report, features, tagDictionary, tagViews);

        for (FeatureExec feature : features) {
            emitFeature(report, feature);
        }
        report.endReport();

        System.out.println("PdfReportTest.usecase~~> " + output);
    }

    private void emitOverview(PdfReport report,
                              final List<FeatureExec> features,
                              final TagDictionary tagDictionary,
                              final TagViews tagViews) {
        report.emit(new PdfSimpleEmitter() {
            @Override
            public void emit(EmitterContext emitterContext) {
                Sections sections = emitterContext.sections();
                Section section = sections.newSection("Overview", 1);
                try {
                    sections.newSection("Features", 2);
                    emitterContext.emit(new FeatureSummaryListOfSection(features, 3));
                    sections.leaveSection(2);

                    sections.newSection("Tags", 2);
                    emitterContext.emit(tagDictionary);
                    sections.leaveSection(2);

                    sections.newSection("Consolidated tag views", 2);
                    emitterContext.emit(tagViews);
                    sections.leaveSection(2);

                } finally {
                    sections.leaveSection(1);
                }
                emitterContext.append(section);
            }
        });
    }

    private void emitFeature(PdfReport report, FeatureExec feature) {
        report.emit(feature);
    }

    private void emitMarkdownPreamble(PdfReport report) throws IOException {
        report.emit(Markdown.fromUTF8Resource("/tzatziki/pdf/00-preambule.md"));
    }

    private void emitSampleStepsPreamble(PdfReport report) {
        report.emit(new PdfSimpleEmitter() {
            @Override
            public void emit(EmitterContext emitterContext) {
                Sections sections = emitterContext.sections();
                Section section = sections.newSection("Sample Steps", 1, false);
                try {
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
                emitterContext.append(section);
            }
        });
    }

    private List<FeatureExec> loadSample() throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream("/tzatziki/pdf/coffeemachine-exec.json");
        return new JsonIO().load(in);
    }
}