package tzatziki.pdf;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Margin {

    public static Margin create(float margin) {
        return new Margin(margin);
    }

    public final float marginLeft;
    public final float marginRight;
    public final float marginTop;
    public final float marginBottom;

    public Margin(float margin) {
        this(margin, margin, margin, margin);
    }

    public Margin(float marginLeftRight, float marginTopBottom) {
        this(marginLeftRight, marginLeftRight, marginTopBottom, marginTopBottom);
    }

    public Margin(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }
}
