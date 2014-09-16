package tzatziki.pdf.support;

import com.itextpdf.text.DocumentException;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.PostProcessor;
import gutenberg.itext.SimpleEmitter;
import gutenberg.itext.support.ITextContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.support.TagView;
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

    private Logger log = LoggerFactory.getLogger(PdfReport.class);

    private final Settings settings;
    private ITextContext iTextContext;
    private File outputDst, outputTmp;

    public PdfReport(Configuration configuration) {
        this.settings = new Settings();
        configuration.configure(settings);
    }

    public void startReport(File output) throws FileNotFoundException, DocumentException {
        this.outputDst = output;
        this.outputTmp = new File(output.getAbsolutePath() + "~");
        this.iTextContext = new ITextContextBuilder()
                .usingStyles(settings.styles())
                .declare(Settings.class, settings)
                .build()
                .open(outputTmp);
        new DefaultPdfEmitters().registerDefaults(iTextContext);
    }

    public ITextContext iTextContext() {
        if (iTextContext == null)
            throw new IllegalStateException("Context is only available once the report is started");
        return iTextContext;
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

    /**
     * Indicates content start.
     * This usually change the page numbering, and indicates the position of the Table of Contents.
     */
    public void startContent() {
        iTextContext.pageNumber().startMainMatter();
    }

    public void endReport(PostProcessor... postProcessors) {
        iTextContext.close();

        File tmp = outputTmp;
        for (int i = 0; i < postProcessors.length; i++) {
            PostProcessor postProcessor = postProcessors[i];

            File tmp2 = new File(outputTmp.getAbsolutePath() + i);
            postProcessor.postProcess(iTextContext, tmp, tmp2);
            tmp = tmp2;
        }
        if (!tmp.renameTo(outputDst))
            log.warn("Fail to rename generated file {}", tmp);
    }
}
