package tzatziki.analysis.exec.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LineRange {
    public final Integer first;
    public final Integer last;

    public LineRange(Integer first, Integer last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public String toString() {
        return "LineRange[" + first + ", " + last + ']';
    }
}
