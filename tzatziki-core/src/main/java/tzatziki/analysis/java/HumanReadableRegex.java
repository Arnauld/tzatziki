package tzatziki.analysis.java;

import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HumanReadableRegex {
    private final String rawPattern;
    private final Pattern pattern;

    public HumanReadableRegex(String rawPattern) {
        this.rawPattern = rawPattern;
        this.pattern = Pattern.compile(rawPattern);
    }

    public String humanReadable() {
        String s = discardStartAndEndModifier(rawPattern);
        s = replaceCapturingDigits(s);
        s = replaceCapturingDecimal(s);
        s = replaceCapturingAnything(s);
        s = replaceOptionalCharacterWithParenthesis(s);
        return s;
    }

    private String replaceOptionalCharacterWithParenthesis(String s) {
        return s.replaceAll("([a-z])\\?", "($1)");
    }

    private String replaceCapturingAnything(String s) {
        return s.replaceAll("\\((\\[\\^\"\\][+*]|\\.[+*])\\)", "<anything>");
    }

    private String replaceCapturingDigits(String s) {
        return s.replaceAll("\\(\\\\d[+*]\\)", "<integer>");
    }

    private String replaceCapturingDecimal(String s) {
        return s.replaceAll("\\(\\\\d[+](\\\\.\\?)?\\|\\\\d[*]\\\\.\\\\d[+]\\)", "<decimal>");
    }


    private String discardStartAndEndModifier(String value) {
        return value
                .replaceAll("^\\^(.*)\\$", "$1")
                .replaceAll("^\\^(.*)", "$1")
                .replaceAll("(.*)\\$", "$1");
    }
}
