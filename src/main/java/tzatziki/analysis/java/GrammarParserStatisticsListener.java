package tzatziki.analysis.java;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GrammarParserStatisticsListener extends GrammarParserListenerAdapter {
    private int packagesParsed = 0;
    private int classesParsed = 0;
    private int methodsParsed = 0;

    public int numberOfClassesParsed() {
        return classesParsed;
    }

    public int numberOfPackagesParsed() {
        return packagesParsed;
    }

    public int numberOfMethodsParsed() {
        return methodsParsed;
    }

    @Override
    public void exitingPackage(JavaPackage pkg) {
        packagesParsed++;
    }

    @Override
    public void exitingClass(JavaClass klazz) {
        classesParsed++;
    }

    @Override
    public void exitingMethod(JavaMethod method) {
        methodsParsed++;
    }
}
