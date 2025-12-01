# Quick Start Guide

## Prerequisites Check

Before starting, verify you have:

```bash
# Check Java version (must be 17+)
java -version

# Check Maven version (must be 3.6+)
mvn -version
```

If Java or Maven are not installed:
- **Java**: Download from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/)
- **Maven**: Download from [Apache Maven](https://maven.apache.org/download.cgi)

## Running the Application (3 Easy Steps)

### Method 1: Maven (Recommended for Development)

```bash
# Step 1: Navigate to project directory
cd "OS lab"

# Step 2: Run the application
mvn clean javafx:run
```

That's it! The application will compile and launch automatically.

### Method 2: Runnable JAR (Recommended for Distribution)

```bash
# Step 1: Build the JAR
mvn clean package

# Step 2: Run the JAR
java -jar target/cpu-scheduler-1.0.0.jar
```

The JAR file can be distributed and run on any computer with Java 17+.

## First Time Using the App

1. **The app launches with 3 sample processes already loaded** (P1, P2, P3)
2. **Select an algorithm** from the dropdown (try "FCFS" first)
3. **Click "Run Simulation"**
4. **Click the Play button (▶)** to watch the animation
5. **Experiment**:
   - Click on Gantt chart segments to jump to that time
   - Use keyboard arrows to step through manually
   - Try different algorithms to compare results

## Testing Different Algorithms

### FCFS (First-Come, First-Served)
- Select "FCFS" from dropdown
- Click "Run Simulation"
- Observe: processes run in arrival order

### Round-Robin
- Select "Round-Robin" from dropdown
- Set Quantum to 2 (or try different values)
- Click "Run Simulation"
- Observe: processes alternate execution

### SJF (Shortest Job First)
- Select "SJF" from dropdown
- Click "Run Simulation"
- Observe: shortest burst times execute first

### Priority Scheduling
- Select "Priority" from dropdown
- Click "Run Simulation"
- Observe: lower priority numbers execute first

## Importing Sample Data

```bash
# The project includes sample JSON files
File → Import JSON → Select "examples/sample-processes.json"
```

Or create your own processes:
1. Click "Add Process"
2. Double-click cells to edit values
3. Click "Run Simulation"

## Keyboard Shortcuts

- `Space` - Play/Pause
- `←` - Step backward
- `→` - Step forward  
- `Home` - Jump to start
- `End` - Jump to end

## Troubleshooting

### "Command not found: mvn"
Maven is not installed. Download from https://maven.apache.org/download.cgi

### "Error: JavaFX runtime components are missing"
You're trying to run the JAR incorrectly. Use one of these methods:
```bash
mvn javafx:run
# OR
java -jar target/cpu-scheduler-1.0.0.jar
```

### Application window is blank
Toggle the theme: View → Toggle Theme

### Tests failing
```bash
# Clean and rebuild
mvn clean test
```

## Video Demo Script

Record a 2-minute demo showing:

1. **Launch** (5 sec): Open the app
2. **Add Processes** (15 sec): Add 3-4 processes manually or click "Generate Random"
3. **Run FCFS** (30 sec): Select FCFS, run simulation, show playback controls
4. **Run Round-Robin** (30 sec): Switch to RR with quantum=2, show different behavior
5. **Click Gantt Chart** (15 sec): Click segments to jump around timeline
6. **Show Metrics** (15 sec): Highlight waiting time and turnaround time
7. **Export/Import** (10 sec): Export to JSON, clear, re-import

## Next Steps

- Read the full README.md for detailed documentation
- Run unit tests: `mvn test`
- Modify CSS themes in `src/main/resources/styles/`
- Add new scheduling algorithms by implementing `Scheduler` interface

## Support

For issues or questions:
1. Check the README.md file
2. Verify Java 17+ is installed
3. Ensure Maven dependencies downloaded: `mvn dependency:resolve`

Enjoy visualizing CPU scheduling algorithms! 🚀
