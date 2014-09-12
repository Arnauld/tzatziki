package tzatziki.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import gutenberg.itext.Styles;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EmitterContext {
    private final ITextContext context;
    private final Settings settings;
    private final Sections sections;
    private final Styles styles;

    public EmitterContext(ITextContext context,
                          Settings settings,
                          Sections sections,
                          Styles styles) {
        this.context = context;
        this.settings = settings;
        this.sections = sections;
        this.styles = styles;
    }

    public ITextContext iTextContext() {
        return context;
    }

    public Styles styles() {
        return styles;
    }

    public Document getDocument() {
        return context.getDocument();
    }

    public PdfWriter getPdfWriter() {
        return context.getPdfWriter();
    }


    public Settings getSettings() {
        return settings;
    }


}
