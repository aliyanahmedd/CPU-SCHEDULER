package com.scheduler.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom component for displaying the ready queue.
 */
public class ReadyQueueView extends VBox {
    private FlowPane queuePane;
    private Map<String, VBox> processCards;

    public ReadyQueueView() {
        processCards = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        setSpacing(10);
        setPadding(new Insets(10));
        getStyleClass().add("ready-queue-view");

        Label titleLabel = new Label("Ready Queue");
        titleLabel.getStyleClass().add("section-title");

        queuePane = new FlowPane();
        queuePane.setHgap(8);
        queuePane.setVgap(8);
        queuePane.setPrefWrapLength(250);

        getChildren().addAll(titleLabel, queuePane);
    }

    /**
     * Update the ready queue display.
     */
    public void updateQueue(List<String> processIds) {
        queuePane.getChildren().clear();
        processCards.clear();

        if (processIds.isEmpty()) {
            Label emptyLabel = new Label("(empty)");
            emptyLabel.getStyleClass().add("empty-queue-label");
            queuePane.getChildren().add(emptyLabel);
            return;
        }

        for (String pid : processIds) {
            VBox card = createProcessCard(pid);
            processCards.put(pid, card);
            queuePane.getChildren().add(card);
            
            // Fade in animation
            FadeTransition fade = new FadeTransition(Duration.millis(300), card);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        }
    }

    private VBox createProcessCard(String pid) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(8));
        card.getStyleClass().add("process-card");
        
        Label pidLabel = new Label(pid);
        pidLabel.getStyleClass().add("process-card-label");
        
        card.getChildren().add(pidLabel);
        return card;
    }
}
