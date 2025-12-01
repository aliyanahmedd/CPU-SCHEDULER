package com.cpuscheduler.algorithm;

import com.cpuscheduler.model.GanttEntry;
import com.cpuscheduler.model.Process;
import com.cpuscheduler.model.SchedulingResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Round Robin scheduling algorithm.
 * Preemptive algorithm that gives each process a fixed time quantum.
 */
public class RoundRobinScheduler implements SchedulingAlgorithm {
    private final int timeQuantum;

    public RoundRobinScheduler(int timeQuantum) {
        if (timeQuantum <= 0) {
            throw new IllegalArgumentException("Time quantum must be positive");
        }
        this.timeQuantum = timeQuantum;
    }

    @Override
    public String getName() {
        return "Round Robin (TQ=" + timeQuantum + ")";
    }

    @Override
    public SchedulingResult schedule(List<Process> originalProcesses) {
        if (originalProcesses == null || originalProcesses.isEmpty()) {
            return new SchedulingResult(getName(), new ArrayList<>(), new ArrayList<>(), timeQuantum);
        }

        // Create copies to avoid modifying original processes
        List<Process> processes = originalProcesses.stream()
                .map(Process::copy)
                .sorted(Comparator.comparingInt(Process::getArrivalTime)
                        .thenComparingInt(Process::getId))
                .collect(Collectors.toList());

        // Reset remaining times
        processes.forEach(p -> p.setRemainingTime(p.getBurstTime()));

        List<GanttEntry> ganttChart = new ArrayList<>();
        Queue<Process> readyQueue = new LinkedList<>();
        Set<Process> inQueue = new HashSet<>();
        Set<Process> completed = new HashSet<>();
        int currentTime = 0;
        int processIndex = 0;

        // Add processes that arrive at time 0
        while (processIndex < processes.size() && processes.get(processIndex).getArrivalTime() <= currentTime) {
            Process p = processes.get(processIndex);
            readyQueue.add(p);
            inQueue.add(p);
            processIndex++;
        }

        while (completed.size() < processes.size()) {
            if (readyQueue.isEmpty()) {
                // No process ready, advance to next arrival
                if (processIndex < processes.size()) {
                    currentTime = processes.get(processIndex).getArrivalTime();
                    while (processIndex < processes.size() && 
                           processes.get(processIndex).getArrivalTime() <= currentTime) {
                        Process p = processes.get(processIndex);
                        if (!inQueue.contains(p) && !completed.contains(p)) {
                            readyQueue.add(p);
                            inQueue.add(p);
                        }
                        processIndex++;
                    }
                }
                continue;
            }

            Process process = readyQueue.poll();

            // Record response time (first time on CPU)
            if (process.getResponseTime() == -1) {
                process.setResponseTime(currentTime - process.getArrivalTime());
            }

            int startTime = currentTime;
            int executeTime = Math.min(timeQuantum, process.getRemainingTime());
            int endTime = currentTime + executeTime;

            // Add Gantt entry
            ganttChart.add(new GanttEntry(process.getId(), process.getName(), startTime, endTime));

            process.setRemainingTime(process.getRemainingTime() - executeTime);
            currentTime = endTime;

            // Add newly arrived processes to ready queue before re-adding current process
            while (processIndex < processes.size() && 
                   processes.get(processIndex).getArrivalTime() <= currentTime) {
                Process p = processes.get(processIndex);
                if (!inQueue.contains(p) && !completed.contains(p)) {
                    readyQueue.add(p);
                    inQueue.add(p);
                }
                processIndex++;
            }

            if (process.getRemainingTime() > 0) {
                // Process not finished, add back to queue
                readyQueue.add(process);
            } else {
                // Process completed
                process.setCompletionTime(endTime);
                process.setTurnaroundTime(endTime - process.getArrivalTime());
                process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
                completed.add(process);
            }
        }

        return new SchedulingResult(getName(), processes, ganttChart, timeQuantum);
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }
}
