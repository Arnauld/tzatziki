package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface HasComments {
    FluentIterable<String> comments();
}
