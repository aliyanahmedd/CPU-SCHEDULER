package com.scheduler.model;

import javafx.scene.paint.Color;

/**
 * Represents a process in the CPU scheduling simulation.
 */
public class ProcessModel {
    private String id;
    private int arrival;
    private int burst;
    private int remaining;
    private int priority;
    private Integer startTime;
    private Integer completionTime;
    private Color color;

    public ProcessModel(String id, int arrival, int burst, int priority) {
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
        this.priority = priority;
        this.startTime = null;
        this.completionTime = null;
        this.color = generateColor();
    }

    public ProcessModel(String id, int arrival, int burst) {
        this(id, arrival, burst, 0);
    }

    // Copy constructor for simulation
    public ProcessModel(ProcessModel other) {
        this.id = other.id;
        this.arrival = other.arrival;
        this.burst = other.burst;
        this.remaining = other.remaining;
        this.priority = other.priority;
        this.startTime = other.startTime;
        this.completionTime = other.completionTime;
        this.color = other.color;
    }

    private Color generateColor() {
        // Generate bright saturated colors
        double hue = Math.random() * 360;
        return Color.hsb(hue, 0.7, 0.9);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getArrival() {
        return arrival;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public int getBurst() {
        return burst;
    }

    public void setBurst(int burst) {
        this.burst = burst;
        this.remaining = burst;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        if (this.startTime == null) {
            this.startTime = startTime;
        }
    }

    public Integer getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Integer completionTime) {
        this.completionTime = completionTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getWaitingTime() {
        if (completionTime == null) return 0;
        return completionTime - arrival - burst;
    }

    public int getTurnaroundTime() {
        if (completionTime == null) return 0;
        return completionTime - arrival;
    }

    public void reset() {
        this.remaining = this.burst;
        this.startTime = null;
        this.completionTime = null;
    }

    @Override
    public String toString() {
        return id;
    }
}
