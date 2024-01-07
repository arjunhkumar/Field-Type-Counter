#!/bin/bash

# Clean the current target dir
rm -r target/
rm -r artifacts/

# Build the source code into a single .jar file including all the dependencies. Generates source.jar also inside the target dir.
mvn clean install assembly:single source:jar

# Copy the artifacts
mkdir artifacts/
cp target/FTC-1.0-jar-with-dependencies.jar artifacts/FTC-1.0.jar
