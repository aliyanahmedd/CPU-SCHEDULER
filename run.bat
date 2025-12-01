@echo off
REM Quick run script for CPU Scheduler Visualizer

echo Starting CPU Scheduler Visualizer...
echo.

REM Prefer project wrapper (mvnw), then mvnd, then mvn
where mvnw.cmd >nul 2>&1
if %errorlevel% equ 0 (
    call mvnw.cmd javafx:run
) else (
    where mvnd >nul 2>&1
    if %errorlevel% equ 0 (
        call mvnd javafx:run
    ) else (
        where mvn >nul 2>&1
        if %errorlevel% equ 0 (
            call mvn javafx:run
        ) else (
            echo ERROR: No Maven runtime found (mvnw, mvnd, or mvn).
            echo The included wrapper will download Maven automatically when run.
            echo Try: mvnw.cmd javafx:run
            pause
            exit /b 1
        )
    )
)

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Failed to run application.
    echo Please ensure Maven and Java are installed correctly.
    pause
    exit /b 1
)
