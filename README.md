# tzatziki


Release
-------

First check for **snapshot** dependencies:

```bash
    fgrep SNAPSHOT **/pom.xml
```

[Maven Release Plugin: The Final Nail in the Coffin](http://axelfontaine.com/blog/final-nail.html)

```bash
    mvn versions:set -DnewVersion=1.0.1
    mvn clean deploy scm:tag -Psign-artifacts
    git status
    git add .
    git commit -m "gutenberg 1.0.1"
    mvn versions:set -DnewVersion=1.0.2-SNAPSHOT
    git add .
    git commit -m "gutenberg 1.0.2-snapshot"
    git push
```

