package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExamplesRow {
    private final List<String> comments;
    private final List<String> cells;
    private final Integer line;

    public ExamplesRow(List<String> comments, List<String> cells, Integer line) {
        this.comments = comments;
        this.cells = cells;
        this.line = line;
    }

    public FluentIterable<String> comments() {
        return FluentIterable.from(comments);
    }

    public FluentIterable<String> cells() {
        return FluentIterable.from(cells);
    }

    public Integer getLine() {
        return line;
    }
}
