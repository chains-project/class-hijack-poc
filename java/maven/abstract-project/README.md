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
   ```text
   Connection is valid!
   Connected to port 5432 with user postgres
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
   mvn clean package -Passembly
    java -jar target/victim-1.0-jar-with-dependencies.jar
   ```
   Does not work in this case, as the resolves dependencies are [wrapped in 
   an unordered set](https://github.com/apache/maven-assembly-plugin/blob/ada2cc1c9025f32edb7bfd7fe7e630e9719a71d3/src/main/java/org/apache/maven/plugins/assembly/artifact/DefaultDependencyResolver.java#L247).
3. `org.apache.maven.plugins:maven-jar-plugin`:
   ```shell
    mvn clean package -Pjar
    java -jar target/victim-1.0.jar
   ```
   This plugin creates a JAR file and the dependencies are loaded from 
   another directory.
   The attack works because the order of the dependencies is preserved in the JAR file.

4. `org.springframework.boot:spring-boot-maven-plugin`:
   ```shell
    mvn clean package -Pspring
    java -jar target/victim-1.0-spring-boot.jar
   ```
   This plugin directly embeds the dependencies in the JAR file, and the malicious class is included.
   The order of the dependencies is also the same as resolved dependencies in 
   the JAR file.

5. `org.apache.felix:maven-bundle-plugin`:
   ```shell
    mvn clean package -Pbundle
    java -jar target/victim-1.0.jar
   ```
   The order of dependencies have to be reversed because unlike other 
   plugins, `maven-bundle-plugin` overrides the genuine `org.apache.commons.
   lang3.StringUtils` class with the malicious one if the malicious class is 
   present later in the classpath.

6. `io.quarkus:quarkus-maven-plugin`:
   ```shell
    mvn clean package -Pquarkus
    java -jar target/victim-1.0-quarkus.jar
   ```
   It gives a warning about the exact class being overridden, but the malicious class is still executed.
   ```text
   [WARNING] [io.quarkus.deployment.pkg.steps.JarResultBuildStep] Dependencies with duplicate files detected. The dependencies [dev.scored:D1::jar:1.0.0[paths: /home/aman/.m2/repository/dev/scored/D1/1.0.0/D1-1.0.0.jar;], org.apache.commons:commons-lang3::jar:3.17.0[paths: /home/aman/.m2/repository/org/apache/commons/commons-lang3/3.17.0/commons-lang3-3.17.0.jar;]] contain duplicate files, e.g. org/apache/commons/lang3/StringUtils.class
   ```
### Output

All these methods will result in the same output when running the victim application:
```
88                                88                           88  
88                                88                           88  
88                                88                           88  
88,dPPYba,  ,adPPYYba,  ,adPPYba, 88   ,d8  ,adPPYba,  ,adPPYb,88  
88P'    "8a ""     `Y8 a8"     "" 88 ,a8"  a8P_____88 a8"    `Y88  
88       88 ,adPPPPP88 8b         8888[    8PP""""""" 8b       88  
88       88 88,    ,88 "8a,   ,aa 88`"Yba, "8b,   ,aa "8a,   ,d88  
88       88 `"8bbdP"Y8  `"Ybbd8"' 88   `Y8a `"Ybbd8"'  `"8bbdP"Y8  

Connection is valid!
Connected to port 5432 with user postgres
```

## Mitigation Strategies

### Sealed jar

The concept is to seal `postgresql` jar.

1. Package victim application using the profile `jar`.
2. The postgresql jar is in `target/lib/postgresql-42.7.7.jar`.
3. Edit its `META-INF/MANIFEST.MF` file to add the following line:
   ```
   Sealed: true
   ```
4. Run the victim application:
   ```shell
   java -jar target/victim-1.0.jar
   ```
   This will result in the following output:
   ```text
   sealing violation: package org.postgresql is sealed
   java.lang.SecurityException: sealing violation: package org.postgresql is 
   sealed
   ```

### Java Modules

See https://github.com/FredBonux/class-hijack-poc/tree/mitigation-module
or :
<details>
<summary>apply this patch</summary>

```diff
diff --git a/java/maven/abstract-project/install-me-first/D1/src/main/java/dev/scored/D1.java b/java/maven/abstract-project/install-me-first/D1/src/main/java/d1/D1.java
similarity index 51%
rename from java/maven/abstract-project/install-me-first/D1/src/main/java/dev/scored/D1.java
rename to java/maven/abstract-project/install-me-first/D1/src/main/java/d1/D1.java
index b8d2137..0fa34eb 100644
--- a/java/maven/abstract-project/install-me-first/D1/src/main/java/dev/scored/D1.java
+++ b/java/maven/abstract-project/install-me-first/D1/src/main/java/d1/D1.java
@@ -1,4 +1,4 @@
-package dev.scored;
+package d1;

public class D1 {
}
diff --git a/java/maven/abstract-project/install-me-first/D1/src/main/java/module-info.java b/java/maven/abstract-project/install-me-first/D1/src/main/java/module-info.java
new file mode 100644
index 0000000..b11ef75
--- /dev/null
+++ b/java/maven/abstract-project/install-me-first/D1/src/main/java/module-info.java
@@ -0,0 +1,3 @@
+module D1 {
+       requires D11;
+}
\ No newline at end of file
diff --git a/java/maven/abstract-project/install-me-first/D11/src/main/java/dev/scored/D11.java b/java/maven/abstract-project/install-me-first/D11/src/main/java/d11/D11.java
similarity index 52%
rename from java/maven/abstract-project/install-me-first/D11/src/main/java/dev/scored/D11.java
rename to java/maven/abstract-project/install-me-first/D11/src/main/java/d11/D11.java
index ddca44f..938a60a 100644
--- a/java/maven/abstract-project/install-me-first/D11/src/main/java/dev/scored/D11.java
+++ b/java/maven/abstract-project/install-me-first/D11/src/main/java/d11/D11.java
@@ -1,4 +1,4 @@
-package dev.scored;
+package d11;

public class D11 {
}
diff --git a/java/maven/abstract-project/install-me-first/D12/src/main/java/dev/scored/D12.java b/java/maven/abstract-project/install-me-first/D12/src/main/java/d12/D12.java
similarity index 52%
rename from java/maven/abstract-project/install-me-first/D12/src/main/java/dev/scored/D12.java
rename to java/maven/abstract-project/install-me-first/D12/src/main/java/d12/D12.java
index fc3c8c5..5bfc68d 100644
--- a/java/maven/abstract-project/install-me-first/D12/src/main/java/dev/scored/D12.java
+++ b/java/maven/abstract-project/install-me-first/D12/src/main/java/d12/D12.java
@@ -1,4 +1,4 @@
-package dev.scored;
+package d12;

public class D12 {
}
diff --git a/java/maven/abstract-project/victim/src/main/java/module-info.java b/java/maven/abstract-project/victim/src/main/java/module-info.java
new file mode 100644
index 0000000..606d291
--- /dev/null
+++ b/java/maven/abstract-project/victim/src/main/java/module-info.java
@@ -0,0 +1,6 @@
+module victim {
+       requires D1; // gadget dependency
+       requires org.postgresql.jdbc; // D2
+
+       requires java.sql;
+}
\ No newline at end of file
```

</details>


The packaging of victim application will fail.

### Maven Enforcer Plugin

Using any of the above packaging techniques with profile `enforcer` and 
expect a build failure.
Example:
```shell
mvn clean package -Passembly,enforcer
```
```text
[ERROR] Rule 0: org.codehaus.mojo.extraenforcer.dependencies.BanDuplicateClasses failed with message:
[ERROR] Duplicate classes found:
[ERROR] 
[ERROR]   Found in:
[ERROR]     dev.scored:D11:jar:1.0.0:compile
[ERROR]     org.postgresql:postgresql:jar:42.7.7:compile
[ERROR]   Duplicate classes:
[ERROR]     org/postgresql/Driver.class
```