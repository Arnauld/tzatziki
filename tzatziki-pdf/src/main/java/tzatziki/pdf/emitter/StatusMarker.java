package tzatziki.pdf.emitter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import gutenberg.itext.FontAwesomeAdapter;
import tzatziki.analysis.exec.model.Status;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StatusMarker {
    private FontAwesomeAdapter fontAwesomeAdapter;
    private float symbolSize = 12;

    public Chunk statusMarker(Status status) {
        switch (status) {
            case Passed:
                return fontAwesomeAdapter().symbol("check-circle", symbolSize, BaseColor.GREEN.darker());
            case Skipped:
                return fontAwesomeAdapter().symbol("exclamation-circle", symbolSize, BaseColor.ORANGE);
            case Undefined:
                return fontAwesomeAdapter().symbol("question-circle", symbolSize, BaseColor.RED.darker());
            case Failed:
                return fontAwesomeAdapter().symbol("ban", symbolSize, BaseColor.RED);
            case Pending:
                return fontAwesomeAdapter().symbol("gears", symbolSize, BaseColor.ORANGE);
            default:
                return fontAwesomeAdapter().symbol("minus-circle", symbolSize, BaseColor.BLUE);
        }
    }

    private FontAwesomeAdapter fontAwesomeAdapter() {
        if (fontAwesomeAdapter == null)
            try {
                fontAwesomeAdapter = new FontAwesomeAdapter();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        return fontAwesomeAdapter;
    }
}
