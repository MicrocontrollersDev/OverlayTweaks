package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.Shifter;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyConstant(method = "render", constant = @Constant(intValue = 553648127))
    private int tabOpacity(int opacity) {
        return withTabOpacity(opacity, OverlayTweaksConfig.CONFIG.instance().tabPlayerListOpacity / 100F);
    }

    @Unique
    private int withTabOpacity(int color, float opacity) {
        return (int) (opacity * 255) << 24 | (color & 0xFFFFFF);
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    private void textLatency(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        int ping = entry.getLatency();
        int color = -5636096;
        if (ping >= 0 && ping < 75) color = -15466667;
        else if (ping >= 75 && ping < 145) color = -14773218;
        else if (ping >= 145 && ping < 200) color = -4733653;
        else if (ping >= 200 && ping < 300) color = -13779;
        else if (ping >= 300 && ping < 400) color = -6458098;
        else if (ping >= 400) color = -4318437;
        String pingString = String.valueOf(ping);
        if (OverlayTweaksConfig.CONFIG.instance().hideFalsePing && (ping <= 1 || ping >= 999)) pingString = "";
        if (OverlayTweaksConfig.CONFIG.instance().showPingInTab) {
            if (OverlayTweaksConfig.CONFIG.instance().scalePingDisplay) {
                context.getMatrices().scale(0.5F, 0.5F, 0.5F);
                context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, pingString, 2 * (x + width) - MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(ping)) - 4, 2 * y + 4, color);
                context.getMatrices().scale(2F, 2F, 2F);
            } else context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, pingString, x + width - MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(ping)), y, color);
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void moveTabDown(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        float distance = OverlayTweaksConfig.CONFIG.instance().moveTabBelowBossBars
                ? ((Shifter) client.inGameHud.getBossBarHud()).overlayTweaks$getShift()
                : OverlayTweaksConfig.CONFIG.instance().moveTabDown;
        context.getMatrices().translate(0, distance, 0);
    }

}
