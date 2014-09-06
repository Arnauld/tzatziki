package tzatziki.pdf;

import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Comments {
    public static final String NL = "\n";
    private static final String COMMENT = "#";
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\s*" + COMMENT);

    public static String discardCommentChar(String value) {
        return COMMENT_PATTERN.matcher(value).replaceAll("");
    }

    public static boolean startsWithComment(String text) {
        return text.startsWith(COMMENT);
    }
}
