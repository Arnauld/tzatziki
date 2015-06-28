tzatziki
--------

## Getting started


**First generate the execution report**

This can be achieved by adding the tzatziki reporter that will track all information during cucumber execution.
Reporter will then create an execution file: `target/myapp/exec.json`

```java

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = true,
        tags = {"~@wip", "~@notImplemented"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/myapp")
public class RunAllFeatures {
}

```

**Generate a report based on the execution file**

Whereas this may seem a bit complicated, this is actually really simple.

1. Read the execution file (`myapp/exec.json`) back to the tzatziki model of execution (`List<FeatureExec>`)
2. Indicate the output file; (see [TestSettings](#test-settings)
3. Configure the report using the basic builder
    * declare the `imageDir` variable used in the markdown image path definition (see [preamble.md](#preamble-md)); this will be replaced within markdown file to complete image path. You can use whatever variable name you want
    * Main title of the report and its sub-title; they only appear in the default first page renderer

Then in order, the following content will be included in the document:

1. Include the markdown file named `preamble.md`
2. All features
3. Sample steps used as a legend for icon

```java
package myapp.feature;

import com.itextpdf.text.DocumentException;
import gutenberg.itext.FontModifier;
import gutenberg.itext.Styles;
import gutenberg.itext.model.Markdown;
import org.apache.commons.io.IOUtils;
import tzatziki.analysis.exec.gson.JsonIO;
import tzatziki.analysis.exec.model.FeatureExec;
import tzatziki.pdf.support.Configuration;
import tzatziki.pdf.support.DefaultPdfReportBuilder;
import myapp.TestSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PdfSimpleReport {
    public void generate() throws IOException, DocumentException {
        List<FeatureExec> execs = loadExec(new File(buildDir(), "myapp/exec.json"));

        File fileOut = new File(buildDir(), "myapp/report.pdf");

        new DefaultPdfReportBuilder()
                .using(new Configuration()
                                .displayFeatureTags(true)
                                .displayScenarioTags(true)
                                .declareProperty("imageDir",
                                        new File(baseDir(), "/src/test/resources/myapp/feature/images").toURI().toString())
                                .adjustFont(Styles.TABLE_HEADER_FONT, new FontModifier().size(10.0f))
                )
                .title("myapp")
                .subTitle("Technical & Functional specifications")
                .markup(Markdown.fromUTF8Resource("/myapp/feature/preamble.md"))
                .features(execs)
                .sampleSteps()
                .generate(fileOut);
    }

    private static File buildDir() {
        String baseDir = new TestSettings().getBuildDir();
        return new File(baseDir);
    }

    private static File baseDir() {
        String baseDir = new TestSettings().getBaseDir();
        return new File(baseDir);
    }

    private static List<FeatureExec> loadExec(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return new JsonIO().load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
```

## A more advanced report




## Test settings

Simply provide settings, project directory, resource path and so on...

The snippet provided are a basic usage of both property file and maven resource filtering.

`myapp/test-settings.properties`

```
buildDir=${project.build.directory}
baseDir=${project.basedir}
```


`pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    ...

    <build>
        <testResources>
            <testResource>
                <directory>src/test/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*.properties</include>
                </includes>
            </testResource>
            <testResource>
                <directory>src/test/resources</directory>
                <filtering>false</filtering>
                <excludes>
                    <exclude>**/*.properties</exclude>
                </excludes>
            </testResource>
        </testResources>

        ...

    </build>
</project>
```

`myapp.TestSettings`

```java
public class TestSettings {

    private Properties properties;

    public TestSettings() {
    }

    public String getBuildDir() {
        return getProperties().getProperty("buildDir");
    }

    public String getBaseDir() {
        return getProperties().getProperty("baseDir");
    }

    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            InputStream stream = null;
            try {
                stream = getClass().getResourceAsStream("/myapp/test-settings.properties");
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to open settings", e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
        return properties;
    }
}
```

## Tag Dictionary (#tag-dictionary)

By using a tag dictionary, you can ensure everyone is using the same tags.
Checks can also be easily added to ensure only tags defined within the dictionary are used within the feature files.
This prevents misspell by raising an error.

An example of `tags.properties`

```
wip=Work in progress
notImplemented=Behavior not implemented

default=Define standard and predefined objects with basic characteristics (e.g. limit order...)

orderBook=Order Book

limitOrder=Limit Order
marketOrder=Market Order
stopOrder=Stop 'Loss' Order

timeInForce=Time In Force

placeOrder=Order placed in order book
bestPrices=Order book query to retrieve actual best prices (best bid or ask)
cumulativeView=Order book cumulative view
matchingPrinciple=Order book's Matching principles

```
