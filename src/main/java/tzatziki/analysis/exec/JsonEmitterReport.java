package tzatziki.analysis.exec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.FeatureExec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JsonEmitterReport extends ExecutionReport {
    private final Logger log = LoggerFactory.getLogger(JsonEmitterReport.class);
    private final File outFile;
    private FileWriter writer;
    private Gson gson;
    private int featureCount = 0;

    public JsonEmitterReport(File reportDir) {
        this(reportDir, "exec.json");
    }

    public JsonEmitterReport(File reportDir, String filename) {
        outFile = new File(reportDir, filename);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    protected Appendable out() {
        if (writer == null) {
            try {
                outFile.getParentFile().mkdirs();
                writer = new FileWriter(outFile);
                log.info("Generating json at {}", outFile.getAbsolutePath());
                writer.append("[");
            } catch (IOException ioe) {
                throw new ReportException(ioe);
            }
        }
        return writer;
    }

    @Override
    protected void emit(FeatureExec feature) {
        featureCount++;
        appendOutSeparatorIfRequired();
        gson.toJson(feature, out());
    }

    private void appendOutSeparatorIfRequired() {
        if (featureCount > 1) {
            try {
                writer.append(",\n");
            } catch (IOException ioe) {
                throw new ReportException(ioe);
            }
        }
    }

    @Override
    public void close() {
        try {
            writer.append("]");
            log.info("Json generated at {}", outFile.getAbsolutePath());
            writer.close();
        } catch (IOException e) {
            log.error("Oops", e);
        }
    }

    public static class ReportException extends RuntimeException {
        public ReportException(Throwable cause) {
            super(cause);
        }
    }
}
