# Java-Class-Hijack: PoC Repository

## Overview

This repository contains the Proof-of-Concept (PoC) code and replication scripts for the paper [**"Java-Class-Hijack: Software Supply Chain Attack for Java based on Maven Dependency Resolution and Java Classloading"**](https://) by Federico Bono, Frank Reyes, Aman Sharma, Benoit Baudry, and Martin Monperrus.

### Abstract

We introduce Java-Class-Hijack, a novel software supply chain attack that enables an attacker to inject malicious code by crafting a class that shadows a legitimate class in the dependency tree. This PoC demonstrates the feasibility of the attack and replicates it in the German Corona-Warn-App server application. The attack shows how a transitive dependency deep within the dependency tree can hijack a class from a direct dependency, posing significant security risks to Java applications.

## Repository Structure

```
class-hijack-poc
├── android
├── java
│   ├── gradle
│   └── maven
│       ├── abstract-project
│       └── real-project
│           ├── cwa-server.zip
│           └── json-schema.zip
└── php
├── README.md
└── LICENSE
```

### Key Components

- `java/maven/real-project/cwa-server.zip`: Contains the replication of the attack on the Corona-Warn-App backend service.
- `java/maven/real-project/json-schema.zip`: Contains additional resources needed for the replication.
- `java/maven/abstract-project`: Abstract project setup demonstrating the class hijacking.
- `java/gradle`: Gradle-based project setup.
- `android`: Android-specific implementations.
- `php`: PHP-specific implementations.

## Getting Started

### Prerequisites

- Java 8 or later
- Maven 3.6 or later
- Gradle (for Gradle projects)
- PHP and Composer (for PHP projects)

## Attack Description

The attack takes place in two steps:

1. **Crafting a Malicious Class:** The attacker creates a malicious class with the same fully qualified name as a legitimate class.
2. **Embedding the Malicious Class:** The attacker embeds this malicious class in a dependency that is included earlier in the dependency resolution order.

For more details, refer to the paper section on the attack methodology.

## Replication in Real-World Project

The PoC includes scripts to replicate the attack on the Corona-Warn-App backend service (`cwa-server`).
Detailed instructions to setup and run the application are coming soon.

## Mitigation Strategies

To mitigate such attacks, consider the following strategies:

- Use dependency management tools that detect and prevent such conflicts.
- Regularly audit your dependency tree.
- Implement strict version controls and use trusted repositories.
- Use Java Modules to avoid package name colisions.

## Contributing

We welcome contributions to improve this PoC. Please fork the repository and create a pull request with your changes.

## Contact

For any questions or feedback, please contact the authors at:

- Federico Bono: fbono@kth.se
- Frank Reyes: frankrg@kth.se
- Aman Sharma: amansha@kth.se
- Benoit Baudry: benoit.baudry@umontreal.ca
- Martin Monperrus: monperrus@kth.se