#!/bin/bash

# Obtener la ruta absoluta del directorio donde está este script
SCRIPT_DIR="$(dirname "$(readlink -f "$0")")"

# Ruta relativa a la carpeta donde están las librerías JavaFX
MODULEPATH="$SCRIPT_DIR/.idea/libraries/javafx-sdk-24.0.1/lib"

# Ejecutar Java con el module-path y módulos JavaFX, y el jar en target
java --module-path "$MODULEPATH" --add-modules javafx.controls,javafx.fxml -jar "$SCRIPT_DIR/target/gpps-1.0-SNAPSHOT.jar"
