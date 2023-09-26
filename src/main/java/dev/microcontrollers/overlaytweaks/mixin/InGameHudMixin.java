package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

// BetterF3 has a priority of 1100. This is to prevent a crash with cancelDebugCrosshair.
@Mixin(value = InGameHud.class, priority = 1200)
public class InGameHudMixin {
    @Shadow
    public float vignetteDarkness;
    @Shadow
    private Text title;
    @Shadow
    private Text subtitle;
    @Final
    @Shadow
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE;
    @Final
    @Shadow
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;
    @Final
    @Shadow
    private static Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Mutable
    @Final
    @Shadow
    private final DebugHud debugHud;
    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    public InGameHudMixin(DebugHud debugHud) {
        this.debugHud = debugHud;
    }


    @Inject(method = "updateVignetteDarkness", at = @At("TAIL"))
    private void changeVignetteDarkness(Entity entity, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().customVignetteDarkness)
            this.vignetteDarkness = OverlayTweaksConfig.CONFIG.instance().customVignetteDarknessValue / 100;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V"), index = 2)
    private float changePumpkinOpacity(float opacity) {
        return OverlayTweaksConfig.CONFIG.instance().pumpkinOpacity / 100F;
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void cancelTooltip(DrawContext context, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().removeItemTooltip) ci.cancel();
    }

    // these names are inspired by Patcher https://github.com/Sk1erLLC/Patcher
    @Unique
    private final Map<Enchantment, String> enchantmentIdToString = new HashMap<>() {{
        put(Enchantment.byRawId(0), "P");       // Protection
        put(Enchantment.byRawId(1), "FP");      // Fire Protection
        put(Enchantment.byRawId(2), "FF");      // Feather Falling
        put(Enchantment.byRawId(3), "BP");      // Blast Protection
        put(Enchantment.byRawId(4), "PP");      // Projectile Protection
        put(Enchantment.byRawId(5), "R");       // Respiration
        put(Enchantment.byRawId(6), "AA");      // Aqua Infinity
        put(Enchantment.byRawId(7), "T");       // Thorns
        put(Enchantment.byRawId(8), "DS");      // Depth Strider
        put(Enchantment.byRawId(9), "FW");      // Frost Walker
        put(Enchantment.byRawId(10), "CoB");    // Curse of Binding
        put(Enchantment.byRawId(11), "SS");     // Soul Speed
        put(Enchantment.byRawId(12), "SS");     // Swift Sneak
        put(Enchantment.byRawId(13), "SH");     // Sharpness
        put(Enchantment.byRawId(14), "SM");     // Smite
        put(Enchantment.byRawId(15), "BoA");    // Bane of Arthropods
        put(Enchantment.byRawId(16), "KB");     // Knockback
        put(Enchantment.byRawId(17), "FA");     // Fire Aspect
        put(Enchantment.byRawId(18), "L");      // Looting
        put(Enchantment.byRawId(19), "SE");     // Sweeping Edge
        put(Enchantment.byRawId(20), "EFF");    // Efficiency
        put(Enchantment.byRawId(21), "ST");     // Silk Touch
        put(Enchantment.byRawId(22), "UNB");    // Unbreaking
        put(Enchantment.byRawId(23), "FORT");   // Fortune
        put(Enchantment.byRawId(24), "POW");    // Power
        put(Enchantment.byRawId(25), "PUN");    // Punch
        put(Enchantment.byRawId(26), "F");      // Flame
        put(Enchantment.byRawId(27), "INF");    // Infinity
        put(Enchantment.byRawId(28), "LoS");    // Luck of the Sea
        put(Enchantment.byRawId(29), "LURE");   // Lure
        put(Enchantment.byRawId(30), "LOY");    // Loyalty
        put(Enchantment.byRawId(31), "IMP");    // Impaling
        put(Enchantment.byRawId(32), "RIP");    // Riptide
        put(Enchantment.byRawId(33), "CHAN");   // Channeling
        put(Enchantment.byRawId(34), "MS");     // Multishot
        put(Enchantment.byRawId(35), "QC");     // Quick Charge
        put(Enchantment.byRawId(36), "PIER");   // Piercing
        put(Enchantment.byRawId(37), "MEND");   // Mending
        put(Enchantment.byRawId(38), "CoV");    // Vanishing
    }};

