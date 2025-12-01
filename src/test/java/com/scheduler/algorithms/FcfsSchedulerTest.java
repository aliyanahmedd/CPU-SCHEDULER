package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FCFS scheduling algorithm.
 */
class FcfsSchedulerTest {

    @Test
    void testBasicFcfsScheduling() {
        FcfsScheduler scheduler = new FcfsScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 5, 2),
            new ProcessModel("P2", 2, 3, 1),
            new ProcessModel("P3", 4, 1, 3)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        // Expected: P1:0-5, P2:5-8, P3:8-9
        assertEquals(3, segments.size());
        
        assertEquals("P1", segments.get(0).getPid());
        assertEquals(0, segments.get(0).getStart());
        assertEquals(5, segments.get(0).getEnd());
        
        assertEquals("P2", segments.get(1).getPid());
        assertEquals(5, segments.get(1).getStart());
        assertEquals(8, segments.get(1).getEnd());
        
        assertEquals("P3", segments.get(2).getPid());
        assertEquals(8, segments.get(2).getStart());
        assertEquals(9, segments.get(2).getEnd());
    }

    @Test
    void testFcfsWithIdleTime() {
        FcfsScheduler scheduler = new FcfsScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 3),
            new ProcessModel("P2", 5, 2)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        // Expected: P1:0-3, idle:3-5, P2:5-7
        assertEquals(3, segments.size());
        
        assertEquals("P1", segments.get(0).getPid());
        assertEquals("idle", segments.get(1).getPid());
        assertEquals("P2", segments.get(2).getPid());
    }

    @Test
    void testEmptyProcessList() {
        FcfsScheduler scheduler = new FcfsScheduler();
        List<ProcessModel> processes = new ArrayList<>();
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        assertTrue(segments.isEmpty());
    }
}
