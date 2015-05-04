package tzatziki.pdf.emitter;

import com.google.common.base.Optional;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import gutenberg.itext.Styles;
import gutenberg.itext.model.Markdown;
import gutenberg.util.KeyValues;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.pdf.Comments;
import tzatziki.pdf.Settings;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureEmitter implements Emitter<FeatureExec> {

    public static final String DISPLAY_URI = "feature-display-uri";
    public static final String DISPLAY_TAGS = "feature-display-tags";
    public static final String FEATURE_HEADER_LEVEL_OFFSET = "feature-header-level-offset";

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);
    //

    @Override
    public void emit(FeatureExec feature, ITextContext emitterContext) {
        KeyValues kvs = emitterContext.keyValues();
        Sections sections = emitterContext.sections();

        Integer rawOffset = kvs.getInteger(FEATURE_HEADER_LEVEL_OFFSET).orNull();
        int headerLevel = 1 + ((rawOffset == null) ? 0 : rawOffset);

        Section featureChap = sections.newSection(feature.name(), headerLevel);
        try {

            // Uri
            if (kvs.getBoolean(DISPLAY_URI, true)) {
                emitUri(feature, emitterContext);
            }

            // Tags
            if (kvs.getBoolean(DISPLAY_TAGS, true)) {
                emitTags(feature, emitterContext);
            }

            // Description
            emitDescription(feature, emitterContext);

            // Scenario
            for (ScenarioExec scenario : feature.scenario()) {
                emitterContext.emit(ScenarioExec.class, scenario);
            }
        } finally {
            sections.leaveSection(headerLevel);
        }

        if(headerLevel==1)
            emitterContext.append(featureChap);
    }

    private void emitUri(FeatureExec feature, ITextContext emitterContext) {
        Styles styles = emitterContext.keyValues().<Styles>getNullable(Styles.class).get();
        Paragraph uri = new Paragraph("Uri: " + feature.uri(), styles.getFontOrDefault(Settings.META_FONT));
        emitterContext.append(uri);
    }

    private void emitTags(FeatureExec feature, ITextContext emitterContext) {
        emitterContext.emit(Tags.class, new Tags(feature.tags()));
    }

    protected void emitDescription(FeatureExec feature, ITextContext emitterContext) {
        // Description
        StringBuilder b = new StringBuilder();
        String description = feature.description();
        if (StringUtils.isNotBlank(description)) {
            b.append(description);
        }

        Optional<ScenarioExec> first = feature.scenario().first();
        if (first.isPresent()) {
            ScenarioExec scenarioExec = first.get();
            for (String comment : scenarioExec.comments()) {
                String uncommented = Comments.discardCommentChar(comment);
                if (!Comments.startsWithComment(uncommented)) { // double # case
                    b.append(uncommented).append(Comments.NL);
                }
            }
        }

        if (b.length() > 0) {
            log.debug("Description content >>{}<<", b);
            emitterContext.emit(Markdown.class, new Markdown(b.toString()));
        }
    }
}
