package samples.coffeemachine;

import static tzatziki.junit.SanityTagChecker.loadFeaturesFromSourceDirectory;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;

import org.junit.runner.RunWith;

import samples.TestSettings;
import tzatziki.analysis.check.CheckAtLeastOneTagsExist;
import tzatziki.analysis.check.CucumberPart;
import tzatziki.analysis.check.TagChecker;
import tzatziki.analysis.step.Features;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.junit.SanityTagChecker;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(SanityTagChecker.class)
public class CoffeeMachineDrinkTagCheckTest {

    @SanityTagChecker.TagDictionaryProvider
    public static TagDictionary tagDictionary() {
        return new TagDictionary()
                .declareTag("@coffee")
                .declareTag("@tea")
                .declareTag("@chocolate")
                .declareTag("@orangeJuice")
                .declareTag("@noDrink")
                ;
    }

    @SanityTagChecker.FeaturesProvider
    public static Features features() {
        String basedir = new TestSettings().getBaseDir();
        return loadFeaturesFromSourceDirectory(new File(basedir, "src/main/resources/samples/coffeemachine"));
    }
    
    @SanityTagChecker.TagCheckerProvider
    public static TagChecker checker() {
        return new CheckAtLeastOneTagsExist();
    }

    @SanityTagChecker.CheckScopeProvider
    public static Set<CucumberPart> scope() {
        return EnumSet.of(CucumberPart.Scenario);
    }
}
