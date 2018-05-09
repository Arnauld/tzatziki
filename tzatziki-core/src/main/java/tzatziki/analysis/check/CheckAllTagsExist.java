package tzatziki.analysis.check;

import java.util.List;

import org.junit.Assert;

import com.google.common.collect.Lists;

import tzatziki.analysis.tag.TagDictionary;

/**
 * Check that all tags are declared in the TagDictionary
 * @author pverdage
 *
 */
public class CheckAllTagsExist implements TagChecker {

    @Override
    public void evaluate(TagDictionary dictionary, List<String> tags) {
        List<String> unknown = Lists.newArrayList();
        for (String tag : tags) {
            if (!dictionary.containsTag(tag))
                unknown.add(tag);
        }
        if (!unknown.isEmpty())
            Assert.fail("Unknown tag(s): " + unknown);
    }

}