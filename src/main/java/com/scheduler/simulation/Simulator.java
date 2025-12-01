package com.scheduler.simulation;

import com.scheduler.algorithms.Scheduler;
import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;
import com.scheduler.model.SimulationState;

import java.util.*;
import java.util.function.Consumer;

/**
 * Manages the simulation of CPU scheduling with tick-based stepping.
 */
public class Simulator {
    private SimulationState state;
    private List<ProcessModel> originalProcesses;
    private Scheduler scheduler;
    private Map<String, Object> schedulerParams;
    private Consumer<SimulationState> updateCallback;
    
    private List<TickState> tickStates;
    private int currentTickIndex;

    public Simulator() {
        this.state = new SimulationState();
        this.originalProcesses = new ArrayList<>();
        this.tickStates = new ArrayList<>();
        this.currentTickIndex = 0;
    }

    /**
     * Initialize the simulation with processes and a scheduling algorithm.
     */
    public void initialize(List<ProcessModel> processes, Scheduler scheduler, Map<String, Object> params) {
        this.originalProcesses = new ArrayList<>(processes);
        this.scheduler = scheduler;
        this.schedulerParams = params;
        
        // Reset all processes
        for (ProcessModel p : processes) {
            p.reset();
        }

        // Run the scheduling algorithm
        List<GanttSegment> segments = scheduler.schedule(processes, params);
        
        // Build process map with updated completion times
        Map<String, ProcessModel> processMap = new HashMap<>();
        for (ProcessModel p : processes) {
            processMap.put(p.getId(), p);
        }

        state.setSegments(segments);
        state.setProcessMap(processMap);
        state.setCurrentTick(0);

        // Build tick states for stepping
        buildTickStates();
        currentTickIndex = 0;
        
        if (currentTickIndex < tickStates.size()) {
            applyTickState(tickStates.get(currentTickIndex));
        }
    }

    /**
     * Build a timeline of states for each tick.
     */
    private void buildTickStates() {
        tickStates.clear();
        
        if (state.getSegments().isEmpty()) {
            return;
        }

        int totalTime = state.getTotalTime();
        
        for (int tick = 0; tick <= totalTime; tick++) {
            TickState tickState = new TickState();
            tickState.tick = tick;
            
            // Find current segment
            GanttSegment currentSegment = null;
            for (GanttSegment seg : state.getSegments()) {
                if (tick >= seg.getStart() && tick < seg.getEnd()) {
                    currentSegment = seg;
                    break;
                }
            }
            
            if (currentSegment != null && !currentSegment.isIdle()) {
                tickState.runningProcess = currentSegment.getPid();
            } else {
                tickState.runningProcess = null;
            }
            
            // Build ready queue at this tick
            tickState.readyQueue = buildReadyQueueAtTick(tick);
            
            tickStates.add(tickState);
        }
    }

    /**
     * Build the ready queue snapshot at a specific tick.
     */
    private List<String> buildReadyQueueAtTick(int tick) {
        List<String> queue = new ArrayList<>();
        
        // For simplicity, show processes that have arrived but not completed
        for (ProcessModel p : state.getProcessMap().values()) {
            if (p.getArrival() <= tick) {
                Integer completion = p.getCompletionTime();
                if (completion == null || completion > tick) {
                    // Check if currently running
                    boolean isRunning = false;
                    for (GanttSegment seg : state.getSegments()) {
                        if (seg.getPid().equals(p.getId()) && tick >= seg.getStart() && tick < seg.getEnd()) {
                            isRunning = true;
                            break;
                        }
                    }
                    if (!isRunning) {
                        queue.add(p.getId());
                    }
                }
            }
        }
        
        return queue;
    }

    /**
     * Apply a tick state to the simulation state.
     */
    private void applyTickState(TickState tickState) {
        state.setCurrentTick(tickState.tick);
        state.setCurrentRunningProcess(tickState.runningProcess);
        state.setReadyQueueSnapshot(tickState.readyQueue);
        
        if (updateCallback != null) {
            updateCallback.accept(state);
        }
    }

    /**
     * Step forward one tick.
     */
    public boolean stepForward() {
        if (currentTickIndex < tickStates.size() - 1) {
            currentTickIndex++;
            applyTickState(tickStates.get(currentTickIndex));
            return true;
        }
        return false;
    }

    /**
     * Step backward one tick.
     */
    public boolean stepBackward() {
        if (currentTickIndex > 0) {
            currentTickIndex--;
            applyTickState(tickStates.get(currentTickIndex));
            return true;
        }
        return false;
    }

    /**
     * Jump to a specific tick.
     */
    public void jumpToTick(int tick) {
        if (tick >= 0 && tick < tickStates.size()) {
            currentTickIndex = tick;
            applyTickState(tickStates.get(currentTickIndex));
        }
    }

    /**
     * Jump to start.
     */
    public void jumpToStart() {
        jumpToTick(0);
    }

    /**
     * Jump to end.
     */
    public void jumpToEnd() {
        if (!tickStates.isEmpty()) {
            jumpToTick(tickStates.size() - 1);
        }
    }

    /**
     * Check if at the end of simulation.
     */
    public boolean isAtEnd() {
        return currentTickIndex >= tickStates.size() - 1;
    }

    /**
     * Check if at the start of simulation.
     */
    public boolean isAtStart() {
        return currentTickIndex == 0;
    }

    /**
     * Reset the simulation to initial state.
     */
    public void reset() {
        if (!originalProcesses.isEmpty() && scheduler != null) {
            initialize(originalProcesses, scheduler, schedulerParams);
        }
    }

    /**
     * Set a callback to be invoked when simulation state changes.
     */
    public void setUpdateCallback(Consumer<SimulationState> callback) {
        this.updateCallback = callback;
    }

    public SimulationState getState() {
        return state;
    }

    /**
     * Internal class to store state at a specific tick.
     */
    private static class TickState {
        int tick;
        String runningProcess;
        List<String> readyQueue;
    }
}
