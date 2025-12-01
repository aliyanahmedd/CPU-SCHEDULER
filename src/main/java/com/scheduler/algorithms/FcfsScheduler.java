package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;

import java.util.*;

/**
 * First-Come, First-Served (FCFS) scheduling algorithm.
 * Non-preemptive scheduling based on arrival time.
 */
public class FcfsScheduler implements Scheduler {

    @Override
    public String getName() {
        return "FCFS";
    }

    @Override
    public List<GanttSegment> schedule(List<ProcessModel> processes, Map<String, Object> params) {
        List<GanttSegment> segments = new ArrayList<>();
        if (processes.isEmpty()) return segments;

        // Create copies to avoid modifying original
        List<ProcessModel> processList = new ArrayList<>();
        for (ProcessModel p : processes) {
            processList.add(new ProcessModel(p));
        }

        // Sort by arrival time, then by ID for deterministic behavior
        processList.sort(Comparator.comparingInt(ProcessModel::getArrival)
                .thenComparing(ProcessModel::getId));

        int currentTime = 0;
        
        for (ProcessModel process : processList) {
            // Add idle time if needed
            if (currentTime < process.getArrival()) {
                segments.add(new GanttSegment("idle", currentTime, process.getArrival()));
                currentTime = process.getArrival();
            }

            // Process execution
            process.setStartTime(currentTime);
            int finishTime = currentTime + process.getBurst();
            segments.add(new GanttSegment(process.getId(), currentTime, finishTime));
            
            process.setCompletionTime(finishTime);
            process.setRemaining(0);
            currentTime = finishTime;
        }

        return segments;
    }
}
