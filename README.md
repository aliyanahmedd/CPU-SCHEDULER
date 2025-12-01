# CPU Scheduler Visualizer

An interactive desktop application built with Java and JavaFX for learning and demonstrating common CPU scheduling algorithms.

![Java](https://img.shields.io/badge/Java-17+-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-17-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

## Features

- **Multiple Scheduling Algorithms**
  - First Come First Served (FCFS)
  - Shortest Job First (SJF)
  - Priority Scheduling
  - Round Robin (with configurable time quantum)

- **Interactive Process Management**
  - Add, edit, and remove processes
  - Configure arrival time, burst time, and priority
  - Real-time table editing

- **Live Visualization**
  - Dynamic Gantt chart showing process execution
  - Color-coded process blocks
  - Idle time visualization

- **Metrics Dashboard**
  - Average Waiting Time
  - Average Turnaround Time
  - Average Response Time
  - CPU Utilization
  - Throughput
  - Per-process metrics

- **Import/Export Scenarios**
  - Save process configurations as JSON
  - Load previously saved scenarios

## Requirements

- Java 17 or higher
- No additional installation required (uses Maven wrapper)

## Quick Start

### Build and Run

```bash
# Clone the repository
git clone https://github.com/aliyanahmedd/CPU-SCHEDULER.git
cd CPU-SCHEDULER

# Build the project
./mvnw clean compile

# Run the application
./mvnw javafx:run
```

### Run Tests

```bash
./mvnw test
```

### Package as JAR

```bash
./mvnw package
```

## Usage

1. **Add Processes**: Click "Add Process" to add processes to the table
2. **Edit Processes**: Click on any cell in the table to edit values
3. **Select Algorithm**: Choose a scheduling algorithm from the dropdown
4. **Set Time Quantum**: For Round Robin, specify the time quantum
5. **Run Simulation**: Click "Run Simulation" to see results
6. **Export/Import**: Save or load process scenarios using the Export/Import buttons

## Project Structure

```
src/
├── main/java/com/cpuscheduler/
│   ├── App.java                 # Main application
│   ├── algorithm/               # Scheduling algorithms
│   │   ├── SchedulingAlgorithm.java
│   │   ├── FCFSScheduler.java
│   │   ├── SJFScheduler.java
│   │   ├── PriorityScheduler.java
│   │   └── RoundRobinScheduler.java
│   ├── model/                   # Data models
│   │   ├── Process.java
│   │   ├── GanttEntry.java
│   │   └── SchedulingResult.java
│   ├── ui/                      # UI components
│   │   ├── GanttChartPane.java
│   │   ├── MetricsDashboard.java
│   │   └── ProcessTable.java
│   └── util/                    # Utilities
│       └── ScenarioIO.java
└── test/java/com/cpuscheduler/  # Unit tests
```

## Scheduling Algorithms Explained

### FCFS (First Come First Served)
Processes are executed in the order they arrive. Simple but can lead to the "convoy effect."

### SJF (Shortest Job First)
Selects the process with the shortest burst time. Minimizes average waiting time but requires knowing burst times in advance.

### Priority Scheduling
Executes processes based on priority (lower number = higher priority). Can cause starvation of low-priority processes.

### Round Robin
Each process gets a fixed time quantum. Fair allocation but can have high context switch overhead.

## License

MIT License - feel free to use this project for learning and educational purposes.