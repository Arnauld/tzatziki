package tzatziki.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.itext.pegdown.InvocationContext;
import gutenberg.pegdown.plugin.AttributesPlugin;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MarkdownEmitter implements PdfEmitter<Markdown> {

    private Logger log = LoggerFactory.getLogger(MarkdownEmitter.class);

    @Override
    public void emit(Markdown value, EmitterContext emitterContext) {
        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(value.raw().toCharArray());

        try {
            InvocationContext context =
                    new InvocationContext(emitterContext.iTextContext())
                            .useSections(emitterContext.sections());
            List<Element> elements = context.process(0, rootNode);

            Section section = emitterContext.sections().currentSection();
            for (Element element : elements) {
                section.add(element);
            }
        } catch (IOException e) {
            log.error("Fail to generate markdown", e);
            emitRaw(value, emitterContext);
        } catch (DocumentException e) {
            log.error("Fail to generate markdown", e);
            emitRaw(value, emitterContext);
        }
    }

    private void emitRaw(Markdown value, EmitterContext emitterContext) {
        Section section = emitterContext.sections().currentSection();
        section.add(new Paragraph(value.raw()));
    }
}
