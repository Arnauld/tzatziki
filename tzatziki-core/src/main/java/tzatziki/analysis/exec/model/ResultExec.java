package tzatziki.analysis.exec.model;

import tzatziki.util.ExceptionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ResultExec {
    private static final String PASSED = "passed";
    private static final String SKIPPED = "skipped";
    private static final String UNDEFINED = "undefined";
    private static final String FAILED = "failed";
    private static final String PENDING = "pending";
    private static final List<String> KNOWNS = Arrays.asList(PASSED, SKIPPED, UNDEFINED, FAILED, PENDING);

    private final String status;
    private final String error;
    private final String errorMessage;
    private final Long duration;

    public ResultExec(String status, Throwable error, String errorMessage, Long duration) {
        this.status = ensureStatusIsValid(status);
        this.error = ExceptionUtils.toString(error);
        this.errorMessage = errorMessage;
        this.duration = duration;
    }

    public boolean isPassed() {
        return PASSED.equalsIgnoreCase(status);
    }

    private static String ensureStatusIsValid(String status) {
        String lowerCase = status.toLowerCase();
        if (KNOWNS.contains(lowerCase))
            return lowerCase;
        else
            throw new IllegalArgumentException("Unknown status <" + status + "> not in: " + KNOWNS);
    }
}
