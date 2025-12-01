package com.cpuscheduler.ui;

import com.cpuscheduler.model.GanttEntry;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gantt chart visualization component for CPU scheduling.
 */
public class GanttChartPane extends VBox {
    private static final double UNIT_WIDTH = 30;
    private static final double BAR_HEIGHT = 40;
    private static final double TIME_LABEL_HEIGHT = 25;
    
    private static final Color[] COLORS = {
        Color.web("#3498db"),  // Blue
        Color.web("#e74c3c"),  // Red
        Color.web("#2ecc71"),  // Green
        Color.web("#f39c12"),  // Orange
        Color.web("#9b59b6"),  // Purple
        Color.web("#1abc9c"),  // Teal
        Color.web("#e67e22"),  // Dark Orange
        Color.web("#34495e"),  // Dark Gray
        Color.web("#16a085"),  // Dark Teal
        Color.web("#c0392b")   // Dark Red
    };

    private final HBox chartContainer;
    private final ScrollPane scrollPane;
    private final Map<Integer, Color> processColors;

    public GanttChartPane() {
        this.processColors = new HashMap<>();
        
        Label titleLabel = new Label("Gantt Chart");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setPadding(new Insets(0, 0, 10, 0));

        chartContainer = new HBox(0);
        chartContainer.setAlignment(Pos.CENTER_LEFT);
        chartContainer.setPadding(new Insets(10));

        scrollPane = new ScrollPane(chartContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(BAR_HEIGHT + TIME_LABEL_HEIGHT + 40);
        scrollPane.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        this.getChildren().addAll(titleLabel, scrollPane);
        this.setSpacing(5);
        
        showEmptyMessage();
    }

    /**
     * Updates the Gantt chart with new entries.
     */
    public void updateChart(List<GanttEntry> entries) {
        chartContainer.getChildren().clear();
        processColors.clear();

        if (entries == null || entries.isEmpty()) {
            showEmptyMessage();
            return;
        }

        int colorIndex = 0;
        int lastEndTime = 0;

        for (GanttEntry entry : entries) {
            // Add idle time if there's a gap
            if (entry.getStartTime() > lastEndTime) {
                addIdleBlock(lastEndTime, entry.getStartTime());
            }

            // Assign color to process if not already assigned
            if (!processColors.containsKey(entry.getProcessId())) {
                processColors.put(entry.getProcessId(), COLORS[colorIndex % COLORS.length]);
                colorIndex++;
            }

            addProcessBlock(entry);
            lastEndTime = entry.getEndTime();
        }
    }

    private void addProcessBlock(GanttEntry entry) {
        double width = entry.getDuration() * UNIT_WIDTH;
        Color color = processColors.get(entry.getProcessId());

        VBox block = new VBox(2);
        block.setAlignment(Pos.CENTER);

        // Process bar
        StackPane barPane = new StackPane();
        Rectangle bar = new Rectangle(width, BAR_HEIGHT);
        bar.setFill(color);
        bar.setStroke(Color.WHITE);
        bar.setStrokeWidth(1);
        bar.setArcWidth(5);
        bar.setArcHeight(5);

        Label processLabel = new Label(entry.getProcessName());
        processLabel.setTextFill(Color.WHITE);
        processLabel.setFont(Font.font("System", FontWeight.BOLD, 11));

        barPane.getChildren().addAll(bar, processLabel);

        // Time labels
        HBox timeLabels = new HBox();
        timeLabels.setPrefWidth(width);
        timeLabels.setAlignment(Pos.CENTER);
        
        Label startLabel = new Label(String.valueOf(entry.getStartTime()));
        startLabel.setFont(Font.font("System", 10));
        startLabel.setPrefWidth(width / 2);
        startLabel.setAlignment(Pos.CENTER_LEFT);
        
        Label endLabel = new Label(String.valueOf(entry.getEndTime()));
        endLabel.setFont(Font.font("System", 10));
        endLabel.setPrefWidth(width / 2);
        endLabel.setAlignment(Pos.CENTER_RIGHT);

        timeLabels.getChildren().addAll(startLabel, endLabel);

        block.getChildren().addAll(barPane, timeLabels);
        chartContainer.getChildren().add(block);
    }

    private void addIdleBlock(int startTime, int endTime) {
        double width = (endTime - startTime) * UNIT_WIDTH;

        VBox block = new VBox(2);
        block.setAlignment(Pos.CENTER);

        // Idle bar
        StackPane barPane = new StackPane();
        Rectangle bar = new Rectangle(width, BAR_HEIGHT);
        bar.setFill(Color.LIGHTGRAY);
        bar.setStroke(Color.WHITE);
        bar.setStrokeWidth(1);
        bar.setArcWidth(5);
        bar.setArcHeight(5);

        Label idleLabel = new Label("Idle");
        idleLabel.setTextFill(Color.GRAY);
        idleLabel.setFont(Font.font("System", FontWeight.NORMAL, 10));

        barPane.getChildren().addAll(bar, idleLabel);

        // Time labels
        HBox timeLabels = new HBox();
        timeLabels.setPrefWidth(width);
        timeLabels.setAlignment(Pos.CENTER);
        
        Label startLabel = new Label(String.valueOf(startTime));
        startLabel.setFont(Font.font("System", 10));
        startLabel.setPrefWidth(width / 2);
        startLabel.setAlignment(Pos.CENTER_LEFT);
        
        Label endLbl = new Label(String.valueOf(endTime));
        endLbl.setFont(Font.font("System", 10));
        endLbl.setPrefWidth(width / 2);
        endLbl.setAlignment(Pos.CENTER_RIGHT);

        timeLabels.getChildren().addAll(startLabel, endLbl);

        block.getChildren().addAll(barPane, timeLabels);
        chartContainer.getChildren().add(block);
    }

    private void showEmptyMessage() {
        chartContainer.getChildren().clear();
        Label emptyLabel = new Label("Run a simulation to see the Gantt chart");
        emptyLabel.setTextFill(Color.GRAY);
        emptyLabel.setFont(Font.font("System", 12));
        chartContainer.getChildren().add(emptyLabel);
    }

    /**
     * Clears the Gantt chart.
     */
    public void clear() {
        showEmptyMessage();
        processColors.clear();
    }
}
