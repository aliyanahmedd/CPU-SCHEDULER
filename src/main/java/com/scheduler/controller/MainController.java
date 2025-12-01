package com.scheduler.controller;

import com.scheduler.algorithms.*;
import com.scheduler.model.ProcessModel;
import com.scheduler.model.SimulationState;
import com.scheduler.simulation.Simulator;
import com.scheduler.util.ColorPalette;
import com.scheduler.util.JsonIO;
import com.scheduler.view.GanttView;
import com.scheduler.view.MetricsView;
import com.scheduler.view.ProcessTableView;
import com.scheduler.view.ReadyQueueView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.util.*;

/**
 * Main controller for the CPU Scheduler application.
 */
public class MainController {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    
    // UI Components
    private ProcessTableView processTable;
    private GanttView ganttView;
    private ReadyQueueView readyQueueView;
    private MetricsView metricsView;
    
    private ComboBox<String> algorithmCombo;
    private Spinner<Integer> quantumSpinner;
    private Label quantumLabel;
    private Label currentTimeLabel;
    private Label currentProcessLabel;
    private Slider speedSlider;
    
    private Button runButton;
    private Button resetButton;
    private Button stepBackButton;
    private Button playPauseButton;
    private Button stepForwardButton;
    private Button jumpStartButton;
    private Button jumpEndButton;
    
    // Simulation
    private Simulator simulator;
    private Timeline playbackTimeline;
    private boolean isPlaying = false;
    private boolean isDarkTheme = true;
    
    // Schedulers
    private Map<String, Scheduler> schedulers;

    public MainController(Stage stage) {
        this.stage = stage;
        this.simulator = new Simulator();
        this.schedulers = new HashMap<>();
        
        schedulers.put("FCFS", new FcfsScheduler());
        schedulers.put("Round-Robin", new RrScheduler());
        schedulers.put("SJF", new SjfScheduler());
        schedulers.put("Priority", new PriorityScheduler());
        
        initializeUI();
        setupEventHandlers();
        setupSimulatorCallback();
        addSampleProcesses();
    }

    private void initializeUI() {
        root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Top: Menu and toolbar
        VBox topBox = new VBox(10);
        topBox.getChildren().addAll(createMenuBar(), createToolbar());
        root.setTop(topBox);
        
        // Left: Controls and process editor
        VBox leftPanel = createLeftPanel();
        root.setLeft(leftPanel);
        
        // Center: Gantt chart
        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);
        
        // Right: Live state and metrics
        VBox rightPanel = createRightPanel();
        root.setRight(rightPanel);
        
