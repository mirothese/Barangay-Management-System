@echo off
cls
echo ========================================
echo   Barangay Management System
echo   Complete Build Script
echo ========================================
echo.

cd /d "%~dp0"

echo Creating directories...
if not exist models mkdir models
if not exist dao mkdir dao
if not exist ui mkdir ui
if not exist utils mkdir utils
if not exist data mkdir data

echo.
echo Compiling all Java files...
javac -encoding UTF-8 -cp . *.java models/*.java dao/*.java ui/*.java utils/*.java

if %errorlevel% == 0 (
    echo.
    echo ========================================
    echo   Build Successful!
    echo ========================================
    echo.
    echo Launching Barangay Management System...
    echo.
    java Main
) else (
    echo.
    echo ========================================
    echo   Build Failed!
    echo ========================================
    echo.
    echo Please check the errors above.
    pause
)