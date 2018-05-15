package tzatziki.analysis.check;

import java.util.List;

import tzatziki.analysis.tag.TagDictionary;

public interface TagChecker {
    void evaluate(TagDictionary dictionary, List<String> tags);
}