package samples.coffeemachine;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import samples.TestSettings;
import tzatziki.analysis.java.AvailableStepsXls;
import tzatziki.analysis.java.ConsoleOutputListener;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.java.GrammarParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CoffeeMachineSuite.ExecuteFeatures.class,
        CoffeeMachineSuite.StaticAnalysis.class})
public class CoffeeMachineSuite {

    @RunWith(Cucumber.class)
    @CucumberOptions(
            format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/samples/coffeemachine/suite"
    )
    public static class ExecuteFeatures {
    }

    public static class StaticAnalysis {
        @Test
        public void generateXlsGrammar() {
            TestSettings settings = new TestSettings();
            String buildDir = settings.getBuildDir();
            String baseDir = settings.getBaseDir();

            File javaSourceTree = new File(baseDir, "src/main/java/samples/coffeemachine/");
            Grammar grammar = new GrammarParser()
                    //.usingListener(new ConsoleOutputListener())
                    .usingSourceDirectory(javaSourceTree)
                    .process();

            assertThat(grammar.hasEntries())
                    .describedAs("No entry found at " + javaSourceTree.getAbsolutePath())
                    .isTrue();

            File fileDst = new File(buildDir, "samples/coffeemachine/suite/grammar.xls");
            AvailableStepsXls xls = new AvailableStepsXls(fileDst);
            xls.emit(grammar);
            xls.close();
        }
    }

    @AfterClass
    public static void generateExecutionReport() throws FileNotFoundException {
        new FileInputStream(executionResultFile());
    }

    public static File executionResultFile() {
        String buildDir = new TestSettings().getBuildDir();
        return new File(buildDir, "samples/coffeemachine/suite/exec.json");
    }

    public static File featuresSourceTree() {
        String baseDir = new TestSettings().getBaseDir();
        return new File(baseDir, "src/main/resources/samples/coffeemachine");
    }


}
