package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.microcontrollers.overlaytweaks.InvScale;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = GameRenderer.class, priority = 1001)
public class GameRendererMixin {
    @ModifyExpressionValue(method = "getFov", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(DDD)D"))
    private double removeWaterFov(double original) {
        if (OverlayTweaksConfig.CONFIG.instance().removeWaterFov) return 1.0;
        else return original;
    }

    @ModifyReturnValue(method = "getNightVisionStrength", at = @At("RETURN"))
    private static float cleanerNightVision(float original) {
        assert MinecraftClient.getInstance().player != null;
        StatusEffectInstance statusEffectInstance = MinecraftClient.getInstance().player.getStatusEffect(StatusEffects.NIGHT_VISION);
        assert statusEffectInstance != null;
        if (OverlayTweaksConfig.CONFIG.instance().cleanerNightVision)
            return !statusEffectInstance.isDurationBelow(200) ? 1.0F : (float) statusEffectInstance.getDuration() / 200F;
        return original;
    }

    @WrapWithCondition(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public boolean disableScreenBobbing(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        return !OverlayTweaksConfig.CONFIG.instance().disableScreenBobbing;
    }

    @WrapWithCondition(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public boolean disableHandBobbing(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        if (OverlayTweaksConfig.CONFIG.instance().disableHandBobbing) return false;
        if (OverlayTweaksConfig.CONFIG.instance().disableMapBobbing) {
            ClientPlayerEntity entity = MinecraftClient.getInstance().player;
            if (entity != null) {
                ItemStack mainHand = entity.getMainHandStack();
                ItemStack offHand = entity.getOffHandStack();
                if (mainHand != null && mainHand.getItem() instanceof FilledMapItem) return false;
                return offHand == null || !(offHand.getItem() instanceof FilledMapItem);
            }
        }
        return true;
    }

    @WrapWithCondition(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public boolean disableHandDamageTilt(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        return !OverlayTweaksConfig.CONFIG.instance().disableHandDamage;
    }

    @Redirect(method = "renderHand", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z"))
    public boolean keepHand(GameOptions instance) {
        return !OverlayTweaksConfig.CONFIG.instance().keepHand && instance.hudHidden;
    }

    @WrapWithCondition(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public boolean disableScreenDamageTilt(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        return !OverlayTweaksConfig.CONFIG.instance().disableScreenDamage;
    }

    /*
        The following methods were taken from DulkirMod-Fabric under MPL 2.0
        https://github.com/inglettronald/DulkirMod-Fabric/blob/master/LICENSE
        No changes of note have been made other than adapting to this project
     */

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V"), index = 1)
    public int fixMouseX(int mouseX) {
        return (int) (mouseX / InvScale.getScale());
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/gui/DrawContext;IIF)V"), index = 2)
    public int fixMouseY(int mouseY) {
        return (int) (mouseY / InvScale.getScale());
    }

    // ignore the error about method params
    @Inject(method = "render", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.BEFORE, ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onScreenRenderPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci, float f, boolean bl, int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack, DrawContext drawContext) {
        drawContext.getMatrices().push();
        drawContext.getMatrices().scale(InvScale.getScale(), InvScale.getScale(), 1f);
    }

    // ignore the error about method params
    @Inject(method = "render", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", shift = At.Shift.AFTER, ordinal = 3), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onScreenRenderPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci, float f, boolean bl, int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack, DrawContext drawContext) {
        drawContext.getMatrices().pop();
    }

}
