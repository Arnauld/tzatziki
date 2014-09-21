package tzatziki.pdf.emitter;

import com.google.common.base.Optional;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.pdf.Comments;
import tzatziki.pdf.Settings;
import gutenberg.itext.model.Markdown;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureEmitter implements Emitter<FeatureExec> {

    public static final String DISPLAY_URI = "feature-display-uri";
    public static final String DISPLAY_TAGS = "feature-display-tags";

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);
    //

    @Override
    public void emit(FeatureExec feature, ITextContext emitterContext) {
        Settings settings = emitterContext.get(Settings.class);
        Sections sections = emitterContext.sections();

        Section featureChap = sections.newSection(feature.name(), 1);
        try {

            // Uri
            if (settings.getBoolean(DISPLAY_URI, true)) {
                emitUri(feature, emitterContext);
            }

            // Tags
            if (settings.getBoolean(DISPLAY_TAGS, true)) {
                emitTags(feature, emitterContext);
            }

            // Description
            emitDescription(feature, emitterContext);

            // Scenario
            for (ScenarioExec scenario : feature.scenario()) {
                emitterContext.emit(ScenarioExec.class, scenario);
            }
        } finally {
            sections.leaveSection(1);
        }

        emitterContext.append(featureChap);
    }

    private void emitUri(FeatureExec feature, ITextContext emitterContext) {
        Settings settings = emitterContext.get(Settings.class);
        Paragraph uri = new Paragraph("Uri: " + feature.uri(), settings.styles().getFontOrDefault(Settings.META_FONT));
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
