@echo off
title IsharaApp Launcher
echo ============================
echo     Starting IsharaApp
echo ============================

:: Kill any process already using port 6767
echo Clearing port 6767...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :6767') do (
    taskkill /PID %%a /F >nul 2>&1
)

:: ROOT is wherever this bat lives (IsharaApp folder)
set ROOT=%~dp0
set JAVA_DIR=%ROOT%Java\IsharaCV
set FX=%ROOT%Java\javafx-sdk-21.0.10\lib
set LIB=%JAVA_DIR%\lib\jna-5.18.1.jar;%JAVA_DIR%\lib\jna-platform-5.18.1.jar;%JAVA_DIR%\lib\gson-2.10.1.jar
set CP=%JAVA_DIR%\bin;%LIB%

echo Starting Java Application...
start "IsharaApp" cmd /k "java --module-path "%FX%" --add-modules javafx.controls -cp "%CP%" app.IsharaApp"
echo ============================
echo      IsharaApp Running!
echo ============================
pause