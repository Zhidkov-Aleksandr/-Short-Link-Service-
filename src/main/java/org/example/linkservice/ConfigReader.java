package com.example.linkservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {
    private int maxLifetimeHours;
    private int maxTransitions;
    private static final int DEFAULT_MAX_LIFETIME_HOURS = 24;
    private static final int DEFAULT_MAX_TRANSITIONS = 100;
    public ConfigReader(String filePath) {
        this.maxLifetimeHours = DEFAULT_MAX_LIFETIME_HOURS;
        this.maxTransitions = DEFAULT_MAX_TRANSITIONS;
        loadConfig(filePath);
    }
    private void loadConfig(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        if (key.equalsIgnoreCase("MAX_LIFETIME_HOURS")) {
                            this.maxLifetimeHours = Integer.parseInt(value);
                        } else if (key.equalsIgnoreCase("MAX_TRANSITIONS")) {
                            this.maxTransitions = Integer.parseInt(value);
                        }
                    }
                }
            }
        } catch (IOException e) {}
    }
    public int getMaxLifetimeHours() {
        return maxLifetimeHours;
    }
    public int getMaxTransitions() {
        return maxTransitions;
    }
}
