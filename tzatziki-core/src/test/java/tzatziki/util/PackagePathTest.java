package tzatziki.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tzatziki.util.PackagePath.directSubPackageOf;


public class PackagePathTest {

    @Test
    public void directSubPackageOf_should_work___() {
        assertThat(directSubPackageOf("", "tzatziki.util.matcher.string")).isEqualTo("tzatziki");
        assertThat(directSubPackageOf("tzatziki", "tzatziki.util")).isEqualTo("util");
        assertThat(directSubPackageOf("tzatziki.util", "tzatziki.util.matcher")).isEqualTo("matcher");
        assertThat(directSubPackageOf("tzatziki.util", "tzatziki.util.matcher.string")).isEqualTo("matcher");
    }

    @Test
    public void directSubPackageOf_should_return_null_when_same_packages_are_provided() {
        assertThat(directSubPackageOf("tzatziki.pdf", "tzatziki.pdf")).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void directSubPackageOf_should_throw_when_packages_does_not_belong_to_the_same_tree() {
        directSubPackageOf("tzatziki.pdf", "tzatziki.util");
    }
}