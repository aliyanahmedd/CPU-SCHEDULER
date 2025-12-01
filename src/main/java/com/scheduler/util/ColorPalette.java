package com.scheduler.util;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility for generating color palettes.
 */
public class ColorPalette {
    private static final List<Color> PALETTE = new ArrayList<>();
    private static int colorIndex = 0;

    static {
        // Modern, saturated colors
        PALETTE.add(Color.web("#FF6B6B")); // Red
        PALETTE.add(Color.web("#4ECDC4")); // Teal
        PALETTE.add(Color.web("#45B7D1")); // Blue
        PALETTE.add(Color.web("#FFA07A")); // Orange
        PALETTE.add(Color.web("#98D8C8")); // Mint
        PALETTE.add(Color.web("#F7DC6F")); // Yellow
        PALETTE.add(Color.web("#BB8FCE")); // Purple
        PALETTE.add(Color.web("#85C1E2")); // Sky Blue
        PALETTE.add(Color.web("#F8B739")); // Gold
        PALETTE.add(Color.web("#52B788")); // Green
    }

    /**
     * Get the next color from the palette.
     */
    public static Color getNextColor() {
        Color color = PALETTE.get(colorIndex % PALETTE.size());
        colorIndex++;
        return color;
    }

    /**
     * Reset the color index.
     */
    public static void reset() {
        colorIndex = 0;
    }
}
