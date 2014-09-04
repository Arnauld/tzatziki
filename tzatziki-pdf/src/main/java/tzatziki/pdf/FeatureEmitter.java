package tzatziki.pdf;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.itext.Sections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureEmitter implements PdfEmitter<FeatureExec> {

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);
    //
    private Margin descriptionMargin = Margin.create(10);

    @Override
    public void emit(FeatureExec feature, EmitterContext emitterContext) {
        Configuration configuration = emitterContext.getConfiguration();
        Sections sections = emitterContext.sections();

        Font font = sections.sectionTitlePrimaryFont(1);
        Paragraph title = new Paragraph(feature.name(), font);
        Section featureChap = sections.newSection(title, 1);

        // Uri
        if (configuration.shouldDisplayUri()) {
            Paragraph uri = new Paragraph("Uri: " + feature.uri(), configuration.defaultMetaFont());
            featureChap.add(uri);
        }

        // Description
        String description = feature.description();
        if (StringUtils.isNotBlank(description)) {
            Paragraph paragraph = new Paragraph("", configuration.defaultFont());
            paragraph.setSpacingBefore(descriptionMargin.marginTop);
            paragraph.setSpacingAfter(descriptionMargin.marginBottom);
            paragraph.setIndentationLeft(descriptionMargin.marginLeft);
            emitterContext.emit(Markdown.class, new Markdown(description));
            featureChap.add(paragraph);
        }

        // Scenario
        for (ScenarioExec scenario : feature.scenario()) {
            emitterContext.emit(ScenarioExec.class, scenario);
        }

        sections.leaveSection(1);

        try {
            emitterContext.emit(featureChap);
        } catch (DocumentException e) {
            log.warn("Failed to emit feature '{}'", feature.name(), e);
        }
    }
}
