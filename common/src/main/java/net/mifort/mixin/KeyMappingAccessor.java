package net.mifort.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {

    @Accessor("ALL")
    static Map<String, KeyMapping> birdeye$getAll() {
        throw new AssertionError();
    }

    @Accessor("key")
    InputConstants.Key birdeye$getKey();

    @Accessor("clickCount")
    int birdeye$getClickCount();

    @Accessor("clickCount")
    void birdeye$setClickCount(int clickCount);
}