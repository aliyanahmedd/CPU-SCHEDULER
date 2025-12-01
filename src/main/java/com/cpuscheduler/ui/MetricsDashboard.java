package com.cpuscheduler.ui;

import com.cpuscheduler.model.Process;
import com.cpuscheduler.model.SchedulingResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * Metrics dashboard component showing scheduling statistics.
 */
public class MetricsDashboard extends VBox {
    private final Label algorithmLabel;
    private final Label avgWaitingTimeLabel;
    private final Label avgTurnaroundTimeLabel;
    private final Label avgResponseTimeLabel;
    private final Label throughputLabel;
    private final Label cpuUtilizationLabel;
    private final Label totalTimeLabel;
    private final VBox processMetricsBox;

    public MetricsDashboard() {
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #ddd; -fx-border-radius: 5;");
        this.setPrefWidth(280);

        Label titleLabel = new Label("Metrics Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Algorithm info
        algorithmLabel = createValueLabel("N/A");
        HBox algoBox = createMetricRow("Algorithm:", algorithmLabel);

        // Overall metrics section
        Label overallTitle = new Label("Overall Metrics");
        overallTitle.setFont(Font.font("System", FontWeight.BOLD, 13));
        overallTitle.setPadding(new Insets(10, 0, 5, 0));

        avgWaitingTimeLabel = createValueLabel("-");
        avgTurnaroundTimeLabel = createValueLabel("-");
        avgResponseTimeLabel = createValueLabel("-");
        throughputLabel = createValueLabel("-");
        cpuUtilizationLabel = createValueLabel("-");
        totalTimeLabel = createValueLabel("-");

        VBox overallMetrics = new VBox(8);
        overallMetrics.getChildren().addAll(
            createMetricRow("Avg Waiting Time:", avgWaitingTimeLabel),
            createMetricRow("Avg Turnaround Time:", avgTurnaroundTimeLabel),
            createMetricRow("Avg Response Time:", avgResponseTimeLabel),
            createMetricRow("Throughput:", throughputLabel),
            createMetricRow("CPU Utilization:", cpuUtilizationLabel),
            createMetricRow("Total Time:", totalTimeLabel)
        );

        // Process-specific metrics section
        Label processTitle = new Label("Process Metrics");
        processTitle.setFont(Font.font("System", FontWeight.BOLD, 13));
        processTitle.setPadding(new Insets(10, 0, 5, 0));

        processMetricsBox = new VBox(5);
        showEmptyProcessMetrics();

        this.getChildren().addAll(
            titleLabel,
            algoBox,
            overallTitle,
            overallMetrics,
            processTitle,
            processMetricsBox
        );
    }

    /**
     * Updates the dashboard with new scheduling results.
     */
    public void updateMetrics(SchedulingResult result) {
        if (result == null) {
            clear();
            return;
        }

        algorithmLabel.setText(result.getAlgorithmName());
        avgWaitingTimeLabel.setText(String.format("%.2f units", result.getAverageWaitingTime()));
        avgTurnaroundTimeLabel.setText(String.format("%.2f units", result.getAverageTurnaroundTime()));
        avgResponseTimeLabel.setText(String.format("%.2f units", result.getAverageResponseTime()));
        throughputLabel.setText(String.format("%.4f proc/unit", result.getThroughput()));
        cpuUtilizationLabel.setText(String.format("%.1f%%", result.getCpuUtilization()));
        totalTimeLabel.setText(String.format("%d units", result.getTotalTime()));

        updateProcessMetrics(result.getProcesses());
    }

    private void updateProcessMetrics(List<Process> processes) {
        processMetricsBox.getChildren().clear();
        
        if (processes.isEmpty()) {
            showEmptyProcessMetrics();
            return;
        }

        // Header
        HBox header = new HBox(5);
        header.getChildren().addAll(
            createHeaderCell("Process", 60),
            createHeaderCell("WT", 40),
            createHeaderCell("TAT", 40),
            createHeaderCell("RT", 40),
            createHeaderCell("CT", 40)
        );
        header.setStyle("-fx-background-color: #e9ecef; -fx-padding: 5;");
        processMetricsBox.getChildren().add(header);

        // Process rows
        for (Process p : processes) {
            HBox row = new HBox(5);
            row.getChildren().addAll(
                createCell(p.getName(), 60),
                createCell(String.valueOf(p.getWaitingTime()), 40),
                createCell(String.valueOf(p.getTurnaroundTime()), 40),
                createCell(String.valueOf(p.getResponseTime()), 40),
                createCell(String.valueOf(p.getCompletionTime()), 40)
            );
            row.setStyle("-fx-padding: 3;");
            processMetricsBox.getChildren().add(row);
        }
    }

    private Label createHeaderCell(String text, double width) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 10));
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private Label createCell(String text, double width) {
        Label label = new Label(text);
        label.setFont(Font.font("System", 10));
        label.setPrefWidth(width);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private void showEmptyProcessMetrics() {
        processMetricsBox.getChildren().clear();
        Label emptyLabel = new Label("No results yet");
        emptyLabel.setTextFill(Color.GRAY);
        emptyLabel.setFont(Font.font("System", 11));
        processMetricsBox.getChildren().add(emptyLabel);
    }

    private HBox createMetricRow(String labelText, Label valueLabel) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(labelText);
        nameLabel.setFont(Font.font("System", 12));
        nameLabel.setPrefWidth(130);
        
        row.getChildren().addAll(nameLabel, valueLabel);
        return row;
    }

    private Label createValueLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 12));
        label.setTextFill(Color.web("#2c3e50"));
        return label;
    }

    /**
     * Clears all metrics to default state.
     */
    public void clear() {
        algorithmLabel.setText("N/A");
        avgWaitingTimeLabel.setText("-");
        avgTurnaroundTimeLabel.setText("-");
        avgResponseTimeLabel.setText("-");
        throughputLabel.setText("-");
        cpuUtilizationLabel.setText("-");
        totalTimeLabel.setText("-");
        showEmptyProcessMetrics();
    }
}
