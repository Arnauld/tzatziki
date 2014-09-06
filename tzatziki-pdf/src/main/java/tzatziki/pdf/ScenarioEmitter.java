package tzatziki.pdf;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.FontAwesomeAdapter;
import gutenberg.itext.Sections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.ResultExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.StepExec;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioEmitter implements PdfEmitter<ScenarioExec> {

    public static final String TAG_FONT = "scenario-tag-font";
    public static final String DISPLAY_TAGS = "scenario-display-tags";

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);

    private boolean debugTable = false;
    private FontAwesomeAdapter fontAwesomeAdapter;

    @Override
    public void emit(ScenarioExec scenario, EmitterContext emitterContext) {
        Configuration configuration = emitterContext.getConfiguration();
        Sections sections = emitterContext.sections();

        Section scenarioChap = sections.newSection(scenario.name(), 2);
        try {
            if (configuration.getBoolean(DISPLAY_TAGS, true)) {
                emitTags(scenario, scenarioChap, emitterContext);
            }
            emitDescription(scenario, scenarioChap, emitterContext);
            emitEmbeddings(scenario, scenarioChap, emitterContext);
            emitSteps(scenario, scenarioChap, emitterContext);
        } finally {
            sections.leaveSection(2); // end-of-scenario
        }
    }

    protected void emitSteps(ScenarioExec scenario, Section scenarioChap, EmitterContext emitterContext) {
        PdfPTable steps = new PdfPTable(new float[]{1f, 2f, 22f});
        for (StepExec stepExec : scenario.steps()) {
            emitStep(scenario, steps, stepExec, emitterContext);
        }
        steps.setWidthPercentage(100);
        steps.setSpacingBefore(5f);
        steps.setSpacingAfter(5f);
        steps.setHorizontalAlignment(Element.ALIGN_LEFT);
        scenarioChap.add(steps);
    }

    private void emitStep(ScenarioExec scenario, PdfPTable steps, StepExec step, EmitterContext emitterContext) {
        Phrase statusSymbol = new Phrase(statusMarker(step.result()));
        PdfPCell statusCell = new PdfPCell(statusSymbol);
        statusCell.setVerticalAlignment(Element.ALIGN_TOP);
        statusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        if (!debugTable)
            statusCell.setBorder(Rectangle.NO_BORDER);

        Paragraph pKeyword = new Paragraph(step.keyword());
        PdfPCell keywordCell = new PdfPCell(pKeyword);
        keywordCell.setVerticalAlignment(Element.ALIGN_TOP);
        keywordCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        if (!debugTable)
            keywordCell.setBorder(Rectangle.NO_BORDER);

        Paragraph pPhrase = new Paragraph(step.name());
        PdfPCell phraseCell = new PdfPCell(pPhrase);
        phraseCell.setVerticalAlignment(Element.ALIGN_TOP);
        phraseCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        if (!debugTable)
            phraseCell.setBorder(Rectangle.NO_BORDER);

        steps.addCell(statusCell);
        steps.addCell(keywordCell);
        steps.addCell(phraseCell);
    }

    protected Chunk statusMarker(ResultExec result) {
        float symbolSize = 12;
        if (result.isPassed())
            return fontAwesomeAdapter().symbol("check-circle", symbolSize, BaseColor.GREEN.darker());
        else if (result.isFailed())
            return fontAwesomeAdapter().symbol("ban", symbolSize, BaseColor.RED);
        else if (result.isPending())
            return fontAwesomeAdapter().symbol("gears", symbolSize, BaseColor.ORANGE);
        else if (result.isSkipped())
            return fontAwesomeAdapter().symbol("exclamation-circle", symbolSize, BaseColor.ORANGE);
        else if (result.isUndefined())
            return fontAwesomeAdapter().symbol("question-circle", symbolSize, BaseColor.ORANGE);
        else
            return fontAwesomeAdapter().symbol("minus-circle", symbolSize, BaseColor.ORANGE);
    }

    private FontAwesomeAdapter fontAwesomeAdapter() {
        if (fontAwesomeAdapter == null)
            try {
                fontAwesomeAdapter = new FontAwesomeAdapter();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        return fontAwesomeAdapter;
    }

    protected void emitEmbeddings(ScenarioExec scenario, Section scenarioChap, EmitterContext emitterContext) {
    }

    protected void emitDescription(ScenarioExec scenario, Section scenarioChap, EmitterContext emitterContext) {
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

    protected void emitTags(ScenarioExec scenario, Section scenarioChap, EmitterContext emitterContext) {
        FluentIterable<String> tags = scenario.tags();
        if (tags.isEmpty())
            return;

        Configuration configuration = emitterContext.getConfiguration();

        Paragraph pTags = new Paragraph("Tags: ", configuration.defaultMetaFont());
        boolean first = true;
        Font tagFont = configuration.getFont(TAG_FONT).or(tagFont(configuration));
        for (String text : tags) {
            if (first) {
                first = false;
            } else {
                text = ", " + text;
            }

            pTags.add(new Chunk(text, tagFont));
        }
        scenarioChap.add(pTags);
    }

    private Supplier<? extends Font> tagFont(final Configuration configuration) {
        return new Supplier<Font>() {
            @Override
            public Font get() {
                return new Font(configuration.defaultMonospaceBaseFont(), 8, Font.ITALIC, new CMYKColor(25, 255, 255, 17));
            }
        };
    }
}
