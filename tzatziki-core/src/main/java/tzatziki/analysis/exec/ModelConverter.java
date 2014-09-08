package tzatziki.analysis.exec;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import gherkin.formatter.Argument;
import gherkin.formatter.model.*;
import tzatziki.analysis.exec.model.*;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ModelConverter {
    public FeatureExec convertFeature(String uri, Feature feature) {
        FeatureExec featureExec = new FeatureExec(uri, feature.getKeyword(), feature.getName());
        featureExec.declareTags(convertTags(feature.getTags()));
        featureExec.declareComments(convertComments(feature.getComments()));
        featureExec.declareDescription(feature.getDescription());

        return featureExec;
    }

    public BackgroundExec convertBackground(Background background) {
        BackgroundExec backgroundExec = new BackgroundExec(background.getKeyword(), background.getName());
        backgroundExec.declareComments(convertComments(background.getComments()));
        backgroundExec.declareDescription(background.getDescription());

        return backgroundExec;
    }

    public ScenarioExec convertScenario(Scenario scenario) {
        ScenarioExec scenarioExec = new ScenarioExec(scenario.getKeyword(), scenario.getName());
        scenarioExec.declareLineRange(convertLineRange(scenario.getLineRange()));
        scenarioExec.declareTags(convertTags(scenario.getTags()));
        scenarioExec.declareComments(convertComments(scenario.getComments()));
        scenarioExec.declareDescription(scenario.getDescription());

        return scenarioExec;
    }

    private LineRange convertLineRange(Range lineRange) {
        return new LineRange(lineRange.getFirst(), lineRange.getLast());
    }

    public ScenarioOutlineExec convertScenarioOutline(ScenarioOutline scenarioOutline) {
        ScenarioOutlineExec outlineExec = new ScenarioOutlineExec(scenarioOutline.getKeyword(), scenarioOutline.getName());
        outlineExec.declareTags(convertTags(scenarioOutline.getTags()));
        outlineExec.declareComments(convertComments(scenarioOutline.getComments()));
        outlineExec.declareDescription(scenarioOutline.getDescription());

        return outlineExec;
    }

    public ExamplesExec convertExamples(Examples examples) {
        ExamplesExec examplesExec = new ExamplesExec(examples.getKeyword(), examples.getName());
        examplesExec.declareTags(convertTags(examples.getTags()));
        examplesExec.declareComments(convertComments(examples.getComments()));
        examplesExec.declareDescription(examples.getDescription());

        return examplesExec;
    }

    public StepExec convertStep(Step step) {
        StepExec stepExec = new StepExec(step.getKeyword(), step.getName());
        stepExec.declareComments(convertComments(step.getComments()));
        DocString docString = step.getDocString();
        if (docString != null) {
            stepExec.declareDocString(docString.getValue());
        }
        stepExec.declareTable(convertTable(step.getRows()));
        return stepExec;
    }

    private DataTable convertTable(List<DataTableRow> rows) {
        DataTable table = new DataTable();
        if (rows != null) {
            for (DataTableRow row : rows) {
                table.declareRow(convertRow(row));
            }
        }
        return table;
    }

    private DataTable.Row convertRow(DataTableRow row) {
        return new DataTable.Row(row.getCells(), convertComments(row.getComments()));
    }

    public ResultExec convertResult(Result result) {
        return new ResultExec(
                result.getStatus(),
                result.getError(),
                result.getErrorMessage(),
                result.getDuration());
    }

    public MatchExec convertMatch(Match match) {
        return new MatchExec(
                match.getLocation(),
                convertArguments(match.getArguments()));
    }

    private List<MatchExec.Arg> convertArguments(List<Argument> arguments) {
        return FluentIterable.from(arguments).transform(new Function<Argument, MatchExec.Arg>() {
            @Override
            public MatchExec.Arg apply(Argument input) {
                return new MatchExec.Arg(input.getVal(), input.getOffset());
            }
        }).toList();
    }

    private List<String> convertComments(List<Comment> comments) {
        return FluentIterable.from(comments).transform(new Function<Comment, String>() {
            @Override
            public String apply(Comment input) {
                return input.getValue();
            }
        }).toList();
    }

    private List<String> convertTags(List<Tag> tags) {
        return FluentIterable.from(tags).transform(new Function<Tag, String>() {
            @Override
            public String apply(Tag input) {
                return input.getName();
            }
        }).toList();
    }
}
