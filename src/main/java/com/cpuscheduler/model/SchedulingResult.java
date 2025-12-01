package com.cpuscheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of running a scheduling algorithm.
 * Contains the Gantt chart entries and calculated metrics.
 */
public class SchedulingResult {
    private final List<GanttEntry> ganttChart;
    private final List<Process> processes;
    private final String algorithmName;
    private final int timeQuantum; // Used for Round Robin

    public SchedulingResult(String algorithmName, List<Process> processes, List<GanttEntry> ganttChart) {
        this(algorithmName, processes, ganttChart, 0);
    }

    public SchedulingResult(String algorithmName, List<Process> processes, List<GanttEntry> ganttChart, int timeQuantum) {
        this.algorithmName = algorithmName;
        this.processes = new ArrayList<>(processes);
        this.ganttChart = new ArrayList<>(ganttChart);
        this.timeQuantum = timeQuantum;
    }

    public List<GanttEntry> getGanttChart() {
        return new ArrayList<>(ganttChart);
    }

    public List<Process> getProcesses() {
        return new ArrayList<>(processes);
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    /**
     * Calculates average waiting time across all processes.
     */
    public double getAverageWaitingTime() {
        if (processes.isEmpty()) return 0;
        return processes.stream()
                .mapToInt(Process::getWaitingTime)
                .average()
                .orElse(0);
    }

    /**
     * Calculates average turnaround time across all processes.
     */
    public double getAverageTurnaroundTime() {
        if (processes.isEmpty()) return 0;
        return processes.stream()
                .mapToInt(Process::getTurnaroundTime)
                .average()
                .orElse(0);
    }

    /**
     * Calculates average response time across all processes.
     */
    public double getAverageResponseTime() {
        if (processes.isEmpty()) return 0;
        return processes.stream()
                .mapToInt(Process::getResponseTime)
                .average()
                .orElse(0);
    }

    /**
     * Returns the total completion time (makespan).
     */
    public int getTotalTime() {
        if (ganttChart.isEmpty()) return 0;
        return ganttChart.stream()
                .mapToInt(GanttEntry::getEndTime)
                .max()
                .orElse(0);
    }

    /**
     * Calculates CPU throughput (processes per unit time).
     */
    public double getThroughput() {
        int totalTime = getTotalTime();
        if (totalTime == 0) return 0;
        return (double) processes.size() / totalTime;
    }

    /**
     * Calculates CPU utilization percentage.
     */
    public double getCpuUtilization() {
        int totalTime = getTotalTime();
        if (totalTime == 0) return 0;
        int busyTime = ganttChart.stream()
                .mapToInt(GanttEntry::getDuration)
                .sum();
        return (double) busyTime / totalTime * 100;
    }
}
