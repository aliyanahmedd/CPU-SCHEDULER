package com.cpuscheduler.algorithm;

import com.cpuscheduler.model.Process;
import com.cpuscheduler.model.SchedulingResult;
import java.util.List;

/**
 * Interface for CPU scheduling algorithms.
 */
public interface SchedulingAlgorithm {
    
    /**
     * Returns the name of the algorithm.
     */
    String getName();
    
    /**
     * Schedules the given processes and returns the result.
     * @param processes List of processes to schedule
     * @return SchedulingResult containing Gantt chart and metrics
     */
    SchedulingResult schedule(List<Process> processes);
}
