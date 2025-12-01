package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Priority scheduling algorithm.
 */
class PrioritySchedulerTest {

    @Test
    void testPriorityScheduling() {
        PriorityScheduler scheduler = new PriorityScheduler();
        
        // Lower priority number = higher priority
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 4, 3),
            new ProcessModel("P2", 0, 3, 1),
            new ProcessModel("P3", 0, 2, 2)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        assertEquals(3, segments.size());
        
        // Expected order: P2 (priority 1), P3 (priority 2), P1 (priority 3)
        assertEquals("P2", segments.get(0).getPid());
        assertEquals("P3", segments.get(1).getPid());
        assertEquals("P1", segments.get(2).getPid());
    }

    @Test
    void testPriorityWithArrivalTimes() {
        PriorityScheduler scheduler = new PriorityScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 3, 2),
            new ProcessModel("P2", 2, 2, 1),
            new ProcessModel("P3", 4, 1, 3)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        assertFalse(segments.isEmpty());
        
        // P1 should start first (only one arrived)
        assertEquals("P1", segments.get(0).getPid());
        
        // After P1 completes, P2 should run (higher priority than P3)
        assertEquals("P2", segments.get(1).getPid());
    }

    @Test
    void testPrioritySamePriority() {
        PriorityScheduler scheduler = new PriorityScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 2, 1),
            new ProcessModel("P2", 0, 2, 1)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        assertEquals(2, segments.size());
        
        // With same priority, should follow arrival order and ID
        assertEquals("P1", segments.get(0).getPid());
        assertEquals("P2", segments.get(1).getPid());
    }
}
