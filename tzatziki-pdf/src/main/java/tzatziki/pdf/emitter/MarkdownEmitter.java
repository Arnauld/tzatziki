package tzatziki.pdf.emitter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Styles;
import gutenberg.itext.pegdown.InvocationContext;
import gutenberg.pegdown.plugin.AttributesPlugin;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.pdf.model.Markdown;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MarkdownEmitter implements Emitter<Markdown> {

    private Logger log = LoggerFactory.getLogger(MarkdownEmitter.class);

    @Override
    public void emit(Markdown value, ITextContext emitterContext) {
        Styles styles = emitterContext.styles();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(value.raw().toCharArray());

        try {
            InvocationContext context =
                    new InvocationContext(emitterContext, styles)
                            .useSections(emitterContext.sections());
            List<Element> elements = context.process(0, rootNode);
            emitterContext.appendAll(elements);
        } catch (IOException e) {
            log.error("Fail to generate markdown", e);
            emitRaw(value, emitterContext);
        } catch (DocumentException e) {
            log.error("Fail to generate markdown", e);
            emitRaw(value, emitterContext);
        }
    }

    private void emitRaw(Markdown value, ITextContext emitterContext) {
        emitterContext.append(new Paragraph(value.raw()));
    }
}
