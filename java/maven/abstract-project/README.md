# Hijacking in abstract-project

There are two projects in this directory:
1. `install-me-first`. This project setups the dependency tree for the victim.
2. `victim`. This project contains the victim application.

## Normal Execution

1. ```shell
   install-me-first$ mvn clean install
   ```
   This will install dependencies `D1`, `D11`, and `D12` in the local Maven repository.
2. ```shell
   victim$ mvn clean package
   ```
   This will compile the victim application which is dependent upon `D1`.
3. ```shell
   victim$ java -jar target/victim-1.0.jar
   ```
   This will output:
   ```shell
   hello world!
   ```

## Malicious Execution

Simply change the first command to inject the malicious class `org.apache.
commons.lang3.StringUtils`.
   ```shell
   install-me-first$ mvn clean install -Pinject
   ```
### Ways to package the malicious class

1. `org.apache.maven.plugins:maven-shade-plugin` (default):
   ```shell
   mvn clean package
   java -jar target/victim-1.0.jar
   ```
   All the dependencies will be packaged into a single JAR file, including the malicious class.
2. `org.apache.maven.plugins:maven-assembly-plugin`
   ```shell
   mvn clean package -Dassembly
    java -jar target/victim-1.0-jar-with-dependencies.jar
   ```
   Does not work in this case, as the resolves dependencies are [wrapped in 
   an unordered set](https://github.com/apache/maven-assembly-plugin/blob/ada2cc1c9025f32edb7bfd7fe7e630e9719a71d3/src/main/java/org/apache/maven/plugins/assembly/artifact/DefaultDependencyResolver.java#L247).
3. `org.apache.maven.plugins:maven-jar-plugin`:
   ```shell
    mvn clean package -Djar
    java -jar target/victim-1.0.jar
   ```
   This plugin creates a JAR file and the dependencies are loaded from 
   another directory.
   The attack works because the order of the dependencies is preserved in the JAR file.

4. `org.springframework.boot:spring-boot-maven-plugin`:
   ```shell
    mvn clean package -Dspring
    java -jar target/victim-1.0-spring-boot.jar
   ```
   This plugin directly embeds the dependencies in the JAR file, and the malicious class is included.
   The order of the dependencies is also the same as resolved dependencies in 
   the JAR file.

5. `org.apache.felix:maven-bundle-plugin`:
   ```shell
    mvn clean package -Dbundle
    java -jar target/victim-1.0.jar
   ```
   The order of dependencies have to be reversed because unlike other 
   plugins, `maven-bundle-plugin` overrides the genuine `org.apache.commons.
   lang3.StringUtils` class with the malicious one if the malicious class is 
   present later in the classpath.

6. `io.quarkus:quarkus-maven-plugin`:
   ```shell
    mvn clean package -Dquarkus
    java -jar target/victim-1.0-quarkus.jar
   ```
   It gives a warning about the exact class being overridden, but the malicious class is still executed.
   ```text
   [WARNING] [io.quarkus.deployment.pkg.steps.JarResultBuildStep] Dependencies with duplicate files detected. The dependencies [dev.scored:D1::jar:1.0.0[paths: /home/aman/.m2/repository/dev/scored/D1/1.0.0/D1-1.0.0.jar;], org.apache.commons:commons-lang3::jar:3.17.0[paths: /home/aman/.m2/repository/org/apache/commons/commons-lang3/3.17.0/commons-lang3-3.17.0.jar;]] contain duplicate files, e.g. org/apache/commons/lang3/StringUtils.class
   ```
### Output

All these methods will result in the same output (except 
`maven-assembly-plugin`) when running the victim application:
```
shadow file sent to hacker ;)
hello world!
```