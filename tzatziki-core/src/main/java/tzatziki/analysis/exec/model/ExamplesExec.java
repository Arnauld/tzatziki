package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExamplesExec {
    private final String keyword;
    private final String name;
    private List<String> tags = Lists.newArrayList();
    private List<String> comments = Lists.newArrayList();
    private String description;
    private List<ExamplesRow> examplesRows;

    public ExamplesExec(String keyword, String name) {
        this.keyword = keyword;
        this.name = name;
    }

    public ExamplesExec declareTags(List<String> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public ExamplesExec declareComments(List<String> comments) {
        this.comments.addAll(comments);
        return this;
    }

    public ExamplesExec declareDescription(String description) {
        this.description = description;
        return this;
    }

    public ExamplesExec declareRows(List<ExamplesRow> examplesRows) {
        this.examplesRows = examplesRows;
        return this;
    }

    public String name() {
        return name;
    }

    public FluentIterable<String> tags() {
        return FluentIterable.from(tags);
    }

    public FluentIterable<ExamplesRow> rows() {
        return FluentIterable.from(examplesRows);
    }

    public ExamplesExec recursiveCopy() {
        return new ExamplesExec(keyword, name)
                .declareTags(tags)
                .declareComments(comments)
                .declareDescription(description)
                .declareRows(examplesRows);
    }

    public int rowCount() {
        return examplesRows.size();
    }

    public int columnCount() {
        return examplesRows.get(0).cells().size();
    }

    public String keyword() {
        return keyword;
    }
}
