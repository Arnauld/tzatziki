package tzatziki.pdf.emitter;

import com.google.common.base.Optional;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.itext.Sections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.pdf.Comments;
import tzatziki.pdf.Configuration;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.Margin;
import tzatziki.pdf.PdfEmitter;
import tzatziki.pdf.model.Markdown;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureEmitter implements PdfEmitter<FeatureExec> {

    public static final String DISPLAY_URI = "feature-display-uri";
    public static final String DISPLAY_TAGS = "feature-display-tags";

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);
    //
    private Margin descriptionMargin = Margin.create(10);

    @Override
    public void emit(FeatureExec feature, EmitterContext emitterContext) {
        Configuration configuration = emitterContext.getConfiguration();
        Sections sections = emitterContext.sections();

        Section featureChap = sections.newSection(feature.name(), 1);
        try {

            // Uri
            if (configuration.getBoolean(DISPLAY_URI, true)) {
                Paragraph uri = new Paragraph("Uri: " + feature.uri(), configuration.defaultMetaFont());
                featureChap.add(uri);
            }

            // Description
            emitDescription(feature, featureChap, emitterContext);

            // Scenario
            for (ScenarioExec scenario : feature.scenario()) {
                emitterContext.emit(ScenarioExec.class, scenario);
            }
        } finally {
            sections.leaveSection(1);
        }

        try {
            emitterContext.emit(featureChap);
        } catch (DocumentException e) {
            log.warn("Failed to emit feature '{}'", feature.name(), e);
        }
    }

    protected void emitDescription(FeatureExec feature, Section featureChap, EmitterContext emitterContext) {
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
