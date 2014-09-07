package tzatziki.pdf.support;

import com.itextpdf.text.Font;
import gutenberg.itext.Colors;
import gutenberg.itext.FontCopier;
import gutenberg.itext.Styles;
import tzatziki.pdf.Settings;
import tzatziki.pdf.emitter.FeatureEmitter;
import tzatziki.pdf.emitter.ScenarioEmitter;

import static com.itextpdf.text.Font.BOLD;
import static com.itextpdf.text.Font.NORMAL;
import static gutenberg.itext.Colors.DARK_RED;
import static gutenberg.itext.Styles.CODE_FONT;
import static tzatziki.pdf.Settings.EMPHASIZE_COLOR;
import static tzatziki.pdf.emitter.StepsEmitter.STEP_KEYWORD_FONT;
import static tzatziki.pdf.emitter.StepsEmitter.STEP_PHRASE_FONT;
import static tzatziki.pdf.emitter.TagsEmitter.TAG_FONT;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Configuration {

    public void configure(Settings settings) {
        configureProperties(settings);
        configureStyles(settings.styles());
    }

    protected void configureProperties(Settings settings) {
        settings.defineProperty(FeatureEmitter.DISPLAY_URI, true);
        settings.defineProperty(FeatureEmitter.DISPLAY_TAGS, true);
        settings.defineProperty(ScenarioEmitter.DISPLAY_TAGS, true);
    }

    protected void configureStyles(Styles styles) {
        configureColors(styles);
        configureFonts(styles);
    }

    private void configureFonts(Styles styles) {
        Font metaFont = new FontCopier(styles.defaultFont()).style(NORMAL).size(8).color(DARK_RED).get();
        styles.registerFont(Settings.META_FONT, metaFont);
        styles.registerFont(STEP_KEYWORD_FONT, styles.getFontOrDefault(CODE_FONT, BOLD, styles.getColor(EMPHASIZE_COLOR).or(DARK_RED)));
        styles.registerFont(STEP_PHRASE_FONT, styles.getFontOrDefault(CODE_FONT, NORMAL, styles.defaultColor()));
        styles.registerFont(TAG_FONT, metaFont);
    }

    private void configureColors(Styles styles) {
        styles.registerColor(Settings.PRIMARY_COLOR, DARK_RED);
        styles.registerColor(EMPHASIZE_COLOR, Colors.GRAY);
    }
}
