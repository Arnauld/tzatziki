package tzatziki.analysis.java;

import com.google.common.collect.FluentIterable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Grammar {

    private final PackageEntry root;

    public Grammar() {
        this.root = new PackageEntry("");
    }

    public void declarePackage(PackageEntry subPkgEntry) {
        root.declareSubPackage(subPkgEntry);
    }

    public void declareClass(ClassEntry classEntry) {
        root.declareClass(classEntry);
    }

    public boolean hasEntries() {
        return root.hasEntries();
    }

    public FluentIterable<PackageEntry> packages() {
        return root.subPackages();
    }

    public FluentIterable<ClassEntry> classes() {
        return root.classes();
    }

    public FluentIterable<MethodEntry> matchingEntries(String text) {
        return root.matchingEntries(text);
    }
}
