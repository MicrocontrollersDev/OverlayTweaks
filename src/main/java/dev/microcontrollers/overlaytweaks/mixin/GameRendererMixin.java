package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyExpressionValue(method = "getFov", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(DDD)D"))
    private double removeWaterFov(double original) {
        if (OverlayTweaksConfig.INSTANCE.getConfig().removeWaterFov) return 1.0;
        else return original;
    }

    @Inject(method = "getNightVisionStrength", at = @At("TAIL"), cancellable = true)
    private static void cleanerNightVision(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        StatusEffectInstance statusEffectInstance = entity.getStatusEffect(StatusEffects.NIGHT_VISION);
        assert statusEffectInstance != null;
        if (OverlayTweaksConfig.INSTANCE.getConfig().cleanerNightVision)
            cir.setReturnValue(!statusEffectInstance.isDurationBelow(200) ? 1.0F : (float) statusEffectInstance.getDuration() / 200F);
    }

}
