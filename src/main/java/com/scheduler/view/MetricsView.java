package com.scheduler.view;

import com.scheduler.model.ProcessModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Collection;

/**
 * Component for displaying process metrics (waiting time, turnaround time, etc.)
 */
public class MetricsView extends BorderPane {
    private TableView<ProcessModel> metricsTable;
    private ObservableList<ProcessModel> processes;
    private Label avgWaitingLabel;
    private Label avgTurnaroundLabel;
    private Label totalTimeLabel;

    public MetricsView() {
        processes = FXCollections.observableArrayList();
        initializeUI();
    }

    private void initializeUI() {
        VBox mainBox = new VBox(10);
        mainBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Process Metrics");
        titleLabel.getStyleClass().add("section-title");

        metricsTable = new TableView<>();
        metricsTable.setItems(processes);
        metricsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        metricsTable.setPrefHeight(200);

        // Columns
        TableColumn<ProcessModel, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ProcessModel, Integer> arrivalCol = new TableColumn<>("Arrival");
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrival"));

        TableColumn<ProcessModel, Integer> burstCol = new TableColumn<>("Burst");
        burstCol.setCellValueFactory(new PropertyValueFactory<>("burst"));

        TableColumn<ProcessModel, Integer> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<ProcessModel, Integer> completionCol = new TableColumn<>("Completion");
        completionCol.setCellValueFactory(new PropertyValueFactory<>("completionTime"));

        TableColumn<ProcessModel, Integer> waitingCol = new TableColumn<>("Waiting");
        waitingCol.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));

        TableColumn<ProcessModel, Integer> turnaroundCol = new TableColumn<>("Turnaround");
        turnaroundCol.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));

        metricsTable.getColumns().addAll(idCol, arrivalCol, burstCol, startCol, 
                                         completionCol, waitingCol, turnaroundCol);

        // Summary section
        VBox summaryBox = new VBox(5);
        summaryBox.getStyleClass().add("metrics-summary");
        summaryBox.setPadding(new Insets(10));

        avgWaitingLabel = new Label("Avg Waiting Time: -");
        avgWaitingLabel.getStyleClass().add("metric-label");

        avgTurnaroundLabel = new Label("Avg Turnaround Time: -");
        avgTurnaroundLabel.getStyleClass().add("metric-label");

        totalTimeLabel = new Label("Total Time: -");
        totalTimeLabel.getStyleClass().add("metric-label");

        summaryBox.getChildren().addAll(avgWaitingLabel, avgTurnaroundLabel, totalTimeLabel);

        mainBox.getChildren().addAll(titleLabel, metricsTable, summaryBox);
        setCenter(mainBox);
    }

    public void updateMetrics(Collection<ProcessModel> processList, int totalTime) {
        processes.clear();
        processes.addAll(processList);

        if (!processList.isEmpty()) {
            double avgWaiting = processList.stream()
                    .mapToInt(ProcessModel::getWaitingTime)
                    .average()
                    .orElse(0.0);

            double avgTurnaround = processList.stream()
                    .mapToInt(ProcessModel::getTurnaroundTime)
                    .average()
                    .orElse(0.0);

            avgWaitingLabel.setText(String.format("Avg Waiting Time: %.2f", avgWaiting));
            avgTurnaroundLabel.setText(String.format("Avg Turnaround Time: %.2f", avgTurnaround));
            totalTimeLabel.setText("Total Time: " + totalTime);
        } else {
            avgWaitingLabel.setText("Avg Waiting Time: -");
            avgTurnaroundLabel.setText("Avg Turnaround Time: -");
            totalTimeLabel.setText("Total Time: -");
        }
    }

    public void clear() {
        processes.clear();
        avgWaitingLabel.setText("Avg Waiting Time: -");
        avgTurnaroundLabel.setText("Avg Turnaround Time: -");
        totalTimeLabel.setText("Total Time: -");
    }
}
