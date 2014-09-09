package tzatziki.pdf.emitter;

import com.google.common.base.Function;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import gutenberg.itext.AlternateTableRowBackground;
import gutenberg.itext.CellStyler;
import gutenberg.itext.DefaultBodyCellStyler;
import gutenberg.itext.PercentBackgroundEvent;
import gutenberg.itext.Styles;
import tzatziki.analysis.exec.tag.TagView;
import tzatziki.analysis.exec.tag.TagViews;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfEmitter;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagViewsEmitter implements PdfEmitter<TagViews> {

    private CellStyler bodyCellStyler;

    public TagViewsEmitter() {
        this(null);
    }

    public TagViewsEmitter(CellStyler bodyCellStyler) {
        this.bodyCellStyler = bodyCellStyler;
    }

    @Override
    public void emit(TagViews views, EmitterContext emitterContext) {
        Styles styles = emitterContext.styles();

        float w = 0.8f;

        PdfPTable table = new PdfPTable(new float[]{w, 1 - w});
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(5f);
        table.setTableEvent(new AlternateTableRowBackground(styles));

        emitBody(table, views, emitterContext);

        emitterContext.append(table);
    }

    private void emitBody(PdfPTable table, TagViews views, EmitterContext emitterContext) {
        Styles styles = emitterContext.styles();
        CellStyler styler = bodyCellStyler;
        if (styler == null)
            styler = new DefaultBodyCellStyler(styles);

        for (TagView tagView : views) {
            Phrase tagPhrase = new Phrase(tagView.description(), styler.cellFont());
            Phrase descPhrase = new Phrase(tagView.nbPassed() + "/" + tagView.nbTotal(), styler.cellFont());

            PdfPCell tagCell = new PdfPCell(tagPhrase);
            PdfPCell descCell = new PdfPCell(descPhrase);
            tagCell = styler.applyStyle(tagCell);
            descCell = styler.applyStyle(descCell);

            Function<Float, BaseColor> colorProvider = constant(BaseColor.GREEN.darker());
            descCell.setCellEvent(
                    new PercentBackgroundEvent(
                            tagView.nbPassed(),
                            tagView.nbTotal(),
                            colorProvider));
            table.addCell(tagCell);
            table.addCell(descCell);
        }
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
