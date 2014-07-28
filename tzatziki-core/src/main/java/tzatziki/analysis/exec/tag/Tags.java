package tzatziki.analysis.exec.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Tags {

    public static Tags from(String... tags) {
        return new Tags(Arrays.asList(tags));
    }

    public static Tags from(Collection<String> tags) {
        return new Tags(tags);
    }

    private final Collection<String> tags;

    public Tags(Collection<String> tags) {
        this.tags = tags;
    }


    public List<String> toList() {
        return new ArrayList<String>(tags);
    }

}
