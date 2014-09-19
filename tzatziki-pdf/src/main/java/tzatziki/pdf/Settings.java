package tzatziki.pdf;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import gutenberg.itext.Styles;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Settings {
    public static final String PRIMARY_COLOR = "primary-color";
    public static final String EMPHASIZE_COLOR = "emphasize-color";
    public static final String META_FONT = "meta-font";

    //
    private Styles styles;
    private Map<Object, Object> properties = Maps.newHashMap();

    public Styles styles() {
        if (styles == null) {
            styles = new Styles().initDefaults();
        }
        return styles;
    }

    public Optional<String> getString(Object propertyKey) {
        Object aValue = properties.get(propertyKey);
        if (aValue == null)
            return Optional.absent();
        else
            return Optional.of((String)aValue);
    }

    public Optional<Boolean> getBoolean(Object propertyKey) {
        Object aValue = properties.get(propertyKey);
        if (aValue == null)
            return Optional.absent();
        else
            return Optional.of((Boolean)aValue);
    }

    public boolean getBoolean(Object propertyKey, boolean defaultValue) {
        return getBoolean(propertyKey).or(defaultValue);
    }

    public void defineProperty(Object key, Object value) {
        properties.put(key, value);
    }
}
