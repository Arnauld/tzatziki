package tzatziki.analysis.exec;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.BackgroundExec;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.analysis.exec.model.ScenarioExec;
import tzatziki.analysis.exec.model.ScenarioOutlineExec;
import tzatziki.analysis.exec.model.StepContainer;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.util.MemoizableIterator;

import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public abstract class ExecutionReport implements Formatter, Reporter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ModelConverter converter;
    //
    private String currentUri;
    private FeatureExec currentFeature;
    private StepContainer currentStepContainer;
    private StepExec currentStep;
    //
    private MemoizableIterator<StepExec> stepIterator;

    public ExecutionReport() {
        this(new ModelConverter());
    }

    public ExecutionReport(ModelConverter converter) {
        this.converter = converter;
    }

    @Override
    public void uri(String uri) {
        log.debug("uri: {}", uri);
        this.currentUri = uri;
    }

    @Override
    public void feature(Feature feature) {
        log.debug("feature: {}", feature.getName());

        currentStep = null;
        currentStepContainer = null;
        stepIterator = null;

        flushCurrentFeature();
        this.currentFeature = converter.convertFeature(currentUri, feature);
    }

    private void flushCurrentFeature() {
        if (currentFeature != null) {
            emit(currentFeature);
        }
        currentFeature = null;
    }

    protected abstract void emit(FeatureExec feature);

    @Override
    public void background(Background background) {
        log.debug("background: {}", background.getName());
        currentStep = null;
        stepIterator = null;

        BackgroundExec backgroundExec = converter.convertBackground(background);
        currentFeature.background(backgroundExec);
        currentStepContainer = backgroundExec;
    }

    @Override
    public void scenario(Scenario scenario) {
        log.debug("scenario: {}", scenario.getName());
        currentStep = null;
        stepIterator = null;

        ScenarioExec scenarioExec = converter.convertScenario(scenario);
        currentFeature.declareScenario(scenarioExec);
        currentStepContainer = scenarioExec;
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        log.debug("scenarioOutline: {}", scenarioOutline.getName());
        currentStep = null;
        stepIterator = null;

        ScenarioOutlineExec scenarioOutlineExec = converter.convertScenarioOutline(scenarioOutline);
        currentFeature.declareScenarioOutline(scenarioOutlineExec);
        currentStepContainer = scenarioOutlineExec;
    }

    @Override
    public void examples(Examples examples) {
        log.debug("examples: {}", examples.getName());
        currentStep = null;

        ScenarioOutlineExec outlineExec = (ScenarioOutlineExec) currentStepContainer;
        outlineExec.declareExamples(converter.convertExamples(examples));
    }

    @Override
    public void step(Step step) {
        log.debug("step: {}", step.getName());
        StepExec stepExec = converter.convertStep(step);
        currentStepContainer.declareStep(stepExec);
        currentStep = stepExec;
    }

    @Override
    public void result(Result result) {
        log.debug("result: {}", result);
        stepIterator.current().declareResult(converter.convertResult(result));
    }

    @Override
    public void match(Match match) {
        log.debug("match: {}", match);

        // unfortunately match does not apply to the current step,
        // there are invoked once all scenario's steps are defined
        if(stepIterator == null)
            stepIterator = MemoizableIterator.wrap(currentStepContainer.steps().iterator());

        StepExec stepExec = stepIterator.next();
        stepExec.declareMatch(converter.convertMatch(match));
    }


    @Override
    public void embedding(String mimeType, byte[] data) {
        log.debug("embedding: {}", mimeType);
        if (currentStep != null)
            currentStep.embedding(mimeType, data);
        else if (currentStepContainer != null)
            currentStepContainer.embedding(mimeType, data);
    }

    @Override
    public void write(String text) {
        log.debug("write: {}", text);
        if (currentStep != null) {
            currentStep.text(text);
            return;
        }
        if (currentStepContainer != null) {
            currentStepContainer.text(text);
            return;
        }
        log.warn("Unsupported text call");
    }

    @Override
    public void eof() {
        log.debug("eof");
    }

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
        log.debug("syntaxError {} {}", state, event);
    }

    @Override
    public void done() {
        log.debug("done");
        flushCurrentFeature();
    }

    @Override
    public void close() {
        log.debug("close");
    }

    @Override
    public void before(Match match, Result result) {
        log.debug("before");
    }

    @Override
    public void after(Match match, Result result) {
        log.debug("after");
    }

}