    @Inject(method = "renderHotbar", at = @At(value = "HEAD"))
    private void drawItemDamage(float tickDelta, DrawContext context, CallbackInfo ci) {
        if (!OverlayTweaksConfig.CONFIG.instance().hotbarEnchantmentGlance && !OverlayTweaksConfig.CONFIG.instance().hotbarDamageGlance) return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return;
        StringBuilder enchantmentStringBuilder = new StringBuilder();
        double itemDamage = 0.0;
        String itemDamageStr = "";

        if ((stack.getItem() instanceof ToolItem item)) {
            itemDamage = item.getAttributeModifiers(stack, EquipmentSlot.MAINHAND).entries().stream()
                    .filter(entry -> entry.getValue().getOperation() == EntityAttributeModifier.Operation.ADDITION)
                    .filter(entry -> "Weapon modifier".equals(entry.getValue().getName()) || "Tool modifier".equals(entry.getValue().getName()))
                    .mapToDouble(entry -> entry.getValue().getValue() + 1)
                    .findFirst().orElse(0d);

            int enchantLvl = 0;
            for (NbtElement enchantmentData : stack.getEnchantments()) {
                if (enchantmentData instanceof NbtCompound enchantment) {
                    if ("minecraft:sharpness".equals(enchantment.getString("id"))) {
                        enchantLvl = enchantment.getShort("lvl");
                        break;  // exit loop when sharp is found
                    }
                }
            }

            // https://minecraft.wiki/w/Sharpness
            itemDamage += (enchantLvl > 0) ? (0.5 * (enchantLvl - 1) + 1.0) : 0;
        }

        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.get(stack);
        for (Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
            enchantmentStringBuilder.append(enchantmentIdToString.get(entry.getKey())).append(" ").append(entry.getValue()).append(" ");
        }

        String itemEnchantStr = enchantmentStringBuilder.toString().trim();
        if (itemEnchantStr.equals("P 0")) itemEnchantStr = ""; // compatibility with Hypixel Skyblock custom enchants

        if (itemDamage != 0) itemDamageStr = "+" + (itemDamage % 1 == 0 ? Integer.toString((int) itemDamage) : Double.toString(itemDamage));
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        // the x and y values here and in the draw method are pretty much entirely random
        // these are just eyeballed to look nice and centered which is why the math is stupid and makes no sense
        int xush = 47;
        if (player.isCreative()) xush = 31;
        int x = (context.getScaledWindowWidth() / 2) - (renderer.getWidth(itemDamageStr) / 2);
        int y = context.getScaledWindowHeight() - xush - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;

        RenderSystem.enableBlend(); // without this the hotbar loses all translucency for some reason
        RenderSystem.disableDepthTest();
        context.getMatrices().scale(0.5F, 0.5F, 0.5F);
        if (OverlayTweaksConfig.CONFIG.instance().hotbarEnchantmentGlance && !itemEnchantStr.isEmpty())
            context.drawTextWithShadow(renderer, itemEnchantStr, context.getScaledWindowWidth() - (renderer.getWidth(itemEnchantStr) / 2), 2 * y - 15, 16777215); // white color
        if (OverlayTweaksConfig.CONFIG.instance().hotbarDamageGlance && !itemDamageStr.isEmpty())
            context.drawTextWithShadow(renderer, itemDamageStr, 2 * x + 7, 2 * y + 3, 16777215); // white color
        context.getMatrices().scale(2F, 2F, 2F);
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), index = 2)
    private int moveHotbarUp(int y) {
        return y - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"), index = 2)
    private int moveHotbarUp2(int o) {
        return o - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), index = 2)
    private int moveMountJumpUp(int k) {
        return k - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), index = 2)
    private int moveExperienceUp(int l) {
        return l - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"), index = 6)
    private int moveExperienceUp2(int l) {
        return l - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"), index = 3)
    private int moveExperienceUp3(int l) {
        return l - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"), index = 3)
    private int moveTooltipUp(int y) {
        int xextra = 0;
        if (OverlayTweaksConfig.CONFIG.instance().hotbarEnchantmentGlance) xextra = 7;
        return y - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy - xextra;
    }

    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), index = 2)
    private int moveStatusUp(int s) {
        return s - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"), index = 3)
    private int moveStatusUp2(int o) {
        return o - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @ModifyArg(method = "renderMountHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), index = 2)
    private int moveMountHealthUp(int m) {
        return m - OverlayTweaksConfig.CONFIG.instance().moveHotbarBy;
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void isInContainer(DrawContext context, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen != null && OverlayTweaksConfig.CONFIG.instance().hideCrosshairInContainers)
            ci.cancel();
    }

    @ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean removeFirstPersonCheck(boolean original) {
        if (OverlayTweaksConfig.CONFIG.instance().showCrosshairInPerspective) return true;
        else return original;
    }

    @WrapWithCondition(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private boolean removeBlending(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor, GlStateManager.SrcFactor srcAlpha, GlStateManager.DstFactor dstAlpha) {
        return !OverlayTweaksConfig.CONFIG.instance().removeCrosshairBlending;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V", shift = At.Shift.AFTER))
    private void changeCrosshairOpacity(DrawContext context, CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().removeCrosshairBlending) RenderSystem.setShaderColor(1F, 1F, 1F, OverlayTweaksConfig.CONFIG.instance().crosshairOpacity / 100F);
    }

    @ModifyArg(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;renderCrosshair(I)V"))
    private int changeDebugCrosshairSize(int size) {
        return OverlayTweaksConfig.CONFIG.instance().debugCrosshairSize;
    }

    @ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowDebugHud()Z"))
    private boolean cancelDebugCrosshair(boolean original) {
        if (OverlayTweaksConfig.CONFIG.instance().useNormalCrosshair) return false;
        else if (OverlayTweaksConfig.CONFIG.instance().useDebugCrosshair) return true;
        return original;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V"))
    private void showCooldownOnDebug(DrawContext context, CallbackInfo ci) {
        if (!OverlayTweaksConfig.CONFIG.instance().fixDebugCooldown) return;
        if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
            assert this.client.player != null;
            float attackCooldownProgress = this.client.player.getAttackCooldownProgress(0.0F);
            boolean bool = false;
            if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && attackCooldownProgress >= 1.0F) {
                bool = this.client.player.getAttackCooldownProgressPerTick() > 5.0F;
                bool &= this.client.targetedEntity.isAlive();
            }
            int height = this.scaledHeight / 2 - 7 + 16;
            int width = this.scaledWidth / 2 - 8;
            if (bool) {
                context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, width, height, 16, 16);
            } else if (attackCooldownProgress < 1.0F) {
                int l = (int)(attackCooldownProgress * 17.0F);
                context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, width, height, 16, 4);
                context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, width, height, l, 4);
            }
        }
        RenderSystem.defaultBlendFunc();
    }

    @Inject(method = "shouldRenderSpectatorCrosshair", at = @At("HEAD"), cancellable = true)
    private void showInSpectator(HitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
        if (OverlayTweaksConfig.CONFIG.instance().showCrosshairInSpectator) cir.setReturnValue(true);
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void removeScoreboardInDebug(CallbackInfo ci) {
        if (OverlayTweaksConfig.CONFIG.instance().hideScoreboardInDebug && client.getDebugHud().shouldShowDebugHud()) ci.cancel();
    }

    /*
        The following methods were taken from Easeify under LGPLV3
        https://github.com/Polyfrost/Easeify/blob/main/LICENSE
        The code has been updated to 1.20 and with several fixes
     */

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    private int clamp(int value) {
        if (OverlayTweaksConfig.CONFIG.instance().disableTitles) {
            return 0;
        } else {
            return value;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void modifyTitle(DrawContext context, float tickDelta, CallbackInfo ci) {
        float titleScale = OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
        // MCCIsland uses a giant title to black out your screen when switching worlds, so let's keep that
        if (OverlayTweaksConfig.CONFIG.instance().autoTitleScale && (MinecraftClient.getInstance().getCurrentServerEntry() != null && !MinecraftClient.getInstance().getCurrentServerEntry().address.contains("mccisland.net"))) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(title) * 4.0F;
            if (width > context.getScaledWindowWidth()) {
                titleScale = (context.getScaledWindowWidth() / width) * OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
            }
        }
        context.getMatrices().scale(titleScale, titleScale, titleScale);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", ordinal = 1, shift = At.Shift.AFTER))
    private void modifySubtitle(DrawContext context, float tickDelta, CallbackInfo ci) {
        float titleScale = OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
        // MCCIsland uses a giant title to black out your screen when switching worlds, so let's keep that
        if (OverlayTweaksConfig.CONFIG.instance().autoTitleScale && (MinecraftClient.getInstance().getCurrentServerEntry() != null && !MinecraftClient.getInstance().getCurrentServerEntry().address.contains("mccisland.net"))) {
            final float width = MinecraftClient.getInstance().textRenderer.getWidth(subtitle) * 2.0F;
            if (width > context.getScaledWindowWidth()) {
                titleScale = (context.getScaledWindowWidth() / width) * OverlayTweaksConfig.CONFIG.instance().titleScale / 100;
            }
        }
        context.getMatrices().scale(titleScale, titleScale, titleScale);
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 255, ordinal = 3))
    private int modifyOpacity(int constant) {
        return (int) (OverlayTweaksConfig.CONFIG.instance().titleOpacity / 100 * 255);
    }

}
