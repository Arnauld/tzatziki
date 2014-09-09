package tzatziki.pdf.emitter;

import com.google.common.collect.FluentIterable;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.AlternateTableRowBackground;
import gutenberg.itext.CellStyler;
import gutenberg.itext.DefaultBodyCellStyler;
import gutenberg.itext.DefaultHeaderCellStyler;
import gutenberg.itext.Styles;
import tzatziki.analysis.tag.Tag;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfEmitter;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagDictionaryEmitter implements PdfEmitter<TagDictionary> {

    private CellStyler headerCellStyler;
    private CellStyler bodyCellStyler;

    public TagDictionaryEmitter() {
        this(null, null);
    }

    public TagDictionaryEmitter(CellStyler headerCellStyler, CellStyler bodyCellStyler) {
        this.headerCellStyler = headerCellStyler;
        this.bodyCellStyler = bodyCellStyler;
    }

    @Override
    public void emit(TagDictionary dictionary, EmitterContext emitterContext) {
        Styles styles = emitterContext.styles();
        if (headerCellStyler == null)
            headerCellStyler = new DefaultHeaderCellStyler(styles);

        FluentIterable<Tag> tags = dictionary.tags();

        float w = widthPercent(tags, emitterContext);

        PdfPTable table = new PdfPTable(new float[]{w, 1 - w});
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(5f);
        table.setTableEvent(new AlternateTableRowBackground(styles));

        emitHeaders(table, headerCellStyler);
        emitBody(table, tags, emitterContext);

        emitterContext.append(table);
    }

    private float widthPercent(FluentIterable<Tag> tags, EmitterContext emitterContext) {
        Rectangle artBox = emitterContext.iTextContext().getDocumentArtBox();

        Font font = headerCellStyler.cellFont();
        BaseFont baseFont = font.getBaseFont();
        float len = 0;
        for (Tag tag : tags) {
            float sLen = baseFont.getWidthPoint(tag.getTag(), font.getSize());
            len = Math.max(len, sLen);
        }
        len = Math.min(artBox.getWidth() / 2, len);

        return len / artBox.getWidth();
    }

    private void emitBody(PdfPTable table, FluentIterable<Tag> tags, EmitterContext emitterContext) {
        Styles styles = emitterContext.styles();
        CellStyler styler = bodyCellStyler;
        if (styler == null)
            styler = new DefaultBodyCellStyler(styles);

        for (Tag tag : tags) {
            Phrase tagPhrase = new Phrase(tag.getTag(), styler.cellFont());
            Phrase descPhrase = new Phrase(tag.getDescription(), styler.cellFont());

            PdfPCell tagCell = new PdfPCell(tagPhrase);
            PdfPCell descCell = new PdfPCell(descPhrase);
            table.addCell(styler.applyStyle(tagCell));
            table.addCell(styler.applyStyle(descCell));
        }
    }

    private void emitHeaders(PdfPTable table, CellStyler styler) {
        Phrase tagHeader = new Phrase("Tag", styler.cellFont());
        Phrase descHeader = new Phrase("Description", styler.cellFont());
        table.addCell(styler.applyStyle(new PdfPCell(tagHeader)));
        table.addCell(styler.applyStyle(new PdfPCell(descHeader)));
        table.setHeaderRows(1);
    }
}
