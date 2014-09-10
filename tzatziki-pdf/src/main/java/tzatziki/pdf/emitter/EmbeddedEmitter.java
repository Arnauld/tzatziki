package tzatziki.pdf.emitter;

import tzatziki.analysis.exec.model.Embedded;
import tzatziki.pdf.EmitterContext;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EmbeddedEmitter implements tzatziki.pdf.PdfEmitter<Embedded> {
    @Override
    public void emit(Embedded value, EmitterContext emitterContext) {
        String mimeType = value.mimeType();
        if(mimeType.startsWith("lang/")) {
            String lang = mimeType.substring("lang/".length());

        }
    }
}
