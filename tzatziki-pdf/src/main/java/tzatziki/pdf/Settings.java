package tzatziki.pdf;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import gutenberg.itext.Colors;
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
    private Map<Object, Boolean> booleanProperties = Maps.newHashMap();

    public Styles styles() {
        if (styles == null) {
            styles = new Styles().initDefaults();
        }
        return styles;
    }

    public Optional<Boolean> getBoolean(Object propertyKey) {
        Boolean aBoolean = booleanProperties.get(propertyKey);
        if (aBoolean == null)
            return Optional.absent();
        else
            return Optional.of(aBoolean);
    }

    public boolean getBoolean(Object propertyKey, boolean defaultValue) {
        return getBoolean(propertyKey).or(defaultValue);
    }

    public void defineProperty(Object key, boolean booleanValue) {
        booleanProperties.put(key, booleanValue);
    }
}
