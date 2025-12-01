package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;

import java.util.*;

/**
 * Shortest Job First (SJF) scheduling algorithm.
 * Non-preemptive scheduling based on burst time.
 */
public class SjfScheduler implements Scheduler {

    @Override
    public String getName() {
        return "SJF";
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

            // Select process with shortest burst
            ProcessModel shortest = available.stream()
                    .min(Comparator.comparingInt(ProcessModel::getBurst)
                            .thenComparing(ProcessModel::getArrival)
                            .thenComparing(ProcessModel::getId))
                    .orElse(null);

            if (shortest != null) {
                shortest.setStartTime(currentTime);
                int finishTime = currentTime + shortest.getBurst();
                segments.add(new GanttSegment(shortest.getId(), currentTime, finishTime));
                
                shortest.setCompletionTime(finishTime);
                shortest.setRemaining(0);
                currentTime = finishTime;
                
                remaining.remove(shortest);
                completed.add(shortest);
            }
        }

        return segments;
    }
}
