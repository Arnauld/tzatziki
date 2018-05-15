package tzatziki.junit;

import static org.junit.runner.Description.createTestDescription;

import java.util.List;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.common.collect.Lists;

import tzatziki.analysis.check.CucumberPart;
import tzatziki.analysis.check.TagChecker;
import tzatziki.analysis.step.Feature;
import tzatziki.analysis.step.Scenario;
import tzatziki.analysis.step.ScenarioOutline;
import tzatziki.analysis.tag.TagDictionary;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FeatureTagRunner extends ParentRunner<FeatureTagRunner.TagCheck> {

    private final Feature feature;
    private final TagDictionary tagDictionary;
    private final TagChecker tagChecker;
    private final List<TagCheck> children;
    private Description description;
    private Set<CucumberPart> scopeToTest;

    public FeatureTagRunner(Class<?> klazz, Feature feature, TagDictionary tagDictionary, TagChecker tagChecker, Set<CucumberPart> scopeToTest) throws InitializationError {
        super(klazz);
        this.feature = feature;
        this.tagDictionary = tagDictionary;
        this.tagChecker = tagChecker;
        this.scopeToTest = scopeToTest;
        this.children = initChildren();
    }

    private List<TagCheck> initChildren() {
        List<TagCheck> children = Lists.newArrayList();
        if (scopeToTest.contains(CucumberPart.Feature)) {
            children.add(new TagCheck(createTestDescription(feature.uri(), feature.name()), feature.getTags(), tagDictionary, tagChecker));
        }

        if (scopeToTest.contains(CucumberPart.Scenario)) {
            for (Scenario scenario : feature.scenario())
                children.add(new TagCheck(createTestDescription(feature.uri(), scenario.getVisualName()), scenario.getTags(), tagDictionary, tagChecker));
        }
        if (scopeToTest.contains(CucumberPart.ScenarioOutline)) {
            for (ScenarioOutline outline : feature.scenarioOutlines())
                children.add(new TagCheck(createTestDescription(feature.uri(), outline.getVisualName()), outline.getTags(), tagDictionary, tagChecker));
        }
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
        private final TagChecker checker;

        public TagCheck(Description description, List<String> tags, TagDictionary dictionary, TagChecker checker) {
            this.description = description;
            this.tags = tags;
            this.dictionary = dictionary;
            this.checker = checker;
        }

        public Description getDescription() {
            return description;
        }

        @Override
        public void evaluate() throws Throwable {
            checker.evaluate(dictionary, tags);
        }
    }
}
