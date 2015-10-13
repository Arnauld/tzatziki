package tzatziki.analysis.java;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MethodEntry extends Describable {
    private final String methodName;
    private final List<String> args;

    private final List<KeywordBasedPattern> patterns = Lists.newArrayList();
    private final List<Parameter> parameters = Lists.newArrayList();
    private final Set<UsedBy> usedBySet = Sets.newHashSet();

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
        return FluentIterable.from(patterns).transform(new Function<KeywordBasedPattern, String>() {
            @Override
            public String apply(KeywordBasedPattern input) {
                return input.getPattern();
            }
        });
    }

    public FluentIterable<KeywordBasedPattern> keywordBasedPatterns() {
        return FluentIterable.from(patterns);
    }

    public boolean hasPatterns() {
        return !patterns.isEmpty();
    }

    public void declarePattern(String keyword, String pattern) {
        if (pattern != null)
            patterns.add(new KeywordBasedPattern(keyword, pattern));
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
        for (KeywordBasedPattern p : patterns) {
            Pattern pattern = Pattern.compile(p.getPattern());
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
