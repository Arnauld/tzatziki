package tzatziki.analysis.java;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PackageEntry extends Describable {
    private final String name;
    private final List<ClassEntry> classEntries;
    private final List<PackageEntry> subPkgEntries;

    public PackageEntry(String name) {
        this.name = name;
        this.classEntries = Lists.newArrayList();
        this.subPkgEntries = Lists.newArrayList();
    }

    public String name() {
        return name;
    }

    public void declareClass(ClassEntry classEntry) {
        classEntries.add(classEntry);
    }

    public void declareSubPackage(PackageEntry subPkgEntry) {
        subPkgEntries.add(subPkgEntry);
    }

    public boolean hasEntries() {
        return hasSubPackageEntries() || hasClassEntries();
    }

    public boolean hasSubPackageEntries() {
        return !subPkgEntries.isEmpty();
    }

    public boolean hasClassEntries() {
        return !classEntries.isEmpty();
    }


    public FluentIterable<PackageEntry> subPackages() {
        return FluentIterable.from(subPkgEntries);
    }

    public FluentIterable<ClassEntry> classes() {
        return FluentIterable.from(classEntries);
    }

    public FluentIterable<MethodEntry> matchingEntries(String text) {
        List<MethodEntry> matches = Lists.newArrayList();
        for (PackageEntry subGroup : subPkgEntries) {
            subGroup.matchingEntries(text).copyInto(matches);
        }
        for (ClassEntry clazz : classEntries) {
            clazz.matchingEntries(text).copyInto(matches);
        }
        return FluentIterable.from(matches);
    }

}
