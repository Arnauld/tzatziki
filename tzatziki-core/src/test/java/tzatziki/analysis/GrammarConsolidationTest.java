package tzatziki.analysis;

import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import tzatziki.TestSettings;
import tzatziki.analysis.java.Grammar;
import tzatziki.analysis.java.GrammarParser;
import tzatziki.analysis.step.FeatureParser;
import tzatziki.analysis.step.Features;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GrammarConsolidationTest {

    //
    private File stepTree;
    private File javaTree;
    private String pkg;

    @Before
    public void setUp() throws IOException {
        String basedir = new TestSettings().getBaseDir();
        pkg = getClass().getPackage().getName() + ".java.stepdefs";
        stepTree = new File(basedir, "src/test/resources/tzatziki/analysis/step");
        javaTree = new File(basedir, "src/test/java/" + pkg.replace(".", "/"));
    }

    @Test
    public void usecase() {
        // Given
        FeatureParser featureParser = new FeatureParser().usingSourceDirectory(stepTree);
        Features features = featureParser.process();

        GrammarParser grammarParser = new GrammarParser().usingSourceDirectory(javaTree);
        Grammar grammar = grammarParser.process();

        GrammarConsolidation consolidation = new GrammarConsolidation(grammar, features);

        //When
        consolidation.consolidate();

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(grammar);
        assertThat(json).isEqualTo(
                "" +
                        "{\n" +
                        "  \"pkgEntries\": [\n" +
                        "    {\n" +
                        "      \"name\": \"" + pkg + "\",\n" +
                        "      \"classEntries\": [\n" +
                        "        {\n" +
                        "          \"packageName\": \"" + pkg + "\",\n" +
                        "          \"name\": \"OptionStepdefs\",\n" +
                        "          \"methodEntries\": [\n" +
                        "            {\n" +
                        "              \"methodName\": \"I_order_a_with_sugar\",\n" +
                        "              \"args\": [\n" +
                        "                \"java.lang.String\",\n" +
                        "                \"int\"\n" +
                        "              ],\n" +
                        "              \"patterns\": [\n" +
                        "                \"^I order an? \\\"([^\\\"]*)\\\" with (\\\\d+) sugar$\"\n" +
                        "              ],\n" +
                        "              \"parameters\": [\n" +
                        "                {\n" +
                        "                  \"index\": 0,\n" +
                        "                  \"name\": \"drinkType\",\n" +
                        "                  \"parameterDoc\": \"type of drink\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"index\": 1,\n" +
                        "                  \"name\": \"nbSugar\",\n" +
                        "                  \"parameterDoc\": \"number of sugar (if applicable)\"\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"usedBySet\": [\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioName\": \"Scenario: A tea with not enough money\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"| 0.55 | Chocolate | 0 | M:Not enough money 0.05 missing |\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"| 0.25 | Coffee | 0 | M:Not enough money 0.25 missing |\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioName\": \"Scenario: A tea with just enough money\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioName\": \"Scenario: A coffee with more than required money\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"subdomain/extra-hot.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"Scenario: Extra sugar with Orange Juice is ignored\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"| 0.05 | Tea | 1 | M:Not enough money 0.35 missing |\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"subdomain/running-out.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"Scenario: Last Coffee\"\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"comment\": \"Order a \\u003cb\\u003edrink\\u003c/b\\u003e with a number of sugar.\\nIf the drink does not support the addition of sugar it won\\u0027t\\nbe checked here ({@link String}).\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"methodName\": \"the_instruction_generated_should_be\",\n" +
                        "              \"args\": [\n" +
                        "                \"java.lang.String\"\n" +
                        "              ],\n" +
                        "              \"patterns\": [\n" +
                        "                \"^the instruction generated should be \\\"([^\\\"]*)\\\"$\"\n" +
                        "              ],\n" +
                        "              \"parameters\": [\n" +
                        "                {\n" +
                        "                  \"index\": 0,\n" +
                        "                  \"name\": \"expectedProtocol\"\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"usedBySet\": [\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioName\": \"Scenario: A tea with not enough money\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"subdomain/extra-hot.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"Scenario: An Orange juice\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"| 0.55 | Chocolate | 0 | M:Not enough money 0.05 missing |\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"subdomain/extra-hot.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"Scenario: Extra hot with Orange Juice is ignored\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"| 0.25 | Coffee | 0 | M:Not enough money 0.25 missing |\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioName\": \"Scenario: A tea with just enough money\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioName\": \"Scenario: A coffee with more than required money\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"subdomain/extra-hot.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"Scenario: Extra sugar with Orange Juice is ignored\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"| 0.05 | Tea | 1 | M:Not enough money 0.35 missing |\"\n" +
                        "                },\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"subdomain/extra-hot.feature\",\n" +
                        "                  \"scenarioOutlineName\": \"Scenario Outline: Check missing money\",\n" +
                        "                  \"scenarioName\": \"Scenario: An extra hot tea with 1 sugar\"\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"methodName\": \"the_message_is_sent\",\n" +
                        "              \"args\": [\n" +
                        "                \"java.lang.String\"\n" +
                        "              ],\n" +
                        "              \"patterns\": [\n" +
                        "                \"^the message \\\"([^\\\"]*)\\\" is sent$\"\n" +
                        "              ],\n" +
                        "              \"parameters\": [\n" +
                        "                {\n" +
                        "                  \"index\": 0,\n" +
                        "                  \"name\": \"message\"\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"usedBySet\": []\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"methodName\": \"I_ve_inserted_€_in_the_machine\",\n" +
                        "              \"args\": [\n" +
                        "                \"int\"\n" +
                        "              ],\n" +
                        "              \"patterns\": [\n" +
                        "                \"^I\\u0027ve inserted (\\\\d+)€ in the machine$\"\n" +
                        "              ],\n" +
                        "              \"parameters\": [\n" +
                        "                {\n" +
                        "                  \"index\": 0,\n" +
                        "                  \"name\": \"amountInEuro\"\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"usedBySet\": [\n" +
                        "                {\n" +
                        "                  \"featureUri\": \"going-into-business.feature\",\n" +
                        "                  \"scenarioName\": \"Scenario: A coffee with more than required money\"\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"methodName\": \"the_report_output_should_be\",\n" +
                        "              \"args\": [\n" +
                        "                \"java.lang.String\"\n" +
                        "              ],\n" +
                        "              \"patterns\": [\n" +
                        "                \"^the report output should be$\"\n" +
                        "              ],\n" +
                        "              \"parameters\": [\n" +
                        "                {\n" +
                        "                  \"index\": 0,\n" +
                        "                  \"name\": \"rawReport\"\n" +
                        "                }\n" +
                        "              ],\n" +
                        "              \"usedBySet\": []\n" +
                        "            }\n" +
                        "          ],\n" +
                        "          \"comment\": \"\"\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"subPkgEntries\": []\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"
        );
    }
}