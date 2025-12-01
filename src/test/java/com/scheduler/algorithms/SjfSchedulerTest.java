package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SJF scheduling algorithm.
 */
class SjfSchedulerTest {

    @Test
    void testSjfScheduling() {
        SjfScheduler scheduler = new SjfScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 8),
            new ProcessModel("P2", 1, 4),
            new ProcessModel("P3", 2, 2),
            new ProcessModel("P4", 3, 1)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        // Expected order after P1 starts: P1 completes, then P4 (1), P3 (2), P2 (4)
        assertFalse(segments.isEmpty());
        
        // P1 should start at 0 since it's the first arrival
        assertEquals("P1", segments.get(0).getPid());
        assertEquals(0, segments.get(0).getStart());
        
        // After P1, shortest jobs should be selected
        // P4 (burst 1) should be next
        assertEquals("P4", segments.get(1).getPid());
    }

    @Test
    void testSjfWithSameBurst() {
        SjfScheduler scheduler = new SjfScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 3),
            new ProcessModel("P2", 0, 3),
            new ProcessModel("P3", 0, 3)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        assertEquals(3, segments.size());
        
        // With same burst times, should follow arrival order and then ID
        assertEquals("P1", segments.get(0).getPid());
    }

    @Test
    void testSjfWithIdleTime() {
        SjfScheduler scheduler = new SjfScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 2),
            new ProcessModel("P2", 5, 1)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        // Should have: P1, idle, P2
        assertEquals(3, segments.size());
        assertEquals("idle", segments.get(1).getPid());
    }
}
