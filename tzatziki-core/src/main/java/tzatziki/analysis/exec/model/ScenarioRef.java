package tzatziki.analysis.exec.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioRef {
    private final String featureUri;
    private final String featureName;
    private final String scenarioName;
    private final LineRange lineRange;

    public ScenarioRef(String featureUri, String featureName, LineRange lineRange, String scenarioName) {
        this.featureUri = featureUri;
        this.featureName = featureName;
        this.scenarioName = scenarioName;
        this.lineRange = lineRange;
    }

    public String featureUri() {
        return featureUri;
    }

    public String featureName() {
        return featureName;
    }

    public String scenarioName() {
        return scenarioName;
    }

    public LineRange scenarioLineRange() {
        return lineRange;
    }

    @Override
    public String toString() {
        return "ScenarioRef{" +
                "featureUri='" + featureUri + '\'' +
                ", featureName='" + featureName + '\'' +
                ", scenarioName='" + scenarioName + '\'' +
                ", lineRange=" + lineRange +
                '}';
    }
}
