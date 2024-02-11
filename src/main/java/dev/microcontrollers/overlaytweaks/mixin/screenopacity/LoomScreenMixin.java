package dev.microcontrollers.overlaytweaks.mixin.screenopacity;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.microcontrollers.overlaytweaks.InvScale;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LoomScreen.class)
public class LoomScreenMixin {
    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    private void containerOpacityStart(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, OverlayTweaksConfig.CONFIG.instance().containerTextureOpacity / 100F);
    }

    @Inject(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", shift = At.Shift.AFTER))
    private void containerOpacityEnd(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
    }

    /*
        The following method was taken from DulkirMod-Fabric under MPL 2.0
        https://github.com/inglettronald/DulkirMod-Fabric/blob/master/LICENSE
        No changes of note have been made other than adapting to this project
     */
    @Inject(method = "drawBanner", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onCreateMatrix(DrawContext context, RegistryEntry<BannerPattern> pattern, int x, int y, CallbackInfo ci, NbtCompound nbtCompound, NbtList nbtList, ItemStack itemStack, MatrixStack matrixStack) {
        matrixStack.scale(InvScale.getScale(), InvScale.getScale(), 1F);
    }

}
