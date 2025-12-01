package com.cpuscheduler;

import com.cpuscheduler.util.ScenarioIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ScenarioIO export/import functionality.
 */
class ScenarioIOTest {

    @TempDir
    Path tempDir;

    @Test
    void testExportAndImport() throws IOException {
        List<com.cpuscheduler.model.Process> originalProcesses = Arrays.asList(
            new com.cpuscheduler.model.Process(1, "P1", 0, 5, 2),
            new com.cpuscheduler.model.Process(2, "P2", 1, 3, 1),
            new com.cpuscheduler.model.Process(3, "P3", 2, 8, 3)
        );

        File file = tempDir.resolve("test-scenario.json").toFile();
        
        // Export
        ScenarioIO.exportScenario(originalProcesses, file);
        assertTrue(file.exists());
        
        // Import
        List<com.cpuscheduler.model.Process> importedProcesses = ScenarioIO.importScenario(file);
        
        assertEquals(originalProcesses.size(), importedProcesses.size());
        
        for (int i = 0; i < originalProcesses.size(); i++) {
            com.cpuscheduler.model.Process orig = originalProcesses.get(i);
            com.cpuscheduler.model.Process imp = importedProcesses.get(i);
            
            assertEquals(orig.getId(), imp.getId());
            assertEquals(orig.getName(), imp.getName());
            assertEquals(orig.getArrivalTime(), imp.getArrivalTime());
            assertEquals(orig.getBurstTime(), imp.getBurstTime());
            assertEquals(orig.getPriority(), imp.getPriority());
        }
    }

    @Test
    void testExportToString() {
        List<com.cpuscheduler.model.Process> processes = Arrays.asList(
            new com.cpuscheduler.model.Process(1, "P1", 0, 5, 2)
        );

        String json = ScenarioIO.exportToString(processes);
        
        assertNotNull(json);
        assertTrue(json.contains("P1"));
        assertTrue(json.contains("\"arrivalTime\""));
        assertTrue(json.contains("\"burstTime\""));
    }

    @Test
    void testImportFromString() {
        String json = """
            [
                {"id": 1, "name": "P1", "arrivalTime": 0, "burstTime": 5, "priority": 2},
                {"id": 2, "name": "P2", "arrivalTime": 1, "burstTime": 3, "priority": 1}
            ]
            """;

        List<com.cpuscheduler.model.Process> processes = ScenarioIO.importFromString(json);
        
        assertEquals(2, processes.size());
        assertEquals("P1", processes.get(0).getName());
        assertEquals("P2", processes.get(1).getName());
        assertEquals(5, processes.get(0).getBurstTime());
        assertEquals(3, processes.get(1).getBurstTime());
    }

    @Test
    void testEmptyScenario() throws IOException {
        File file = tempDir.resolve("empty-scenario.json").toFile();
        
        // Export empty list
        ScenarioIO.exportScenario(List.of(), file);
        
        // Import should return empty list
        List<com.cpuscheduler.model.Process> imported = ScenarioIO.importScenario(file);
        assertTrue(imported.isEmpty());
    }
}
