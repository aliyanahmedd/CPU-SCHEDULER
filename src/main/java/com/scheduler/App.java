package com.scheduler;

import com.scheduler.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main JavaFX Application class.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController(primaryStage);
        controller.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
