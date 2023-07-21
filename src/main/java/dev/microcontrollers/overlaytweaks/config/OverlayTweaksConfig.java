package dev.microcontrollers.overlaytweaks.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class OverlayTweaksConfig {
    public static final ConfigInstance<OverlayTweaksConfig> INSTANCE = GsonConfigInstance.createBuilder(OverlayTweaksConfig.class)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("overlaytweaks.json"))
            .build();

    @ConfigEntry
    public boolean removeWaterOverlay = true;

    @ConfigEntry
    public boolean removeFireOverlay = true;

    @ConfigEntry
    public double fireOverlayHeight = 0.0;

    @ConfigEntry
    public float customFireOverlayOpacity = 100F;

    @ConfigEntry
    public boolean customVignetteDarkness = false;

    @ConfigEntry
    public double customVignetteDarknessValue = 0.0;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, ((defaults, config, builder) -> builder
                .title(Text.literal("Overlay Tweaks"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Overlay Tweaks"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Water Overlay"))
                                .description(OptionDescription.of(Text.of("Removes the underwater overlay when in water.")))
                                .binding(defaults.removeWaterOverlay, () -> config.removeWaterOverlay, newVal -> config.removeWaterOverlay = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Fire Overlay When Resistant"))
                                .description(OptionDescription.of(Text.of("Removes the fire overlay when you are resistant to fire, such as when you have fire resistance or are in creative mode.")))
                                .binding(defaults.removeFireOverlay, () -> config.removeFireOverlay, newVal -> config.removeFireOverlay = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(double.class)
                                .name(Text.literal("Change Fire Overlay Height"))
                                .description(OptionDescription.of(Text.of("Change the height of the fire overlay if your pack does not have low fire. May improve visibility. Set to 0.0 for default vanilla.")))
                                .binding(0.0, () -> config.fireOverlayHeight, newVal -> config.fireOverlayHeight = newVal)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(-0.5, 0.0)
                                        .step(0.01))
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Fire Overlay Opacity"))
                                .description(OptionDescription.of(Text.of("The value for fire overlay opacity. This is controlled as a percentage.")))
                                .binding(100F, () -> config.customFireOverlayOpacity, newVal -> config.customFireOverlayOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value)))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Constant Vignette Darkness"))
                                .description(OptionDescription.of(Text.of("Apply a constant vignette regardless of sky light level.")))
                                .binding(defaults.customVignetteDarkness, () -> config.customVignetteDarkness, newVal -> config.customVignetteDarkness = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(double.class)
                                .name(Text.literal("Constant Vignette Darkness Value"))
                                .description(OptionDescription.of(Text.of("The value for constant vignette.")))
                                .binding(0.0, () -> config.customVignetteDarknessValue, newVal -> config.customVignetteDarknessValue = newVal)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.1f", value)))
                                        .range(0.0, 1.0)
                                        .step(0.1))
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}