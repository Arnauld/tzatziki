package tzatziki.pdf.emitter;

import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.pdf.EmitterContext;
import tzatziki.pdf.model.Markdown;
import tzatziki.pdf.model.Steps;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DefaultEmitters {
    public void registerDefaults(EmitterContext context) {
        context.register(FeatureExec.class, new FeatureEmitter());
        context.register(ScenarioExec.class, new ScenarioEmitter());
        context.register(Markdown.class, new MarkdownEmitter());
        context.register(Steps.class, new StepsEmitter());
        context.register(Tags.class, new TagsEmitter());
    }


}
