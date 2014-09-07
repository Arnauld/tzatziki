package tzatziki.pdf.emitter;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.FontAwesomeAdapter;
import tzatziki.analysis.exec.model.ResultExec;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfEmitter;
import tzatziki.pdf.model.Steps;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StepsEmitter implements PdfEmitter<Steps> {

    private boolean debugTable = false;
    private FontAwesomeAdapter fontAwesomeAdapter;

    @Override
    public void emit(Steps stepContainer, EmitterContext emitterContext) {
        PdfPTable steps = new PdfPTable(new float[]{1f, 2f, 22f});
        for (StepExec stepExec : stepContainer.steps()) {
            emitStep(steps, stepExec, emitterContext);
        }
        steps.setWidthPercentage(100);
        steps.setSpacingBefore(5f);
        steps.setSpacingAfter(5f);
        steps.setHorizontalAlignment(Element.ALIGN_LEFT);

        emitterContext.sections().currentSection().add(steps);
    }

    private void emitStep(PdfPTable steps, StepExec step, EmitterContext emitterContext) {
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
            return fontAwesomeAdapter().symbol("question-circle", symbolSize, BaseColor.RED.darker());
        else
            return fontAwesomeAdapter().symbol("minus-circle", symbolSize, BaseColor.BLUE);
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
}
