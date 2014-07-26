package tzatziki.analysis.java;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GrammarParserListenerAdapter implements GrammarParserListener {
    @Override
    public void enteringPackage(JavaPackage pkg) {
    }

    @Override
    public void exitingPackage(JavaPackage pkg) {
    }

    @Override
    public void enteringClass(JavaClass klazz) {
    }

    @Override
    public void exitingClass(JavaClass klazz) {
    }

    @Override
    public void enteringMethod(JavaMethod method) {
    }

    @Override
    public void exitingMethod(JavaMethod method) {
    }
}
