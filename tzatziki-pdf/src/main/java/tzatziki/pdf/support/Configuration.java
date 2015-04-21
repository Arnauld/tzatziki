package tzatziki.pdf.support;

import com.google.common.collect.Maps;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import gutenberg.itext.Colors;
import gutenberg.itext.FontCopier;
import gutenberg.itext.FontModifier;
import gutenberg.itext.HeaderFooter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Styles;
import gutenberg.itext.support.FirstPageRenderer;
import gutenberg.util.VariableResolver;
import tzatziki.pdf.Settings;
import tzatziki.pdf.emitter.DefaultPdfEmitters;
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

    public static final Object TITLE = "title";
    public static final Object SUB_TITLE = "sub-title";
    public static final Object HEADER_TITLE = "header-title";

    private Map<Object, Object> properties = Maps.newHashMap();
    private Map<Object, FontModifier> fontModifiers = Maps.newHashMap();
    private Map<Object, BaseColor> colors = Maps.newHashMap();

    public Configuration() {
        displayFeatureUri(true);
        displayFeatureTags(true);
        displayScenarioTags(true);
    }

    public Configuration declareProperty(Object key, Object value) {
        properties.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(Object key) {
        return (T) properties.get(key);
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

    public Configuration adjustFont(Object key, FontModifier modifier) {
        fontModifiers.put(key, modifier);
        return this;
    }

    public Configuration defineColor(Object key, BaseColor color) {
        colors.put(key, color);
        return this;
    }

    public void configureContext(ITextContext iTextContext) {
        configureEmitters(iTextContext);
        configureSettings(iTextContext);
        configureVariableResolver(iTextContext);
    }

    protected void configureVariableResolver(ITextContext iTextContext) {
        VariableResolver variableResolver = iTextContext.variableResolver();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            variableResolver.declare(
                    String.valueOf(entry.getKey()),
                    String.valueOf(entry.getValue()));
        }
    }

    protected void configureSettings(ITextContext iTextContext) {
        Settings settings = iTextContext.get(Settings.class);
        if (settings == null) {
            settings = new Settings();
            iTextContext.declare(Settings.class, settings);
        }

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
        configureFontModifiers(styles);
    }

    private void configureFontModifiers(Styles styles) {
        for (Map.Entry<Object, FontModifier> entry : fontModifiers.entrySet()) {
            styles.registerFontModifier(entry.getKey(), entry.getValue());
        }
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

        for (Map.Entry<Object, BaseColor> entry : colors.entrySet()) {
            styles.registerColor(entry.getKey(), entry.getValue());
        }
    }

    private void configureEmitters(ITextContext iTextContext) {
        new DefaultPdfEmitters().registerDefaults(iTextContext);
    }
}
