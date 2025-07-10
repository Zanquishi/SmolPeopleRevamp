package com.teleboom.smolpeople;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class SmolPeopleConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("smolpeople.json");
    
    private static SmolPeopleConfig instance;
    
    public float scaleFactor = 0.5f;
    public boolean enabled = true;
    
    public static SmolPeopleConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }
    
    private static SmolPeopleConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
                return GSON.fromJson(reader, SmolPeopleConfig.class);
            } catch (Exception e) {
                System.err.println("Failed to load SmolPeople config: " + e.getMessage());
            }
        }
        return new SmolPeopleConfig();
    }
    
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            System.err.println("Failed to save SmolPeople config: " + e.getMessage());
        }
    }
    
    public float getScaleFactor() {
        return enabled ? scaleFactor : 1.0f;
    }
}
