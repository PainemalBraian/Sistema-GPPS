#!/bin/bash
set -e

# 1. Directorio del script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 2. Calcular raíz del proyecto (subir dos niveles: Ejecutables/Linux)
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# 3. Configurar JDK local
export JAVA_HOME="$PROJECT_ROOT/.idea/libraries/Linux/jdk-24.0.1"
export PATH="$JAVA_HOME/bin:$PATH"

# 4. Verificar versión de Java usada
# echo "Usando Java:"
# java -version
# echo

# 5. Verificar Maven
# echo "Verificando Maven:"
# mvn -v
# echo

# 6. Configurar JavaFX
MODULEPATH="$PROJECT_ROOT/.idea/libraries/Linux/javafx-sdk-24.0.1/lib"
echo "Usando JavaFX desde: $MODULEPATH"
echo

# 7. Compilar con Maven y ejecutar
cd "$PROJECT_ROOT"
echo "Compilando proyecto..."
mvn clean
mvn javafx:run

# 8. (Opcional) Limpieza o ejecución del JAR
# java --module-path "$MODULEPATH" \
#      --add-modules javafx.controls,javafx.fxml \
#      -jar "$PROJECT_ROOT/target/gpps-1.0-SNAPSHOT.jar"
