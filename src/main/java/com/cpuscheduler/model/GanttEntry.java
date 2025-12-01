package com.cpuscheduler.model;

/**
 * Represents a single execution block in the Gantt chart.
 * A GanttEntry tracks when a process runs on the CPU.
 */
public class GanttEntry {
    private final int processId;
    private final String processName;
    private final int startTime;
    private final int endTime;

    public GanttEntry(int processId, String processName, int startTime, int endTime) {
        this.processId = processId;
        this.processName = processName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getProcessId() {
        return processId;
    }

    public String getProcessName() {
        return processName;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return String.format("[%s: %d-%d]", processName, startTime, endTime);
    }
}
