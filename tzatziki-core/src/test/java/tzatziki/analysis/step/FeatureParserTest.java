package tzatziki.analysis.step;

import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import tzatziki.TestSettings;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureParserTest {

    //
    private File sourceTree;

    @Before
    public void setUp() throws IOException {
        String basedir = new TestSettings().getBaseDir();
        sourceTree = new File(basedir, "src/test/resources/tzatziki/analysis/step");
    }

    @Test
    public void usecase() {
        // Given
        FeatureParser parser = new FeatureParser().usingSourceDirectory(sourceTree);

        //When
        Features features = parser.process();

        //Then
        String s = new GsonBuilder().setPrettyPrinting().create().toJson(features);
        assertThat(s).isEqualTo("" +
                "{\n" +
                "  \"featureList\": [\n" +
                "    {\n" +
                "      \"uri\": \"going-into-business.feature\",\n" +
                "      \"name\": \"Going into business\",\n" +
                "      \"tags\": [],\n" +
                "      \"scenarios\": [\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: A tea with just enough money\",\n" +
                "          \"tags\": [\n" +
                "            \"@Payment\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"Given\",\n" +
                "              \"text\": \"I\\u0027ve inserted 0.40€ in the machine\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order a \\\"Tea\\\" with 1 sugar\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"the instruction generated should be \\\"T:1:0\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: A tea with not enough money\",\n" +
                "          \"tags\": [\n" +
                "            \"@Payment\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"Given\",\n" +
                "              \"text\": \"I\\u0027ve inserted 0.30€ in the machine\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order a \\\"Tea\\\" with 1 sugar\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"the instruction generated should be \\\"M:Not enough money 0.10 missing\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: A coffee with more than required money\",\n" +
                "          \"tags\": [\n" +
                "            \"@Payment\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"Given\",\n" +
                "              \"text\": \"I\\u0027ve inserted 2€ in the machine\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order a \\\"Coffee\\\" with 0 sugar\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"the instruction generated should be \\\"C::\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ],\n" +
                "      \"scenarioOutlines\": [\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario Outline: Check missing money\",\n" +
                "          \"tags\": [\n" +
                "            \"@Payment\"\n" +
                "          ],\n" +
                "          \"exampleScenarios\": [\n" +
                "            {\n" +
                "              \"visualName\": \"| 0.25 | Coffee | 0 | M:Not enough money 0.25 missing |\",\n" +
                "              \"tags\": [\n" +
                "                \"@Payment\"\n" +
                "              ],\n" +
                "              \"stepList\": [\n" +
                "                {\n" +
                "                  \"keyword\": \"Given\",\n" +
                "                  \"text\": \"I\\u0027ve inserted 0.25€ in the machine\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"When\",\n" +
                "                  \"text\": \"I order a \\\"Coffee\\\" with 0 sugar\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"Then\",\n" +
                "                  \"text\": \"the instruction generated should be \\\"M:Not enough money 0.25 missing\\\"\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"visualName\": \"| 0.55 | Chocolate | 0 | M:Not enough money 0.05 missing |\",\n" +
                "              \"tags\": [\n" +
                "                \"@Payment\"\n" +
                "              ],\n" +
                "              \"stepList\": [\n" +
                "                {\n" +
                "                  \"keyword\": \"Given\",\n" +
                "                  \"text\": \"I\\u0027ve inserted 0.55€ in the machine\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"When\",\n" +
                "                  \"text\": \"I order a \\\"Chocolate\\\" with 0 sugar\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"Then\",\n" +
                "                  \"text\": \"the instruction generated should be \\\"M:Not enough money 0.05 missing\\\"\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"visualName\": \"| 0.05 | Tea | 1 | M:Not enough money 0.35 missing |\",\n" +
                "              \"tags\": [\n" +
                "                \"@Payment\"\n" +
                "              ],\n" +
                "              \"stepList\": [\n" +
                "                {\n" +
                "                  \"keyword\": \"Given\",\n" +
                "                  \"text\": \"I\\u0027ve inserted 0.05€ in the machine\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"When\",\n" +
                "                  \"text\": \"I order a \\\"Tea\\\" with 1 sugar\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"Then\",\n" +
                "                  \"text\": \"the instruction generated should be \\\"M:Not enough money 0.35 missing\\\"\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"uri\": \"subdomain/extra-hot.feature\",\n" +
                "      \"name\": \"Extra Hot and Orange Juice\",\n" +
                "      \"tags\": [],\n" +
                "      \"scenarios\": [\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: An extra hot tea with 1 sugar\",\n" +
                "          \"tags\": [\n" +
                "            \"@ProtocolOrder\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order an extra hot \\\"Tea\\\" with 1 sugar\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"the instruction generated should be \\\"Th:1:0\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: An Orange juice\",\n" +
                "          \"tags\": [\n" +
                "            \"@ProtocolOrder\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order an \\\"Orange Juice\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"the instruction generated should be \\\"O::\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: Extra sugar with Orange Juice is ignored\",\n" +
                "          \"tags\": [\n" +
                "            \"@ProtocolOrder\",\n" +
                "            \"@wip\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order an \\\"Orange Juice\\\" with 1 sugar\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"the instruction generated should be \\\"O::\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: Extra hot with Orange Juice is ignored\",\n" +
                "          \"tags\": [\n" +
                "            \"@ProtocolOrder\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order an extra hot \\\"Orange Juice\\\" with 1 sugar\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"the instruction generated should be \\\"O::\\\"\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ],\n" +
                "      \"scenarioOutlines\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"uri\": \"subdomain/running-out.feature\",\n" +
                "      \"name\": \"Running Out\",\n" +
                "      \"tags\": [],\n" +
                "      \"scenarios\": [\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: Last Coffee\",\n" +
                "          \"tags\": [\n" +
                "            \"@Notification\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"Given\",\n" +
                "              \"text\": \"no more \\\"Coffee\\\" remaining in the machine\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I order a \\\"Coffee\\\" with 1 sugar\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"a mail should have been sent indicating \\\"Coffee\\\" is running out\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario: Manually send an email\",\n" +
                "          \"tags\": [\n" +
                "            \"@manual\",\n" +
                "            \"@Notification\"\n" +
                "          ],\n" +
                "          \"stepList\": [\n" +
                "            {\n" +
                "              \"keyword\": \"Given\",\n" +
                "              \"text\": \"an empty machine\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"When\",\n" +
                "              \"text\": \"I click on the \\\"Send Test Email\\\" button\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            },\n" +
                "            {\n" +
                "              \"keyword\": \"Then\",\n" +
                "              \"text\": \"a test mail should have been sent\",\n" +
                "              \"grammarMatchCount\": 0\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ],\n" +
                "      \"scenarioOutlines\": [\n" +
                "        {\n" +
                "          \"visualName\": \"Scenario Outline: Last beverage\",\n" +
                "          \"tags\": [\n" +
                "            \"@Notification\"\n" +
                "          ],\n" +
                "          \"exampleScenarios\": [\n" +
                "            {\n" +
                "              \"visualName\": \"| Orange juice |\",\n" +
                "              \"tags\": [\n" +
                "                \"@Notification\"\n" +
                "              ],\n" +
                "              \"stepList\": [\n" +
                "                {\n" +
                "                  \"keyword\": \"Given\",\n" +
                "                  \"text\": \"no more \\\"Orange juice\\\" remaining in the machine\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"When\",\n" +
                "                  \"text\": \"I order a \\\"Orange juice\\\"\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"Then\",\n" +
                "                  \"text\": \"a mail should have been sent indicating \\\"Orange juice\\\" is running out\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"visualName\": \"| Tea |\",\n" +
                "              \"tags\": [\n" +
                "                \"@Notification\"\n" +
                "              ],\n" +
                "              \"stepList\": [\n" +
                "                {\n" +
                "                  \"keyword\": \"Given\",\n" +
                "                  \"text\": \"no more \\\"Tea\\\" remaining in the machine\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"When\",\n" +
                "                  \"text\": \"I order a \\\"Tea\\\"\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"Then\",\n" +
                "                  \"text\": \"a mail should have been sent indicating \\\"Tea\\\" is running out\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"visualName\": \"| Chocolate |\",\n" +
                "              \"tags\": [\n" +
                "                \"@Notification\"\n" +
                "              ],\n" +
                "              \"stepList\": [\n" +
                "                {\n" +
                "                  \"keyword\": \"Given\",\n" +
                "                  \"text\": \"no more \\\"Chocolate\\\" remaining in the machine\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"When\",\n" +
                "                  \"text\": \"I order a \\\"Chocolate\\\"\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                },\n" +
                "                {\n" +
                "                  \"keyword\": \"Then\",\n" +
                "                  \"text\": \"a mail should have been sent indicating \\\"Chocolate\\\" is running out\",\n" +
                "                  \"grammarMatchCount\": 0\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }

}