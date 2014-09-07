package tzatziki.pdf;

import com.google.common.base.Optional;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import gutenberg.itext.ITextUtils;

import java.io.IOException;

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

    public BaseFont defaultMonospaceBaseFont() {
        try {
            return ITextUtils.inconsolata();
        } catch (IOException e) {
            return FontFactory.getFont(FontFactory.COURIER).getBaseFont();
        } catch (DocumentException e) {
            return FontFactory.getFont(FontFactory.COURIER).getBaseFont();
        }
    }

    public BaseColor emphasizeColor() {
        return Colors.GRAY;
    }

    public BaseColor primaryColor() {
        if (primaryColor == null)
            primaryColor = Colors.DARK_RED;
        return primaryColor;
    }

    public BaseColor defaultColor() {
        return BaseColor.BLACK;
    }

    public Configuration withDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
        return this;
    }

    public float defaultFontSize() {
        return 12f;
    }

    /**
     * @see #defaultColor()
     * @see #defaultFontName()
     */
    public Font defaultFont() {
        if (defaultFont == null)
            defaultFont = FontFactory.getFont(defaultFontName(), defaultFontSize(), Font.NORMAL, defaultColor());
        return defaultFont;
    }

    /**
     * @see #defaultColor()
     * @see #defaultFontName()
     */
    public Font defaultStrongFont() {
        return FontFactory.getFont(defaultFontName(), defaultFontSize(), Font.BOLD, defaultColor());
    }

    /**
     * @see #primaryColor()
     * @see #defaultFontName()
     */
    public Font defaultMetaFont() {
        return FontFactory.getFont(defaultFontName(), 8, Font.NORMAL, primaryColor());
    }

    public Font[] headerFonts() {
        return new Font[]{
                FontFactory.getFont(FontFactory.HELVETICA, 18.0f, Font.BOLD, BaseColor.BLACK),
                FontFactory.getFont(FontFactory.HELVETICA, 16.0f, Font.BOLD, BaseColor.DARK_GRAY),
                FontFactory.getFont(FontFactory.HELVETICA, 14.0f, Font.BOLD, BaseColor.DARK_GRAY)
        };
    }

    //

    public Optional<Boolean> getBoolean(String propertyKey) {
        return Optional.of(true);
    }

    public boolean getBoolean(String propertyKey, boolean defaultValue) {
        return getBoolean(propertyKey).or(defaultValue);
    }

    public Optional<Font> getFont(String fontKey) {
        return Optional.absent();
    }
}
