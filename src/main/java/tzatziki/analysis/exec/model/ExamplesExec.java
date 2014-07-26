package tzatziki.analysis.exec.model;

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

    public ExamplesExec(String keyword, String name) {

        this.keyword = keyword;
        this.name = name;
    }

    public void declareTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public void declareComments(List<String> comments) {
        this.comments.addAll(comments);
    }

    public void declareDescription(String description) {
        this.description = description;
    }
}
