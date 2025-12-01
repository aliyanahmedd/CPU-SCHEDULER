package com.scheduler.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.scheduler.model.ProcessModel;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for importing and exporting process configurations as JSON.
 */
public class JsonIO {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Export processes to JSON file.
     */
    public static void exportProcesses(List<ProcessModel> processes, File file) throws IOException {
        JsonArray jsonArray = new JsonArray();
        
        for (ProcessModel p : processes) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", p.getId());
            obj.addProperty("arrival", p.getArrival());
            obj.addProperty("burst", p.getBurst());
            obj.addProperty("priority", p.getPriority());
            
            // Export color as hex
            Color color = p.getColor();
            String colorHex = String.format("#%02X%02X%02X",
                    (int)(color.getRed() * 255),
                    (int)(color.getGreen() * 255),
                    (int)(color.getBlue() * 255));
            obj.addProperty("color", colorHex);
            
            jsonArray.add(obj);
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(jsonArray, writer);
        }
    }

    /**
     * Import processes from JSON file.
     */
    public static List<ProcessModel> importProcesses(File file) throws IOException {
        List<ProcessModel> processes = new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                
                String id = obj.get("id").getAsString();
                int arrival = obj.get("arrival").getAsInt();
                int burst = obj.get("burst").getAsInt();
                int priority = obj.has("priority") ? obj.get("priority").getAsInt() : 0;
                
                ProcessModel process = new ProcessModel(id, arrival, burst, priority);
                
                // Import color if present
                if (obj.has("color")) {
                    String colorHex = obj.get("color").getAsString();
                    Color color = Color.web(colorHex);
                    process.setColor(color);
                }
                
                processes.add(process);
            }
        }

        return processes;
    }
}
