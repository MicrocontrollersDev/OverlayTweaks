package dev.microcontrollers.overlaytweaks.mixin.screenopacity;

import dev.microcontrollers.overlaytweaks.Shifter;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BossBarHud.class)
public class BossBarHudMixin implements Shifter {
    @Unique
    private int distance;

    @ModifyVariable(method = "render", at = @At("TAIL"), ordinal = 1)
    private int getBossBarHeight(int j) {
        distance = j - 19;
        return j;
    }

    @Override
    public int overlayTweaks$getShift() {
        return distance;
    }

}
