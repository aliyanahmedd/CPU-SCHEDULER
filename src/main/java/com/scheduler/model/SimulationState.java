package com.scheduler.model;

import java.util.*;

/**
 * Holds the complete state of a scheduling simulation.
 */
public class SimulationState {
    private int currentTick;
    private List<GanttSegment> segments;
    private Map<String, ProcessModel> processMap;
    private List<String> readyQueueSnapshot;
    private String currentRunningProcess;

    public SimulationState() {
        this.currentTick = 0;
        this.segments = new ArrayList<>();
        this.processMap = new HashMap<>();
        this.readyQueueSnapshot = new ArrayList<>();
        this.currentRunningProcess = null;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public List<GanttSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<GanttSegment> segments) {
        this.segments = segments;
    }

    public Map<String, ProcessModel> getProcessMap() {
        return processMap;
    }

    public void setProcessMap(Map<String, ProcessModel> processMap) {
        this.processMap = processMap;
    }

    public List<String> getReadyQueueSnapshot() {
        return readyQueueSnapshot;
    }

    public void setReadyQueueSnapshot(List<String> readyQueueSnapshot) {
        this.readyQueueSnapshot = readyQueueSnapshot;
    }

    public String getCurrentRunningProcess() {
        return currentRunningProcess;
    }

    public void setCurrentRunningProcess(String currentRunningProcess) {
        this.currentRunningProcess = currentRunningProcess;
    }

    public int getTotalTime() {
        if (segments.isEmpty()) return 0;
        return segments.get(segments.size() - 1).getEnd();
    }

    public double getAverageWaitingTime() {
        if (processMap.isEmpty()) return 0.0;
        return processMap.values().stream()
                .mapToInt(ProcessModel::getWaitingTime)
                .average()
                .orElse(0.0);
    }

    public double getAverageTurnaroundTime() {
        if (processMap.isEmpty()) return 0.0;
        return processMap.values().stream()
                .mapToInt(ProcessModel::getTurnaroundTime)
                .average()
                .orElse(0.0);
    }
}
