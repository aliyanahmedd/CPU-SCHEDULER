# CPU Scheduler Visualizer

An interactive desktop application built with Java and JavaFX that visualizes CPU scheduling algorithms. This educational tool provides a polished, animated view of how different scheduling algorithms affect process execution, waiting times, and turnaround times.

![CPU Scheduler Visualizer](docs/screenshot.png)

## Features

### Scheduling Algorithms
- **FCFS (First-Come, First-Served)** — Non-preemptive scheduling based on arrival time
- **Round-Robin (RR)** — Preemptive scheduling with configurable time quantum
- **SJF (Shortest Job First)** — Non-preemptive scheduling based on burst time
# CPU Scheduler Visualizer

An interactive desktop application (Java + JavaFX) that visualizes CPU scheduling algorithms.

## Quick start (one-command)
Clone, build and run the runnable JAR using the project wrapper (recommended):

```powershell
git clone https://github.com/aliyanahmedd/CPU-SCHEDULER.git
cd CPU-SCHEDULER
.\mvnw.cmd -DskipTests clean package
java -jar target\cpu-scheduler-1.0.0-shaded.jar
```

Notes:
- The repository includes the Maven wrapper (`mvnw`, `mvnw.cmd`) so you don't need Maven preinstalled.
- The build produces a shaded (uber) JAR: `target/cpu-scheduler-1.0.0-shaded.jar`.

If you prefer to run directly from Maven goals:

```powershell
.\mvnw.cmd javafx:run
```

## Requirements
- Java 21 (or newer) — recommended LTS
- `JAVA_HOME` pointing to a Java 21 JDK improves the wrapper start-up (optional but recommended)

Set `JAVA_HOME` (PowerShell example):

```powershell
setx JAVA_HOME "C:\path\to\jdk-21"; $env:JAVA_HOME = "C:\path\to\jdk-21"
```

## Running in an IDE
Import as a Maven project and run `com.scheduler.App` with a Java 21 SDK configured in your IDE.

## CI (GitHub Actions)
This repository contains a GitHub Actions workflow (`.github/workflows/maven.yml`) that builds the project on push and pull requests.

## Project layout

```
cpu-scheduler/
├── src/main/java/com/scheduler/  # application sources
├── src/test/java/...             # unit tests
├── examples/                     # sample JSON process sets
├── mvnw, mvnw.cmd                # Maven wrapper
├── pom.xml                       # Maven configuration
└── README.md
```

## Troubleshooting
- If a `clean` fails on Windows due to a stuck file, delete `target` manually:

```powershell
Remove-Item -LiteralPath .\target -Recurse -Force
```

- If you see a warning about `JAVA_HOME` when using the wrapper, set `JAVA_HOME` as shown above.

## License
This project is licensed under the MIT License — see `LICENSE`.

## Author
Aliyan Ahmed

---

**Version**: 1.0.0  
**Last Updated**: 2025-12-01
