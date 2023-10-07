package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.microcontrollers.overlaytweaks.InvScale;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/*
    The following class was taken from DulkirMod-Fabric under MPL 2.0
    https://github.com/inglettronald/DulkirMod-Fabric/blob/master/LICENSE
    No changes of note have been made other than adapting to this project
 */
@Mixin(Mouse.class)
public class MouseMixin {
    @ModifyExpressionValue(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"))
    public int onMouseButtonWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InvScale.getScale());
    }

    @ModifyExpressionValue(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"))
    public int onMouseButtonHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InvScale.getScale());
    }

    @ModifyExpressionValue(method = "onCursorPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"))
    public int onCursorPosWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InvScale.getScale());
    }

    @ModifyExpressionValue(method = "onCursorPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"))
    public int onCursorPosHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InvScale.getScale());
    }

    @ModifyExpressionValue(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledWidth()I"))
    public int onMouseScrollWidth(int originalScaledWidth) {
        return (int) (originalScaledWidth / InvScale.getScale());
    }

    @ModifyExpressionValue(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getScaledHeight()I"))
    public int onMouseScrollHeight(int originalScaledHeight) {
        return (int) (originalScaledHeight / InvScale.getScale());
    }

}
