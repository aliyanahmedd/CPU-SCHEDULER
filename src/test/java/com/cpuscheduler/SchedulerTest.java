package com.cpuscheduler;

import com.cpuscheduler.algorithm.FCFSScheduler;
import com.cpuscheduler.algorithm.PriorityScheduler;
import com.cpuscheduler.algorithm.RoundRobinScheduler;
import com.cpuscheduler.algorithm.SJFScheduler;
import com.cpuscheduler.model.GanttEntry;
import com.cpuscheduler.model.SchedulingResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CPU scheduling algorithms.
 */
class SchedulerTest {

    @Test
    void testFCFSScheduler() {
        List<com.cpuscheduler.model.Process> processes = Arrays.asList(
            new com.cpuscheduler.model.Process(1, "P1", 0, 5),
            new com.cpuscheduler.model.Process(2, "P2", 1, 3),
            new com.cpuscheduler.model.Process(3, "P3", 2, 8)
        );

        FCFSScheduler scheduler = new FCFSScheduler();
        SchedulingResult result = scheduler.schedule(processes);

        assertNotNull(result);
        assertEquals(3, result.getGanttChart().size());
        assertEquals("First Come First Served (FCFS)", result.getAlgorithmName());

        // Verify order: P1, P2, P3
        List<GanttEntry> gantt = result.getGanttChart();
        assertEquals("P1", gantt.get(0).getProcessName());
        assertEquals("P2", gantt.get(1).getProcessName());
        assertEquals("P3", gantt.get(2).getProcessName());

        // Verify timing: P1: 0-5, P2: 5-8, P3: 8-16
        assertEquals(0, gantt.get(0).getStartTime());
        assertEquals(5, gantt.get(0).getEndTime());
        assertEquals(5, gantt.get(1).getStartTime());
        assertEquals(8, gantt.get(1).getEndTime());
        assertEquals(8, gantt.get(2).getStartTime());
        assertEquals(16, gantt.get(2).getEndTime());
    }

    @Test
    void testSJFScheduler() {
        List<com.cpuscheduler.model.Process> processes = Arrays.asList(
            new com.cpuscheduler.model.Process(1, "P1", 0, 5),
            new com.cpuscheduler.model.Process(2, "P2", 0, 3),
            new com.cpuscheduler.model.Process(3, "P3", 0, 8)
        );

        SJFScheduler scheduler = new SJFScheduler();
        SchedulingResult result = scheduler.schedule(processes);

        assertNotNull(result);
        assertEquals(3, result.getGanttChart().size());

        // Verify order: P2 (shortest), P1, P3 (longest)
        List<GanttEntry> gantt = result.getGanttChart();
        assertEquals("P2", gantt.get(0).getProcessName());
        assertEquals("P1", gantt.get(1).getProcessName());
        assertEquals("P3", gantt.get(2).getProcessName());
    }

    @Test
    void testPriorityScheduler() {
        List<com.cpuscheduler.model.Process> processes = Arrays.asList(
            new com.cpuscheduler.model.Process(1, "P1", 0, 5, 3),
            new com.cpuscheduler.model.Process(2, "P2", 0, 3, 1),  // Highest priority
            new com.cpuscheduler.model.Process(3, "P3", 0, 8, 2)
        );

        PriorityScheduler scheduler = new PriorityScheduler();
        SchedulingResult result = scheduler.schedule(processes);

        assertNotNull(result);
        assertEquals(3, result.getGanttChart().size());

        // Verify order: P2 (priority 1), P3 (priority 2), P1 (priority 3)
        List<GanttEntry> gantt = result.getGanttChart();
        assertEquals("P2", gantt.get(0).getProcessName());
        assertEquals("P3", gantt.get(1).getProcessName());
        assertEquals("P1", gantt.get(2).getProcessName());
    }

    @Test
    void testRoundRobinScheduler() {
        List<com.cpuscheduler.model.Process> processes = Arrays.asList(
            new com.cpuscheduler.model.Process(1, "P1", 0, 4),
            new com.cpuscheduler.model.Process(2, "P2", 0, 3)
        );

        RoundRobinScheduler scheduler = new RoundRobinScheduler(2);
        SchedulingResult result = scheduler.schedule(processes);

        assertNotNull(result);
        assertEquals("Round Robin (TQ=2)", result.getAlgorithmName());
        assertEquals(2, scheduler.getTimeQuantum());

        // With TQ=2: P1(2), P2(2), P1(2), P2(1)
        List<GanttEntry> gantt = result.getGanttChart();
        assertTrue(gantt.size() >= 3); // At least 3 context switches
    }

    @Test
    void testEmptyProcessList() {
        FCFSScheduler scheduler = new FCFSScheduler();
        SchedulingResult result = scheduler.schedule(List.of());

        assertNotNull(result);
        assertTrue(result.getGanttChart().isEmpty());
        assertEquals(0, result.getTotalTime());
    }

    @Test
    void testMetricsCalculation() {
        List<com.cpuscheduler.model.Process> processes = Arrays.asList(
            new com.cpuscheduler.model.Process(1, "P1", 0, 3),
            new com.cpuscheduler.model.Process(2, "P2", 0, 6)
        );

        FCFSScheduler scheduler = new FCFSScheduler();
        SchedulingResult result = scheduler.schedule(processes);

        assertTrue(result.getAverageWaitingTime() >= 0);
        assertTrue(result.getAverageTurnaroundTime() > 0);
        assertTrue(result.getThroughput() > 0);
        assertTrue(result.getCpuUtilization() > 0);
    }

    @Test
    void testRoundRobinInvalidQuantum() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RoundRobinScheduler(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new RoundRobinScheduler(-1);
        });
    }

    @Test
    void testProcessCopy() {
        com.cpuscheduler.model.Process original = new com.cpuscheduler.model.Process(1, "P1", 0, 5, 2);
        original.setWaitingTime(10);
        original.setTurnaroundTime(15);
        
        com.cpuscheduler.model.Process copy = original.copy();
        
        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getArrivalTime(), copy.getArrivalTime());
        assertEquals(original.getBurstTime(), copy.getBurstTime());
        assertEquals(original.getPriority(), copy.getPriority());
        assertEquals(original.getWaitingTime(), copy.getWaitingTime());
        assertEquals(original.getTurnaroundTime(), copy.getTurnaroundTime());
        
        // Verify independence
        copy.setName("P2");
        assertNotEquals(original.getName(), copy.getName());
    }

    @Test
    void testProcessReset() {
        com.cpuscheduler.model.Process process = new com.cpuscheduler.model.Process(1, "P1", 0, 5, 2);
        process.setWaitingTime(10);
        process.setTurnaroundTime(15);
        process.setRemainingTime(3);
        process.setResponseTime(5);
        
        process.reset();
        
        assertEquals(5, process.getRemainingTime()); // Back to burst time
        assertEquals(0, process.getWaitingTime());
        assertEquals(0, process.getTurnaroundTime());
        assertEquals(-1, process.getResponseTime());
    }

    @Test
    void testGanttEntry() {
        GanttEntry entry = new GanttEntry(1, "P1", 0, 5);
        
        assertEquals(1, entry.getProcessId());
        assertEquals("P1", entry.getProcessName());
        assertEquals(0, entry.getStartTime());
        assertEquals(5, entry.getEndTime());
        assertEquals(5, entry.getDuration());
    }
}
