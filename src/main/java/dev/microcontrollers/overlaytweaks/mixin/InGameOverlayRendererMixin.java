package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE;

/*
  Fire overlay height features were taken from Easeify with permission from Wyvest
  Only changes to variables have been changed
  https://github.com/Polyfrost/Easeify/blob/main/LICENSE
 */
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Unique
    private static boolean hasPushed = false;

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelWaterOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (OverlayTweaksConfig.removeWaterOverlay) ci.cancel();
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (OverlayTweaksConfig.removeFireOverlay && client.player != null && (client.player.isCreative() || client.player.hasStatusEffect(FIRE_RESISTANCE))) ci.cancel();
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"))
    private static void moveFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        hasPushed = true;
        matrices.push();
        matrices.translate(0, OverlayTweaksConfig.fireOverlayHeight, 0);
    }

    @Inject(method = "renderFireOverlay", at = @At("RETURN"))
    private static void resetFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (hasPushed) {
            hasPushed = false;
            matrices.pop();
        }
    }

}
