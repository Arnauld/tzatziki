package tzatziki.pdf;

import com.google.common.collect.Maps;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import gutenberg.itext.Styles;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EmitterContext {
    private final ITextContext context;
    private final Configuration configuration;
    private final Sections sections;
    private final Styles styles;

    private Map<Object, PdfEmitter> registered = Maps.newConcurrentMap();

    public EmitterContext(ITextContext context,
                          Configuration configuration,
                          Sections sections,
                          Styles styles) {
        this.context = context;
        this.configuration = configuration;
        this.sections = sections;
        this.styles = styles;
    }

    public ITextContext iTextContext() {
        return context;
    }

    public Styles styles() {
        return styles;
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

    public Configuration getConfiguration() {
        return configuration;
    }

    public void appendAll(Iterable<? extends Element> elements) {
        for (Element e : elements)
            append(e);

    }

    public void append(Element element) {
        try {
            if(element instanceof Chapter) {
                getDocument().add(element);
                sections.leaveSection(1);
                return;
            }

            Section section = sections.currentSection();
            if (section == null) {
                getDocument().add(element);
            } else {
                section.add(element);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void emit(Class<? super T> klazz, T value) {
        PdfEmitter<? super T> emitter = emitterFor(klazz);
        if (emitter == null)
            throw new RuntimeException("No emitter registered for type " + klazz);
        emitter.emit(value, this);
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(T value) {
        Class klazz = value.getClass();
        for (Map.Entry<Object, PdfEmitter> entry : registered.entrySet()) {
            if (entry.getKey() instanceof Class) {
                Class supportedType = (Class) entry.getKey();
                if (supportedType.isAssignableFrom(klazz)) {
                    PdfEmitter emitter = entry.getValue();
                    emitter.emit(value, this);
                    return;
                }
            }
        }
        throw new RuntimeException("No emitter registered or suitable for type " + klazz);
    }

    public Sections sections() {
        return sections;
    }

    public <T> void register(Class<T> type, PdfEmitter<? super T> emitter) {
        registered.put(type, emitter);
    }
}
