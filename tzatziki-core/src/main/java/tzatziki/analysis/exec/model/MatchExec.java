package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MatchExec {
    private final String location;
    private final List<Arg> arguments;

    public MatchExec(String location, List<Arg> arguments) {
        this.location = location;
        this.arguments = arguments;
    }

    public FluentIterable<Arg> getArgs() {
        return FluentIterable.from(arguments);
    }

    public String getLocation() {
        return location;
    }

    public static class Arg {
        public final String val;
        public final Integer offset;

        public Arg(String val, Integer offset) {

            this.val = val;
            this.offset = offset;
        }

        public Integer getOffset() {
            return offset;
        }

        public String getVal() {
            return val;
        }
    }
}
