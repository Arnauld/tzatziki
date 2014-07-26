package tzatziki.analysis.java;

import static tzatziki.util.Equal.areEquals;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UsedBy {
    private final String featureUri;
    private final String scenarioOutlineName;
    private final String scenarioName;

    public UsedBy(String featureUri, String scenarioOutlineName, String scenarioName) {
        this.featureUri = featureUri;
        this.scenarioOutlineName = scenarioOutlineName;
        this.scenarioName = scenarioName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsedBy usedBy = (UsedBy) o;
        return areEquals(featureUri, usedBy.featureUri)
                && areEquals(scenarioName, usedBy.scenarioName)
                && areEquals(scenarioOutlineName, usedBy.scenarioOutlineName);
    }

    @Override
    public int hashCode() {
        int result = featureUri.hashCode();
        result = 31 * result + (scenarioOutlineName != null ? scenarioOutlineName.hashCode() : 0);
        result = 31 * result + scenarioName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UsedBy{" +
                "featureUri='" + featureUri + '\'' +
                ", scenarioOutlineName='" + scenarioOutlineName + '\'' +
                ", scenarioName='" + scenarioName + '\'' +
                '}';
    }
}
