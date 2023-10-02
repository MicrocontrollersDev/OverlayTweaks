package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.HeightLimitView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientWorld.Properties.class)
public class ClientWorldMixin {
    @ModifyReturnValue(method = "getSkyDarknessHeight", at = @At("RETURN"))
    private double getSkyDarknessHeight(double original, HeightLimitView world) {
        if (world != null && OverlayTweaksConfig.CONFIG.instance().cleanerSkyDarkness) return world.getBottomY();
        return original;
    }
}
