package tzatziki.analysis.java;

import com.google.common.collect.FluentIterable;
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
import org.junit.Test;
import tzatziki.TestSettings;

import java.io.File;
import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
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

        //Then
        assertThat(grammar).isNotNull();
        assertThat(grammar.packages()).hasSize(1);

        PackageEntry subGroup = grammar.packages().get(0);
        assertThat(subGroup.name()).isEqualTo(pkg);
        assertThat(subGroup.comment()).isNullOrEmpty();
        assertThat(subGroup.classes()).hasSize(1);

        ClassEntry clazzGroup = subGroup.classes().get(0);
        assertThat(clazzGroup.qualifiedName()).isEqualTo(pkg + ".OptionStepdefs");
        assertThat(clazzGroup.comment()).isNullOrEmpty();

        FluentIterable<MethodEntry> methodEntries = clazzGroup.methods();
        assertThat(methodEntries).hasSize(5);

        //
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(grammar);
        assertThat(json).isEqualTo("" +
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
                "              \"usedBySet\": [],\n" +
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
                "              \"usedBySet\": []\n" +
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
                "              \"usedBySet\": []\n" +
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
                "}");
    }

    @Test
    public void parser_should_parse_and_fill_method_information() {
        // Given
        parser.usingSourceDirectory(sourceTree);

        //When
        Grammar grammar = parser.process();

        ClassEntry clazzGroup = grammar.packages().get(0).classes().get(0);
        FluentIterable<MethodEntry> methodEntries = clazzGroup.methods();

        MethodEntry methodEntry0 = methodEntries.get(0);
        assertThat(methodEntry0.patterns().toList()).containsExactly("^I order an? \"([^\"]*)\" with (\\d+) sugar$");
        assertThat(methodEntry0.comment()).isEqualTo(
                "Order a <b>drink</b> with a number of sugar.\n" +
                        "If the drink does not support the addition of sugar it won't\n" +
                        "be checked here ({@link String})."
        );
        assertThat(methodEntry0.parameters()).hasSize(2);
        assertThat(methodEntry0.parameter(0).getName()).isEqualTo("drinkType");
        assertThat(methodEntry0.parameter(0).getDoc()).isEqualTo("type of drink");
        assertThat(methodEntry0.parameter(1).getName()).isEqualTo("nbSugar");
        assertThat(methodEntry0.parameter(1).getDoc()).isEqualTo("number of sugar (if applicable)");

        MethodEntry methodEntry1 = methodEntries.get(1);
        assertThat(methodEntry1.patterns().toList()).containsExactly("^the instruction generated should be \"([^\"]*)\"$");
        assertThat(methodEntry1.comment()).isNullOrEmpty();
        assertThat(methodEntry1.parameters()).hasSize(1);
        assertThat(methodEntry1.parameter(0).getName()).isEqualTo("expectedProtocol");
        assertThat(methodEntry1.parameter(0).getDoc()).isNullOrEmpty();
    }
}
