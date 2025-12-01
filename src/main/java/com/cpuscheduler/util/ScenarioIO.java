package com.cpuscheduler.util;

import com.cpuscheduler.model.Process;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for exporting and importing process scenarios as JSON.
 */
public class ScenarioIO {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Exports a list of processes to a JSON file.
     */
    public static void exportScenario(List<Process> processes, File file) throws IOException {
        List<ProcessData> data = new ArrayList<>();
        for (Process p : processes) {
            data.add(new ProcessData(p.getId(), p.getName(), p.getArrivalTime(), 
                                     p.getBurstTime(), p.getPriority()));
        }
        
        String json = gson.toJson(data);
        Files.writeString(file.toPath(), json);
    }

    /**
     * Imports a list of processes from a JSON file.
     */
    public static List<Process> importScenario(File file) throws IOException {
        String json = Files.readString(file.toPath());
        Type listType = new TypeToken<List<ProcessData>>(){}.getType();
        List<ProcessData> data = gson.fromJson(json, listType);
        
        List<Process> processes = new ArrayList<>();
        for (ProcessData pd : data) {
            processes.add(new Process(pd.id, pd.name, pd.arrivalTime, pd.burstTime, pd.priority));
        }
        return processes;
    }

    /**
     * Exports a list of processes to a JSON string.
     */
    public static String exportToString(List<Process> processes) {
        List<ProcessData> data = new ArrayList<>();
        for (Process p : processes) {
            data.add(new ProcessData(p.getId(), p.getName(), p.getArrivalTime(), 
                                     p.getBurstTime(), p.getPriority()));
        }
        return gson.toJson(data);
    }

    /**
     * Imports a list of processes from a JSON string.
     */
    public static List<Process> importFromString(String json) {
        Type listType = new TypeToken<List<ProcessData>>(){}.getType();
        List<ProcessData> data = gson.fromJson(json, listType);
        
        List<Process> processes = new ArrayList<>();
        for (ProcessData pd : data) {
            processes.add(new Process(pd.id, pd.name, pd.arrivalTime, pd.burstTime, pd.priority));
        }
        return processes;
    }

    /**
     * Simple data class for JSON serialization (without JavaFX properties).
     */
    private static class ProcessData {
        int id;
        String name;
        int arrivalTime;
        int burstTime;
        int priority;

        ProcessData(int id, String name, int arrivalTime, int burstTime, int priority) {
            this.id = id;
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priority = priority;
        }
    }
}
