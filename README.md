# CPU Scheduler Visualizer

An interactive desktop application built with Java and JavaFX that visualizes CPU scheduling algorithms. This educational tool provides a polished, animated view of how different scheduling algorithms affect process execution, waiting times, and turnaround times.

![CPU Scheduler Visualizer](docs/screenshot.png)

## Features

### Scheduling Algorithms
- **FCFS (First-Come, First-Served)** — Non-preemptive scheduling based on arrival time
- **Round-Robin (RR)** — Preemptive scheduling with configurable time quantum
- **SJF (Shortest Job First)** — Non-preemptive scheduling based on burst time
- **Priority** — Non-preemptive scheduling based on priority values

### Interactive UI
- **Process Editor** — Add, remove, and edit processes with inline table editing
- **Interactive Gantt Chart** — Click segments to jump to specific times; hover for tooltips
- **Playback Controls** — Play/pause, step forward/backward, jump to start/end
- **Live State Display** — View current time, running process, and ready queue in real-time
- **Metrics Dashboard** — See waiting time, turnaround time, and averages for all processes
- **Theme Toggle** — Switch between modern dark and light themes

### User Experience
- Smooth animations for time marker transitions
- Keyboard shortcuts for quick navigation (Space, Arrow keys, Home/End)
- JSON import/export for saving and loading process configurations
- Random process generator for quick testing
- Speed control slider for playback
- Color-coded processes for easy identification

## Requirements

- **Java 21 or higher** (LTS recommended)
- **Maven 3.6+** for building
- **JavaFX 21** (included as Maven dependency)

## Building and Running

### Option 1: Run with the project wrapper (recommended)

On macOS / Linux
```bash
./mvnw javafx:run
```

On Windows (PowerShell or CMD)
```powershell
mvnw.cmd javafx:run
```

### Option 2: Build Runnable JAR

```bash
./mvnw clean package
java -jar target/cpu-scheduler-1.0.0.jar
```

### Option 3: Run from IDE

1. Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Ensure Java 21+ is configured
3. Run the `com.scheduler.App` class

## Usage Guide

### Adding Processes

1. **Manual Entry**
   - Click "Add Process" to add a new row
   - Double-click cells to edit ID, Arrival, Burst, and Priority values
   - Click "Remove Selected" to delete a process

2. **Generate Random**
   - Click "Generate Random" to create 4–7 random processes

3. **Import from JSON**
   - Go to File → Import JSON
   - Select a JSON file with process definitions
   - Example files are in the `examples/` directory

### Running a Simulation

1. Select an algorithm from the dropdown (FCFS, Round-Robin, SJF, Priority)
2. For Round-Robin, set the quantum value (default: 2)
3. Click "Run Simulation"
4. Use playback controls to navigate through the simulation:
   - **⏮** Jump to start
   - **⏪** Step backward
   - **▶/⏸** Play/Pause
   - **⏩** Step forward
   - **⏭** Jump to end
5. Adjust the speed slider to control playback rate

### Keyboard Shortcuts

- `Space` — Play/Pause
- `←` — Step backward
- `→` — Step forward
- `Home` — Jump to start
- `End` — Jump to end

### Interacting with the Gantt Chart

- **Click a segment** — Jump to that time in the simulation
- **Hover over a segment** — View process details in a tooltip

### Exporting Results

- Go to File → Export JSON to save your process configuration
- Saved files can be re-imported later

## Project Structure

```
cpu-scheduler/
├── src/
│   ├── main/
│   │   ├── java/com/scheduler/
│   │   │   ├── App.java
│   │   │   ├── Launcher.java
│   │   │   ├── algorithms/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── simulation/
│   │   │   ├── util/
│   │   │   └── view/
│   │   └── resources/
│   │       └── styles/
│   └── test/
├── examples/
├── pom.xml
└── README.md
```

## Testing

Run the unit tests with the project wrapper or Maven:

```bash
./mvnw test
```

## Technologies Used

- **Java 21**
- **JavaFX 21**
- **ControlsFX**
- **Gson**
- **JUnit 5**
- **Maven**

## Design Highlights

- **MVC Architecture** — Clear separation between model, view, and controller
- **Pure Algorithm Functions** — Schedulers are stateless and testable
- **Tick-based Simulation** — Frame-by-frame state snapshots for playback
- **Responsive Layout** — Adapts to window resizing

## Troubleshooting

- If you encounter "Error: JavaFX runtime components are missing": make sure Java 21+ is configured and run via the wrapper (`./mvnw javafx:run` / `mvnw.cmd javafx:run`).
- For build errors try `./mvnw clean` then `./mvnw package`.

## Future Enhancements

- Preemptive versions of SJF (SRTF) and Priority
- Multi-level queue scheduling
- Export Gantt chart as PNG/SVG
- Side-by-side algorithm comparison
- Annotations for bottlenecks and optimization suggestions

## License

MIT (suggested) — let me know if you want a different license and I will add a LICENSE file.

## Author

Aliyan Ahmed

---

**Version**: 1.0.0  
**Last Updated**: 2025
