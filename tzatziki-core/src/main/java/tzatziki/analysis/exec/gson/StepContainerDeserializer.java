package tzatziki.analysis.exec.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;
import tzatziki.analysis.exec.model.StepContainer;

import java.lang.reflect.Type;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StepContainerDeserializer implements JsonDeserializer<StepContainer> {
    private final Gson delegate;

    public StepContainerDeserializer(Gson delegate) {
        this.delegate = delegate;
    }

    @Override
    public StepContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonPrimitive typeAsJson = json.getAsJsonObject().getAsJsonPrimitive("type");
        if (typeAsJson != null) {
            String type = typeAsJson.getAsString();
            if (type.equals("scenario"))
                return delegate.fromJson(json, ScenarioExec.class);
            else if (type.equals("scenario-outline"))
                return delegate.fromJson(json, ScenarioOutlineExec.class);
        }
        
        // fallback?
        return delegate.fromJson(json, ScenarioExec.class);
    }
}
