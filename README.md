

# ***Holi***
### Herramientas para el front
    🧰 Scene Builder (GUI visual para JavaFX)
    🔧 ¿Qué es Scene Builder?
    Es una herramienta visual para diseñar GUIs en JavaFX usando archivos .fxml (parecido a HTML)
    https://gluonhq.com/products/scene-builder/

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