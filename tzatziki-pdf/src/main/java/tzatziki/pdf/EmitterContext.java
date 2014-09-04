package tzatziki.pdf;

import com.google.common.collect.Maps;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EmitterContext {
    private final ITextContext context;
    private final Configuration configuration;
    private final Sections sections;

    private Map<Object, PdfEmitter> registered = Maps.newConcurrentMap();

    public EmitterContext(ITextContext context,
                          Configuration configuration,
                          Sections sections) {
        this.context = context;
        this.configuration = configuration;
        this.sections = sections;
    }

    public Document getDocument() {
        return context.getDocument();
    }

    public PdfWriter getPdfWriter() {
        return context.getPdfWriter();
    }

    @SuppressWarnings("unchecked")
    public <T> PdfEmitter<T> emitterFor(Class<T> type) {
        return registered.get(type);
    }

    public EmitterContext registerDefaults() {
        registered.put(FeatureExec.class, new FeatureEmitter());
        registered.put(ScenarioExec.class, new ScenarioEmitter());
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void emit(Element element) throws DocumentException {
        Section section = sections.currentSection();
        if (section == null) {
            getDocument().add(element);
        } else {
            section.add(element);
        }
    }

    public <T> void emit(Class<? super T> klazz, T value) {
        PdfEmitter<? super T> emitter = emitterFor(klazz);
        if (emitter == null)
            throw new RuntimeException("No emitter registered for type " + klazz);
        emitter.emit(value, this);
    }

    public Sections sections() {
        return sections;
    }
}
