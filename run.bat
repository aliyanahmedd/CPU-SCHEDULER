@echo off
REM Quick run script for CPU Scheduler Visualizer

REM Ensure running from script directory
pushd "%~dp0"

echo Starting CPU Scheduler Visualizer...
echo.

REM Prefer local Maven wrapper (mvnw.cmd), then mvnd, then mvn
if exist "%~dp0mvnw.cmd" (
    call "%~dp0mvnw.cmd" javafx:run %*
    set rc=%ERRORLEVEL%
    popd
    exit /b %rc%
)

where mvnd >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    mvnd javafx:run %*
    set rc=%ERRORLEVEL%
    popd
    exit /b %rc%
)

where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    mvn javafx:run %*
    set rc=%ERRORLEVEL%
    popd
    exit /b %rc%
)

echo ERROR: No Maven runtime found (mvnw.cmd, mvnd, or mvn).
echo The included wrapper will download Maven automatically when run.
echo Try running: mvnw.cmd javafx:run
pause
popd
exit /b 1
