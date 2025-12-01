package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;

import java.util.List;
import java.util.Map;

/**
 * Interface for CPU scheduling algorithms.
 */
public interface Scheduler {
    /**
     * Schedule the given processes and return the Gantt chart segments.
     * 
     * @param processes List of processes to schedule
     * @param params Additional parameters (e.g., quantum for RR)
     * @return List of Gantt segments representing the schedule
     */
    List<GanttSegment> schedule(List<ProcessModel> processes, Map<String, Object> params);
    
    /**
     * Get the name of this scheduling algorithm.
     * 
     * @return Algorithm name
     */
    String getName();
}
