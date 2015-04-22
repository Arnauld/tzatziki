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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultExec that = (ResultExec) o;

        return !(duration != null ? !duration.equals(that.duration) : that.duration != null)
                && !(error != null ? !error.equals(that.error) : that.error != null)
                && !(errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null)
                && status == that.status;

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    public ResultExec recursiveCopy() {
        // TODO find a suitable to ensure this is still valid
        // or a real copy is made if a field becomes mutable
        return this;
    }
}
