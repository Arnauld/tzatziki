package tzatziki.junit;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import tzatziki.analysis.step.Feature;
import tzatziki.analysis.step.FeatureParser;
import tzatziki.analysis.step.Features;
import tzatziki.analysis.tag.TagDictionary;
import tzatziki.analysis.tag.TagDictionaryLoader;
import tzatziki.util.PropertiesLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SanityTagChecker extends ParentRunner<FeatureTagRunner> {

    private final List<FeatureTagRunner> children;
    private TagDictionary tagDictionary;
    private Description description;

    public SanityTagChecker(Class<?> klazz) throws InitializationError {
        super(klazz);
        tagDictionary = new TagDictionary();
        children = initChildren();
    }

    private List<FeatureTagRunner> initChildren() throws InitializationError {
        List<Throwable> errors = Lists.newArrayList();
        List<FrameworkMethod> dictionnaryMethods = providerMethods(errors, TagDictionaryProvider.class, TagDictionary.class);
        List<FrameworkMethod> featuresMethods = providerMethods(errors, FeaturesProvider.class, Features.class);

        if (!errors.isEmpty()) {
            throw new InitializationError(errors);
        }

        for (FrameworkMethod method : dictionnaryMethods) {
            try {
                TagDictionary dictionary = (TagDictionary) method.invokeExplosively(null);
                tagDictionary.declareTags(dictionary);
            } catch (Throwable err) {
                throw new InitializationError(err);
            }
        }

        Class<?> testClass = getTestClass().getJavaClass();
        List<FeatureTagRunner> children = Lists.newArrayList();
        for (FrameworkMethod method : featuresMethods) {
            try {
                Features features = (Features) method.invokeExplosively(null);
                for (Feature feature : features.features()) {
                    FeatureTagRunner runner = new FeatureTagRunner(testClass, feature, tagDictionary);
                    children.add(runner);
                }
            } catch (Throwable err) {
                throw new InitializationError(err);
            }
        }
        return children;
    }

    private List<FrameworkMethod> providerMethods(List<Throwable> errors,
                                                  Class<? extends Annotation> annotationClass,
                                                  Class<?> returnType) throws InitializationError {
        List<FrameworkMethod> annotatedMethods = getTestClass().getAnnotatedMethods(annotationClass);
        if (annotatedMethods.isEmpty())
            throw new InitializationError("No method annotated with " + annotationClass.getSimpleName() + " found");

        for (FrameworkMethod method : annotatedMethods) {
            validatePublicStatic(method, returnType, errors);
        }
        return annotatedMethods;
    }

    private void validatePublicStatic(FrameworkMethod method, Class<?> returnType, List<Throwable> errors) {
        Method fMethod = method.getMethod();
        if (!Modifier.isStatic(fMethod.getModifiers())) {
            String state = "should";
            errors.add(new Exception("Method " + fMethod.getName() + "() " + state + " be static"));
        }
        if (!Modifier.isPublic(fMethod.getDeclaringClass().getModifiers())) {
            errors.add(new Exception("Class " + fMethod.getDeclaringClass().getName() + " should be public"));
        }
        if (!Modifier.isPublic(fMethod.getModifiers())) {
            errors.add(new Exception("Method " + fMethod.getName() + "() should be public"));
        }
        if (fMethod.getReturnType() != returnType) {
            errors.add(new Exception("Method " + fMethod.getName() + "() should return " + returnType.getSimpleName()));
        }
    }

    @Override
    protected List<FeatureTagRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(FeatureTagRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(FeatureTagRunner child, RunNotifier notifier) {
        child.run(notifier);
    }

    @Override
    public Description getDescription() {
        if (description == null) {
            description = Description.createSuiteDescription(getName(), getTestClass().getJavaClass());
            for (FeatureTagRunner child : getChildren()) {
                description.addChild(describeChild(child));
            }
        }
        return description;
    }

    public static Features loadFeaturesFromSourceDirectory(File... sourceDirectories) {
        FeatureParser parser = new FeatureParser();
        for (File sourceDir : sourceDirectories)
            parser.usingSourceDirectory(sourceDir);
        Features features = parser.process();
        Assert.assertTrue("No features found", !features.features().isEmpty());
        return features;
    }

    public static TagDictionary loadTagsFromUTF8PropertiesResources(String resourcePath) throws IOException {
        return new TagDictionaryLoader().loadTagsFromUTF8PropertiesResources(resourcePath);
    }

    private static class InvalidReturnTypeException extends IllegalArgumentException {
        public InvalidReturnTypeException(String message) {
            super(message);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    public @interface TagDictionaryProvider {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    public @interface FeaturesProvider {
    }
}
