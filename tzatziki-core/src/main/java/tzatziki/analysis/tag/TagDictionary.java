package tzatziki.analysis.tag;

import com.google.common.collect.Maps;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TagDictionary {

    private static final String PREFIX = "@";

    private Map<String, Tag> tags = Maps.newConcurrentMap();

    public TagDictionary() {
    }

    public TagDictionary clear() {
        tags.clear();
        return this;
    }

    public TagDictionary declareTags(Properties properties) {
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String tagKey = (String) propertyNames.nextElement();
            declareTag(tagKey, properties.getProperty(tagKey));
        }
        return this;
    }

    public TagDictionary declareTag(String tagKey, String description) {
        getOrInitTag(tagKey).declareDescription(description);
        return this;
    }

    private Tag getOrInitTag(String tagKey) {
        String formatted = format(tagKey);
        Tag tag = tags.get(formatted);
        if (tag == null) {
            tag = new Tag(formatted);
            tags.put(formatted, tag);
        }
        return tag;
    }

    private String format(String tagKey) {
        String formatted = tagKey.trim();
        if (!formatted.startsWith(PREFIX))
            return PREFIX + formatted;
        else
            return formatted;
    }

    public void declareTags(TagDictionary dictionary) {
        tags.putAll(dictionary.tags);
    }

    public boolean containsTag(String tag) {
        return tags.containsKey(format(tag));
    }

    public TagDictionary declareTag(String tag) {
        return declareTag(tag, "");
    }
}
