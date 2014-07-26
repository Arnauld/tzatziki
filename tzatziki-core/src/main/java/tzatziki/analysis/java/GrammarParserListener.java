package tzatziki.analysis.java;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface GrammarParserListener {
    void enteringPackage(JavaPackage pkg);

    void exitingPackage(JavaPackage pkg);

    void enteringClass(JavaClass klazz);

    void exitingClass(JavaClass klazz);

    void enteringMethod(JavaMethod method);

    void exitingMethod(JavaMethod method);
}
