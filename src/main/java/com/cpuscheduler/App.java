package com.cpuscheduler;

import com.cpuscheduler.algorithm.*;
import com.cpuscheduler.model.Process;
import com.cpuscheduler.model.SchedulingResult;
import com.cpuscheduler.ui.GanttChartPane;
import com.cpuscheduler.ui.MetricsDashboard;
import com.cpuscheduler.ui.ProcessTable;
import com.cpuscheduler.util.ScenarioIO;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Main application for CPU Scheduler Visualizer.
 * Interactive desktop app for learning and demoing CPU scheduling algorithms.
 */
public class App extends Application {
    private ProcessTable processTable;
    private GanttChartPane ganttChart;
    private MetricsDashboard dashboard;
    private ComboBox<String> algorithmCombo;
    private Spinner<Integer> quantumSpinner;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #ffffff;");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Left side - Process table and controls
        VBox leftPane = createLeftPane();
        root.setLeft(leftPane);

        // Center - Gantt chart
        VBox centerPane = createCenterPane();
        root.setCenter(centerPane);

        // Right side - Metrics dashboard
        dashboard = new MetricsDashboard();
        root.setRight(dashboard);

        Scene scene = new Scene(root, 1200, 700);
        stage.setTitle("CPU Scheduler Visualizer");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }

    private VBox createHeader() {
        Label titleLabel = new Label("CPU Scheduler Visualizer");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        Label subtitleLabel = new Label("Learn and demo common CPU scheduling algorithms");
        subtitleLabel.setFont(Font.font("System", 12));
        subtitleLabel.setStyle("-fx-text-fill: #666;");

        VBox header = new VBox(5);
        header.getChildren().addAll(titleLabel, subtitleLabel);
        header.setPadding(new Insets(0, 0, 15, 0));
        
        return header;
    }

    private VBox createLeftPane() {
        processTable = new ProcessTable();
        processTable.setPrefWidth(370);

        // Algorithm selection
        Label algoLabel = new Label("Algorithm:");
        algoLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        algorithmCombo = new ComboBox<>();
        algorithmCombo.getItems().addAll(
            "FCFS (First Come First Served)",
            "SJF (Shortest Job First)",
            "Priority Scheduling",
            "Round Robin"
        );
        algorithmCombo.setValue("FCFS (First Come First Served)");
        algorithmCombo.setPrefWidth(220);
        algorithmCombo.setOnAction(e -> updateQuantumVisibility());

        // Time quantum for Round Robin
        Label quantumLabel = new Label("Time Quantum:");
        quantumSpinner = new Spinner<>(1, 100, 2);
        quantumSpinner.setEditable(true);
        quantumSpinner.setPrefWidth(80);
        
        HBox quantumBox = new HBox(10);
        quantumBox.setAlignment(Pos.CENTER_LEFT);
        quantumBox.getChildren().addAll(quantumLabel, quantumSpinner);
        quantumBox.setVisible(false);
        quantumBox.setManaged(false);

        // Run button
        Button runBtn = new Button("Run Simulation");
        runBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        runBtn.setPrefWidth(150);
        runBtn.setOnAction(e -> runSimulation());

        // Export/Import buttons
        Button exportBtn = new Button("Export");
        exportBtn.setOnAction(e -> exportScenario());
        
        Button importBtn = new Button("Import");
        importBtn.setOnAction(e -> importScenario());

        HBox ioButtons = new HBox(10);
        ioButtons.setAlignment(Pos.CENTER_LEFT);
        ioButtons.getChildren().addAll(exportBtn, importBtn);

        VBox controls = new VBox(10);
        controls.setPadding(new Insets(15, 0, 0, 0));
        controls.getChildren().addAll(
            algoLabel, algorithmCombo, quantumBox, runBtn, 
            new Separator(), ioButtons
        );

        VBox leftPane = new VBox(10);
        leftPane.getChildren().addAll(processTable, controls);
        leftPane.setPadding(new Insets(0, 15, 0, 0));

        return leftPane;
    }

    private VBox createCenterPane() {
        ganttChart = new GanttChartPane();
        
        VBox centerPane = new VBox(10);
        centerPane.getChildren().add(ganttChart);
        centerPane.setPadding(new Insets(0, 15, 0, 0));
        VBox.setVgrow(ganttChart, Priority.ALWAYS);
        
        return centerPane;
    }

    private void updateQuantumVisibility() {
        boolean isRoundRobin = algorithmCombo.getValue().startsWith("Round Robin");
        HBox quantumBox = (HBox) ((VBox) algorithmCombo.getParent()).getChildren().get(2);
        quantumBox.setVisible(isRoundRobin);
        quantumBox.setManaged(isRoundRobin);
    }

    private void runSimulation() {
        List<Process> processes = processTable.getProcesses();
        
        if (processes.isEmpty()) {
            showAlert("No Processes", "Please add at least one process before running the simulation.");
            return;
        }

        SchedulingAlgorithm algorithm = getSelectedAlgorithm();
        SchedulingResult result = algorithm.schedule(processes);

        ganttChart.updateChart(result.getGanttChart());
        dashboard.updateMetrics(result);
    }

    private SchedulingAlgorithm getSelectedAlgorithm() {
        String selected = algorithmCombo.getValue();
        
        if (selected.startsWith("FCFS")) {
            return new FCFSScheduler();
        } else if (selected.startsWith("SJF")) {
            return new SJFScheduler();
        } else if (selected.startsWith("Priority")) {
            return new PriorityScheduler();
        } else if (selected.startsWith("Round Robin")) {
            return new RoundRobinScheduler(quantumSpinner.getValue());
        }
        
        return new FCFSScheduler(); // Default
    }

    private void exportScenario() {
        List<Process> processes = processTable.getProcesses();
        
        if (processes.isEmpty()) {
            showAlert("No Processes", "No processes to export.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Scenario");
        fileChooser.setInitialFileName("scenario.json");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                ScenarioIO.exportScenario(processes, file);
                showInfo("Export Successful", "Scenario exported to: " + file.getName());
            } catch (Exception e) {
                showAlert("Export Error", "Failed to export scenario: " + e.getMessage());
            }
        }
    }

    private void importScenario() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Scenario");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                List<Process> processes = ScenarioIO.importScenario(file);
                processTable.setProcesses(processes);
                ganttChart.clear();
                dashboard.clear();
                showInfo("Import Successful", "Imported " + processes.size() + " processes from: " + file.getName());
            } catch (Exception e) {
                showAlert("Import Error", "Failed to import scenario: " + e.getMessage());
            }
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}
