package tzatziki.pdf.support;

import com.itextpdf.text.DocumentException;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.SimpleEmitter;
import gutenberg.itext.Styles;
import gutenberg.itext.support.ITextContextBuilder;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.tag.TagView;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.pdf.Settings;
import tzatziki.pdf.emitter.DefaultPdfEmitters;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PdfReport {
    private final Settings settings;
    private ITextContext iTextContext;

    public PdfReport(Configuration configuration) {
        this.settings = new Settings();
        configuration.configure(settings);
    }

    public void startReport(File output) throws FileNotFoundException, DocumentException {
        Styles styles = settings.styles();
        iTextContext = new ITextContextBuilder()
                .usingStyles(styles)
                .declare(Settings.class, settings)
                .build()
                .open(output);
        new DefaultPdfEmitters().registerDefaults(iTextContext);
    }

    public void endReport() {
        iTextContext.close();
    }

    public <T> void emit(T value) {
        iTextContext.emit(value);
    }

    public void emit(SimpleEmitter emitter) {
        emitter.emit(iTextContext);
    }

    public <T> void emit(Emitter<T> emitter, T arg) {
        emitter.emit(arg, iTextContext);
    }

    public void emit(FeatureExec featureExec) {
        iTextContext.emitterFor(FeatureExec.class).emit(featureExec, iTextContext);
    }

    public void emit(Grammar grammar) {
        iTextContext.emitterFor(Grammar.class).emit(grammar, iTextContext);
    }

    public void emit(TagDictionary tagDictionary) {
        iTextContext.emitterFor(TagDictionary.class).emit(tagDictionary, iTextContext);
    }

    public void emit(TagView tagView) {
        iTextContext.emitterFor(TagView.class).emit(tagView, iTextContext);
    }

}
