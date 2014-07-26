package tzatziki.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Filter<T> {
    T filter(T value);
}
