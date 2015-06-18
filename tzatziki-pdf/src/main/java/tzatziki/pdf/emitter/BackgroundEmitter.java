package tzatziki.pdf.emitter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.FontCopier;
import gutenberg.itext.ITextContext;
import gutenberg.itext.Sections;
import gutenberg.itext.Styles;
import gutenberg.util.KeyValues;
import tzatziki.analysis.exec.model.BackgroundExec;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class BackgroundEmitter implements gutenberg.itext.Emitter<BackgroundExec> {

    public static final String IN_SUB_SECTION = "background-in-sub-section";
    public static final String TITLE_PATTERN = "background-title-pattern";
    public static final String KEYWORD_COLOR = "background-title-keyword-color";

    private final int hLevel;
    private StepContainerEmitter stepsEmitter;

    public BackgroundEmitter() {
        this(2);
    }

    public BackgroundEmitter(int hLevel) {
        this(hLevel, new StepContainerEmitter());
    }

    public BackgroundEmitter(int hLevel, StepContainerEmitter stepsEmitter) {
        this.hLevel = hLevel;
        this.stepsEmitter = stepsEmitter;
    }

    @Override
    public void emit(BackgroundExec background, ITextContext emitterContext) {
        Sections sections = emitterContext.sections();
        KeyValues kvs = emitterContext.keyValues();

        Integer rawOffset = kvs.getInteger(FeatureEmitter.FEATURE_HEADER_LEVEL_OFFSET).or(0);
        int headerLevel = hLevel + rawOffset;

        Paragraph title = formatTitle(headerLevel, emitterContext, background);

        boolean inSection = kvs.getBoolean(IN_SUB_SECTION, false);
        if (inSection) {
            sections.newSection(title, headerLevel);
        } else {
            emitterContext.append(title);
        }
        try {
            stepsEmitter.emitDescription(background, emitterContext);
            stepsEmitter.emitEmbeddings(background, emitterContext);
            stepsEmitter.emitSteps(background, emitterContext);
        } finally {
            if (inSection) {
                sections.leaveSection(headerLevel); // end-of-background
            }
        }
    }

    protected Paragraph formatTitle(int headerLevel, ITextContext emitterContext, BackgroundExec background) {
        Styles styles = emitterContext.styles();
        Font font = styles.sectionTitleFontForLevel(headerLevel + 1);

        Paragraph p = new Paragraph();
        p.add(new Chunk(background.name(), font));
        p.add(new Chunk(" (", font));
        p.add(new Chunk(background.keyword(), new FontCopier(font).italic().color(styles.getColor(KEYWORD_COLOR).or(BaseColor.LIGHT_GRAY)).get()));
        p.add(new Chunk(")", font));

        return p;
    }

}
