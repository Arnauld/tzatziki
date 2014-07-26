package tzatziki.analysis.step;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Step {
    private final String keyword;
    private final String text;
    private int grammarMatchCount;

    public Step(String keyword, String text) {
        this.keyword = keyword.trim();
        this.text = text;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getText() {
        return text;
    }

    public void traverse(FeatureVisitor visitor) {
        visitor.visitStep(this);
    }

    public void grammarMatchCount(int nb) {
        grammarMatchCount = nb;
    }

    @Override
    public String toString() {
        return "Step{" +
                "@" + keyword + '(' + text + ')' +
                ", matchCount=" + grammarMatchCount +
                '}';
    }
}
