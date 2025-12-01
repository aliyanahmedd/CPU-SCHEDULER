#!/usr/bin/env bash
set -e
# Prefer mvnd, then mvn, otherwise download a local Maven into .mvn and use it.
if command -v mvnd >/dev/null 2>&1; then
  exec mvnd "$@"
fi
if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi

MAVEN_DIR=".mvn/apache-maven-3.9.11"
if [ ! -x "$MAVEN_DIR/bin/mvn" ]; then
  echo "Maven not found. Downloading Maven 3.9.11 to $MAVEN_DIR..."
  mkdir -p .mvn
  if command -v curl >/dev/null 2>&1; then
    curl -L -o .mvn/maven.zip https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip
  elif command -v wget >/dev/null 2>&1; then
    wget -O .mvn/maven.zip https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip
  else
    echo "Please install curl or wget to allow the wrapper to download Maven, or install Maven manually." >&2
    exit 1
  fi
  unzip -q .mvn/maven.zip -d .mvn
  rm .mvn/maven.zip
fi

exec "$MAVEN_DIR/bin/mvn" "$@"
