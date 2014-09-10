package tzatziki.analysis.exec.model;

import com.google.common.collect.FluentIterable;
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

    protected void recursiveCopy(EmbeddingAndWriteContainer copy) {
        copy.embeddedList.addAll(embeddedList);
    }

    public FluentIterable<Embedded> embeddeds() {
        return FluentIterable.from(embeddedList);
    }

}
