package net.mifort.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.mifort.BirdeyeClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BirdeyeConfigScreen {

    public static Screen create(Screen parent) {
        BirdeyeConfig config = BirdeyeClient.CONFIG;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Map Animation"));

        builder.setSavingRunnable(config::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory camera = builder.getOrCreateCategory(Component.literal("Camera"));

        camera.addEntry(
                entryBuilder.startDoubleField(Component.literal("Animation Duration Mul"), config.ANIMATION_DURATION_MUL)
                        .setDefaultValue(0.5)
                        .setSaveConsumer(value -> config.ANIMATION_DURATION_MUL = value)
                        .build()
        );

        camera.addEntry(
                entryBuilder.startDoubleField(Component.literal("Animation Duration Min"), config.ANIMATION_DURATION_MIN)
                        .setDefaultValue(1)
                        .setSaveConsumer(value -> config.ANIMATION_DURATION_MIN = value)
                        .build()
        );

        camera.addEntry(
                entryBuilder.startDoubleField(Component.literal("Animation Duration Max"), config.ANIMATION_DURATION_MAX)
                        .setDefaultValue(3)
                        .setSaveConsumer(value -> config.ANIMATION_DURATION_MAX = value)
                        .build()
        );

        return builder.build();
    }
}
