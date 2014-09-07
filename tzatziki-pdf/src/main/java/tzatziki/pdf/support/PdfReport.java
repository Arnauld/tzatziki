package tzatziki.pdf.support;

import com.itextpdf.text.DocumentException;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.tag.TagView;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.pdf.Configuration;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfEmitter;
import tzatziki.pdf.emitter.DefaultEmitters;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PdfReport {
    private final Configuration configuration;
    private ITextContext iTextContext;
    private EmitterContext emitterContext;

    public PdfReport(Configuration configuration) {
        this.configuration = configuration;
    }

    public void startReport(File output) throws FileNotFoundException, DocumentException {
        iTextContext = new ITextContext().open(output);
        emitterContext = createEmitterContext();
        registerPdfEmitters(emitterContext);
    }

    protected EmitterContext createEmitterContext() {
        Sections sections = new Sections(configuration.headerFonts());
        return new EmitterContext(iTextContext, configuration, sections);
    }

    protected void registerPdfEmitters(EmitterContext emitterContext) {
        new DefaultEmitters().registerDefaults(emitterContext);
    }

    public void endReport() {
        iTextContext.close();
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
