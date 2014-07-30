package tzatziki.analysis.step;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Background {
    private List<Step> stepList = Lists.newArrayList();

    public void add(Step step) {
        stepList.add(step);
    }
}
