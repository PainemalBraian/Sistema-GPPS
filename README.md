# 🧑‍🎓 GPPS - Gestor de Prácticas Profesionales Supervisadas

GPPS es una aplicación desarrollada en Java utilizando JavaFX y Maven, diseñada para gestionar las Prácticas Profesionales Supervisadas de estudiantes universitarios. Permite a los diferentes actores involucrados (estudiantes, docentes, tutores, entidades colaboradoras y dirección de carrera) interactuar en un entorno digital que simula una experiencia profesional real.

- Instaladores de dependencias requeridas por el programa. Para SO windows y Linux situados en el proyecto base
- RUNS Ejectuables del programa para Windows y Linux situados en el proyecto base

## 🚀 Características principales

- Registro de proyectos de práctica (propios o seleccionados)
- Carga y seguimiento de informes parciales y finales
- Cronograma de actividades y control de avance
- Comunicación interna entre usuarios
- Evaluación del desempeño por tutores y docentes
- Gestión de propuestas por entidades colaboradoras
- Validación y cierre de prácticas por la dirección de carrera

## 🛠 Tecnologías utilizadas

- **Lenguaje Principal:** Java 24
- https://jdk.java.net/24/
  
- **Framework UI:** JavaFX
    🧰 Scene Builder (GUI visual para JavaFX)
    🔧 ¿Qué es Scene Builder?
    Es una herramienta visual para diseñar GUIs en JavaFX usando archivos .fxml (parecido a HTML)
    https://gluonhq.com/products/scene-builder/
Usamos la version: 
JavaFX SDK 24.0.1
https://gluonhq.com/products/javafx/

SDK de Project Structure: Oracle OpenJDK 24.0.1

## Comandos de ejecución por terminal RUN
Situar la terminal en el directorio del proyecto "...\Sistema-GPPS"

- mvn clean           -> Construir dependencias del pom
- mvn javafx:run      -> Ejecutar Proyecto

## Configuración del run para compilador (IntellIJ)
Configurar Run del compilador. Agregar en sección (VM OPTION) . (Ajustar path objetivo del comando /Windows/ o /Linux/)

- `   --module-path "$PROJECT_DIR$/.idea/libraries/Windows/javafx-sdk-24.0.1/lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=ALL-UNNAMED --enable-native-access=javafx.graphics 
`
## Configuración de Variables de entorno del sistema (Si es solicitado)
- AVISO
  Los ejecutables del programa estan preparados para armar esto "automaticamente" pero si se necesita configurar manualmente seguir lo siguiente.

  En caso de error por versión incompatible se tendrá que ajustar la versión del jdk usado por el sistema.
  Windows - Configuración de Variable de entorno del sistema (Variables del sistema)
- Agregar en la variable de sistema "Path" el path Absoluto del "jdk-24.0.1\bin"
- C:\Users\...\openjdk-24.0.1\bin

  Linux - Si aparece un error por versión incompatible, podés configurar el JDK local con estos comandos.
 1. Ubicá la carpeta raíz del proyecto.
- `   cd /ruta/hacia/el/proyecto/Sistema-GPPS
`
 2. Configurar el JDK local
- `    export JAVA_HOME="$PWD/.idea/libraries/Linux/jdk-24.0.1"
`   
- ` export PATH="$JAVA_HOME/bin:$PATH"
`
 3. (Opcional) Verificar la version cargada
- java -version


- (Opcional)
- Nueva Variable
- JAVA_HOME
- %JAVA_HOME%\bin
- C:\...\.jdks\openjdk-24.0.1

## Otros
- Atento a notificación "Deprecated" ocasionado al ejecutar. (Solo es informativo)
Provocado por advertencia de "deprecated method in sun.misc.Unsafe"
IGNORAR

- **Gestor de dependencias:** Maven

- **Base de datos:** MySQL

- **Generación de PDF:** OpenPDF

- **Estilos UI:** CSS para JavaFX
  
- **Entorno de desarrollo (IDE):** IntelliJ IDEA

- **Herramienta de diseño visual de interfaces:** Scene Builder

## 👀 Visualizador WEB del diagrama de relaciones
- **https://drawsql.app/teams/d-314/diagrams/gpps/embed**
