package tzatziki.junit;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import tzatziki.TestSettings;
import tzatziki.analysis.step.Features;
import tzatziki.analysis.tag.TagDictionary;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class SanityTagCheckerTest {

    @Test
    public void noMethodAnnotated_should_raise_an_error() {
        Result result = new JUnitCore().run(T1NoAnnotatedMethods.class);
        assertThat(result.getFailures()).isNotEmpty();
    }

    @RunWith(SanityTagChecker.class)
    public static class T1NoAnnotatedMethods {
    }

    @Test
    public void noMethodAnnotated_with_TagDictionaryProvider_should_raise_an_error() {
        Result result = new JUnitCore().run(T2TagDictionaryProviderNotStatic.class);
        assertThat(result.getFailures()).isNotEmpty();
    }

    @RunWith(SanityTagChecker.class)
    public static class T2NoTagDictionaryProvider {
        @SanityTagChecker.FeaturesProvider
        public static Features features() {
            return null;
        }
    }

    @Test
    public void annotated_with_TagDictionaryProvider_should_be_static() {
        Result result = new JUnitCore().run(T2TagDictionaryProviderNotStatic.class);
        assertThat(result.getFailures()).isNotEmpty();
    }

    @RunWith(SanityTagChecker.class)
    public static class T2TagDictionaryProviderNotStatic {
        @SanityTagChecker.TagDictionaryProvider
        public TagDictionary tagDictionary() {
            return null;
        }

        @SanityTagChecker.FeaturesProvider
        public static TagDictionary features() {
            return null;
        }
    }

    @Test
    public void noMethodAnnotated_with_FeaturesProvider_should_raise_an_error() {
        Result result = new JUnitCore().run(T3.class);
        assertThat(result.getFailures()).isNotEmpty();
    }

    @RunWith(SanityTagChecker.class)
    public static class T3 {
        @SanityTagChecker.TagDictionaryProvider
        public static TagDictionary tagDictionary() {
            return new TagDictionary().declareTag("@wip", "work in progress");
        }
    }

    @Test
    public void annotated_with_FeaturesProvider_should_raise_an_error_if_returnType_is_not_a_Features() {
        Result result = new JUnitCore().run(T3InvalidReturnTypeForFeaturesProvider.class);
        assertThat(result.getFailures()).isNotEmpty();
    }

    @RunWith(SanityTagChecker.class)
    public static class T3InvalidReturnTypeForFeaturesProvider {
        @SanityTagChecker.TagDictionaryProvider
        public static TagDictionary tagDictionary() {
            return null;
        }

        @SanityTagChecker.FeaturesProvider
        public static TagDictionary features() {
            return null;
        }
    }

    @Test
    public void annotated_with_TagDictionary_should_raise_an_error_if_returnType_is_not_a_TagDictionary() {
        Result result = new JUnitCore().run(T3InvalidReturnTypeForTagDictionary.class);
        assertThat(result.getFailures()).isNotEmpty();
    }

    @RunWith(SanityTagChecker.class)
    public static class T3InvalidReturnTypeForTagDictionary {
        @SanityTagChecker.TagDictionaryProvider
        public static Features tagDictionary() {
            return null;
        }

        @SanityTagChecker.FeaturesProvider
        public static Features features() {
            return null;
        }
    }

    @Test
    public void usecase() {
        JUnitCore unitCore = new JUnitCore();
        unitCore.addListener(new RunListener() {
            @Override
            public void testRunStarted(Description description) throws Exception {
                Thread.dumpStack();
                System.out.println("SanityTagCheckerTest.testRunStarted::" + description.getTestClass());
                System.out.println("SanityTagCheckerTest.testRunStarted::" + description.getMethodName());
            }

            @Override
            public void testStarted(Description description) throws Exception {
                System.out.println("SanityTagCheckerTest.testStarted::" + description.getClassName());
            }
        });
        Result result = unitCore.run(AllTagsAreDefined.class);
        assertThat(result.getFailures()).isEmpty();
        assertThat(result.wasSuccessful()).isTrue();
    }

    @RunWith(SanityTagChecker.class)
    public static class AllTagsAreDefined {
        @SanityTagChecker.TagDictionaryProvider
        public static TagDictionary tagDictionary() {
            return new TagDictionary().declareTag("@wip", "").declareTag("@option", "");
        }

        @SanityTagChecker.FeaturesProvider
        public static Features features() {
            String basedir = new TestSettings().getBaseDir();
            return SanityTagChecker.loadFeaturesFromSourceDirectory(new File(basedir, "src/test/java/samples/coffeemachine"));
        }
    }
}