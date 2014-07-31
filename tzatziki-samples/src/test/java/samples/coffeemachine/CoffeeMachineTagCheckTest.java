package samples.coffeemachine;

import org.junit.runner.RunWith;
import samples.TestSettings;
import tzatziki.analysis.step.Features;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.junit.SanityTagChecker;

import java.io.File;

import static tzatziki.junit.SanityTagChecker.loadFeaturesFromSourceDirectory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(SanityTagChecker.class)
public class CoffeeMachineTagCheckTest {

    @SanityTagChecker.TagDictionaryProvider
    public static TagDictionary tagDictionary() {
        return new TagDictionary()
                .declareTag("@wip")
                .declareTag("@protocol")
                .declareTag("@notification")
                .declareTag("@message")
                .declareTag("@runningOut")
                .declareTag("@coffee")
                .declareTag("@tea")
                .declareTag("@chocolate")
                .declareTag("@sugar")
                .declareTag("@noSugar")
                .declareTag("@takeOrder")
                .declareTag("@payment")
                .declareTag("@reporting")
                .declareTag("@manual")
                ;
    }

    @SanityTagChecker.FeaturesProvider
    public static Features features() {
        String basedir = new TestSettings().getBaseDir();
        return loadFeaturesFromSourceDirectory(new File(basedir, "src/main/resources/samples/coffeemachine"));
    }
}
