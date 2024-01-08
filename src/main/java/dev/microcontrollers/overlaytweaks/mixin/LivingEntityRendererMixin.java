package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @WrapOperation(method = "getRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;getLayer(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer transparentEntityRenderLayer(EntityModel model, Identifier texture, Operation<RenderLayer> original) {
        // let's not set this unless we absolutely have to
        if ((OverlayTweaksConfig.CONFIG.instance().horseOpacity != 100  && texture.toString().contains("horse")) ||
                (OverlayTweaksConfig.CONFIG.instance().pigOpacity != 100 && texture.toString().contains("pig")) ||
                (OverlayTweaksConfig.CONFIG.instance().striderOpacity != 100 && texture.toString().contains("strider")) ||
                (OverlayTweaksConfig.CONFIG.instance().camelOpacity != 100 && texture.toString().contains("camel"))) {
//            if (!texture.toString().contains("villager"))
            return RenderLayer.getEntityTranslucent(texture);
        }
        return original.call(model, texture);
    }

    @ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void transparentRiddenEntity(Args args, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (livingEntity instanceof AbstractHorseEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().horseOpacity != 0) {
            args.set(7, OverlayTweaksConfig.CONFIG.instance().horseOpacity / 100F);
        } else if (livingEntity instanceof PigEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().pigOpacity != 0) {
            args.set(7, OverlayTweaksConfig.CONFIG.instance().pigOpacity / 100F);
        } else if (livingEntity instanceof StriderEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().striderOpacity != 0) {
            args.set(7, OverlayTweaksConfig.CONFIG.instance().striderOpacity / 100F);
        } else if (livingEntity instanceof CamelEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().camelOpacity != 0) {
            args.set(7, OverlayTweaksConfig.CONFIG.instance().camelOpacity / 100F);
        }
    }

    // if it's 0, let's just cancel the rendering. this will also help prevent translucency sorting issues
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void cancelRiddenEntity(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity instanceof AbstractHorseEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().horseOpacity == 0) {
            ci.cancel();
        } else if (livingEntity instanceof PigEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().pigOpacity == 0) {
            ci.cancel();
        } else if (livingEntity instanceof StriderEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().striderOpacity == 0) {
            ci.cancel();
        } else if (livingEntity instanceof CamelEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && OverlayTweaksConfig.CONFIG.instance().camelOpacity == 0) {
            ci.cancel();
        }
    }

}
