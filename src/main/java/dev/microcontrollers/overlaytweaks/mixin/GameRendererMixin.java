package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyExpressionValue(method = "getFov", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(DDD)D"))
    private double removeWaterFov(double original) {
        if (OverlayTweaksConfig.CONFIG.instance().removeWaterFov) return 1.0;
        else return original;
    }

    @Inject(method = "getNightVisionStrength", at = @At("TAIL"), cancellable = true)
    private static void cleanerNightVision(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        StatusEffectInstance statusEffectInstance = entity.getStatusEffect(StatusEffects.NIGHT_VISION);
        assert statusEffectInstance != null;
        if (OverlayTweaksConfig.CONFIG.instance().cleanerNightVision)
            cir.setReturnValue(!statusEffectInstance.isDurationBelow(200) ? 1.0F : (float) statusEffectInstance.getDuration() / 200F);
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

    @WrapWithCondition(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public boolean disableScreenDamageTilt(GameRenderer instance, MatrixStack matrices, float tickDelta) {
        return !OverlayTweaksConfig.CONFIG.instance().disableScreenDamage;
    }

}
