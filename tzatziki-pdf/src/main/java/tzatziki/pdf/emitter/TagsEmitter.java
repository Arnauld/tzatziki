package tzatziki.pdf.emitter;

import com.google.common.collect.FluentIterable;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.Styles;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.PdfEmitter;
import tzatziki.pdf.Settings;
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

        Settings settings = emitterContext.getSettings();

        Styles styles = settings.styles();

        Paragraph pTags = new Paragraph("Tags: ", styles.getFontOrDefault(Settings.META_FONT));
        boolean first = true;
        Font tagFont = styles.getFontOrDefault(TAG_FONT);
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
}
