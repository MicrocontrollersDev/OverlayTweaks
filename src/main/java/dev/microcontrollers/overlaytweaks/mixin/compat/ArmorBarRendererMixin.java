package dev.microcontrollers.overlaytweaks.mixin.compat;

import com.redlimerl.detailab.render.ArmorBarRenderer;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(ArmorBarRenderer.class)
public class ArmorBarRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"))
    private int moveDetailArmorBarRenderer(Window instance) {
        return instance.getScaledHeight() - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }
}
