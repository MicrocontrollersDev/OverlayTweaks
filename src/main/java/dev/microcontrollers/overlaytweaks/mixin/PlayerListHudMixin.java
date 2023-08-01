package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @ModifyConstant(method = "render", constant = @Constant(intValue = 553648127))
    private int tabOpacity(int opacity) {
        return withTabOpacity(opacity, OverlayTweaksConfig.INSTANCE.getConfig().tabPlayerListOpacity / 100F);
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
        if (OverlayTweaksConfig.INSTANCE.getConfig().hideFalsePing && (ping <= 1 || ping >= 999)) pingString = "";
        if (OverlayTweaksConfig.INSTANCE.getConfig().showPingInTab) {
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, pingString, x + width - MinecraftClient.getInstance().textRenderer.getWidth(String.valueOf(ping)), y, color);
            ci.cancel();
        }
    }

}
