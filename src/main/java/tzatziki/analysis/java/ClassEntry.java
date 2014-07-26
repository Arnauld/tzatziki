package tzatziki.analysis.java;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ClassEntry extends Describable {
    private final String packageName;
    private final String name;
    private final List<MethodEntry> methodEntries;

    public ClassEntry(String packageName, String name) {
        this.packageName = packageName;
        this.name = name;
        this.methodEntries = Lists.newArrayList();
    }

    public String name() {
        return name;
    }

    public String qualifiedName() {
        return packageName + '.' + name();
    }

    public void declareEntry(MethodEntry methodEntry) {
        methodEntries.add(methodEntry);
    }

    public boolean hasEntries() {
        return !methodEntries.isEmpty();
    }

    public FluentIterable<MethodEntry> methods() {
        return FluentIterable.from(methodEntries);
    }

    public FluentIterable<MethodEntry> matchingEntries(final String text) {
        return methods().filter(new Predicate<MethodEntry>() {
            @Override
            public boolean apply(MethodEntry methodEntry) {
                return methodEntry.matches(text);
            }
        });
    }
}
