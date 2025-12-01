# CPU Scheduler Visualizer

An interactive desktop application built with Java and JavaFX that visualizes CPU scheduling algorithms. This educational tool provides a polished, animated view of how different scheduling algorithms affect process execution, waiting times, and turnaround times.

![CPU Scheduler Visualizer](docs/screenshot.png)

## Features

### Scheduling Algorithms
- **FCFS (First-Come, First-Served)** - Non-preemptive scheduling based on arrival time
- **Round-Robin (RR)** - Preemptive scheduling with configurable time quantum
- **SJF (Shortest Job First)** - Non-preemptive scheduling based on burst time
- **Priority** - Non-preemptive scheduling based on priority values

### Interactive UI
- **Process Editor** - Add, remove, and edit processes with inline table editing
- **Interactive Gantt Chart** - Click segments to jump to specific times; hover for tooltips
- **Playback Controls** - Play/pause, step forward/backward, jump to start/end
- **Live State Display** - View current time, running process, and ready queue in real-time
- **Metrics Dashboard** - See waiting time, turnaround time, and averages for all processes
- **Theme Toggle** - Switch between modern dark and light themes

### User Experience
- Smooth animations for time marker transitions
- Keyboard shortcuts for quick navigation (Space, Arrow keys, Home/End)
- JSON import/export for saving and loading process configurations
- Random process generator for quick testing
- Speed control slider for playback
- Color-coded processes for easy identification

## Requirements

- **Java 17 or higher** (LTS recommended)
 - **Java 21 or higher** (LTS recommended)
- **Maven 3.6+** for building
- **JavaFX 21** (included as Maven dependency)

## Building and Running

### Option 1: Run with Maven

```bash
# Run the application directly (recommended: use the project wrapper)
# On macOS / Linux
./mvnw javafx:run
# On Windows (PowerShell or CMD)
mvnw.cmd javafx:run
```

### Option 2: Build Runnable JAR

```bash
# Package the application as a JAR (using the wrapper)
./mvnw clean package

# Run the packaged JAR
java -jar target/cpu-scheduler-1.0.0.jar
```

### Option 3: Run from IDE

1. Import the project as a Maven project in your IDE (IntelliJ IDEA, Eclipse, VS Code)
3. Make sure Java 21+ is configured
3. Run the `com.scheduler.App` class

## Usage Guide

### Adding Processes

1. **Manual Entry**
   - Click "Add Process" to add a new row
   - Double-click cells to edit ID, Arrival, Burst, and Priority values
   - Click "Remove Selected" to delete a process

2. **Generate Random**
   - Click "Generate Random" to create 4-7 random processes

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

- `Space` - Play/Pause
- `←` - Step backward
- `→` - Step forward
- `Home` - Jump to start
- `End` - Jump to end

### Interacting with the Gantt Chart

- **Click a segment** - Jump to that time in the simulation
- **Hover over a segment** - View process details in a tooltip

### Exporting Results

- Go to File → Export JSON to save your process configuration
- Saved files can be re-imported later

## Project Structure

```
cpu-scheduler/
├── src/
│   ├── main/
│   │   ├── java/com/scheduler/
│   │   │   ├── App.java                    # JavaFX Application entry
│   │   │   ├── Launcher.java               # Main class for shaded JAR
│   │   │   ├── algorithms/                 # Scheduling algorithms
│   │   │   │   ├── Scheduler.java
│   │   │   │   ├── FcfsScheduler.java
│   │   │   │   ├── RrScheduler.java
│   │   │   │   ├── SjfScheduler.java
│   │   │   │   └── PriorityScheduler.java
│   │   │   ├── controller/
│   │   │   │   └── MainController.java     # Main UI controller
│   │   │   ├── model/                      # Data models
│   │   │   │   ├── ProcessModel.java
│   │   │   │   ├── GanttSegment.java
│   │   │   │   └── SimulationState.java
│   │   │   ├── simulation/
│   │   │   │   └── Simulator.java          # Simulation engine
│   │   │   ├── util/                       # Utilities
│   │   │   │   ├── JsonIO.java
│   │   │   │   └── ColorPalette.java
│   │   │   └── view/                       # Custom UI components
│   │   │       ├── GanttView.java
│   │   │       ├── ProcessTableView.java
│   │   │       ├── ReadyQueueView.java
│   │   │       └── MetricsView.java
│   │   └── resources/
│   │       └── styles/                     # CSS themes
│   │           ├── dark-theme.css
│   │           └── light-theme.css
│   └── test/
│       └── java/com/scheduler/algorithms/  # Unit tests
│           ├── FcfsSchedulerTest.java
│           ├── RrSchedulerTest.java
│           ├── SjfSchedulerTest.java
│           └── PrioritySchedulerTest.java
├── examples/                               # Sample JSON files
│   ├── sample-processes.json
│   └── complex-processes.json
├── pom.xml                                 # Maven configuration
└── README.md
```

