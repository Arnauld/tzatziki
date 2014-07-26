package tzatziki.analysis.java;

import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MethodEntry extends Describable {
    private final String methodName;
    private final List<String> args;

    private final List<String> patterns = new ArrayList<String>();
    private final List<Parameter> parameters = new ArrayList<Parameter>();
    private final Set<UsedBy> usedBySet = new HashSet<UsedBy>();

    public MethodEntry(String methodName, List<String> args) {
        this.methodName = methodName;
        this.args = args;
    }

    public String name() {
        return methodName;
    }

    public String signature() {
        return methodName + "(" + args + ")";
    }

    public FluentIterable<String> patterns() {
        return FluentIterable.from(patterns);
    }

    public boolean hasPatterns() {
        return !patterns.isEmpty();
    }

    public void declarePattern(String keyword, String pattern) {
        if (pattern != null)
            patterns.add(pattern);
    }

    public List<Parameter> parameters() {
        return parameters;
    }

    public void defineParameter(int index,
                                String name,
                                String parameterDoc) {
        parameters.add(new Parameter(index, name, parameterDoc));
    }

    public Parameter parameter(int index) {
        return parameters.get(index);
    }

    public void declareUsedBy(UsedBy usedBy) {
        usedBySet.add(usedBy);
    }

    public boolean matches(String text) {
        for (String regex : patterns) {
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(text).matches())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "patterns=" + patterns +
                ", parameters=" + parameters +
                ", usedBySet=" + usedBySet +
                '}';
    }

}
