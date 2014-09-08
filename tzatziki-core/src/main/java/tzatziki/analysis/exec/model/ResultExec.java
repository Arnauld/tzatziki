package tzatziki.analysis.exec.model;

import com.google.common.base.Predicate;
import tzatziki.util.ExceptionUtils;

import java.util.Arrays;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ResultExec {

    public static Predicate<ResultExec> resultPassed = statusEquals(Status.Passed);
    public static Predicate<ResultExec> resultSkipped = statusEquals(Status.Skipped);
    public static Predicate<ResultExec> resultUndefined = statusEquals(Status.Undefined);
    public static Predicate<ResultExec> resultFailed = statusEquals(Status.Failed);
    public static Predicate<ResultExec> resultPending = statusEquals(Status.Pending);

    private final Status status;
    private final String error;
    private final String errorMessage;
    private final Long duration;

    public ResultExec(String status, Throwable error, String errorMessage, Long duration) {
        this.status = ensureStatusIsValid(status);
        this.error = ExceptionUtils.toString(error);
        this.errorMessage = errorMessage;
        this.duration = duration;
    }

    public Status status() {
        return status;
    }

    private static Status ensureStatusIsValid(String status) {
        Status result = Status.fromString(status);
        if (result != null)
            return result;
        else
            throw new IllegalArgumentException("Unknown status <" + status + "> not in: " +
                    Arrays.toString(Status.values()));
    }

    private static Predicate<ResultExec> statusEquals(final Status expectedStatus) {
        return new Predicate<ResultExec>() {
            @Override
            public boolean apply(ResultExec input) {
                return expectedStatus.equals(input.status);
            }
        };
    }

    public boolean isPassed() {
        return Status.Passed == status;
    }

    public boolean isSkipped() {
        return Status.Skipped == status;
    }

    public boolean isUndefined() {
        return Status.Undefined == status;
    }

    public boolean isFailed() {
        return Status.Failed == status;
    }

    public boolean isPending() {
        return Status.Pending == status;
    }
}
