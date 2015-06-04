package tzatziki.pdf.emitter;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.AlternateTableRowBackground;
import gutenberg.itext.Emitter;
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
import tzatziki.pdf.model.ScenarioOutlineWithResolved;
import tzatziki.pdf.model.Steps;
import tzatziki.pdf.model.Tags;

import java.util.Iterator;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioOutlineEmitter implements Emitter<ScenarioOutlineWithResolved> {

    public static final String DISPLAY_TAGS = "scenario-display-tags";
    public static final String EXAMPLES_CELL_HEADER = StepsEmitter.STEP_TABLE_CELL_HEADER;
    public static final String EXAMPLES_CELL = StepsEmitter.STEP_TABLE_CELL;


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
    public void emit(ScenarioOutlineWithResolved scenarioOutlineWithResolved, ITextContext emitterContext) {
        Sections sections = emitterContext.sections();
        KeyValues kvs = emitterContext.keyValues();


        Integer rawOffset = kvs.getInteger(FeatureEmitter.FEATURE_HEADER_LEVEL_OFFSET).or(0);
        int headerLevel = hLevel + rawOffset;

        ScenarioOutlineExec outline = scenarioOutlineWithResolved.outline();

        sections.newSection(outline.name(), headerLevel);
        try {
            if (kvs.getBoolean(DISPLAY_TAGS, true)) {
                emitTags(outline, emitterContext);
            }
            emitDescription(outline, emitterContext);
            emitEmbeddings(outline, emitterContext);
            emitSteps(outline, emitterContext);

            Iterator<ScenarioExec> scenarioIt = scenarioOutlineWithResolved.resolved().iterator();
            for (ExamplesExec examplesExec : outline.examples()) {
                emitExamples(outline, examplesExec, scenarioIt, emitterContext, headerLevel + 1);
            }
        } finally {
            sections.leaveSection(headerLevel); // end-of-scenario
        }
    }

    private void emitExamples(ScenarioOutlineExec outline, ExamplesExec examplesExec, Iterator<ScenarioExec> scenarioIt, ITextContext emitterContext, int headerLevel) {
        KeyValues kvs = emitterContext.keyValues();
        Styles styles = kvs.<Styles>getNullable(Styles.class).get();

        Sections sections = emitterContext.sections();
        sections.newSection(firstNotNullOrEmpty(examplesExec.name(), examplesExec.keyword()), headerLevel);
        try {
            int nbCols = examplesExec.columnCount();
            float[] widths = new float[1 + nbCols];
            widths[0] = 1f;
            for (int i = 1; i <= nbCols; i++) {
                widths[i] = 24f / nbCols;
            }
            PdfPTable table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            table.setTableEvent(new AlternateTableRowBackground(styles));

            int rownum = 0;
            for (ExamplesRow row : examplesExec.rows()) {
                Optional<StepExec> firstStepInError = Optional.absent();

                Font font = null;
                if (rownum == 0) {
                    Optional<Font> fontOpt = styles.getFont(EXAMPLES_CELL_HEADER);
                    if (fontOpt.isPresent()) {
                        font = fontOpt.get();
                    }
                }
                if (font == null)
                    font = styles.getFontOrDefault(EXAMPLES_CELL);


                if (rownum == 0) {
                    PdfPCell status = new PdfPCell(new Phrase(""));
                    table.addCell(noBorder(status));
                } else {
                    ScenarioExec scenarioExec = scenarioIt.next();
                    Status status = scenarioExec.status();
                    table.addCell(statusCell(status));
                    if (status != Status.Passed) {
                        firstStepInError = scenarioExec.steps().firstMatch(Predicates.not(StepExec.statusPassed));
                    }
                }

                for (String value : row.cells()) {
                    PdfPCell cell = new PdfPCell(new Phrase(value, font));
                    table.addCell(noBorder(cell));
                }

                if (firstStepInError.isPresent()) {
                    table.addCell(noBorder(new PdfPCell()));
                    table.addCell(colspan(nbCols, stepCell(firstStepInError.get(), styles, emitterContext)));
                }

                rownum++;
            }

            emitterContext.append(table);

        } catch (Exception e) {
            log.warn("Fail to emit outline", e);
            throw new RuntimeException(e);
        } finally {
            sections.leaveSection(headerLevel); // end-of-examples
        }
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
}
