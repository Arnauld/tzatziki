package tzatziki.pdf.emitter;

import com.itextpdf.text.Paragraph;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.model.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.Embedded;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EmbeddedEmitter implements Emitter<Embedded> {

    private Logger log = LoggerFactory.getLogger(EmbeddedEmitter.class);

    @Override
    public void emit(Embedded value, ITextContext emitterContext) {
        String mimeType = value.mimeType();
        if (mimeType.equalsIgnoreCase(SourceCode.MIME_TYPE)) {
            SourceCode sourceCode = SourceCode.fromBytes(value.data());
            emitterContext.emit(sourceCode);
        } else if (mimeType.startsWith("plain/text")) {
            String text = value.isText() ? value.text() : new String(value.data());
            Paragraph p = new Paragraph(text);
            emitterContext.emit(p);
        } else {
            log.warn("Unsupported mime type {}, data discarded", mimeType);
        }
    }
}
