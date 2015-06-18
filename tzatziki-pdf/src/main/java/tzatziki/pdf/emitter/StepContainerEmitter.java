package tzatziki.pdf.emitter;

import com.google.common.base.Optional;
import gutenberg.itext.ITextContext;
import gutenberg.itext.model.Markdown;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tzatziki.analysis.exec.model.EmbeddingAndWriteContainer;
import tzatziki.analysis.exec.model.HasTags;
import tzatziki.analysis.exec.model.StepContainer;
import tzatziki.analysis.exec.model.StepExec;
import tzatziki.pdf.Comments;
import tzatziki.pdf.model.Steps;
import tzatziki.pdf.model.Tags;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StepContainerEmitter {

    private Logger log = LoggerFactory.getLogger(BackgroundEmitter.class);

    public void emitTags(HasTags hasTags, ITextContext emitterContext) {
        emitterContext.emit(Tags.class, new Tags(hasTags.tags()));
    }

    public void emitSteps(StepContainer stepContainer, ITextContext emitterContext) {
        emitterContext.emit(Steps.class, new Steps(stepContainer.steps()));
    }

    public void emitEmbeddings(EmbeddingAndWriteContainer scenario, ITextContext emitterContext) {
    }

    public void emitDescription(StepContainer stepContainer, ITextContext emitterContext) {
        // Description
        StringBuilder b = new StringBuilder();
        String description = stepContainer.description();
        if (StringUtils.isNotBlank(description)) {
            b.append(description);
        }

        Optional<StepExec> first = stepContainer.steps().first();
        if (first.isPresent()) {
            StepExec stepExec = first.get();
            for (String comment : stepExec.comments()) {
                String uncommented = Comments.discardCommentChar(comment);
                if (!Comments.startsWithComment(uncommented)) { // double # case
                    b.append(uncommented).append(Comments.NL);
                }
            }
        }

        if (b.length() > 0) {
            log.debug("Description content >>{}<<", b);
            emitterContext.emit(Markdown.class, new Markdown(b.toString()));
        }
    }

}
