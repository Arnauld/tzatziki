package tzatziki.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Configuration {
    private Rectangle pageSize = PageSize.A4;
    private Margin documentMargin = new Margin(50);
    //
    private int chapterCount;
    //
    private String defaultFontName;
    private BaseColor primaryColor;
    private Font chapterTitleFont;
    private Font defaultFont;

    public Configuration withDocumentMargin(Margin documentMargin) {
        this.documentMargin = documentMargin;
        return this;
    }

    public Margin getDocumentMargin() {
        return documentMargin;
    }

    public Document createDocument() {
        return new Document(pageSize,
                documentMargin.marginLeft,
                documentMargin.marginRight,
                documentMargin.marginTop,
                documentMargin.marginBottom);
    }

    public Rectangle getDocumentArtBox() {
        return new Rectangle(
                documentMargin.marginLeft,
                documentMargin.marginBottom,
                pageSize.getWidth() - documentMargin.marginRight,
                pageSize.getHeight() - documentMargin.marginTop);
    }

    public Configuration withDefaultFontName(String fontName) {
        this.defaultFontName = fontName;
        return this;
    }

    public String defaultFontName() {
        if (defaultFontName == null)
            defaultFontName = FontFactory.HELVETICA;
        return defaultFontName;
    }

    public BaseColor primaryColor() {
        if (primaryColor == null)
            primaryColor = Colors.DARK_RED;
        return primaryColor;
    }

    public boolean shouldDisplayUri() {
        return true;
    }


    public BaseColor defaultColor() {
        return BaseColor.BLACK;
    }

    public Configuration withDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
        return this;
    }

    /**
     * @see #defaultColor()
     * @see #defaultFontName()
     */
    public Font defaultFont() {
        if (defaultFont == null)
            defaultFont = FontFactory.getFont(defaultFontName(), 12, Font.NORMAL, defaultColor());
        return defaultFont;
    }

    /**
     * @see #defaultColor()
     * @see #defaultFontName()
     */
    public Font defaultStrongFont() {
        return FontFactory.getFont(defaultFontName(), 12, Font.BOLD, defaultColor());
    }

    /**
     * @see #primaryColor()
     * @see #defaultFontName()
     */
    public Font defaultMetaFont() {
        return FontFactory.getFont(defaultFontName(), 8, Font.NORMAL, primaryColor());
    }

    public Font[] sectionTitleFonts() {
        return new Font[]{
                FontFactory.getFont(FontFactory.HELVETICA, 18.0f, Font.BOLD, BaseColor.BLACK),
                FontFactory.getFont(FontFactory.HELVETICA, 16.0f, Font.BOLD, BaseColor.DARK_GRAY),
                FontFactory.getFont(FontFactory.HELVETICA, 14.0f, Font.BOLD, BaseColor.DARK_GRAY)
        };
    }
}
