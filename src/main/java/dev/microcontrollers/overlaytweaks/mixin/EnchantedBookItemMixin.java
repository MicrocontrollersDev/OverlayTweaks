package dev.microcontrollers.overlaytweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.item.EnchantedBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
    @ModifyReturnValue(method = "hasGlint", at = @At("RETURN"))
    public boolean removeBookGlint(boolean original) {
        return !OverlayTweaksConfig.CONFIG.instance().removeBookGlint;
    }

}
