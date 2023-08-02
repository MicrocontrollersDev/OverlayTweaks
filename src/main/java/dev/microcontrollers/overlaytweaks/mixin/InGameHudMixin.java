package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    public float vignetteDarkness;
    @Shadow
    private Text title;
    @Shadow
    private Text subtitle;
    @Final
    @Shadow
    private static final Identifier ICONS = new Identifier("textures/gui/icons.png");
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "updateVignetteDarkness", at = @At("TAIL"))
    private void changeVignetteDarkness(Entity entity, CallbackInfo ci) {
        if (OverlayTweaksConfig.INSTANCE.getConfig().customVignetteDarkness)
            this.vignetteDarkness = OverlayTweaksConfig.INSTANCE.getConfig().customVignetteDarknessValue / 100;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V"), index = 2)
    private float changePumpkinOpacity(float opacity) {
        return OverlayTweaksConfig.INSTANCE.getConfig().pumpkinOpacity / 100F;
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"), index = 2)
    private int moveHotbarUp(int y) {
        return y - shouldMove();
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"), index = 2)
    private int moveHotbarUp2(int o) {
        return o - shouldMove();
    }

    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"), index = 2)
    private int moveMountJumpUp(int k) {
        return k - shouldMove();
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"), index = 2)
    private int moveExperienceUp(int l) {
        return l - shouldMove();
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"), index = 3)
    private int moveExperienceUp2(int l) {
        return l - shouldMove();
    }

    @ModifyArgs(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void moveTooltipUp(Args args) {
        args.set(1, (int) args.get(1) - shouldMove());
        args.set(3, (int) args.get(3) - shouldMove());
    }

    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"), index = 2)
    private int moveStatusUp(int s) {
        return s - shouldMove();
    }

    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"), index = 3)
    private int moveStatusUp2(int o) {
        return o - shouldMove();
    }

    @ModifyArg(method = "renderMountHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"), index = 2)
    private int moveMountHealthUp(int m) {
        return m - shouldMove();
    }

    @Unique
    private int shouldMove() {
        if (OverlayTweaksConfig.INSTANCE.getConfig().shouldMoveHotbar) return 2;
        return 0;
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void isInContainer(DrawContext context, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen != null && OverlayTweaksConfig.INSTANCE.getConfig().hideCrosshairInContainers)
            ci.cancel();
    }

    @ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean removeFirstPersonCheck(boolean original) {
        if (OverlayTweaksConfig.INSTANCE.getConfig().showCrosshairInPerspective) return true;
        else return original;
    }

    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private boolean removeBlending(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        return !OverlayTweaksConfig.INSTANCE.getConfig().removeCrosshairBlending;
    }

    @Redirect(method = "renderCrosshair", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugEnabled:Z", opcode = Opcodes.GETFIELD))
    private boolean cancelDebugCrosshair(GameOptions gameOptions) {
        if (OverlayTweaksConfig.INSTANCE.getConfig().useNormalCrosshair) return false;
        else if (OverlayTweaksConfig.INSTANCE.getConfig().useDebugCrosshair) return true;
        return gameOptions.debugEnabled;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V"))
    private void showCooldownOnDebug(DrawContext context, CallbackInfo ci) {
        if (!OverlayTweaksConfig.INSTANCE.getConfig().fixDebugCooldown) return;
        context.drawTexture(ICONS, (this.scaledWidth - 15) / 2, (this.scaledHeight - 15) / 2, 0, 0, 15, 15);
        if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
            assert this.client.player != null;
            float f = this.client.player.getAttackCooldownProgress(0.0F);
            boolean bl = false;
            if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && f >= 1.0F) {
                bl = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
                bl &= this.client.targetedEntity.isAlive();
            }
            int j = this.scaledHeight / 2 - 7 + 16;
            int k = this.scaledWidth / 2 - 8;
            if (bl) {
                context.drawTexture(ICONS, k, j, 68, 94, 16, 16);
            } else if (f < 1.0F) {
                int l = (int)(f * 17.0F);
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
                context.drawTexture(ICONS, k, j, 36, 94, 16, 4);
                context.drawTexture(ICONS, k, j, 52, 94, l, 4);
            }
            RenderSystem.defaultBlendFunc();
        }
    }

    /*
        The following methods were taken from Easeify under LGPLV3
        https://github.com/Polyfrost/Easeify/blob/main/LICENSE
        The code has been updated to 1.20 and with slight changes to variables
     */

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int clamp(int value) {
        if (OverlayTweaksConfig.INSTANCE.getConfig().disableTitles) {
            return 0;
        } else {
            return value;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void modifyTitle(DrawContext context, float tickDelta, CallbackInfo ci) {
        float titleScale = OverlayTweaksConfig.INSTANCE.getConfig().titleScale / 100;
        if (OverlayTweaksConfig.INSTANCE.getConfig().autoTitleScale) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(title) * 4.0F;
            if (width > context.getScaledWindowWidth()) {
                titleScale = (context.getScaledWindowWidth() / width) * OverlayTweaksConfig.INSTANCE.getConfig().titleScale / 100;
            }
        }
        context.getMatrices().scale(titleScale, titleScale, titleScale);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER))
    private void modifySubtitle(DrawContext context, float tickDelta, CallbackInfo ci) {
        float titleScale = OverlayTweaksConfig.INSTANCE.getConfig().titleScale / 100;
        if (OverlayTweaksConfig.INSTANCE.getConfig().autoTitleScale) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(subtitle) * 2.0F;
            if (width > context.getScaledWindowWidth()) {
                titleScale = (context.getScaledWindowWidth() / width) * OverlayTweaksConfig.INSTANCE.getConfig().titleScale / 100;
            }
        }
        context.getMatrices().scale(titleScale, titleScale, titleScale);
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 255, ordinal = 3))
    private int modifyOpacity(int constant) {
        return (int) (OverlayTweaksConfig.INSTANCE.getConfig().titleOpacity / 100 * 255);
    }

}
