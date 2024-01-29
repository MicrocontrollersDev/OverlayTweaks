package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/*
    The following methods were taken from Easeify under LGPLV3
    https://github.com/Polyfrost/Easeify/blob/main/LICENSE
    Code is mostly the same with name changes and better mixin compatability
 */
@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {
    @ModifyExpressionValue(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"))
    private float makeItemStatic(float value) {
        return OverlayTweaksConfig.CONFIG.instance().staticItems ? -1.0f : value;
    }

    @ModifyReturnValue(method = "getRenderedAmount", at = @At("RETURN"))
    private int forceStackAmount(int original) {
        if (OverlayTweaksConfig.CONFIG.instance().unstackedItems) return 1;
        return original;
    }

}
