@echo off
cd /d "c:\Users\respi\Downloads\BIMS"

echo Cleaning old class files...
del /s /q *.class

echo Compiling Java files...
javac -encoding UTF-8 view\*.java controller\*.java components\*.java model\*.java dao\*.java utils\*.java

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Running application...
java view.Main
pause