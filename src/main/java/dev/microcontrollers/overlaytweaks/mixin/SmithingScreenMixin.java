package dev.microcontrollers.overlaytweaks.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: FIGURE OUT WHY THIS DOESNT WORK
@Mixin(SmithingScreen.class)
public class SmithingScreenMixin {
    @Inject(method = "drawInvalidRecipeArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private void containerOpacityStart(DrawContext context, int x, int y, CallbackInfo ci) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, OverlayTweaksConfig.INSTANCE.getConfig().containerTextureOpacity / 100F);
    }

    @Inject(method = "drawInvalidRecipeArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", shift = At.Shift.AFTER))
    private void containerOpacityEnd(DrawContext context, int x, int y, CallbackInfo ci) {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    @Inject(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;updateTexture(Ljava/util/List;)V"))
    private void containerOpacityStartTick(CallbackInfo ci) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, OverlayTweaksConfig.INSTANCE.getConfig().containerTextureOpacity / 100F);
    }

    @Inject(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/CyclingSlotIcon;updateTexture(Ljava/util/List;)V", shift = At.Shift.AFTER))
    private void containerOpacityEndTick(CallbackInfo ci) {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }
}
