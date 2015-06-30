package tzatziki.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LoadJson {
    private String charsetName = "UTF8";

    public JsonNode loadFromResource(String resourceName) throws IOException {
        InputStream in = getClass().getResourceAsStream(resourceName);
        try {
            Reader reader = new InputStreamReader(in, charsetName);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, JsonNode.class);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public JsonNode loadFromString(String content) throws IOException {
        Reader reader = new StringReader(content);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reader, JsonNode.class);
    }
}
