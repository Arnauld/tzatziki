package tzatziki.pdf;

import com.itextpdf.text.DocumentException;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ResultExec;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.analysis.exec.tag.TagView;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.tag.TagDictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PdfReport {
    private final Configuration configuration;
    private ITextContext iTextContext;
    private EmitterContext emitterContext;
    private Sections sections;

    public PdfReport(Configuration configuration) {
        this.configuration = configuration;
    }

    public void startReport(File output) throws FileNotFoundException, DocumentException {
        iTextContext = new ITextContext().open(output);
        sections = new Sections(configuration.headerFonts());
        emitterContext = new EmitterContext(iTextContext, configuration, sections).registerDefaults();
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
