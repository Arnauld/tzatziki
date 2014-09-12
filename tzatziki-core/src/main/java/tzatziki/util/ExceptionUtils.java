package tzatziki.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExceptionUtils {
    public static String toString(Throwable error) {
        if (error == null)
            return null;

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        error.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
