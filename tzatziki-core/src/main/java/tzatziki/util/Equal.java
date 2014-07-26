package tzatziki.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Equal {
    public static boolean areEquals(Object strOne, Object strTwo) {
        if (strOne == null) {
            return strTwo == null;
        } else {
            return strTwo != null && strOne.equals(strTwo);
        }
    }

}
