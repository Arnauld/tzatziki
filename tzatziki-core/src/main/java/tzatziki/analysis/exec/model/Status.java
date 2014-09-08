package tzatziki.analysis.exec.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum Status {
    Passed,
    Skipped,
    Undefined,
    Failed,
    Pending;

    public static Status fromString(String status) {
        for (Status s : values()) {
            if(s.name().equalsIgnoreCase(status))
                return s;
        }
        return null;
    }

}
