package tzatziki.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JsonPathTest {

    @Test
    public void should_return_root_value() throws IOException {
        JsonPath path = new JsonPath("/message");
        JsonNode node = new LoadJson().loadFromString("{'message':'hello'}".replace("'", "\""));
        assertThat(path.evaluate(node)).isEqualTo(node.get("message"));
    }

    @Test
    public void should_return_root_value__as_string() throws IOException {
        JsonPath path = new JsonPath("/message");
        JsonNode node = new LoadJson().loadFromString("{'message':'hello'}".replace("'", "\""));
        assertThat(path.evaluateString(node)).isEqualTo("hello");
    }

    @Test
    public void should_return_the_nth_value_from_root_array() throws IOException {
        JsonNode node = new LoadJson().loadFromString("{'message': ['hello', 'world']}".replace("'", "\""));

        JsonPath path0 = new JsonPath("/message[0]");
        assertThat(path0.evaluateString(node)).isEqualTo("hello");
        JsonPath path1 = new JsonPath("/message[1]");
        assertThat(path1.evaluateString(node)).isEqualTo("world");
    }

    @Test
    public void should_return_the_nth_value_from_a_nested_array_from_root_array() throws IOException {
        JsonNode node = new LoadJson().loadFromString("{'message': [['hello'], ['world']]}".replace("'", "\""));

        JsonPath path0 = new JsonPath("/message[0][0]");
        assertThat(path0.evaluateString(node)).isEqualTo("hello");
        JsonPath path1 = new JsonPath("/message[1][0]");
        assertThat(path1.evaluateString(node)).isEqualTo("world");
    }

    @Test
    public void should_return_nested_object_value_from_root__as_string() throws IOException {
        JsonPath path = new JsonPath("/message/hello");
        JsonNode node = new LoadJson().loadFromString("{'message': {'hello':'world'}}".replace("'", "\""));
        assertThat(path.evaluateString(node)).isEqualTo("world");
    }

    @Test(expected = JsonPath.InvalidExpressionException.class)
    public void should_throw_exception_when_brackets_are_unbalanced() throws IOException {
        JsonNode node = new LoadJson().loadFromString("{'message': {'hello':'world'}}".replace("'", "\""));
        new JsonPath("/message[0").evaluate(node);
    }

    @Test
    public void should_traverse_object_then_array_then_object__as_string() throws IOException {
        JsonPath path = new JsonPath("/message[0]/say[0]");
        JsonNode node = new LoadJson().loadFromString(("{'message': [" +
                "{'say':['hello', 'world']}," +
                "{'wat':['oups']}]}").replace("'", "\""));
        assertThat(path.evaluateString(node)).isEqualTo("hello");
    }

}
