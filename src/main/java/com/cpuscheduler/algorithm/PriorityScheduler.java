package com.cpuscheduler.algorithm;

import com.cpuscheduler.model.GanttEntry;
import com.cpuscheduler.model.Process;
import com.cpuscheduler.model.SchedulingResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Priority scheduling algorithm.
 * Non-preemptive algorithm that selects the process with highest priority (lower number = higher priority).
 */
public class PriorityScheduler implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "Priority Scheduling";
    }

    @Override
    public SchedulingResult schedule(List<Process> originalProcesses) {
        if (originalProcesses == null || originalProcesses.isEmpty()) {
            return new SchedulingResult(getName(), new ArrayList<>(), new ArrayList<>());
        }

        // Create copies to avoid modifying original processes
        List<Process> processes = originalProcesses.stream()
                .map(Process::copy)
                .collect(Collectors.toList());

        List<Process> completed = new ArrayList<>();
        List<GanttEntry> ganttChart = new ArrayList<>();
        int[] time = {0}; // Use array to allow modification in lambda

        while (completed.size() < processes.size()) {
            // Find available processes (arrived and not completed)
            final int currentTime = time[0];
            List<Process> available = processes.stream()
                    .filter(p -> p.getArrivalTime() <= currentTime && !completed.contains(p))
                    .sorted(Comparator.comparingInt(Process::getPriority)
                            .thenComparingInt(Process::getArrivalTime)
                            .thenComparingInt(Process::getId))
                    .collect(Collectors.toList());

            if (available.isEmpty()) {
                // No process available, advance time to next arrival
                int nextArrival = processes.stream()
                        .filter(p -> !completed.contains(p))
                        .mapToInt(Process::getArrivalTime)
                        .min()
                        .orElse(currentTime + 1);
                time[0] = nextArrival;
                continue;
            }

            // Select process with highest priority (lowest priority number)
            Process process = available.get(0);

            // Record response time
            process.setResponseTime(currentTime - process.getArrivalTime());

            int startTime = currentTime;
            int endTime = currentTime + process.getBurstTime();

            // Add Gantt entry
            ganttChart.add(new GanttEntry(process.getId(), process.getName(), startTime, endTime));

            // Update process metrics
            process.setCompletionTime(endTime);
            process.setTurnaroundTime(endTime - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            process.setRemainingTime(0);

            completed.add(process);
            time[0] = endTime;
        }

        return new SchedulingResult(getName(), processes, ganttChart);
    }
}
