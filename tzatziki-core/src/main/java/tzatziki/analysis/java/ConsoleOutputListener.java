package tzatziki.analysis.java;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;

import java.util.Collection;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ConsoleOutputListener implements GrammarParserListener {
    @Override
    public void aboutToParsePackages(Collection<JavaPackage> packages) {
        System.out.println("ConsoleOutputListener.aboutToParsePackages: " + packages);
    }

    @Override
    public void aboutToParseClasses(Collection<JavaClass> classes) {
        System.out.println("ConsoleOutputListener.aboutToParseClasses: " + classes);
    }

    @Override
    public void enteringPackage(JavaPackage pkg) {
        System.out.println("ConsoleOutputListener.enteringPackage: " + pkg);
    }

    @Override
    public void exitingPackage(JavaPackage pkg) {
        System.out.println("ConsoleOutputListener.exitingPackage: " + pkg);
    }

    @Override
    public void enteringClass(JavaClass klazz) {
        System.out.println("ConsoleOutputListener.enteringClass: " + klazz);
    }

    @Override
    public void exitingClass(JavaClass klazz) {
        System.out.println("ConsoleOutputListener.exitingClass: " + klazz);
    }

    @Override
    public void enteringMethod(JavaMethod method) {
        System.out.println("ConsoleOutputListener.enteringMethod: " + method);
    }

    @Override
    public void exitingMethod(JavaMethod method) {
        System.out.println("ConsoleOutputListener.exitingMethod: " + method);
    }
}
