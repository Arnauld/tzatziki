package tzatziki.analysis.java;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Parameter {
    private final int index;
    private final String name;
    private final String parameterDoc;

    public Parameter(int index, String name, String parameterDoc) {
        this.index = index;
        this.name = name;
        this.parameterDoc = parameterDoc;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getDoc() {
        return parameterDoc;
    }
}
