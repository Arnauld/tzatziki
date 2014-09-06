package tzatziki.pdf;

import com.google.common.base.Optional;
import gutenberg.itext.Sections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.StepExec;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioEmitter implements PdfEmitter<ScenarioExec> {

    public static final String DISPLAY_TAGS = "scenario-display-tags";

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);


    @Override
    public void emit(ScenarioExec scenario, EmitterContext emitterContext) {
        Configuration configuration = emitterContext.getConfiguration();
        Sections sections = emitterContext.sections();

        sections.newSection(scenario.name(), 2);
        try {
            if (configuration.getBoolean(DISPLAY_TAGS, true)) {
                emitTags(scenario, emitterContext);
            }
            emitDescription(scenario, emitterContext);
            emitEmbeddings(scenario, emitterContext);
            emitSteps(scenario, emitterContext);
        } finally {
            sections.leaveSection(2); // end-of-scenario
        }
    }

    private void emitTags(ScenarioExec scenario, EmitterContext emitterContext) {
        emitterContext.emit(Tags.class, new Tags(scenario.tags()));
    }

    private void emitSteps(ScenarioExec scenario, EmitterContext emitterContext) {
        emitterContext.emit(Steps.class, new Steps(scenario.steps()));
    }

    protected void emitEmbeddings(ScenarioExec scenario, EmitterContext emitterContext) {
    }

    protected void emitDescription(ScenarioExec scenario, EmitterContext emitterContext) {
        // Description
        StringBuilder b = new StringBuilder();
        String description = scenario.description();
        if (StringUtils.isNotBlank(description)) {
            b.append(description);
        }

        Optional<StepExec> first = scenario.steps().first();
        if (first.isPresent()) {
            StepExec stepExec = first.get();
            for (String comment : stepExec.comments()) {
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
