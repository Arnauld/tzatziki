package tzatziki.analysis.java;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.*;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GrammarParser {
    public static Pattern STEP_KEYWORD_QUALIFIED_NAME = Pattern.compile("cucumber\\.api\\.java\\.[^.]+\\.(.+)");
    //
    private GrammarParserListener listener = new GrammarParserListenerAdapter();
    private JavaProjectBuilder builder = new JavaProjectBuilder();

    public GrammarParser usingSourceDirectory(File sourceTree) {
        builder.addSourceTree(sourceTree);
        return this;
    }

    public GrammarParser usingListener(GrammarParserListener listener) {
        this.listener = listener;
        return this;
    }

    public Grammar process() {
        Grammar grammar = new Grammar();
        for (JavaPackage pkg : builder.getPackages()) {
            PackageEntry pkgEntry = analyzePackage(pkg);
            if (pkgEntry.hasEntries())
                grammar.declarePackage(pkgEntry);
        }
        return grammar;
    }

    private PackageEntry analyzePackage(JavaPackage pkg) {
        listener.enteringPackage(pkg);

        PackageEntry pkgGroup = new PackageEntry(pkg.getName());
        describe(pkgGroup, pkg);

        for (JavaClass klazz : pkg.getClasses()) {
            ClassEntry classEntry = analyzeClass(klazz);
            if (classEntry.hasEntries())
                pkgGroup.declareClass(classEntry);
        }

//        for (JavaPackage subPkg : pkg.getSubPackages()) {
//            PackageEntry subPkgEntry = analyzePackage(subPkg);
//            if(subPkgEntry.hasEntries())
//                pkgGroup.declareSubPackage(subPkgEntry);
//        }
        listener.exitingPackage(pkg);
        return pkgGroup;
    }

    private ClassEntry analyzeClass(JavaClass klazz) {
        listener.enteringClass(klazz);

        ClassEntry group = new ClassEntry(klazz.getPackageName(), klazz.getName());
        describe(group, klazz);

        for (JavaMethod method : klazz.getMethods()) {
            MethodEntry methodEntry = analyzeMethod(method);
            if (methodEntry.hasPatterns()) {
                group.declareEntry(methodEntry);
            }
        }
        listener.exitingClass(klazz);
        return group;
    }

    private static void describe(Describable describable, JavaAnnotatedElement klazz) {
        describable.describeWith(klazz.getComment());
    }

    private MethodEntry analyzeMethod(JavaMethod method) {
        listener.enteringMethod(method);

        List<JavaType> parameterTypes = method.getParameterTypes();
        MethodEntry methodEntry = new MethodEntry(method.getName(), typesAsStrings(parameterTypes));
        describe(methodEntry, method);

        fillWithPatterns(method, methodEntry);

        if (methodEntry.hasPatterns()) {
            fillWithParameters(method, methodEntry);
        }

        listener.exitingMethod(method);
        return methodEntry;
    }

    private static List<String> typesAsStrings(List<JavaType> parameterTypes) {
        return FluentIterable.from(parameterTypes).transform(new Function<JavaType, String>() {
            @Override
            public String apply(JavaType javaType) {
                return javaType.getFullyQualifiedName();
            }
        }).toList();
    }

    private void fillWithParameters(JavaMethod method, MethodEntry methodEntry) {
        List<DocletTag> paramDocs = method.getTagsByName("param");
        List<JavaParameter> parameters = method.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            JavaParameter param = parameters.get(i);
            String paramName = param.getName();
            DocletTag paramDoc = findParamDocByNameOrIndex(paramName, i, paramDocs);
            String doc = (paramDoc != null) ? extractDoc(paramName, paramDoc) : null;

            methodEntry.defineParameter(i, paramName, doc);
        }
    }

    private String extractDoc(String value, DocletTag paramDoc) {
        String cleaned = paramDoc.getValue().trim();
        if (cleaned.startsWith(value))
            return cleaned.substring(value.length()).trim();
        return cleaned;
    }

    /**
     * Attempt to retrieve the parameter (<code>@param</code> doclet parameter)
     * using the parameter name, otherwise fallback to the parameter index.
     */
    private DocletTag findParamDocByNameOrIndex(String name, int paramIndex, List<DocletTag> tags) {
        for (DocletTag tag : tags) {
            if (name.equals(tag.getParameters().get(0))) {
                return tag;
            }
        }
        if (paramIndex < tags.size()) {
            return tags.get(paramIndex);
        }
        return null;
    }

    private void fillWithPatterns(JavaMethod method, MethodEntry methodEntry) {
        List<JavaAnnotation> annotations = method.getAnnotations();
        for (JavaAnnotation annotation : annotations) {
            String keyword = extractStepKeywordIfRelevant(annotation);
            if (keyword != null) {
                String pattern = String.valueOf(annotation.getNamedParameter("value"));
                methodEntry.declarePattern(keyword, unescape(unquote(pattern)));
            }
        }
    }

    private String extractStepKeywordIfRelevant(JavaAnnotation annotation) {
        JavaClass annotationType = annotation.getType();
        String name = qualifiedName(annotationType);

        // Note: it seems that a java annotation is itself annotated
        // with '@cucumber.runtime.java.StepDefAnnotation'
        // it may be a safer way to determine if an annotation corresponds
        // to a keyword...

        Matcher matcher = STEP_KEYWORD_QUALIFIED_NAME.matcher(name);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String qualifiedName(JavaClass klazz) {
        return klazz.getPackageName() + "." + klazz.getName();
    }

    private static String unescape(String value) {
        return value.replaceAll("\\\\(.)", "$1");
    }

    private static String unquote(String value) {
        int len = value.length();
        if (len >= 2 && value.charAt(0) == '"' && value.charAt(len - 1) == '"')
            return value.substring(1, len - 1);
        return value;
    }

}
