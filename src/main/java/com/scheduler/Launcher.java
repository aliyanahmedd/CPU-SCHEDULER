package com.scheduler;

/**
 * Launcher class to work around JavaFX module issues when creating a shaded JAR.
 * This class doesn't extend Application, so it can be used as the main class in the manifest.
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
