@echo off
setlocal

REM Obtener la ruta absoluta del directorio del script
set SCRIPT_DIR=%~dp0

REM Moverse al directorio del proyecto
cd /d "%SCRIPT_DIR%"

REM Ejecutar Maven para construir el proyecto
mvn clean package