        // Bottom: Playback controls
        HBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);
        
        scene = new Scene(root, 1400, 800);
        loadStylesheet();
        
        stage.setScene(scene);
        stage.setTitle("CPU Scheduler Visualizer");
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem importItem = new MenuItem("Import JSON...");
        importItem.setOnAction(e -> importProcesses());
        
        MenuItem exportItem = new MenuItem("Export JSON...");
        exportItem.setOnAction(e -> exportProcesses());
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> Platform.exit());
        
        fileMenu.getItems().addAll(importItem, exportItem, new SeparatorMenuItem(), exitItem);
        
        // View menu
        Menu viewMenu = new Menu("View");
        MenuItem toggleThemeItem = new MenuItem("Toggle Theme");
        toggleThemeItem.setOnAction(e -> toggleTheme());
        viewMenu.getItems().add(toggleThemeItem);
        
        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox(15);
        toolbar.setPadding(new Insets(10));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("toolbar");
        
        Label titleLabel = new Label("CPU Scheduler Visualizer");
        titleLabel.getStyleClass().add("app-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label themeLabel = new Label("Dark Theme");
        ToggleSwitch themeToggle = new ToggleSwitch();
        themeToggle.setSelected(true);
        themeToggle.selectedProperty().addListener((obs, old, newVal) -> {
            isDarkTheme = newVal;
            loadStylesheet();
        });
        
        toolbar.getChildren().addAll(titleLabel, spacer, themeLabel, themeToggle);
        return toolbar;
    }

    private VBox createLeftPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(350);
        panel.getStyleClass().add("side-panel");
        
        Label controlsLabel = new Label("Controls & Processes");
        controlsLabel.getStyleClass().add("section-title");
        
        // Algorithm selection
        HBox algoBox = new HBox(10);
        algoBox.setAlignment(Pos.CENTER_LEFT);
        Label algoLabel = new Label("Algorithm:");
        algorithmCombo = new ComboBox<>(FXCollections.observableArrayList(
                "FCFS", "Round-Robin", "SJF", "Priority"));
        algorithmCombo.setValue("FCFS");
        algoBox.getChildren().addAll(algoLabel, algorithmCombo);
        
        // Quantum input (for Round-Robin)
        HBox quantumBox = new HBox(10);
        quantumBox.setAlignment(Pos.CENTER_LEFT);
        quantumLabel = new Label("Quantum:");
        quantumSpinner = new Spinner<>(1, 20, 2);
        quantumSpinner.setEditable(true);
        quantumSpinner.setPrefWidth(80);
        quantumBox.getChildren().addAll(quantumLabel, quantumSpinner);
        quantumBox.setVisible(false);
        
        // Process management buttons
        HBox buttonBox1 = new HBox(10);
        Button addProcessButton = new Button("Add Process");
        addProcessButton.setOnAction(e -> addProcess());
        Button removeProcessButton = new Button("Remove Selected");
        removeProcessButton.setOnAction(e -> processTable.removeSelectedProcess());
        buttonBox1.getChildren().addAll(addProcessButton, removeProcessButton);
        
        HBox buttonBox2 = new HBox(10);
        Button generateButton = new Button("Generate Random");
        generateButton.setOnAction(e -> generateRandomProcesses());
        Button clearButton = new Button("Clear All");
        clearButton.setOnAction(e -> processTable.clearProcesses());
        buttonBox2.getChildren().addAll(generateButton, clearButton);
        
        // Process table
        processTable = new ProcessTableView();
        processTable.setPrefHeight(250);
        
        // Run/Reset buttons
        HBox runBox = new HBox(10);
        runButton = new Button("Run Simulation");
        runButton.setPrefWidth(150);
        runButton.getStyleClass().add("primary-button");
        runButton.setOnAction(e -> runSimulation());
        
        resetButton = new Button("Reset");
        resetButton.setPrefWidth(100);
        resetButton.setOnAction(e -> resetSimulation());
        resetButton.setDisable(true);
        
        runBox.getChildren().addAll(runButton, resetButton);
        
        panel.getChildren().addAll(controlsLabel, algoBox, quantumBox, 
                buttonBox1, buttonBox2, processTable, runBox);
        
        return panel;
    }

    private VBox createCenterPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        
        Label ganttLabel = new Label("Gantt Chart Timeline");
        ganttLabel.getStyleClass().add("section-title");
        
        ganttView = new GanttView();
        VBox.setVgrow(ganttView, Priority.ALWAYS);
        
        panel.getChildren().addAll(ganttLabel, ganttView);
        return panel;
    }

    private VBox createRightPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(350);
        panel.getStyleClass().add("side-panel");
        
        Label stateLabel = new Label("Live State");
        stateLabel.getStyleClass().add("section-title");
        
        // Current state display
        VBox stateBox = new VBox(8);
        stateBox.setPadding(new Insets(10));
        stateBox.getStyleClass().add("state-box");
        
        currentTimeLabel = new Label("Current Time: 0");
        currentTimeLabel.getStyleClass().add("info-label");
        
        currentProcessLabel = new Label("Running: None");
        currentProcessLabel.getStyleClass().add("info-label");
        
        stateBox.getChildren().addAll(currentTimeLabel, currentProcessLabel);
        
        // Ready queue
        readyQueueView = new ReadyQueueView();
        
        // Metrics
        metricsView = new MetricsView();
        VBox.setVgrow(metricsView, Priority.ALWAYS);
        
        panel.getChildren().addAll(stateLabel, stateBox, readyQueueView, metricsView);
        return panel;
    }

    private HBox createBottomPanel() {
        HBox panel = new HBox(15);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.CENTER);
        panel.getStyleClass().add("controls-panel");
        
        jumpStartButton = new Button("⏮");
        jumpStartButton.setOnAction(e -> jumpToStart());
        jumpStartButton.setDisable(true);
        
        stepBackButton = new Button("⏪");
        stepBackButton.setOnAction(e -> stepBackward());
        stepBackButton.setDisable(true);
        
        playPauseButton = new Button("▶");
        playPauseButton.setOnAction(e -> togglePlayPause());
        playPauseButton.setDisable(true);
        playPauseButton.setPrefWidth(60);
        
        stepForwardButton = new Button("⏩");
        stepForwardButton.setOnAction(e -> stepForward());
        stepForwardButton.setDisable(true);
        
        jumpEndButton = new Button("⏭");
        jumpEndButton.setOnAction(e -> jumpToEnd());
        jumpEndButton.setDisable(true);
        
        Label speedLabel = new Label("Speed:");
        speedSlider = new Slider(0.5, 5.0, 1.0);
        speedSlider.setPrefWidth(150);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(1.0);
        
        panel.getChildren().addAll(jumpStartButton, stepBackButton, playPauseButton, 
                stepForwardButton, jumpEndButton, speedLabel, speedSlider);
        
        return panel;
    }

    private void setupEventHandlers() {
        // Show/hide quantum input based on algorithm
        algorithmCombo.valueProperty().addListener((obs, old, newVal) -> {
            quantumLabel.setVisible("Round-Robin".equals(newVal));
            quantumSpinner.setVisible("Round-Robin".equals(newVal));
        });
        
        // Gantt segment click handler
        ganttView.setOnSegmentClick(tick -> {
            simulator.jumpToTick(tick);
        });
        
        // Keyboard shortcuts
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case SPACE:
                    if (!playPauseButton.isDisable()) {
                        togglePlayPause();
                    }
                    break;
                case LEFT:
                    if (!stepBackButton.isDisable()) {
                        stepBackward();
                    }
                    break;
                case RIGHT:
                    if (!stepForwardButton.isDisable()) {
                        stepForward();
                    }
                    break;
                case HOME:
                    if (!jumpStartButton.isDisable()) {
                        jumpToStart();
                    }
                    break;
                case END:
                    if (!jumpEndButton.isDisable()) {
                        jumpToEnd();
                    }
                    break;
            }
        });
    }

    private void setupSimulatorCallback() {
        simulator.setUpdateCallback(this::updateUIFromState);
    }

    private void updateUIFromState(SimulationState state) {
        Platform.runLater(() -> {
            currentTimeLabel.setText("Current Time: " + state.getCurrentTick());
            
            String running = state.getCurrentRunningProcess();
            currentProcessLabel.setText("Running: " + (running != null ? running : "None"));
            
            readyQueueView.updateQueue(state.getReadyQueueSnapshot());
            
            ganttView.updateTimeMarker(state.getCurrentTick());
            
            metricsView.updateMetrics(state.getProcessMap().values(), state.getTotalTime());
        });
    }

    private void addProcess() {
        int nextId = processTable.getProcesses().size() + 1;
        ProcessModel process = new ProcessModel("P" + nextId, 0, 5, nextId);
        process.setColor(ColorPalette.getNextColor());
        processTable.addProcess(process);
    }

    private void generateRandomProcesses() {
        processTable.clearProcesses();
        ColorPalette.reset();
        
        Random random = new Random();
        int count = 4 + random.nextInt(4); // 4-7 processes
        
        for (int i = 1; i <= count; i++) {
            int arrival = random.nextInt(8);
            int burst = 1 + random.nextInt(8);
            int priority = 1 + random.nextInt(5);
            
            ProcessModel process = new ProcessModel("P" + i, arrival, burst, priority);
            process.setColor(ColorPalette.getNextColor());
            processTable.addProcess(process);
        }
    }

    private void addSampleProcesses() {
        ColorPalette.reset();
        
        ProcessModel p1 = new ProcessModel("P1", 0, 5, 2);
        p1.setColor(ColorPalette.getNextColor());
        
        ProcessModel p2 = new ProcessModel("P2", 2, 3, 1);
        p2.setColor(ColorPalette.getNextColor());
        
        ProcessModel p3 = new ProcessModel("P3", 4, 1, 3);
        p3.setColor(ColorPalette.getNextColor());
        
        processTable.addProcess(p1);
        processTable.addProcess(p2);
        processTable.addProcess(p3);
    }

    private void runSimulation() {
        if (processTable.getProcesses().isEmpty()) {
            showAlert("No Processes", "Please add processes before running simulation.");
            return;
        }
        
        String algorithm = algorithmCombo.getValue();
        Scheduler scheduler = schedulers.get(algorithm);
        
        Map<String, Object> params = new HashMap<>();
        if ("Round-Robin".equals(algorithm)) {
            params.put("quantum", quantumSpinner.getValue());
        }
        
        List<ProcessModel> processes = new ArrayList<>(processTable.getProcesses());
        simulator.initialize(processes, scheduler, params);
        
        ganttView.renderGantt(simulator.getState().getSegments(), 
                             simulator.getState().getProcessMap());
        
        enablePlaybackControls(true);
        runButton.setDisable(true);
        resetButton.setDisable(false);
    }

    private void resetSimulation() {
        stopPlayback();
        simulator.reset();
        
        enablePlaybackControls(false);
        runButton.setDisable(false);
        resetButton.setDisable(true);
        
        metricsView.clear();
        currentTimeLabel.setText("Current Time: 0");
        currentProcessLabel.setText("Running: None");
        readyQueueView.updateQueue(new ArrayList<>());
    }

    private void togglePlayPause() {
        if (isPlaying) {
            stopPlayback();
        } else {
            startPlayback();
        }
    }

    private void startPlayback() {
        isPlaying = true;
        playPauseButton.setText("⏸");
        
        playbackTimeline = new Timeline(new KeyFrame(Duration.millis(1000.0 / speedSlider.getValue()), e -> {
            boolean canStep = simulator.stepForward();
            if (!canStep) {
                stopPlayback();
            }
        }));
        playbackTimeline.setCycleCount(Animation.INDEFINITE);
        playbackTimeline.play();
        
        // Update timeline speed when slider changes
        speedSlider.valueProperty().addListener((obs, old, newVal) -> {
            if (playbackTimeline != null) {
                playbackTimeline.setRate(newVal.doubleValue());
            }
        });
    }

    private void stopPlayback() {
        isPlaying = false;
        playPauseButton.setText("▶");
        
        if (playbackTimeline != null) {
            playbackTimeline.stop();
            playbackTimeline = null;
        }
    }

    private void stepForward() {
        stopPlayback();
        simulator.stepForward();
    }

    private void stepBackward() {
        stopPlayback();
        simulator.stepBackward();
    }

    private void jumpToStart() {
        stopPlayback();
        simulator.jumpToStart();
    }

    private void jumpToEnd() {
        stopPlayback();
        simulator.jumpToEnd();
    }

    private void enablePlaybackControls(boolean enable) {
        jumpStartButton.setDisable(!enable);
        stepBackButton.setDisable(!enable);
        playPauseButton.setDisable(!enable);
        stepForwardButton.setDisable(!enable);
        jumpEndButton.setDisable(!enable);
    }

    private void importProcesses() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Processes");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                List<ProcessModel> processes = JsonIO.importProcesses(file);
                processTable.clearProcesses();
                for (ProcessModel p : processes) {
                    processTable.addProcess(p);
                }
                showInfo("Import Successful", "Imported " + processes.size() + " processes.");
            } catch (Exception e) {
                showAlert("Import Failed", "Failed to import processes: " + e.getMessage());
            }
        }
    }

    private void exportProcesses() {
        if (processTable.getProcesses().isEmpty()) {
            showAlert("No Processes", "No processes to export.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Processes");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialFileName("processes.json");
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                JsonIO.exportProcesses(new ArrayList<>(processTable.getProcesses()), file);
                showInfo("Export Successful", "Exported to " + file.getName());
            } catch (Exception e) {
                showAlert("Export Failed", "Failed to export processes: " + e.getMessage());
            }
        }
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        loadStylesheet();
    }

    private void loadStylesheet() {
        scene.getStylesheets().clear();
        String stylesheet = isDarkTheme ? "dark-theme.css" : "light-theme.css";
        scene.getStylesheets().add(getClass().getResource("/styles/" + stylesheet).toExternalForm());
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("CPU Scheduler Visualizer");
        alert.setContentText("An interactive educational tool for visualizing CPU scheduling algorithms.\n\n" +
                "Algorithms: FCFS, Round-Robin, SJF, Priority\n\n" +
                "Built with JavaFX\nVersion 1.0.0");
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show() {
        stage.show();
    }
}
