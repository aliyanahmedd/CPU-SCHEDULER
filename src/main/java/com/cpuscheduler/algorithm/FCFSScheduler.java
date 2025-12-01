package com.cpuscheduler.algorithm;

import com.cpuscheduler.model.GanttEntry;
import com.cpuscheduler.model.Process;
import com.cpuscheduler.model.SchedulingResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * First Come First Served (FCFS) scheduling algorithm.
 * Non-preemptive algorithm that processes jobs in arrival order.
 */
public class FCFSScheduler implements SchedulingAlgorithm {

    @Override
    public String getName() {
        return "First Come First Served (FCFS)";
    }

    @Override
    public SchedulingResult schedule(List<Process> originalProcesses) {
        if (originalProcesses == null || originalProcesses.isEmpty()) {
            return new SchedulingResult(getName(), new ArrayList<>(), new ArrayList<>());
        }

        // Create copies to avoid modifying original processes
        List<Process> processes = originalProcesses.stream()
                .map(Process::copy)
                .sorted(Comparator.comparingInt(Process::getArrivalTime)
                        .thenComparingInt(Process::getId))
                .collect(Collectors.toList());

        List<GanttEntry> ganttChart = new ArrayList<>();
        int currentTime = 0;

        for (Process process : processes) {
            // If process hasn't arrived yet, wait
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            // Record response time (first time process gets CPU)
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

            currentTime = endTime;
        }

        return new SchedulingResult(getName(), processes, ganttChart);
    }
}
