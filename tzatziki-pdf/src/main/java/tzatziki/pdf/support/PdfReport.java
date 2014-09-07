package tzatziki.pdf.support;

import com.itextpdf.text.DocumentException;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import gutenberg.itext.Styles;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.tag.TagView;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.pdf.Settings;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfEmitter;
import tzatziki.pdf.PdfSimpleEmitter;
import tzatziki.pdf.emitter.DefaultEmitters;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PdfReport {
    private final Settings settings;
    private ITextContext iTextContext;
    private EmitterContext emitterContext;

    public PdfReport(Configuration configuration) {
        this.settings = new Settings();
        configuration.configure(settings);
    }

    public void startReport(File output) throws FileNotFoundException, DocumentException {
        iTextContext = new ITextContext().open(output);
        emitterContext = createEmitterContext();
        registerPdfEmitters(emitterContext);
    }

    protected EmitterContext createEmitterContext() {
        Styles styles = settings.styles();
        Sections sections = new Sections(styles);
        return new EmitterContext(iTextContext, settings, sections, styles);
    }

    protected void registerPdfEmitters(EmitterContext emitterContext) {
        new DefaultEmitters().registerDefaults(emitterContext);
    }

    public void endReport() {
        iTextContext.close();
    }

    public <T> void emit(T value) {
        emitterContext.emit(value);
    }

    public void emit(PdfSimpleEmitter emitter) {
        emitter.emit(emitterContext);
    }

    public <T> void emit(PdfEmitter<T> emitter, T arg) {
        emitter.emit(arg, emitterContext);
    }

    public void emit(FeatureExec featureExec) {
        emitterContext.emitterFor(FeatureExec.class).emit(featureExec, emitterContext);
    }

    public void emit(Grammar grammar) {
        emitterContext.emitterFor(Grammar.class).emit(grammar, emitterContext);
    }

    public void emit(TagDictionary tagDictionary) {
        emitterContext.emitterFor(TagDictionary.class).emit(tagDictionary, emitterContext);
    }

    public void emit(TagView tagView) {
        emitterContext.emitterFor(TagView.class).emit(tagView, emitterContext);
    }

}
