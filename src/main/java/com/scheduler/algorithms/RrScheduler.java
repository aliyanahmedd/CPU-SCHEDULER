package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;

import java.util.*;

/**
 * Round-Robin (RR) scheduling algorithm.
 * Preemptive scheduling with a time quantum.
 */
public class RrScheduler implements Scheduler {

    @Override
    public String getName() {
        return "Round-Robin";
    }

    @Override
    public List<GanttSegment> schedule(List<ProcessModel> processes, Map<String, Object> params) {
        List<GanttSegment> segments = new ArrayList<>();
        if (processes.isEmpty()) return segments;

        int quantum = params != null && params.containsKey("quantum") 
                ? (Integer) params.get("quantum") 
                : 2;

        // Create copies
        Map<String, ProcessModel> processMap = new HashMap<>();
        for (ProcessModel p : processes) {
            ProcessModel copy = new ProcessModel(p);
            processMap.put(copy.getId(), copy);
        }

        Queue<ProcessModel> readyQueue = new LinkedList<>();
        List<ProcessModel> sortedProcesses = new ArrayList<>(processMap.values());
        sortedProcesses.sort(Comparator.comparingInt(ProcessModel::getArrival)
                .thenComparing(ProcessModel::getId));

        int currentTime = 0;
        int processIndex = 0;

        // Add initially arrived processes
        while (processIndex < sortedProcesses.size() 
                && sortedProcesses.get(processIndex).getArrival() <= currentTime) {
            readyQueue.offer(sortedProcesses.get(processIndex));
            processIndex++;
        }

        while (!readyQueue.isEmpty() || processIndex < sortedProcesses.size()) {
            if (readyQueue.isEmpty()) {
                // Idle time
                ProcessModel nextProcess = sortedProcesses.get(processIndex);
                segments.add(new GanttSegment("idle", currentTime, nextProcess.getArrival()));
                currentTime = nextProcess.getArrival();
                readyQueue.offer(nextProcess);
                processIndex++;
                continue;
            }

            ProcessModel current = readyQueue.poll();
            int executionTime = Math.min(quantum, current.getRemaining());
            
            current.setStartTime(currentTime);
            segments.add(new GanttSegment(current.getId(), currentTime, currentTime + executionTime));
            
            current.setRemaining(current.getRemaining() - executionTime);
            currentTime += executionTime;

            // Add newly arrived processes to queue
            List<ProcessModel> newArrivals = new ArrayList<>();
            while (processIndex < sortedProcesses.size() 
                    && sortedProcesses.get(processIndex).getArrival() <= currentTime) {
                newArrivals.add(sortedProcesses.get(processIndex));
                processIndex++;
            }

            // Add new arrivals before re-adding current process (if not finished)
            for (ProcessModel p : newArrivals) {
                readyQueue.offer(p);
            }

            if (current.getRemaining() > 0) {
                readyQueue.offer(current);
            } else {
                current.setCompletionTime(currentTime);
            }
        }

        return segments;
    }
}
