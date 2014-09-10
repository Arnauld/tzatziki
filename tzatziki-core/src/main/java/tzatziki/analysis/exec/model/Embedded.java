package tzatziki.analysis.exec.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Embedded {
    private final String mimeType;
    private final byte[] data;
    private final String text;

    public Embedded(String mimeType, byte[] data) {
        this.mimeType = mimeType;
        this.data = data;
        this.text = null;
    }

    public Embedded(String text) {
        this.mimeType = "plain/text";
        this.data = null;
        this.text = text;
    }

    public boolean isText() {
        return text != null;
    }

    public String mimeType() {
        return mimeType;
    }

    public String text() {
        return text;
    }

    public byte[] data() {
        return data;
    }
}
