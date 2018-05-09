package tzatziki.analysis.check;

import java.util.List;

import org.junit.Assert;

import tzatziki.analysis.tag.TagDictionary;

/**
 * Check that at least on declared tag exist in the TagDictionary.
 * It can be used to check that there is no orphan test regarding a categorization.
 * 
 * @author pverdage
 *
 */
public class CheckAtLeastOneTagsExist implements TagChecker {

    @Override
    public void evaluate(TagDictionary dictionary, List<String> tags) {
        for (String tag : tags) {
            if (dictionary.containsTag(tag))
                return;
        }
        Assert.fail("No tag(s) in dictionary amongst: " + tags);
    }

}