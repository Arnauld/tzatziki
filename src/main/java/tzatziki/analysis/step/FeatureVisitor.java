package tzatziki.analysis.step;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface FeatureVisitor {
    void enterFeature(Feature feature);
    void exitFeature(Feature feature);

    void enterScenario(Scenario scenario);
    void exitScenario(Scenario scenario);

    void enterScenarioOutline(ScenarioOutline scenario);
    void exitScenarioOutline(ScenarioOutline scenario);

    void visitStep(Step step);
}
