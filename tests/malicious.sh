#!/bin/bash

set -e

cd java/maven/abstract-project/install-me-first
mvn clean install -Pinject -q
cd ../victim

declare -A profiles=(
  [shade]="target/victim-1.0.jar"
  [assembly]="target/victim-1.0-jar-with-dependencies.jar"
  [spring]="target/victim-1.0-spring-boot.jar"
  [jar]="target/victim-1.0.jar"
  [quarkus]="target/victim-1.0-quarkus.jar"
  [bundle]="target/victim-1.0.jar"
)

any_failed=0

for profile in "${!profiles[@]}"; do
  echo "=============================="
  echo "Building with profile: $profile"
  mvn clean package -P"$profile" -q

  jar_file="${profiles[$profile]}"
  echo "Running: java -jar $jar_file"

  output=$(java -jar "$jar_file")

  if echo "$output" | grep -q "88.*88.*88"; then
    echo "[PASS] Profile '$profile' passed the output check."
  else
    echo "[FAIL] Profile '$profile' failed the output check."
    echo "-------- Output Start --------"
    echo "$output"
    echo "--------- Output End ---------"
    any_failed=1
  fi
done

if [ "$any_failed" -eq 1 ]; then
  echo "❌ One or more profiles failed. Exiting with code 1."
  exit 1
else
  echo "✅ All profiles passed."
  exit 0
fi
