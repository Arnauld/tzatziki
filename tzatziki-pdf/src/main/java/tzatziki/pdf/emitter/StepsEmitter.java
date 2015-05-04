package tzatziki.pdf.emitter;

import com.google.common.base.Optional;
import com.itextpdf.text.Chunk;
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
import gutenberg.itext.Styles;
import gutenberg.util.Consumer;
import gutenberg.util.KeyValues;
import tzatziki.analysis.exec.model.DataTable;
import tzatziki.analysis.exec.model.Embedded;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.pdf.model.Steps;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StepsEmitter implements Emitter<Steps> {

    public static final String STEP_KEYWORD_FONT = "step-keyword-font";
    public static final String STEP_PHRASE_FONT = "step-phrase-font";
    public static final String STEP_PARAMETER_FONT = "step-parameter-font";
    public static final String STEP_DOCSTRING = "step-docstring";
    public static final String STEP_TABLE_CELL = "step-table-cell";
    public static final String STEP_TABLE_CELL_HEADER = "step-table-cell-header";

    public static final String STEP_TABLE_MODE = "step-table-mode";

    private boolean debugTable = false;
    private StatusMarker statusMarker = new StatusMarker();

    @Override
    public void emit(Steps stepContainer, ITextContext emitterContext) {
        PdfPTable steps = new PdfPTable(new float[]{1f, 2f, 22f});
        for (StepExec stepExec : stepContainer.steps()) {
            emitStep(steps, stepExec, emitterContext);
        }
        steps.setWidthPercentage(100);
        steps.setSpacingBefore(5f);
        steps.setSpacingAfter(5f);
        steps.setHorizontalAlignment(Element.ALIGN_LEFT);

        emitterContext.append(steps);
    }

    private void emitStep(final PdfPTable steps, StepExec step, ITextContext emitterContext) {
        KeyValues kvs = emitterContext.keyValues();
        Styles styles = kvs.<Styles>getNullable(Styles.class).get();

        PdfPCell statusCell = statusCell(step);
        PdfPCell keywordCell = keywordCell(step, styles);
        PdfPCell phraseCell = phraseCell(step, styles);

        steps.addCell(statusCell);
        steps.addCell(keywordCell);
        steps.addCell(phraseCell);

        if (step.hasTable()) {
            // table added on stepParagraph is not visible...
            // thus it becomes a direct nested table
            PdfPTable table = stepDataTable(step.table(), styles);

            int mode = kvs.getInteger(STEP_TABLE_MODE, 1);
            if (mode == 1) {
                steps.addCell(noBorder(colspan(2, new PdfPCell(new Phrase("")))));
                steps.addCell(noBorder(new PdfPCell(table)));
            } else if (mode == 2) {
                steps.addCell(noBorder(new PdfPCell(new Phrase(""))));
                steps.addCell(noBorder(colspan(2, new PdfPCell(table))));
            } else {
                steps.addCell(noBorder(colspan(3, new PdfPCell(table))));
            }
        }

        if (step.hasDocString()) {
            String docString = step.docString();
            Phrase phrase = new Phrase(docString, styles.getFontOrDefault(STEP_DOCSTRING));
            steps.addCell(noBorder(colspan(2, new PdfPCell(new Phrase("")))));
            steps.addCell(noBorder(new PdfPCell(phrase)));
        }

        emitterContext.pushElementConsumer(appendToTable(steps));
        try {
            for (Embedded embedded : step.embeddeds()) {
                emitterContext.emit(embedded);
            }
        } finally {
            emitterContext.popElementConsumer();
        }
    }

    private Consumer<Element> appendToTable(final PdfPTable steps) {
        return new Consumer<Element>() {
            @Override
            public void consume(Element element) {
                PdfPCell cell = new PdfPCell();
                cell.addElement(element);

                steps.addCell(noBorder(colspan(2, new PdfPCell(new Phrase("")))));
                steps.addCell(noBorder(cell));
            }
        };
    }

    private PdfPTable stepDataTable(DataTable table, Styles styles) {
        PdfPTable iTable = new PdfPTable(table.nbColumns());
        iTable.setTableEvent(new AlternateTableRowBackground(styles));

        int rownum = 0;
        for (DataTable.Row row : table.rows()) {

            Font font = null;
            if (rownum == 0) {
                Optional<Font> fontOpt = styles.getFont(STEP_TABLE_CELL_HEADER);
                if (fontOpt.isPresent()) {
                    font = fontOpt.get();
                }
            }
            if (font == null)
                font = styles.getFontOrDefault(STEP_TABLE_CELL);

            for (String value : row.cells()) {
                PdfPCell cell = new PdfPCell(new Phrase(value, font));
                iTable.addCell(noBorder(cell));
            }
            rownum++;
        }

        return iTable;
    }

    private PdfPCell phraseCell(StepExec step, Styles styles) {
        Font stepPhraseFont = styles.getFontOrDefault(STEP_PHRASE_FONT);
        Font stepParamFont = styles.getFontOrDefault(STEP_PARAMETER_FONT);
        Paragraph pPhrase = new Paragraph();
        if (!step.isMatching()) {
            pPhrase.add(new Chunk(step.name(), stepPhraseFont));
        } else {
            for (StepExec.Tok tok : step.tokenizeBody()) {
                Font tokFont = stepPhraseFont;
                if (tok.param)
                    tokFont = stepParamFont;
                pPhrase.add(new Chunk(tok.value, tokFont));
            }
        }
        PdfPCell phraseCell = new PdfPCell(pPhrase);
        phraseCell.setVerticalAlignment(Element.ALIGN_TOP);
        phraseCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        phraseCell = noBorder(phraseCell);
        return phraseCell;
    }

    private PdfPCell keywordCell(StepExec step, Styles styles) {
        Font stepKeywordFont = styles.getFontOrDefault(STEP_KEYWORD_FONT);
        Paragraph pKeyword = new Paragraph(step.keyword(), stepKeywordFont);
        PdfPCell keywordCell = new PdfPCell(pKeyword);
        keywordCell.setVerticalAlignment(Element.ALIGN_TOP);
        keywordCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        keywordCell = noBorder(keywordCell);
        return keywordCell;
    }

    private PdfPCell statusCell(StepExec step) {
        Phrase statusSymbol = new Phrase(statusMarker.statusMarker(step.result().status()));
        PdfPCell statusCell = new PdfPCell(statusSymbol);
        statusCell.setVerticalAlignment(Element.ALIGN_TOP);
        statusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        statusCell = noBorder(statusCell);
        return statusCell;
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
