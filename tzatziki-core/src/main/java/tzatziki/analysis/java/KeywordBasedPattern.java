package tzatziki.analysis.java;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class KeywordBasedPattern {
    private final String keyword;
    private final String pattern;

    public KeywordBasedPattern(String keyword, String pattern) {
        this.keyword = keyword;
        this.pattern = pattern;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getPattern() {
        return pattern;
    }
}
