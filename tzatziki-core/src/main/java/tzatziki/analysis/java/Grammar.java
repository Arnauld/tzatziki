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

    public void traverse(GrammarVisitor visitor) {
        for (ClassEntry classEntry : classes()) {
            traverse(null, classEntry, visitor);
        }
        for (PackageEntry packageEntry : packages()) {
            traverse(packageEntry, visitor);
        }
    }

    private void traverse(PackageEntry packageEntry, ClassEntry classEntry, GrammarVisitor visitor) {
        visitor.enter(packageEntry, classEntry);
        for (MethodEntry methodEntry : classEntry.methods()) {
            visitor.visit(packageEntry, classEntry, methodEntry);
        }
        visitor.leave(packageEntry, classEntry);
    }

    private void traverse(PackageEntry packageEntry, GrammarVisitor visitor) {
        visitor.enter(packageEntry);
        for (PackageEntry subPkgEntry : packageEntry.subPackages()) {
            traverse(subPkgEntry, visitor);
        }
        for (ClassEntry classEntry : packageEntry.classes()) {
            traverse(packageEntry, classEntry, visitor);
        }
        visitor.leave(packageEntry);
    }
}
