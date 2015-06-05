package tzatziki.pdf.emitter;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.Colors;
import gutenberg.itext.Emitter;
import gutenberg.itext.FontCopier;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import gutenberg.itext.Styles;
import gutenberg.itext.model.Markdown;
import gutenberg.util.KeyValues;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.ExamplesExec;
import tzatziki.analysis.exec.model.ExamplesRow;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;
import tzatziki.analysis.exec.model.Status;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.pdf.Comments;
import tzatziki.pdf.Settings;
import tzatziki.pdf.model.ScenarioOutlineWithResolved;
import tzatziki.pdf.model.Steps;
import tzatziki.pdf.model.Tags;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Predicates.not;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioOutlineEmitter implements Emitter<ScenarioOutlineWithResolved> {

    public static final String DISPLAY_TAGS = "scenario-display-tags";
    public static final String EXAMPLES_CELL_HEADER = "examples-table-cell-header";
    public static final String EXAMPLES_CELL = "examples-table-cell";

    private boolean debugTable = false;
    private StatusMarker statusMarker = new StatusMarker();
    private final int hLevel;

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);

    public ScenarioOutlineEmitter() {
        this(2);
    }

    public ScenarioOutlineEmitter(int hLevel) {
        this.hLevel = hLevel;
    }

    @Override
    public void emit(ScenarioOutlineWithResolved scenarioOutlineWithResolved, final ITextContext emitterContext) {
        Sections sections = emitterContext.sections();
        KeyValues kvs = emitterContext.keyValues();


        Integer rawOffset = kvs.getInteger(FeatureEmitter.FEATURE_HEADER_LEVEL_OFFSET).or(0);
        final int headerLevel = hLevel + rawOffset;

        ScenarioOutlineExec outline = scenarioOutlineWithResolved.outline();

        sections.newSection(outline.name(), headerLevel);
        try {
            if (kvs.getBoolean(DISPLAY_TAGS, true)) {
                emitTags(outline, emitterContext);
            }
            emitDescription(outline, emitterContext);
            emitEmbeddings(outline, emitterContext);
            emitSteps(outline, emitterContext);

            emitExamples(scenarioOutlineWithResolved, emitterContext, headerLevel, outline);

        } finally {
            sections.leaveSection(headerLevel); // end-of-scenario-outline
        }
    }

    private void emitExamples(ScenarioOutlineWithResolved scenarioOutlineWithResolved, final ITextContext emitterContext, final int headerLevel, ScenarioOutlineExec outline) {
        final Iterator<ScenarioExec> scenarioIt = scenarioOutlineWithResolved.resolved().iterator();

        // due to the iterator usage, one must not rely on iterable afterwards
        // otherwise scenarioIt will be reconsumed everytime an iteration is performed through the iterable
        List<ExamplesEmitResult> results = outline.examples().transform(new Function<ExamplesExec, ExamplesEmitResult>() {
            @Override
            public ExamplesEmitResult apply(ExamplesExec input) {
                return generateExamples(input, scenarioIt, emitterContext, headerLevel + 1);
            }
        }).toList();


        boolean shouldEmitStepInError = FluentIterable.from(results).firstMatch(new Predicate<ExamplesEmitResult>() {
            @Override
            public boolean apply(ExamplesEmitResult input) {
                return input.stepInError;
            }
        }).isPresent();

        if (shouldEmitStepInError) {
            emitStepInErrorLegend(emitterContext);
        }
        results.forEach(new Consumer<ExamplesEmitResult>() {
            @Override
            public void accept(ExamplesEmitResult result) {
                emitterContext.append(result.exampleTable);
            }
        });
    }

    private void emitStepInErrorLegend(ITextContext emitterContext) {
        Styles styles = emitterContext.styles();

        PdfPTable table = new PdfPTable(new float[]{1f, 24f});

        table.addCell(noBorder(topRight(new PdfPCell(stepInErrorMarker(styles)))));
        table.addCell(noBorder(new PdfPCell(new Phrase(": First step in error within the scenario",
                new FontCopier(styles.defaultFont()).italic().get()))));
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        emitterContext.append(table);
    }

    private ExamplesEmitResult generateExamples(ExamplesExec examplesExec,
                                                Iterator<ScenarioExec> scenarioIt,
                                                ITextContext emitterContext,
                                                int headerLevel) {
        ExamplesEmitResult result = new ExamplesEmitResult();

        KeyValues kvs = emitterContext.keyValues();
        Styles styles = kvs.<Styles>getNullable(Styles.class).get();

        Sections sections = emitterContext.sections();
        sections.newSection(firstNotNullOrEmpty(examplesExec.name(), examplesExec.keyword()), headerLevel);
        try {
            float[] widths = computeWidths(examplesExec);
            PdfPTable table = new PdfPTable(widths);
            table.setWidthPercentage(100);

            int nbCols = examplesExec.columnCount();
            int rownum = 0;

            BaseColor alternateBG = styles.getColor(Styles.TABLE_ALTERNATE_BACKGROUND).or(Colors.VERY_LIGHT_GRAY);

            boolean alternate = true;
            for (ExamplesRow row : examplesExec.rows()) {
                alternate = !alternate;

                BaseColor background = alternate ? alternateBG : null;
                Font font = getExamplesRowFont(styles, rownum);

                Optional<StepExec> firstStepInError = Optional.absent();
                if (rownum == 0) {
                    background = styles.getColor(Styles.TABLE_HEADER_BACKGROUD).get();

                    PdfPCell status = new PdfPCell(new Phrase(""));
                    table.addCell(withBackground(noBorder(status), background));
                } else {
                    ScenarioExec scenarioExec = scenarioIt.next();
                    Status status = scenarioExec.status();
                    table.addCell(withBackground(statusCell(status), background));
                    if (status != Status.Passed) {
                        firstStepInError = scenarioExec.steps().firstMatch(not(StepExec.statusPassed));
                    }
                }

                for (String value : row.cells()) {
                    PdfPCell cell = new PdfPCell(new Phrase(value, font));
                    if (rownum > 0) {
                        cell.setBorder(Rectangle.TOP);
                        cell.setBorderColor(BaseColor.LIGHT_GRAY);
                    } else {
                        cell.setBorder(Rectangle.NO_BORDER);
                    }
                    table.addCell(withBackground(cell, background));
                }

                if (firstStepInError.isPresent()) {
                    result.stepInError = true;
                    PdfPCell cell = new PdfPCell(stepInErrorMarker(styles));
                    table.addCell(withBackground(noBorder(topRight(cell)), background));
                    table.addCell(withBackground(colspan(nbCols, stepCell(firstStepInError.get(), styles, emitterContext)), background));
                }

                rownum++;
            }

            result.exampleTable = table;

        } catch (Exception e) {
            log.warn("Fail to emit outline examples", e);
            throw new RuntimeException(e);
        } finally {
            sections.leaveSection(headerLevel); // end-of-examples
        }
        return result;
    }

    private PdfPCell withBackground(PdfPCell cell, BaseColor background) {
        if (background != null)
            cell.setBackgroundColor(background);
        return cell;
    }

    private float[] computeWidths(ExamplesExec examples) {
        int nbCols = examples.columnCount();

        final int[] maxWidth = new int[nbCols];
        examples.rows().forEach(new Consumer<ExamplesRow>() {
            @Override
            public void accept(ExamplesRow examplesRow) {
                int c = 0;
                for (String s : examplesRow.cells()) {
                    maxWidth[c] = Math.max(s.length(), maxWidth[c]);
                    c++;
                }
            }
        });

        float sum = 0;
        for (int i : maxWidth) {
            sum += i;
        }


        float[] widths = new float[1 + nbCols];
        widths[0] = 1f;
        for (int i = 1; i <= nbCols; i++) {
            widths[i] = 24f * maxWidth[i - 1] / sum;
        }
        return widths;
    }

    private Font getExamplesRowFont(Styles styles, int rownum) {
        Font font = null;
        if (rownum == 0) {
            Optional<Font> fontOpt = styles.getFont(EXAMPLES_CELL_HEADER);
            if (fontOpt.isPresent()) {
                font = fontOpt.get();
            } else {
                font = styles.getFont(Styles.TABLE_HEADER_FONT).get();
            }
        }
        if (font == null)
            font = styles.getFontOrDefault(EXAMPLES_CELL);
        return font;
    }

    private Phrase stepInErrorMarker(Styles styles) {
        return new Phrase(stepInErrorMarker(), styles.getFontOrDefault(Settings.META_FONT));
    }

    private String stepInErrorMarker() {
        return "*";
    }

    private PdfPCell topRight(PdfPCell pdfPCell) {
        pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return pdfPCell;
    }

    private PdfPCell stepCell(StepExec stepExec, Styles styles, ITextContext context) {
        Paragraph stepPhrase = StepsEmitter.formatStep(stepExec, true, styles, context);
        PdfPCell stepCell = new PdfPCell(stepPhrase);
        stepCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        stepCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        stepCell = noBorder(stepCell);
        return stepCell;
    }

    private static String firstNotNullOrEmpty(String one, String two) {
        if (one != null && !one.trim().isEmpty())
            return one;
        return two;
    }

    private PdfPCell statusCell(Status status) {
        Phrase statusSymbol = new Phrase(statusMarker.statusMarker(status));
        PdfPCell statusCell = new PdfPCell(statusSymbol);
        statusCell.setVerticalAlignment(Element.ALIGN_TOP);
        statusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        statusCell = noBorder(statusCell);
        return statusCell;
    }

    private void emitTags(ScenarioOutlineExec scenario, ITextContext emitterContext) {
        emitterContext.emit(Tags.class, new Tags(scenario.tags()));
    }

    private void emitSteps(ScenarioOutlineExec scenario, ITextContext emitterContext) {
        emitterContext.emit(Steps.class, new Steps(scenario.steps()));
    }

    protected void emitEmbeddings(ScenarioOutlineExec scenario, ITextContext emitterContext) {
    }

    protected void emitDescription(ScenarioOutlineExec scenario, ITextContext emitterContext) {
        // Description
        StringBuilder b = new StringBuilder();
        String description = scenario.description();
        if (StringUtils.isNotBlank(description)) {
            b.append(description);
        }

        Optional<StepExec> first = scenario.steps().first();
        if (first.isPresent()) {
            StepExec stepExec = first.get();
            for (String comment : stepExec.comments()) {
                String uncommented = Comments.discardCommentChar(comment);
                if (!Comments.startsWithComment(uncommented)) { // double # case
                    b.append(uncommented).append(Comments.NL);
                }
            }
        }

        if (b.length() > 0) {
            log.debug("Description content >>{}<<", b);
            emitterContext.emit(Markdown.class, new Markdown(b.toString()));
        }
    }

    private static PdfPCell colspan(int colspan, PdfPCell cell) {
        cell.setColspan(colspan);
        return cell;
    }

    private PdfPCell noBorder(PdfPCell cell) {
        if (!debugTable) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }

    private static class ExamplesEmitResult {
        public boolean stepInError = false;
        public PdfPTable exampleTable;
    }
}
