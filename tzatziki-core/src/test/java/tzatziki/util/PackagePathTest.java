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
}