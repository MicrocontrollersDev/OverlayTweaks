package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.microcontrollers.overlaytweaks.InvScale;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static java.lang.Math.ceil;

@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow public int width;
    @Shadow
    public int height;

    @WrapWithCondition(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(IIIIII)V"))
    private boolean shouldRenderBackground(DrawContext instance, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        return OverlayTweaksConfig.CONFIG.instance().containerOpacity > 0;
    }

    @ModifyArgs(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(IIIIII)V"))
    private void changeContainerBackgroundOpacity(Args args) {
        args.set(4, withOpacity(args.get(4), Math.max(OverlayTweaksConfig.CONFIG.instance().containerOpacity / 100F - 16/255F, 0)));
        args.set(5, withOpacity(args.get(5), OverlayTweaksConfig.CONFIG.instance().containerOpacity / 100F));
    }

    @Unique
    private int withOpacity(int color, float opacity) {
        return (int) (opacity * 255) << 24 | (color & 0xFFFFFF);
    }

    /*
        The following methods were taken from DulkirMod-Fabric under MPL 2.0
        https://github.com/inglettronald/DulkirMod-Fabric/blob/master/LICENSE
        No changes of note have been made other than adapting to this project
     */

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/Screen;height:I", shift = At.Shift.AFTER))
    public void onInitAfterViewportSizeSet(MinecraftClient client, int width, int height, CallbackInfo ci) {
        this.width = (int) ceil((double) width / InvScale.getScale());
        this.height = (int) ceil((double) height / InvScale.getScale());
    }

    @Inject(method = "resize", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/Screen;height:I", shift = At.Shift.AFTER))
    public void onResizeAfterViewportSizeSet(MinecraftClient client, int width, int height, CallbackInfo ci) {
        this.width = (int) ceil((double) width / InvScale.getScale());
        this.height = (int) ceil((double) height / InvScale.getScale());
    }

}
