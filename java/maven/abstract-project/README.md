# Java Cross-Dependency Injection PoC
This repository contains a PoC for a malicius attack based on the way depedencies are resolved by Maven in Java projects.
We define 4 packages: a victim, a nicelibrary, an attacker-library and a fakelibrary.

- The victim has 2 dependency: nicelibrary and attacker-library.
- The victim uses NiceClass from nicelibrary.
- attacker-library adds the fakelibrary to its dependencies.
- fakelibrary defines the same package as nicelibrary and contains a malicius NiceClass
- since attacker-library is defined before nicelibrary in victim pom file, NiceClass now is resolved with the fakelibrary version
  

