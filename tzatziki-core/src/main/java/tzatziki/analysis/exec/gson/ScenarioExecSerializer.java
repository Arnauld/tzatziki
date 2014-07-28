package tzatziki.analysis.exec.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import static tzatziki.analysis.exec.gson.StepContainerDeserializer.SCENARIO;
import static tzatziki.analysis.exec.gson.StepContainerDeserializer.TYPE;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioExecSerializer implements JsonSerializer {

    private final Gson delegate;

    public ScenarioExecSerializer(Gson delegate) {
        this.delegate = delegate;
    }

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject serialized = delegate.toJsonTree(src, typeOfSrc).getAsJsonObject();
        serialized.addProperty(TYPE, SCENARIO);
        return serialized;
    }
}
