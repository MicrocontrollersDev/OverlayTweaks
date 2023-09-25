package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @ModifyArgs(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void lowerShieldPosition(Args args) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        if (player.getMainHandStack().isOf(Items.SHIELD) && args.get(3).equals(Hand.MAIN_HAND)) {
            args.set(6, (float) args.get(6) - OverlayTweaksConfig.CONFIG.instance().customShieldHeight);
        }
        else if (player.getOffHandStack().isOf(Items.SHIELD) && args.get(3).equals(Hand.OFF_HAND)) {
            args.set(6, (float) args.get(6) - OverlayTweaksConfig.CONFIG.instance().customShieldHeight);
        }
    }

}
