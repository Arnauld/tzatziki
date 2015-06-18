package tzatziki.pdf.emitter;

import gutenberg.itext.ITextContext;
import tzatziki.analysis.exec.model.BackgroundExec;
import tzatziki.analysis.exec.model.Embedded;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.support.TagViews;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.pdf.model.ScenarioOutlineWithResolved;
import tzatziki.pdf.model.Steps;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DefaultPdfEmitters {
    public void registerDefaults(ITextContext context) {
        context.register(FeatureExec.class, new FeatureEmitter());
        context.register(ScenarioExec.class, new ScenarioEmitter());
        context.register(BackgroundExec.class, new BackgroundEmitter());
        context.register(ScenarioOutlineWithResolved.class, new ScenarioOutlineEmitter());
        context.register(Steps.class, new StepsEmitter());
        context.register(Tags.class, new TagsEmitter());
        context.register(TagDictionary.class, new TagDictionaryEmitter());
        context.register(TagViews.class, new TagViewsEmitter());
        context.register(Embedded.class, new EmbeddedEmitter());
        context.register(Grammar.class, new GrammarEmitter());
    }


}
