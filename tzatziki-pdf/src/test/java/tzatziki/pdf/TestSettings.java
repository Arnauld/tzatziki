package tzatziki.pdf;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TestSettings {

    private Properties properties;

    public TestSettings() {
    }

    public String getBuildDir() {
        return getProperties().getProperty("buildDir");
    }

    public String getBaseDir() {
        return getProperties().getProperty("baseDir");
    }

    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            InputStream stream = null;
            try {
                stream = getClass().getResourceAsStream("/test-settings.properties");
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to open settings", e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
        return properties;
    }
}