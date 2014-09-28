package tzatziki.analysis.java;

import com.google.gson.GsonBuilder;
import cucumber.api.java.en.And;
import cucumber.api.java.en.But;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en_tx.Givenyall;
import cucumber.api.java.en_tx.Thenyall;
import cucumber.api.java.en_tx.Whenyall;
import cucumber.api.java.fr.*;
import cucumber.api.java.it.Quando;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import tzatziki.TestSettings;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static tzatziki.analysis.java.GrammarParser.STEP_KEYWORD_QUALIFIED_NAME;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GrammarParserTest {

    //
    private GrammarParser parser;
    private File sourceTree;
    private String pkg;

    @Before
    public void setUp() throws IOException {
        String basedir = new TestSettings().getBaseDir();
        parser = new GrammarParser();
        pkg = getClass().getPackage().getName() + ".stepdefs";
        sourceTree = new File(basedir, "src/test/java/" + pkg.replace(".", "/"));
    }

    @Test
    public void qualified_step_pattern_should_match_regular_steps__en() {
        assertThat(Given.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(When.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Then.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(And.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(But.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
    }

    @Test
    public void qualified_step_pattern_should_match_regular_steps__en_tx() {
        assertThat(Givenyall.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Whenyall.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Thenyall.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
    }

    @Test
    public void qualified_step_pattern_should_match_regular_steps__fr_it_etc() {
        assertThat(Etantdonné.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Etantdonnée.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Etantdonnées.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Quand.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Alors.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Et.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
        assertThat(Mais.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());

        assertThat(Quando.class.getName()).matches(STEP_KEYWORD_QUALIFIED_NAME.pattern());
    }

    @Test
    @Ignore
    public void parser_should_invoke_listener_during_parsing() {
        // Given
        GrammarParserStatisticsListener listener = new GrammarParserStatisticsListener();
        parser.usingSourceDirectory(sourceTree)
                .usingListener(listener);

        //When
        parser.process();

        //Then
        assertThat(listener.numberOfPackagesParsed()).isEqualTo(1);
        assertThat(listener.numberOfClassesParsed()).isEqualTo(1);
        assertThat(listener.numberOfMethodsParsed()).isEqualTo(7);
    }

    @Test
    public void parser_should_return_a_grammar_organized_by_package_and_classes() {
        // Given
        parser.usingSourceDirectory(sourceTree);

        //When
        Grammar grammar = parser.process();

        //
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(grammar);
        assertThat(json).isEqualTo("" +
                "{\n" +
                "  \"root\": {\n" +
                "    \"name\": \"\",\n" +
                "    \"classEntries\": [],\n" +
                "    \"subPkgEntries\": [\n" +
                "      {\n" +
                "        \"name\": \"tzatziki\",\n" +
                "        \"classEntries\": [],\n" +
                "        \"subPkgEntries\": [\n" +
                "          {\n" +
                "            \"name\": \"tzatziki.analysis\",\n" +
                "            \"classEntries\": [],\n" +
                "            \"subPkgEntries\": [\n" +
                "              {\n" +
                "                \"name\": \"tzatziki.analysis.java\",\n" +
                "                \"classEntries\": [],\n" +
                "                \"subPkgEntries\": [\n" +
                "                  {\n" +
                "                    \"name\": \"tzatziki.analysis.java.stepdefs\",\n" +
                "                    \"classEntries\": [\n" +
                "                      {\n" +
                "                        \"packageName\": \"tzatziki.analysis.java.stepdefs\",\n" +
                "                        \"name\": \"OptionStepdefs\",\n" +
                "                        \"methodEntries\": [\n" +
                "                          {\n" +
                "                            \"methodName\": \"I_order_a_with_sugar\",\n" +
                "                            \"args\": [\n" +
                "                              \"java.lang.String\",\n" +
                "                              \"int\"\n" +
                "                            ],\n" +
                "                            \"patterns\": [\n" +
                "                              {\n" +
                "                                \"keyword\": \"When\",\n" +
                "                                \"pattern\": \"^I order an? \\\"([^\\\"]*)\\\" with (\\\\d+) sugar$\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"parameters\": [\n" +
                "                              {\n" +
                "                                \"index\": 0,\n" +
                "                                \"name\": \"drinkType\",\n" +
                "                                \"parameterDoc\": \"type of drink\"\n" +
                "                              },\n" +
                "                              {\n" +
                "                                \"index\": 1,\n" +
                "                                \"name\": \"nbSugar\",\n" +
                "                                \"parameterDoc\": \"number of sugar (if applicable)\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"usedBySet\": [],\n" +
                "                            \"comment\": \"Order a \\u003cb\\u003edrink\\u003c/b\\u003e with a number of sugar.\\nIf the drink does not support the addition of sugar it won\\u0027t\\nbe checked here ({@link String}).\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"methodName\": \"the_instruction_generated_should_be\",\n" +
                "                            \"args\": [\n" +
                "                              \"java.lang.String\"\n" +
                "                            ],\n" +
                "                            \"patterns\": [\n" +
                "                              {\n" +
                "                                \"keyword\": \"Then\",\n" +
                "                                \"pattern\": \"^the instruction generated should be \\\"([^\\\"]*)\\\"$\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"parameters\": [\n" +
                "                              {\n" +
                "                                \"index\": 0,\n" +
                "                                \"name\": \"expectedProtocol\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"usedBySet\": []\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"methodName\": \"the_message_is_sent\",\n" +
                "                            \"args\": [\n" +
                "                              \"java.lang.String\"\n" +
                "                            ],\n" +
                "                            \"patterns\": [\n" +
                "                              {\n" +
                "                                \"keyword\": \"When\",\n" +
                "                                \"pattern\": \"^the message \\\"([^\\\"]*)\\\" is sent$\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"parameters\": [\n" +
                "                              {\n" +
                "                                \"index\": 0,\n" +
                "                                \"name\": \"message\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"usedBySet\": []\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"methodName\": \"I_ve_inserted_€_in_the_machine\",\n" +
                "                            \"args\": [\n" +
                "                              \"int\"\n" +
                "                            ],\n" +
                "                            \"patterns\": [\n" +
                "                              {\n" +
                "                                \"keyword\": \"Given\",\n" +
                "                                \"pattern\": \"^I\\u0027ve inserted (\\\\d+)€ in the machine$\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"parameters\": [\n" +
                "                              {\n" +
                "                                \"index\": 0,\n" +
                "                                \"name\": \"amountInEuro\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"usedBySet\": []\n" +
                "                          },\n" +
                "                          {\n" +
                "                            \"methodName\": \"the_report_output_should_be\",\n" +
                "                            \"args\": [\n" +
                "                              \"java.lang.String\"\n" +
                "                            ],\n" +
                "                            \"patterns\": [\n" +
                "                              {\n" +
                "                                \"keyword\": \"Then\",\n" +
                "                                \"pattern\": \"^the report output should be$\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"parameters\": [\n" +
                "                              {\n" +
                "                                \"index\": 0,\n" +
                "                                \"name\": \"rawReport\"\n" +
                "                              }\n" +
                "                            ],\n" +
                "                            \"usedBySet\": []\n" +
                "                          }\n" +
                "                        ],\n" +
                "                        \"comment\": \"\"\n" +
                "                      }\n" +
                "                    ],\n" +
                "                    \"subPkgEntries\": []\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}");
    }

}
