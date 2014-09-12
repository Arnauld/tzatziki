package tzatziki.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoizableIteratorTest {

    @Test
    public void usecase () {
        List<String> elements = Arrays.asList("one", "two", "three");
        MemoizableIterator<String> it = new MemoizableIterator<String>(elements.iterator());
        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("one");
        assertThat(it.current()).isEqualTo("one");

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("two");
        assertThat(it.current()).isEqualTo("two");
        assertThat(it.current()).isEqualTo("two");

        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("three");
        assertThat(it.current()).isEqualTo("three");
        assertThat(it.current()).isEqualTo("three");

        assertThat(it.hasNext()).isFalse();
        assertThat(it.current()).isEqualTo("three");
    }

    @Test(expected = NoSuchElementException.class)
    public void next_should_throw_when_no_more_element__initial_empty_case () {
        List<String> elements = Arrays.asList();
        MemoizableIterator<String> it = new MemoizableIterator<String>(elements.iterator());
        it.next();
    }

    @Test(expected = IllegalStateException.class)
    public void current_should_throw__when_no_more_elements () {
        List<String> elements = Arrays.asList();
        MemoizableIterator<String> it = new MemoizableIterator<String>(elements.iterator());
        it.current();
    }

}