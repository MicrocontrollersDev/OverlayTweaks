package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getTextBackgroundColor(I)I"))
    private int modifyTabColor(int opacity) {
        return OverlayTweaksConfig.CONFIG.instance().tabPlayerListColor.getRGB();
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    private void textLatency(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        if (!OverlayTweaksConfig.CONFIG.instance().showPingInTab) return;
        int ping = entry.getLatency();
        int color = -5636096;
        if (ping >= 0 && ping < 75) color = OverlayTweaksConfig.CONFIG.instance().pingColorOne.getRGB();
        else if (ping >= 75 && ping < 145) color = OverlayTweaksConfig.CONFIG.instance().pingColorTwo.getRGB();
        else if (ping >= 145 && ping < 200) color = OverlayTweaksConfig.CONFIG.instance().pingColorThree.getRGB();
        else if (ping >= 200 && ping < 300) color = OverlayTweaksConfig.CONFIG.instance().pingColorFour.getRGB();
        else if (ping >= 300 && ping < 400) color = OverlayTweaksConfig.CONFIG.instance().pingColorFive.getRGB();
        else if (ping >= 400) color = OverlayTweaksConfig.CONFIG.instance().pingColorSix.getRGB();
        String pingString = String.valueOf(ping);
        if (OverlayTweaksConfig.CONFIG.instance().hideFalsePing && (ping <= 1 || ping >= 999)) pingString = "";
        if (OverlayTweaksConfig.CONFIG.instance().scalePingDisplay) {
                context.getMatrices().scale(0.5F, 0.5F, 0.5F);
                context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, pingString, 2 * (x + width) - MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(ping)) - 4, 2 * y + 4, color);
                context.getMatrices().scale(2F, 2F, 2F);
            } else context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, pingString, x + width - MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(ping)), y, color);
        ci.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void moveTabDown(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        float distance = OverlayTweaksConfig.CONFIG.instance().moveTabBelowBossBars
                ? ((Shifter) client.inGameHud.getBossBarHud()).overlayTweaks$getShift()
                : OverlayTweaksConfig.CONFIG.instance().moveTabDown;
        context.getMatrices().translate(0, distance, 0);
    }

    @ModifyConstant(method = "collectPlayerEntries", constant = @Constant(longValue = 80L))
    public long increasePlayerCount(long constant) {
        return OverlayTweaksConfig.CONFIG.instance().maxTabPlayers;
    }

}
