@echo off
REM Build and test script for CPU Scheduler Visualizer

echo ================================
echo CPU Scheduler Visualizer
echo Build and Test Script
echo ================================
echo.

echo [1/4] Checking Java version...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java not found. Please install Java 21 or higher.
    exit /b 1
)
echo.

echo [2/4] Checking Maven...
mvn -version
if %errorlevel% neq 0 (
    echo ERROR: Maven not found. Please install Maven 3.6 or higher.
    exit /b 1
)
echo.

echo [3/4] Running tests...
call mvn clean test
if %errorlevel% neq 0 (
    echo ERROR: Tests failed. Please check the output above.
    exit /b 1
)
echo.

echo [4/4] Building application...
call mvn package -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Build failed. Please check the output above.
    exit /b 1
)
echo.

echo ================================
echo BUILD SUCCESSFUL!
echo ================================
echo.
echo To run the application:
echo   1. mvn javafx:run
echo   2. java -jar target/cpu-scheduler-1.0.0.jar
echo.
echo JAR location: target\cpu-scheduler-1.0.0.jar
echo.

pause
