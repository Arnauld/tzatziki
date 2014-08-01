package tzatziki.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PackagePath {

    public static boolean isSubPackageOf(String parent, String packageName) {
        return packageName.startsWith(parent)
                && !areSamePackage(parent, packageName);
    }

    public static String directSubPackageOf(String parent, String packageName) {
        if (areSamePackage(parent, packageName))
            return null;
        if (!isSubPackageOf(parent, packageName))
            throw new IllegalArgumentException("Package '" + packageName + "' is not a subPackage of '" + parent + "'");

        int dec = parent.length();
        if (dec > 0)
            dec++; // add '.'

        String subTree = packageName.substring(dec);
        int nextPkg = subTree.indexOf('.');
        if (nextPkg < 0)
            return subTree;
        else
            return subTree.substring(0, nextPkg);
    }

    public static boolean areSamePackage(String packageName1, String packageName2) {
        return packageName1.equals(packageName2);
    }
}
