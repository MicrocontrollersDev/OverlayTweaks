package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
    The following methods were taken from Easeify under LGPLV3
    https://github.com/Polyfrost/Easeify/blob/main/LICENSE
    No code has been changed except variables names
 */
@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {
    @ModifyExpressionValue(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;sin(F)F"))
    private float makeItemStatic(float value) {
        return OverlayTweaksConfig.CONFIG.instance().staticItems ? -1.0f : value;
    }

    @Inject(method = "getRenderedAmount", at = @At("HEAD"), cancellable = true)
    private void forceAmount(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (OverlayTweaksConfig.CONFIG.instance().unstackedItems) cir.setReturnValue(1);
    }

}
