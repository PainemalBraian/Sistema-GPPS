@echo off
setlocal enabledelayedexpansion

REM 1. Obtener carpeta del script
set "SCRIPT_DIR=%~dp0"
if "%SCRIPT_DIR:~-1%"=="\" set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

REM 2. Calcular root del proyecto (subir dos niveles si estás en Ejecutables\Windows)
set "PROJECT_ROOT=%SCRIPT_DIR%\..\.."
for %%i in ("%PROJECT_ROOT%") do set "PROJECT_ROOT=%%~fi"

REM 3. Configurar Java local
set "JAVA_HOME=%PROJECT_ROOT%\.idea\libraries\Windows\jdk-24.0.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM 4. Verificar versión de Java usada
REM echo Usando Java:
REM java -version
REM echo.

REM 5. Verificar Maven
REM echo Verificando Maven:
REM mvn -v
REM echo.

REM 6. Configurar JavaFX
echo Seteando Path:
set "MODULEPATH=%PROJECT_ROOT%\.idea\libraries\Windows\javafx-sdk-24.0.1\lib"
echo.

REM 7. Compilar con Maven
cd /d "%PROJECT_ROOT%"
REM call    mvn clean package
call    mvn clean
call    mvn javafx:run

REM 8. Ejecutar la aplicación (Se necesita .jar creado por mvn clean package)
REM call java --module-path "%MODULEPATH%" --add-modules javafx.controls,javafx.fxml --enable-native-access=ALL-UNNAMED --enable-native-access=javafx.graphics -jar "%PROJECT_ROOT%\target\gpps-1.0-SNAPSHOT.jar"

pause
