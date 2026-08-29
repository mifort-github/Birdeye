package net.mifort.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.mifort.BirdeyeClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BirdeyeConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Path CONFIG_DIR;

    public double ANIMATION_DURATION_MUL = 0.5;
    public double ANIMATION_DURATION_MIN = 1;
    public double ANIMATION_DURATION_MAX = 3;

    public static BirdeyeConfig load() {
        Path path = CONFIG_DIR.resolve(BirdeyeClient.MOD_ID + ".json");

        if (!Files.exists(path)) {
            BirdeyeConfig config = new BirdeyeConfig();
            config.save();
            return config;
        }

        try {
            String json = Files.readString(path);
            return GSON.fromJson(json, BirdeyeConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new BirdeyeConfig();
        }
    }

    public void save() {
        try {
            Files.writeString(CONFIG_DIR.resolve(BirdeyeClient.MOD_ID + ".json"), GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}