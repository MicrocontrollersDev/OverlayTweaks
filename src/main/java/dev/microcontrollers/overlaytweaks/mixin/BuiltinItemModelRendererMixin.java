package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/*
    This code was taken and modified from Show Me Your Skin! under the MIT license https://github.com/enjarai/show-me-your-skin/blob/master/LICENSE
    It has been modified to not alter opacity for shields in containers and as items and only in first person, as well as to add other features
 */
@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinItemModelRendererMixin {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ShieldEntityModel;getLayer(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;", ordinal = 0))
    private RenderLayer enableShieldTransparency(ShieldEntityModel model, Identifier texture, Operation<RenderLayer> original) {
        // Might as well force this to be enabled since it doesn't seem to cause any issues.
        return RenderLayer.getEntityTranslucent(texture);
    }

    // We need to get the parent method parameters for mode, so we need to use @ModifyArgs.
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void changeShieldTransparency(Args args, ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        float cooldown = player.getItemCooldownManager().getCooldownProgress(Items.SHIELD, 0);
        // We want this to only change shields that are being held so it doesn't affect containers/dropped items.
        if (mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || mode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND) {
            args.set(7, OverlayTweaksConfig.CONFIG.instance().customShieldOpacity / 100);
            if (!OverlayTweaksConfig.CONFIG.instance().colorShieldCooldown) return;
            if (cooldown <= 1.0F && cooldown > 0.75F) {
                args.set(5, 0F);
                args.set(6, 0F);
            }
            if (cooldown <= 0.75F && cooldown > 0.35F) {
                args.set(5, 0.37F);
                args.set(6, 0.2F);
            }
            if (cooldown <= 0.35F && cooldown > 0F) {
                args.set(6, 0F);
            }
        }
    }

}
