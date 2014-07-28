package tzatziki.analysis.exec.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;

import java.lang.reflect.Type;

import static tzatziki.analysis.exec.gson.StepContainerDeserializer.SCENARIO_OUTLINE;
import static tzatziki.analysis.exec.gson.StepContainerDeserializer.TYPE;

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
        serialized.addProperty(TYPE, SCENARIO_OUTLINE);
        return serialized;
    }
}
