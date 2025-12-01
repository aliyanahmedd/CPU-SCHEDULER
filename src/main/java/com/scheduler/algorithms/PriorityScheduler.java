package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;

import java.util.*;

/**
 * Priority scheduling algorithm.
 * Non-preemptive scheduling based on priority (lower number = higher priority).
 */
public class PriorityScheduler implements Scheduler {

    @Override
    public String getName() {
        return "Priority";
    }

    @Override
    public List<GanttSegment> schedule(List<ProcessModel> processes, Map<String, Object> params) {
        List<GanttSegment> segments = new ArrayList<>();
        if (processes.isEmpty()) return segments;

        // Create copies
        List<ProcessModel> remaining = new ArrayList<>();
        for (ProcessModel p : processes) {
            remaining.add(new ProcessModel(p));
        }

        int currentTime = 0;
        List<ProcessModel> completed = new ArrayList<>();

        while (completed.size() < processes.size()) {
            // Find all arrived processes
            List<ProcessModel> available = new ArrayList<>();
            for (ProcessModel p : remaining) {
                if (p.getArrival() <= currentTime) {
                    available.add(p);
                }
            }

            if (available.isEmpty()) {
                // Idle time - jump to next arrival
                ProcessModel next = remaining.stream()
                        .min(Comparator.comparingInt(ProcessModel::getArrival))
                        .orElse(null);
                if (next != null) {
                    segments.add(new GanttSegment("idle", currentTime, next.getArrival()));
                    currentTime = next.getArrival();
                }
                continue;
            }

            // Select process with highest priority (lowest priority number)
            ProcessModel highestPriority = available.stream()
                    .min(Comparator.comparingInt(ProcessModel::getPriority)
                            .thenComparing(ProcessModel::getArrival)
                            .thenComparing(ProcessModel::getId))
                    .orElse(null);

            if (highestPriority != null) {
                highestPriority.setStartTime(currentTime);
                int finishTime = currentTime + highestPriority.getBurst();
                segments.add(new GanttSegment(highestPriority.getId(), currentTime, finishTime));
                
                highestPriority.setCompletionTime(finishTime);
                highestPriority.setRemaining(0);
                currentTime = finishTime;
                
                remaining.remove(highestPriority);
                completed.add(highestPriority);
            }
        }

        return segments;
    }
}
