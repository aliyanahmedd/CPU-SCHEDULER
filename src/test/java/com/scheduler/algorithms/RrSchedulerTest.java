package com.scheduler.algorithms;

import com.scheduler.model.GanttSegment;
import com.scheduler.model.ProcessModel;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Round-Robin scheduling algorithm.
 */
class RrSchedulerTest {

    @Test
    void testRoundRobinQuantum2() {
        RrScheduler scheduler = new RrScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 5),
            new ProcessModel("P2", 1, 3),
            new ProcessModel("P3", 2, 1)
        );
        
        Map<String, Object> params = new HashMap<>();
        params.put("quantum", 2);
        
        List<GanttSegment> segments = scheduler.schedule(processes, params);
        
        assertFalse(segments.isEmpty());
        
        // Verify all processes complete
        boolean p1Found = false, p2Found = false, p3Found = false;
        for (GanttSegment seg : segments) {
            if (seg.getPid().equals("P1")) p1Found = true;
            if (seg.getPid().equals("P2")) p2Found = true;
            if (seg.getPid().equals("P3")) p3Found = true;
        }
        
        assertTrue(p1Found && p2Found && p3Found, "All processes should be scheduled");
        
        // Verify total execution time equals sum of burst times
        int totalTime = segments.get(segments.size() - 1).getEnd();
        assertEquals(9, totalTime);
    }

    @Test
    void testRoundRobinQuantum1() {
        RrScheduler scheduler = new RrScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 3),
            new ProcessModel("P2", 0, 3)
        );
        
        Map<String, Object> params = new HashMap<>();
        params.put("quantum", 1);
        
        List<GanttSegment> segments = scheduler.schedule(processes, params);
        
        assertEquals(6, segments.size()); // Should alternate between P1 and P2
    }

    @Test
    void testDefaultQuantum() {
        RrScheduler scheduler = new RrScheduler();
        
        List<ProcessModel> processes = Arrays.asList(
            new ProcessModel("P1", 0, 4)
        );
        
        List<GanttSegment> segments = scheduler.schedule(processes, null);
        
        assertFalse(segments.isEmpty());
        assertEquals("P1", segments.get(0).getPid());
    }
}
