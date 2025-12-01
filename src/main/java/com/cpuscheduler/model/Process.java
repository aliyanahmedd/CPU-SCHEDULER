package com.cpuscheduler.model;

import javafx.beans.property.*;

/**
 * Represents a process in the CPU scheduler.
 * Uses JavaFX properties for binding to UI components.
 */
public class Process {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty arrivalTime;
    private final IntegerProperty burstTime;
    private final IntegerProperty priority;
    private final IntegerProperty remainingTime;
    private final IntegerProperty waitingTime;
    private final IntegerProperty turnaroundTime;
    private final IntegerProperty completionTime;
    private final IntegerProperty responseTime;

    public Process(int id, String name, int arrivalTime, int burstTime, int priority) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
        this.burstTime = new SimpleIntegerProperty(burstTime);
        this.priority = new SimpleIntegerProperty(priority);
        this.remainingTime = new SimpleIntegerProperty(burstTime);
        this.waitingTime = new SimpleIntegerProperty(0);
        this.turnaroundTime = new SimpleIntegerProperty(0);
        this.completionTime = new SimpleIntegerProperty(0);
        this.responseTime = new SimpleIntegerProperty(-1);
    }

    public Process(int id, String name, int arrivalTime, int burstTime) {
        this(id, name, arrivalTime, burstTime, 0);
    }

    /**
     * Creates a copy of this process for simulation purposes.
     */
    public Process copy() {
        Process copy = new Process(getId(), getName(), getArrivalTime(), getBurstTime(), getPriority());
        copy.setRemainingTime(getRemainingTime());
        copy.setWaitingTime(getWaitingTime());
        copy.setTurnaroundTime(getTurnaroundTime());
        copy.setCompletionTime(getCompletionTime());
        copy.setResponseTime(getResponseTime());
        return copy;
    }

    /**
     * Resets calculated metrics for a new simulation run.
     */
    public void reset() {
        remainingTime.set(burstTime.get());
        waitingTime.set(0);
        turnaroundTime.set(0);
        completionTime.set(0);
        responseTime.set(-1);
    }

    // ID property
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Name property
    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    // Arrival Time property
    public int getArrivalTime() { return arrivalTime.get(); }
    public void setArrivalTime(int value) { arrivalTime.set(value); }
    public IntegerProperty arrivalTimeProperty() { return arrivalTime; }

    // Burst Time property
    public int getBurstTime() { return burstTime.get(); }
    public void setBurstTime(int value) { burstTime.set(value); }
    public IntegerProperty burstTimeProperty() { return burstTime; }

    // Priority property
    public int getPriority() { return priority.get(); }
    public void setPriority(int value) { priority.set(value); }
    public IntegerProperty priorityProperty() { return priority; }

    // Remaining Time property
    public int getRemainingTime() { return remainingTime.get(); }
    public void setRemainingTime(int value) { remainingTime.set(value); }
    public IntegerProperty remainingTimeProperty() { return remainingTime; }

    // Waiting Time property
    public int getWaitingTime() { return waitingTime.get(); }
    public void setWaitingTime(int value) { waitingTime.set(value); }
    public IntegerProperty waitingTimeProperty() { return waitingTime; }

    // Turnaround Time property
    public int getTurnaroundTime() { return turnaroundTime.get(); }
    public void setTurnaroundTime(int value) { turnaroundTime.set(value); }
    public IntegerProperty turnaroundTimeProperty() { return turnaroundTime; }

    // Completion Time property
    public int getCompletionTime() { return completionTime.get(); }
    public void setCompletionTime(int value) { completionTime.set(value); }
    public IntegerProperty completionTimeProperty() { return completionTime; }

    // Response Time property
    public int getResponseTime() { return responseTime.get(); }
    public void setResponseTime(int value) { responseTime.set(value); }
    public IntegerProperty responseTimeProperty() { return responseTime; }

    @Override
    public String toString() {
        return String.format("Process{id=%d, name='%s', arrival=%d, burst=%d, priority=%d}",
                getId(), getName(), getArrivalTime(), getBurstTime(), getPriority());
    }
}
