package tzatziki.junit;

import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceLoader;
import tzatziki.util.Filter;
import tzatziki.util.Filters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ResourceLoaderWrapper implements ResourceLoader {

    private final ResourceLoader delegate;
    private final Filter<InputStream> filter;

    public ResourceLoaderWrapper(ResourceLoader delegate) {
        this(delegate, Filters.<InputStream>identity());
    }

    public ResourceLoaderWrapper(ResourceLoader delegate, Filter<InputStream> filter) {
        this.delegate = delegate;
        this.filter = filter;
    }

    @Override
    public Iterable<Resource> resources(String path, String suffix) {
        return wrap(delegate.resources(path, suffix));
    }

    private Iterable<Resource> wrap(final Iterable<Resource> resources) {
        return new Iterable<Resource>() {
            @Override
            public Iterator<Resource> iterator() {
                return wrap(resources.iterator());
            }
        };
    }

    private Iterator<Resource> wrap(final Iterator<Resource> iterator) {
        return new Iterator<Resource>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Resource next() {
                return wrap(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    private Resource wrap(final Resource resource) {
        return new Resource() {
            @Override
            public String getPath() {
                return resource.getPath();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return wrap(resource.getInputStream());
            }

            @Override
            public String getClassName() {
                return resource.getClassName();
            }
        };
    }

    protected InputStream wrap(InputStream inputStream) {
        return filter.filter(inputStream);
    }
}
