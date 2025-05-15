# 🧑‍🎓 GPPS - Gestor de Prácticas Profesionales Supervisadas

GPPS es una aplicación desarrollada en Java utilizando JavaFX y Maven, diseñada para gestionar las Prácticas Profesionales Supervisadas de estudiantes universitarios. Permite a los diferentes actores involucrados (estudiantes, docentes, tutores, entidades colaboradoras y dirección de carrera) interactuar en un entorno digital que simula una experiencia profesional real.

## 🚀 Características principales

- Registro de proyectos de práctica (propios o seleccionados)
- Carga y seguimiento de informes parciales y finales
- Cronograma de actividades y control de avance
- Comunicación interna entre usuarios
- Evaluación del desempeño por tutores y docentes
- Gestión de propuestas por entidades colaboradoras
- Validación y cierre de prácticas por la dirección de carrera

## 🛠 Tecnologías utilizadas

- **Lenguaje:** Java 17+
  
- **Framework UI:** JavaFX
    🧰 Scene Builder (GUI visual para JavaFX)
    🔧 ¿Qué es Scene Builder?
    Es una herramienta visual para diseñar GUIs en JavaFX usando archivos .fxml (parecido a HTML)
    https://gluonhq.com/products/scene-builder/
Usamos la version: 
JavaFX SDK 24.0.1
https://gluonhq.com/products/javafx/

SDK de Project Structure: Oracle OpenJDK 24.0.1

Configurar Run del compilador. Agregar (VM OPTION)
--module-path "C:\...\javafx-sdk-24.0.1\lib"  Directorio del sdk
--add-modules javafx.controls,javafx.fxml
--enable-native-access=ALL-UNNAMED
--enable-native-access=javafx.graphics

Atento a notificación "Deprecated" ocasionado al ejecutar. (Solo es informativo)
Provocado por advertencia de "deprecated method in sun.misc.Unsafe"
IGNORAR

- **Gestor de dependencias:** Maven

- **Base de datos:** SQL

- **Estilos UI:** CSS para JavaFX


## 👀 Visualizador WEB del diagrama de relaciones
- **https://drawsql.app/teams/d-314/diagrams/gpps/embed**
