package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface HasTags {
    FluentIterable<String> tags();
}
