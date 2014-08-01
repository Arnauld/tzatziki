package tzatziki.analysis.java;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import tzatziki.util.PackagePath;

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


    private PackageEntry recursivelyFoundOrCreate(String packageName) {
        if (PackagePath.areSamePackage(name, packageName))
            return this;

        String directSubPkg = PackagePath.directSubPackageOf(name, packageName);
        String qualifiedSub;
        if(name.length() > 0)
            qualifiedSub = name + "." + directSubPkg;
        else
            qualifiedSub = directSubPkg;

        for (PackageEntry pkg : subPkgEntries) {
            if (pkg.name().equals(qualifiedSub)) {
                return pkg.recursivelyFoundOrCreate(packageName);
            }
        }

        PackageEntry pkg = new PackageEntry(qualifiedSub);
        subPkgEntries.add(pkg);
        return pkg.recursivelyFoundOrCreate(packageName);
    }

    public void declareClass(ClassEntry classEntry) {
        recursivelyFoundOrCreate(classEntry.packageName()).mergeClass(classEntry);
    }

    private void mergeClass(ClassEntry classEntry) {
        for(ClassEntry entry : classEntries) {
            if(entry.name().equals(classEntry.name())) {
                entry.mergeClass(classEntry);
                return;
            }
        }
        this.classEntries.add(classEntry);
    }

    public void declareSubPackage(PackageEntry subPkgEntry) {
        recursivelyFoundOrCreate(subPkgEntry.name()).mergePackage(subPkgEntry);
    }

    private void mergePackage(PackageEntry subPkgEntry) {
        this.classEntries.addAll(subPkgEntry.classEntries);
        this.subPkgEntries.addAll(subPkgEntry.subPkgEntries);
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
