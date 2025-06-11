@echo off
setlocal

REM Obtener la ruta absoluta del directorio donde está este .bat
set SCRIPT_DIR=%~dp0

REM Ruta relativa a la carpeta donde están las librerías JavaFX
set MODULEPATH=%SCRIPT_DIR%.idea\libraries\javafx-sdk-24.0.1\lib

REM Ejecutar Java con el módulo-path y módulos JavaFX, y el jar en target
java --module-path "%MODULEPATH%" --add-modules javafx.controls,javafx.fxml -jar "%SCRIPT_DIR%target\gpps-1.0-SNAPSHOT.jar"
