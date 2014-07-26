package tzatziki.analysis.exec.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EmbeddingAndWriteContainer {
    private List<Embedded> embeddedList = Lists.newArrayList();

    public void embedding(String mimeType, byte[] data) {
        embeddedList.add(new Embedded(mimeType, data));
    }

    public void text(String text) {
        embeddedList.add(new Embedded(text));
    }


    public static class Embedded {
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
    }
}
