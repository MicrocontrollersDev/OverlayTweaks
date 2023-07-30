package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @ModifyConstant(method = "render", constant = @Constant(intValue = 553648127))
    private int tabOpacity(int opacity) {
        return (int) (opacity * OverlayTweaksConfig.INSTANCE.getConfig().tabPlayerListOpacity);
    }

}
