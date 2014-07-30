package tzatziki.analysis.exec.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class BackgroundExec extends StepContainer {

    private final String keyword;
    private final String name;

    public BackgroundExec(String keyword, String name) {
        this.keyword = keyword;
        this.name = name;
    }

    public BackgroundExec recursiveCopy() {
        BackgroundExec copy = new BackgroundExec(keyword, name);
        recursiveCopy(copy);
        return copy;
    }

}
