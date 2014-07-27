package tzatziki.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Configuration {
    private Rectangle pageSize = PageSize.A4;
    private Margin documentMargin = new Margin(50);

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
}
