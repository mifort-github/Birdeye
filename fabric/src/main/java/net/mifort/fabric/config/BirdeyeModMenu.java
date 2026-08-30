package net.mifort.fabric.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.mifort.config.BirdeyeConfigScreen;

public class BirdeyeModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BirdeyeConfigScreen::create;
    }
}
