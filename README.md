# tzatziki


Release
-------

## Jdk6 Compilation

```
apps/boot2docker

bash-3.2$ docker pull jamesdbloom/docker-java6-maven
bash-3.2$ docker run -i -t jamesdbloom/docker-java6-maven /bin/bash
[ root@4291b084fd0c:/local/git ]$ git clone https://github.com/Arnauld/tzatziki.git
[ root@4291b084fd0c:/local/git ]$ cd tzatziki && mvn clean test
```

## Release

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

