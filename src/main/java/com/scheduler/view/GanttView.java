package com.scheduler.view;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Custom JavaFX component for rendering an interactive Gantt chart.
 */
public class GanttView extends BorderPane {
    private static final double SCALE = 40.0; // pixels per time unit
    private static final double BAR_HEIGHT = 50.0;
    private static final double LABEL_HEIGHT = 30.0;
    
    private Pane chartPane;
    private ScrollPane scrollPane;
    private Line timeMarker;
    private int currentTick = 0;
    private Consumer<Integer> onSegmentClick;

    public GanttView() {
        initializeUI();
    }

    private void initializeUI() {
        chartPane = new Pane();
        chartPane.setPrefHeight(BAR_HEIGHT + LABEL_HEIGHT + 20);
        chartPane.setMinHeight(BAR_HEIGHT + LABEL_HEIGHT + 20);
        
        scrollPane = new ScrollPane(chartPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("gantt-scroll-pane");
        
        setCenter(scrollPane);
    }

    /**
     * Render the Gantt chart from segments.
     */
    public void renderGantt(List<GanttSegment> segments, Map<String, ProcessModel> processMap) {
        chartPane.getChildren().clear();
        
        if (segments.isEmpty()) {
            Label emptyLabel = new Label("No segments to display");
            emptyLabel.setLayoutX(20);
            emptyLabel.setLayoutY(BAR_HEIGHT / 2);
            chartPane.getChildren().add(emptyLabel);
            return;
        }

        int maxTime = segments.get(segments.size() - 1).getEnd();
        chartPane.setPrefWidth(maxTime * SCALE + 50);

        // Draw time scale
        for (int t = 0; t <= maxTime; t++) {
            Line tick = new Line(t * SCALE, BAR_HEIGHT, t * SCALE, BAR_HEIGHT + 10);
            tick.getStyleClass().add("time-tick");
            chartPane.getChildren().add(tick);
            
            Label timeLabel = new Label(String.valueOf(t));
            timeLabel.setLayoutX(t * SCALE - 5);
            timeLabel.setLayoutY(BAR_HEIGHT + 10);
            timeLabel.getStyleClass().add("time-label");
            chartPane.getChildren().add(timeLabel);
        }

        // Draw segments
        for (GanttSegment segment : segments) {
            Rectangle rect = new Rectangle();
            rect.setX(segment.getStart() * SCALE);
            rect.setY(10);
            rect.setWidth((segment.getEnd() - segment.getStart()) * SCALE);
            rect.setHeight(BAR_HEIGHT);
            rect.setArcWidth(8);
            rect.setArcHeight(8);
            
            if (segment.isIdle()) {
                rect.setFill(Color.gray(0.3, 0.3));
                rect.setStroke(Color.gray(0.5));
            } else {
                ProcessModel process = processMap.get(segment.getPid());
                if (process != null) {
                    rect.setFill(process.getColor());
                    rect.setStroke(process.getColor().darker());
                } else {
                    rect.setFill(Color.LIGHTBLUE);
                    rect.setStroke(Color.BLUE);
                }
            }
            
            rect.setStrokeWidth(2);
            rect.getStyleClass().add("gantt-segment");
            
            // Add tooltip
            Tooltip tooltip = createTooltip(segment, processMap);
            Tooltip.install(rect, tooltip);
            
            // Add click handler
            int startTime = segment.getStart();
            rect.setOnMouseClicked(e -> {
                if (onSegmentClick != null) {
                    onSegmentClick.accept(startTime);
                }
            });
            
            // Add hover effect
            rect.setOnMouseEntered(e -> rect.setOpacity(0.8));
            rect.setOnMouseExited(e -> rect.setOpacity(1.0));
            
            chartPane.getChildren().add(rect);
            
            // Add process ID label
            Label pidLabel = new Label(segment.getPid());
            pidLabel.setLayoutX(segment.getStart() * SCALE + 5);
            pidLabel.setLayoutY(25);
            pidLabel.getStyleClass().add("segment-label");
            pidLabel.setMouseTransparent(true);
            chartPane.getChildren().add(pidLabel);
        }

        // Add time marker
        timeMarker = new Line(0, 0, 0, BAR_HEIGHT);
        timeMarker.setStroke(Color.RED);
        timeMarker.setStrokeWidth(3);
        timeMarker.getStyleClass().add("time-marker");
        chartPane.getChildren().add(timeMarker);
        
        updateTimeMarker(0);
    }

    private Tooltip createTooltip(GanttSegment segment, Map<String, ProcessModel> processMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("Process: ").append(segment.getPid()).append("\n");
        sb.append("Start: ").append(segment.getStart()).append("\n");
        sb.append("End: ").append(segment.getEnd()).append("\n");
        sb.append("Duration: ").append(segment.getDuration()).append("\n");
        
        if (!segment.isIdle()) {
            ProcessModel process = processMap.get(segment.getPid());
            if (process != null) {
                sb.append("Waiting Time: ").append(process.getWaitingTime()).append("\n");
                sb.append("Turnaround Time: ").append(process.getTurnaroundTime());
            }
        }
        
        return new Tooltip(sb.toString());
    }

    /**
     * Update the time marker position with animation.
     */
    public void updateTimeMarker(int tick) {
        this.currentTick = tick;
        if (timeMarker != null) {
            double targetX = tick * SCALE;
            
            TranslateTransition transition = new TranslateTransition(Duration.millis(300), timeMarker);
            transition.setToX(targetX);
            transition.play();
            
            // Auto-scroll to keep marker visible
            double viewportWidth = scrollPane.getViewportBounds().getWidth();
            double contentWidth = chartPane.getWidth();
            double hValue = (targetX - viewportWidth / 2) / (contentWidth - viewportWidth);
            scrollPane.setHvalue(Math.max(0, Math.min(1, hValue)));
        }
    }

    /**
     * Set callback for segment click events.
     */
    public void setOnSegmentClick(Consumer<Integer> callback) {
        this.onSegmentClick = callback;
    }
}
