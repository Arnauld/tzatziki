package tzatziki.analysis.exec.gson;

import com.google.gson.*;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;

import java.lang.reflect.Type;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ScenarioOutlineExecSerializer implements JsonSerializer<ScenarioOutlineExec> {

    private final Gson delegate;

    public ScenarioOutlineExecSerializer(Gson delegate) {

        this.delegate = delegate;
    }

    @Override
    public JsonElement serialize(ScenarioOutlineExec src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject serialized = delegate.toJsonTree(src, typeOfSrc).getAsJsonObject();
        serialized.addProperty("type", "scenario-outline");
        return serialized;
    }
}
