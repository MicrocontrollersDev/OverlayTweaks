package dev.microcontrollers.overlaytweaks.mixin;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/*
    The following class is based on NicerTabBackground under MIT
    https://github.com/Benonardo/NicerTabBackground/blob/main/LICENSE
    Only idea and injection location have been reused
 */
@Mixin(TabNavigationWidget.class)
public class TabNavigationWidgetMixin {
    @ModifyConstant(method = "render", constant = @Constant(intValue = -16777216))
    private int modifyTabBackgroundColor(int opacity) {
        return withTabBackgroundOpacity(opacity, OverlayTweaksConfig.CONFIG.instance().tabBackgroundOpacity / 100F);
    }

    @Unique
    private int withTabBackgroundOpacity(int color, float opacity) {
        return (int) (opacity * 255) << 24 | (color & 0xFFFFFF);
    }
}
