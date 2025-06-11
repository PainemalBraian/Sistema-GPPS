#!/bin/bash
set -e

# Ruta del script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Establecer JAVA_HOME local
export JAVA_HOME="$SCRIPT_DIR/.idea/libraries/jdk-24.0.1"
export PATH="$JAVA_HOME/bin:$PATH"

# Ruta a los módulos de JavaFX
MODULEPATH="$SCRIPT_DIR/.idea/libraries/javafx-sdk-24.0.1/lib"

# Compilar el proyecto con Maven
echo "Compilando el proyecto..."
mvn clean package

# Ejecutar el JAR
echo "Ejecutando la aplicación..."
java --module-path "$MODULEPATH" --add-modules javafx.controls,javafx.fxml -jar "$SCRIPT_DIR/target/gpps-1.0-SNAPSHOT.jar"
