package tzatziki.pdf.emitter;

import com.google.common.base.Optional;
import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.pdf.Comments;
import tzatziki.pdf.Settings;
import gutenberg.itext.model.Markdown;
import tzatziki.pdf.model.Steps;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioEmitter implements Emitter<ScenarioExec> {

    public static final String DISPLAY_TAGS = "scenario-display-tags";
    private final int hLevel;

    private Logger log = LoggerFactory.getLogger(FeatureEmitter.class);

    public ScenarioEmitter() {
        this(2);
    }

    public ScenarioEmitter(int hLevel) {
        this.hLevel = hLevel;
    }

    @Override
    public void emit(ScenarioExec scenario, ITextContext emitterContext) {
        Settings settings = emitterContext.get(Settings.class);
        Sections sections = emitterContext.sections();

        sections.newSection(scenario.name(), hLevel);
        try {
            if (settings.getBoolean(DISPLAY_TAGS, true)) {
                emitTags(scenario, emitterContext);
            }
            emitDescription(scenario, emitterContext);
            emitEmbeddings(scenario, emitterContext);
            emitSteps(scenario, emitterContext);
        } finally {
            sections.leaveSection(hLevel); // end-of-scenario
        }
    }

    private void emitTags(ScenarioExec scenario, ITextContext emitterContext) {
        emitterContext.emit(Tags.class, new Tags(scenario.tags()));
    }

    private void emitSteps(ScenarioExec scenario, ITextContext emitterContext) {
        emitterContext.emit(Steps.class, new Steps(scenario.steps()));
    }

    protected void emitEmbeddings(ScenarioExec scenario, ITextContext emitterContext) {
    }

    protected void emitDescription(ScenarioExec scenario, ITextContext emitterContext) {
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