## Testing

Run the unit tests with Maven:

```bash
mvn test
```

The test suite includes:
- Algorithm correctness tests for FCFS, RR, SJF, and Priority
- Edge cases (empty lists, idle time, same priorities/bursts)
- Verification of completion times and scheduling order

## Algorithm Details

### FCFS (First-Come, First-Served)
- **Type**: Non-preemptive
- **Selection**: Processes are scheduled in order of arrival time
- **Best for**: Simple batch systems
- **Drawback**: Can cause convoy effect (long wait times)

### Round-Robin
- **Type**: Preemptive
- **Selection**: Processes execute for a time quantum, then rotate
- **Parameter**: Quantum (default: 2)
- **Best for**: Time-sharing systems
- **Advantage**: Fair CPU allocation

### SJF (Shortest Job First)
- **Type**: Non-preemptive
- **Selection**: Process with shortest burst time executes first
- **Best for**: Minimizing average waiting time
- **Drawback**: Requires knowing burst times in advance

### Priority
- **Type**: Non-preemptive
- **Selection**: Process with highest priority (lowest number) executes first
- **Best for**: Systems with importance hierarchy
- **Note**: Lower priority number = higher priority

## Sample Process Definitions

### Basic Example (FCFS)
```json
[
  { "id": "P1", "arrival": 0, "burst": 5, "priority": 2 },
  { "id": "P2", "arrival": 2, "burst": 3, "priority": 1 },
  { "id": "P3", "arrival": 4, "burst": 1, "priority": 3 }
]
```

**Expected FCFS Gantt**: P1: 0–5 | P2: 5–8 | P3: 8–9

### Round-Robin Example (Quantum = 2)
Same processes with RR result in interleaved execution based on quantum.

## Technologies Used

- **Java 17** - Programming language
- **JavaFX 21** - UI framework
- **ControlsFX** - Enhanced JavaFX controls
- **Gson** - JSON parsing
- **JUnit 5** - Testing framework
- **Maven** - Build and dependency management

## Design Highlights

- **MVC Architecture** - Clear separation between model, view, and controller
- **Pure Algorithm Functions** - Schedulers are stateless and testable
- **Tick-based Simulation** - Frame-by-frame state snapshots for playback
- **Responsive Layout** - Adapts to window resizing
- **Accessibility** - Keyboard navigation and high-contrast themes

## Troubleshooting

### JavaFX Module Issues
If you encounter "Error: JavaFX runtime components are missing":
- Ensure you're using Java 17+
- Run via Maven: `mvn javafx:run`
- Or use the shaded JAR: `java -jar target/cpu-scheduler-1.0.0.jar`

### Build Errors
- Clean the project: `mvn clean`
- Update dependencies: `mvn dependency:resolve`
- Check Java version: `java -version`

### UI Not Displaying Correctly
- Toggle theme (View → Toggle Theme)
- Check CSS files are present in `src/main/resources/styles/`

## Future Enhancements

- [ ] Preemptive versions of SJF (SRTF) and Priority
- [ ] Multi-level queue scheduling
- [ ] Export Gantt chart as PNG/SVG
- [ ] Side-by-side algorithm comparison
- [ ] Annotations for bottlenecks and optimization suggestions

## License

This project is created for educational purposes. Feel free to use and modify for learning.

## Author

Created as an interactive educational tool for understanding CPU scheduling algorithms.

## Acknowledgments

- Built with JavaFX for cross-platform desktop support
- Inspired by operating systems course materials
- Designed for clarity and educational value

---

**Version**: 1.0.0  
**Last Updated**: 2025
