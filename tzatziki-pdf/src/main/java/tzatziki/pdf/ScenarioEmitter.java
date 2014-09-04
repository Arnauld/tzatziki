package tzatziki.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.itext.Sections;
import tzatziki.analysis.exec.model.ScenarioExec;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioEmitter implements PdfEmitter<ScenarioExec> {
    @Override
    public void emit(ScenarioExec scenario, EmitterContext emitterContext) {
        Sections sections = emitterContext.sections();

        Font font = sections.sectionTitlePrimaryFont(2);
        Paragraph title = new Paragraph(scenario.name(), font);
        Section scenarioChap = sections.newSection(title, 2);

        sections.leaveSection(2);
    }
}
