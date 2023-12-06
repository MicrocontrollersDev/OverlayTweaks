package dev.microcontrollers.overlaytweaks;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

/*
    The following class was taken from DulkirMod-Fabric under MPL 2.0
    https://github.com/inglettronald/DulkirMod-Fabric/blob/master/LICENSE
    No changes of note have been made other than adapting to this project
 */
public class InvScale {
    public static float getScale() { // TODO: Fix player model not showing in inventory
        if (MinecraftClient.getInstance().currentScreen instanceof HandledScreen<?>) {
            return OverlayTweaksConfig.CONFIG.instance().containerSize;
        }
        return 1F;
    }

}
