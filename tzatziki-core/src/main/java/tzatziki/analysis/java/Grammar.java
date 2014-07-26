package tzatziki.analysis.java;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Grammar {

    private final List<PackageEntry> pkgEntries;

    public Grammar() {
        this.pkgEntries = Lists.newArrayList();
    }

    public void declarePackage(PackageEntry subPkgEntry) {
        pkgEntries.add(subPkgEntry);
    }

    public boolean hasEntries() {
        return !pkgEntries.isEmpty();
    }

    public FluentIterable<PackageEntry> packages() {
        return FluentIterable.from(pkgEntries);
    }

    public FluentIterable<MethodEntry> matchingEntries(String text) {
        List<MethodEntry> matches = Lists.newArrayList();
        for (PackageEntry subGroup : pkgEntries) {
            subGroup.matchingEntries(text).copyInto(matches);
        }
        return FluentIterable.from(matches);
    }
}
