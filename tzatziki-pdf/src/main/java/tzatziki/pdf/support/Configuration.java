package tzatziki.pdf.support;

import com.google.common.collect.Maps;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import gutenberg.itext.Colors;
import gutenberg.itext.FontCopier;
import gutenberg.itext.HeaderFooter;
import gutenberg.itext.Styles;
import gutenberg.itext.support.FirstPageRenderer;
import tzatziki.pdf.Settings;
import tzatziki.pdf.emitter.FeatureEmitter;
import tzatziki.pdf.emitter.ScenarioEmitter;

import java.util.Map;

import static com.itextpdf.text.Font.BOLD;
import static com.itextpdf.text.Font.NORMAL;
import static gutenberg.itext.Colors.DARK_RED;
import static gutenberg.itext.Styles.CODE_FONT;
import static tzatziki.pdf.Settings.EMPHASIZE_COLOR;
import static tzatziki.pdf.emitter.StepsEmitter.*;
import static tzatziki.pdf.emitter.TagsEmitter.TAG_FONT;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Configuration {

    private Map<Object, Object> properties = Maps.newHashMap();

    public Configuration() {
        displayFeatureUri(true);
        displayFeatureTags(true);
        displayScenarioTags(true);
    }

    public Configuration declareProperty(Object key, Object value) {
        properties.put(key, value);
        return this;
    }

    public Configuration displayFeatureUri(boolean displayFeatureUri) {
        return declareProperty(FeatureEmitter.DISPLAY_URI, displayFeatureUri);
    }

    public Configuration displayFeatureTags(boolean displayFeatureTags) {
        return declareProperty(FeatureEmitter.DISPLAY_TAGS, displayFeatureTags);
    }

    public Configuration displayScenarioTags(boolean displayScenarioTags) {
        return declareProperty(ScenarioEmitter.DISPLAY_TAGS, displayScenarioTags);
    }

    public void configure(Settings settings) {
        configureProperties(settings);
        configureStyles(settings.styles());
    }

    protected void configureProperties(Settings settings) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            settings.defineProperty(entry.getKey(), entry.getValue());
        }
    }

    protected void configureStyles(Styles styles) {
        configureColors(styles);
        configureFonts(styles);
    }

    private void configureFonts(Styles styles) {
        Font metaFont = new FontCopier(styles.defaultFont()).style(NORMAL).size(8).color(DARK_RED).get();
        styles.registerFont(Settings.META_FONT, metaFont);

        BaseColor emphasizedColor = styles.getColor(EMPHASIZE_COLOR).or(DARK_RED);
        BaseColor primaryColor = styles.getColor(Settings.PRIMARY_COLOR).or(DARK_RED);

        styles.registerFont(STEP_KEYWORD_FONT, styles.getFontOrDefault(CODE_FONT, BOLD, emphasizedColor));
        styles.registerFont(STEP_PHRASE_FONT, styles.getFontOrDefault(CODE_FONT, NORMAL, styles.defaultColor()));
        styles.registerFont(STEP_PARAMETER_FONT, styles.getFontOrDefault(CODE_FONT, NORMAL, emphasizedColor));
        styles.registerFont(STEP_DOCSTRING, styles.getFontOrDefault(CODE_FONT, NORMAL, emphasizedColor));
        styles.registerFont(STEP_TABLE_CELL, styles.getFontOrDefault(CODE_FONT, NORMAL, styles.defaultColor()));

        styles.registerFont(TAG_FONT, metaFont);

        styles.registerFont(FirstPageRenderer.FIRST_PAGE_TITLE_FONT, new FontCopier(styles.defaultFont()).style(NORMAL).size(32).color(primaryColor).get());
        styles.registerFont(FirstPageRenderer.FIRST_PAGE_SUBJECT_FONT, new FontCopier(styles.defaultFont()).style(NORMAL).size(18).color(emphasizedColor).get());

        styles.registerFont(HeaderFooter.HEADER_FONT, new FontCopier(styles.defaultFont()).noBold().size(10f).get());
        styles.registerFont(HeaderFooter.FOOTER_FONT, new FontCopier(styles.defaultFont()).noBold().size(10f).get());
    }

    private void configureColors(Styles styles) {
        styles.registerColor(Settings.PRIMARY_COLOR, DARK_RED);
        styles.registerColor(EMPHASIZE_COLOR, Colors.GRAY);
        styles.registerColor(HeaderFooter.HEADER_LINE_COLOR, DARK_RED);
    }
}
