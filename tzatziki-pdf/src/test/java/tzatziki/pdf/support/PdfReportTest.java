package tzatziki.pdf.support;

import com.google.common.base.Function;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import gutenberg.itext.*;
import gutenberg.itext.support.FirstPageRenderer;
import org.junit.Before;
import org.junit.Test;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ResultExec;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.analysis.exec.support.TagView;
import tzatziki.analysis.exec.support.TagViews;
import tzatziki.analysis.exec.tag.TagFilter;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.analysis.tag.TagDictionaryLoader;
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
    public void usecase_coffeeMachine() throws Exception {
        generateReport("/tzatziki/pdf/coffeemachine-exec.json", "coffeeMachine");
    }

    @Test
    public void usecase_stepsWEmbeded() throws Exception {
        generateReport("/tzatziki/pdf/steps-w-embeded-exec.json", "stepsWEmbeded");
    }

    private void generateReport(String resource, String suffix) throws Exception {
        File output = new File(settings.getBuildDir(), getClass().getSimpleName() + "_" + suffix + ".pdf");

        List<FeatureExec> features = loadSample(resource);
        TagDictionary tagDictionary = new TagDictionaryLoader().loadTagsFromUTF8PropertiesResources("/tzatziki/pdf/tags.properties");

        TagViews tagViews = new TagViews().addAll(
                new TagView("Payment (non wip)", TagFilter.from("~@wip", "@payment")),
                new TagView("Non wip about tea", TagFilter.from("~@wip", "@tea")));
        for (FeatureExec featureExec : features) {
            tagViews.consolidateView(featureExec);
        }

        PdfReport report = new PdfReport(configuration);
        report.startReport(output);

        HeaderFooter headerFooter = registerHeaderAndFooter(report);
        emitCoverPage(report, suffix);
        emitSampleStepsPreamble(report);

        report.startContent();
        emitMarkdownPreamble(report);
        emitOverview(report, features, tagDictionary, tagViews);

        for (FeatureExec feature : features) {
            emitFeature(report, feature);
        }
        report.endReport(new TableOfContentsPostProcessor(headerFooter));

        System.out.println("PdfReportTest.usecase~~> " + output);
    }

    private HeaderFooter registerHeaderAndFooter(PdfReport report) {
        ITextContext context = report.iTextContext();
        PageNumber pageNumber = context.pageNumber();
        Styles styles = context.styles();

        Function<PageInfos, Phrase> header = HeaderFooter.none();
        Function<PageInfos, Phrase> footer = HeaderFooter.create(styles, HeaderFooter.FOOTER_FONT, null, "An amazing report - ${sectionTitle}", null);
        HeaderFooter headerFooter = new HeaderFooter(pageNumber, styles, header, footer);

        context.getPdfWriter().setPageEvent(headerFooter);
        return headerFooter;
    }

    private void emitCoverPage(PdfReport report, final String suffix) {
        FirstPageRenderer coverPage = new FirstPageRenderer("An amazing report\n" + suffix, "Sample reporting");
        report.emit(coverPage);
    }

    private void emitOverview(PdfReport report,
                              final List<FeatureExec> features,
                              final TagDictionary tagDictionary,
                              final TagViews tagViews) {
        report.emit(new SimpleEmitter() {
            @Override
            public void emit(ITextContext emitterContext) {
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
        report.emit(new SimpleEmitter() {
            @Override
            public void emit(ITextContext emitterContext) {
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

    private List<FeatureExec> loadSample(String resourcePath) throws UnsupportedEncodingException {
        InputStream in = getClass().getResourceAsStream(resourcePath);
        return new JsonIO().load(in);
    }
}