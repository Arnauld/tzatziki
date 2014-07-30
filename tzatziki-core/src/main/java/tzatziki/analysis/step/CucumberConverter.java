package tzatziki.analysis.step;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import cucumber.runtime.model.CucumberBackground;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.model.Tag;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CucumberConverter {

    public Feature convert(CucumberFeature cucumberFeature) {
        Feature feature = new Feature(cucumberFeature.getUri(), cucumberFeature.getGherkinFeature().getName());
        feature.addTags(convertTags(cucumberFeature.getGherkinFeature().getTags()));
        for (CucumberTagStatement featureElement : cucumberFeature.getFeatureElements()) {
            if (featureElement instanceof CucumberScenario) {
                CucumberScenario scenario = (CucumberScenario) featureElement;
                feature.add(convertScenario(scenario));

                // Workaround... no way to retrieve the background
                CucumberBackground cucumberBackground = scenario.getCucumberBackground();
                if (cucumberBackground != null)
                    feature.background(convertBackground(cucumberBackground));
            } else if (featureElement instanceof CucumberScenarioOutline) {
                CucumberScenarioOutline scenarioOutline = (CucumberScenarioOutline) featureElement;
                feature.add(convertOutline(scenarioOutline));
            }
        }
        return feature;
    }

    private List<String> convertTags(List<Tag> tags) {
        return FluentIterable.from(tags).transform(new Function<Tag, String>() {
            @Override
            public String apply(Tag tag) {
                return tag.getName();
            }
        }).toList();
    }

    private ScenarioOutline convertOutline(CucumberScenarioOutline cucumberScenarioOutline) {
        ScenarioOutline outline = new ScenarioOutline();
        outline.setVisualName(cucumberScenarioOutline.getVisualName());
        outline.addTags(convertTags(cucumberScenarioOutline.getGherkinModel().getTags()));

        for (CucumberExamples cucumberExamples : cucumberScenarioOutline.getCucumberExamplesList()) {
            List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
            for (CucumberScenario exampleScenario : exampleScenarios) {
                outline.add(convertScenario(exampleScenario));
            }
        }
        return outline;
    }

    private Scenario convertScenario(CucumberScenario cucumberScenario) {
        Scenario scenario = new Scenario();
        scenario.setVisualName(cucumberScenario.getVisualName());
        scenario.addTags(convertTags(cucumberScenario.getGherkinModel().getTags()));

        for (gherkin.formatter.model.Step step : cucumberScenario.getSteps())
            scenario.add(convertStep(step));
        return scenario;
    }

    private Background convertBackground(CucumberBackground cucumberBackground) {
        Background background = new Background();
        for (gherkin.formatter.model.Step step : cucumberBackground.getSteps())
            background.add(convertStep(step));
        return background;
    }

    private Step convertStep(gherkin.formatter.model.Step cucumberStep) {
        return new Step(cucumberStep.getKeyword(), cucumberStep.getName());
    }
}
