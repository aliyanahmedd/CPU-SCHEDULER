@echo off
REM mvnw.cmd - lightweight wrapper: prefer mvnd, then mvn, otherwise download Maven into .mvn
where mvnd >nul 2>&1
if %errorlevel% equ 0 (
  mvnd %*
  exit /b %errorlevel%
)
where mvn >nul 2>&1
if %errorlevel% equ 0 (
  mvn %*
  exit /b %errorlevel%
)

if not exist ".mvn\apache-maven-3.9.11\bin\mvn.cmd" (
  echo Maven not found. Downloading Maven 3.9.11 to .mvn\apache-maven-3.9.11 ...
  powershell -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip' -OutFile '.mvn\\maven.zip'; Expand-Archive -LiteralPath '.mvn\\maven.zip' -DestinationPath '.mvn'; Remove-Item '.mvn\\maven.zip'"
)

.mvn\apache-maven-3.9.11\bin\mvn.cmd %*
exit /b %errorlevel%
