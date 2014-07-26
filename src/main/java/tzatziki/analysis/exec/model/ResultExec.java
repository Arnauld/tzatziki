package tzatziki.analysis.exec.model;

import tzatziki.util.ExceptionUtils;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ResultExec {
    private final String status;
    private final String error;
    private final String errorMessage;
    private final Long duration;

    public ResultExec(String status, Throwable error, String errorMessage, Long duration) {
        this.status = status;
        this.error = ExceptionUtils.toString(error);
        this.errorMessage = errorMessage;
        this.duration = duration;
    }
}
