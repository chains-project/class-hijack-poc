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

1. Simply change the first command to inject the malicious class `org.apache.
commons.lang3.StringUtils`.
   ```shell
   install-me-first$ mvn clean install -Pinject
   ```
2. Then run the victim application, after packaging, as before:
   ```shell
    victim$ java -jar target/victim-1.0.jar
    ```
    This will output:
    ```
   shadow file sent to hacker ;)
   hello world!
   ```