package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.client.HUDOverlayHandler;

@Pseudo
@Mixin(HUDOverlayHandler.class)
public class AppleSkinFixMixin {
    @Shadow private int foodIconsOffset;

    @Redirect(method = "onPreRender", at = @At(value = "FIELD", target = "Lsqueek/appleskin/client/HUDOverlayHandler;foodIconsOffset:I", opcode = Opcodes.GETFIELD))
    private int movePreAppleSkinUp(HUDOverlayHandler instance) {
        return this.foodIconsOffset + OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @Redirect(method = "onRender", at = @At(value = "FIELD", target = "Lsqueek/appleskin/client/HUDOverlayHandler;foodIconsOffset:I", opcode = Opcodes.GETFIELD))
    private int moveAppleSkinUp(HUDOverlayHandler instance) {
        return this.foodIconsOffset + OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

}
