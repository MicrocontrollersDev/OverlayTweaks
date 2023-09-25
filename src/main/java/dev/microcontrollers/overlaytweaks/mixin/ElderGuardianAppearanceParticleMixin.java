package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.particle.ElderGuardianAppearanceParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElderGuardianAppearanceParticle.class)
public class ElderGuardianAppearanceParticleMixin {
    @Inject(method = "buildGeometry", at = @At("HEAD"), cancellable = true)
    private void removeElderGuardianJumpscare(VertexConsumer vertexConsumer, Camera camera, float tickDelta, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().removeElderGuardianJumpscare) ci.cancel();
    }

}
