package tzatziki.util;

import java.util.Iterator;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MemoizableIterator<T> implements Iterator<T> {

    public static <T> MemoizableIterator<T> wrap(Iterator<T> iterator) {
        return new MemoizableIterator<T>(iterator);
    }


    private final Iterator<T> delegate;
    private T current;

    public MemoizableIterator(Iterator<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public T next() {
        current = delegate.next();
        return current;
    }

    public T current() {
        return current;
    }

}
