package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Screen.class)
public class ScreenMixin {
    @WrapWithCondition(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(IIIIII)V"))
    private boolean shouldRenderBackground(DrawContext instance, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        return OverlayTweaksConfig.INSTANCE.getConfig().containerOpacity > 0;
    }

    @ModifyArgs(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(IIIIII)V"))
    private void changeContainerOpacity(Args args) {
        args.set(4, withOpacity(args.get(4), Math.max(OverlayTweaksConfig.INSTANCE.getConfig().containerOpacity / 100F - 16/255F, 0)));
        args.set(5, withOpacity(args.get(5), OverlayTweaksConfig.INSTANCE.getConfig().containerOpacity / 100F));
    }

    @Unique
    private int withOpacity(int color, float opacity) {
        return (int) (opacity * 255) << 24 | (color & 0xFFFFFF);
    }

}
