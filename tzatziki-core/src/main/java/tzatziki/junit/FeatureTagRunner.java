package tzatziki.junit;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import tzatziki.analysis.step.Feature;
import tzatziki.analysis.step.Scenario;
import tzatziki.analysis.step.ScenarioOutline;
import tzatziki.analysis.tag.TagDictionary;

import java.util.List;

import static org.junit.runner.Description.createTestDescription;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureTagRunner extends ParentRunner<FeatureTagRunner.TagCheck> {

    private final Feature feature;
    private final TagDictionary tagDictionary;
    private final List<TagCheck> children;
    private Description description;

    public FeatureTagRunner(Class<?> klazz, Feature feature, TagDictionary tagDictionary) throws InitializationError {
        super(klazz);
        this.feature = feature;
        this.tagDictionary = tagDictionary;
        this.children = initChildren();
    }

    private List<TagCheck> initChildren() {
        List<TagCheck> children = Lists.newArrayList();
        children.add(new TagCheck(createTestDescription(feature.uri(), feature.name()), feature.getTags(), tagDictionary));

        for (Scenario scenario : feature.scenario())
            children.add(new TagCheck(createTestDescription(feature.uri(), scenario.getVisualName()), scenario.getTags(), tagDictionary));
        for (ScenarioOutline outline : feature.scenarioOutlines())
            children.add(new TagCheck(createTestDescription(feature.uri(), outline.getVisualName()), outline.getTags(), tagDictionary));

        return children;
    }

    @Override
    public Description getDescription() {
        if (description == null) {
            description = Description.createSuiteDescription(feature.name());
            for (TagCheck child : getChildren()) {
                description.addChild(describeChild(child));
            }
        }
        return description;
    }

    @Override
    protected List<TagCheck> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(TagCheck child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(TagCheck child, RunNotifier notifier) {
        runLeaf(child, child.getDescription(), notifier);
    }

    public static class TagCheck extends Statement {

        private final Description description;
        private final List<String> tags;
        private final TagDictionary dictionary;

        public TagCheck(Description description, List<String> tags, TagDictionary dictionary) {
            this.description = description;
            this.tags = tags;
            this.dictionary = dictionary;
        }

        public Description getDescription() {
            return description;
        }

        @Override
        public void evaluate() throws Throwable {
            List<String> unknown = Lists.newArrayList();
            for (String tag : tags) {
                if (!dictionary.containsTag(tag))
                    unknown.add(tag);
            }
            System.out.println("TagCheck.evaluate(" + tags + ") ");
            if (!unknown.isEmpty())
                Assert.fail("Unknown tag(s): " + unknown);
        }
    }
}
