package tzatziki.pdf;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface PdfEmitter<T> {
    void emit(T value, EmitterContext emitterContext);
}
