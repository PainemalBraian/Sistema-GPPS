#!/bin/bash

# Obtener la ruta absoluta del script
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Moverse al directorio del proyecto
cd "$SCRIPT_DIR"

# Ejecutar Maven para construir el proyecto
mvn clean package
