package tzatziki.pdf.emitter;

import com.google.common.base.Function;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.*;
import tzatziki.analysis.exec.support.TagView;
import tzatziki.analysis.exec.support.TagViews;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagViewsEmitter implements Emitter<TagViews> {

    private CellStyler bodyCellStyler, headerCellStyler;

    public TagViewsEmitter() {
        this(null, null);
    }

    public TagViewsEmitter(CellStyler headerCellStyler, CellStyler bodyCellStyler) {
        this.headerCellStyler = headerCellStyler;
        this.bodyCellStyler = bodyCellStyler;
    }

    @Override
    public void emit(TagViews views, ITextContext emitterContext) {
        Styles styles = emitterContext.styles();

        float w = 0.1f;

        PdfPTable table = new PdfPTable(new float[]{1 - 3 * w, w, w, w});
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(5f);
        table.setTableEvent(new AlternateTableRowBackground(styles));

        emitHeader(table, emitterContext);
        emitBody(table, views, emitterContext);

        emitterContext.append(table);
    }

    private void emitHeader(PdfPTable table, ITextContext emitterContext) {
        Styles styles = emitterContext.styles();
        CellStyler styler = headerCellStyler;
        if (styler == null)
            styler = new DefaultHeaderCellStyler(styles);

        table.addCell(styler.applyStyle(new PdfPCell(new Phrase("Tag/Description", styler.cellFont()))));
        table.addCell(styler.applyStyle(new PdfPCell(new Phrase("Passed", styler.cellFont()))));
        table.addCell(styler.applyStyle(new PdfPCell(new Phrase("Failed", styler.cellFont()))));
        table.addCell(styler.applyStyle(new PdfPCell(new Phrase("Other", styler.cellFont()))));
    }

    private void emitBody(PdfPTable table, TagViews views, ITextContext emitterContext) {
        Styles styles = emitterContext.styles();
        CellStyler styler = bodyCellStyler;
        if (styler == null)
            styler = new DefaultBodyCellStyler(styles);

        for (TagView tagView : views) {

            int nbTotal = tagView.nbTotal();
            int nbFailed = tagView.nbFailed();
            int nbPassed = tagView.nbPassed();
            int nbOther = nbTotal - (nbFailed + nbPassed);

            Phrase tagPhrase = new Phrase(tagView.description(), styler.cellFont());
            PdfPCell tagCell = styler.applyStyle(new PdfPCell(tagPhrase));
            PdfPCell passedCell = valuedCell(nbPassed, nbTotal, styler, constant(BaseColor.GREEN.darker()));
            PdfPCell failedCell = valuedCell(nbFailed, nbTotal, styler, constant(BaseColor.RED.darker()));
            PdfPCell otherCell = valuedCell(nbOther, nbTotal, styler, constant(BaseColor.YELLOW));

            table.addCell(tagCell);
            table.addCell(passedCell);
            table.addCell(failedCell);
            table.addCell(otherCell);
        }
    }

    private PdfPCell valuedCell(int value, int total, CellStyler styler, Function<Float, BaseColor> colorProviders) {
        Phrase phrase = new Phrase(value + "/" + total, styler.cellFont());
        PdfPCell cell = styler.applyStyle(new PdfPCell(phrase));
        cell.setCellEvent(
                new PercentBackgroundEvent(
                        value,
                        total,
                        colorProviders));
        return cell;
    }

    private Function<Float, BaseColor> constant(final BaseColor color) {
        return new Function<Float, BaseColor>() {
            @Override
            public BaseColor apply(Float input) {
                return color;
            }
        };
    }
}
