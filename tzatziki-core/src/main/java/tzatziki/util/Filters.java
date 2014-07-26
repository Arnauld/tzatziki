package tzatziki.util;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Filters {
    public static <T> Filter<T> identity() {
        return new Filter<T>() {
            @Override
            public T filter(T value) {
                return value;
            }
        };
    }

    public static <T> Filter<T> chain(final Filter<T>... filters) {
        if (filters.length == 1)
            return filters[0];
        return new Filter<T>() {
            @Override
            public T filter(T value) {
                T processed = value;
                for (Filter<T> filter : filters) {
                    processed = filter.filter(processed);
                }
                return processed;
            }
        };
    }

    public static <T> Filter<T> chain(final List<Filter<T>> filters) {
        if (filters.size() == 1)
            return filters.get(0);
        return new Filter<T>() {
            @Override
            public T filter(T value) {
                T processed = value;
                for (Filter<T> filter : filters) {
                    processed = filter.filter(processed);
                }
                return processed;
            }
        };
    }
}
