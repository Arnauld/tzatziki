package tzatziki.pdf.emitter;

import com.google.common.base.Supplier;
import com.google.common.collect.FluentIterable;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import tzatziki.pdf.Configuration;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfEmitter;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagsEmitter implements PdfEmitter<Tags> {
    public static final String TAG_FONT = "tag-font";

    @Override
    public void emit(Tags tagContainer, EmitterContext emitterContext) {
        FluentIterable<String> tags = tagContainer.tags();
        if (tags.isEmpty())
            return;

        Configuration configuration = emitterContext.getConfiguration();

        Paragraph pTags = new Paragraph("Tags: ", configuration.defaultMetaFont());
        boolean first = true;
        Font tagFont = configuration.getFont(TAG_FONT).or(tagFont(configuration));
        for (String text : tags) {
            if (first) {
                first = false;
            } else {
                text = ", " + text;
            }

            pTags.add(new Chunk(text, tagFont));
        }

        emitterContext.append(pTags);
    }

    private static Supplier<? extends Font> tagFont(final Configuration configuration) {
        return new Supplier<Font>() {
            @Override
            public Font get() {
                return new Font(configuration.defaultMonospaceBaseFont(), 8, Font.ITALIC, new CMYKColor(25, 255, 255, 17));
            }
        };
    }
}
