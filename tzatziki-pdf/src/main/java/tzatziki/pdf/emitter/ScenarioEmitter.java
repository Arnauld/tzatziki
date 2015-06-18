package tzatziki.pdf.emitter;

import gutenberg.itext.Emitter;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import gutenberg.util.KeyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.ScenarioExec;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioEmitter implements Emitter<ScenarioExec> {

    public static final String DISPLAY_TAGS = "scenario-display-tags";
    private final int hLevel;
    private StepContainerEmitter stepsEmitter;

    private Logger log = LoggerFactory.getLogger(ScenarioEmitter.class);

    public ScenarioEmitter() {
        this(2);
    }

    public ScenarioEmitter(int hLevel) {
        this(hLevel, new StepContainerEmitter());
    }

    public ScenarioEmitter(int hLevel, StepContainerEmitter stepsEmitter) {
        this.hLevel = hLevel;
        this.stepsEmitter = stepsEmitter;
    }

    @Override
    public void emit(ScenarioExec scenario, ITextContext emitterContext) {
        Sections sections = emitterContext.sections();
        KeyValues kvs = emitterContext.keyValues();

        Integer rawOffset = kvs.getInteger(FeatureEmitter.FEATURE_HEADER_LEVEL_OFFSET).or(0);
        int headerLevel = hLevel + rawOffset;

        sections.newSection(scenario.name(), headerLevel);
        try {
            if (kvs.getBoolean(DISPLAY_TAGS, true)) {
                stepsEmitter.emitTags(scenario, emitterContext);
            }
            stepsEmitter.emitDescription(scenario, emitterContext);
            stepsEmitter.emitEmbeddings(scenario, emitterContext);
            stepsEmitter.emitSteps(scenario, emitterContext);
        } finally {
            sections.leaveSection(headerLevel); // end-of-scenario
        }
    }
}
