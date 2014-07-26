package tzatziki.analysis.exec.model;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MatchExec {
    private final String location;
    private final List<String> arguments;

    public MatchExec(String location, List<String> arguments) {
        this.location = location;
        this.arguments = arguments;
    }
}
