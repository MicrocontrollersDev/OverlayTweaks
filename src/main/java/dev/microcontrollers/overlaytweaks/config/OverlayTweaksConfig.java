package dev.microcontrollers.overlaytweaks.config;


import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.GsonConfigInstance;
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Path;

public class OverlayTweaksConfig {
    public static GsonConfigInstance<dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig> configInstance = new GsonConfigInstance<>(dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig.class, Path.of(FabricLoader.getInstance().getConfigDir().toString(), "overlaytweaks.json"));

    @ConfigEntry
    public static boolean removeWaterOverlay;

    @ConfigEntry
    public static boolean removeFireOverlay;

    @ConfigEntry
    public static double fireOverlayHeight;

    @ConfigEntry
    public static boolean customVignetteDarkness;

    @ConfigEntry
    public static double customVignetteDarknessValue;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(configInstance, ((defaults, config, builder) -> builder
                .title(Text.literal("Overlay Tweaks"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Overlay Tweaks"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Water Overlay"))
                                .description(OptionDescription.of(Text.of("Hides crosshair when a container is opened. Great for containers with translucent backgrounds.")))
                                .binding(false, () -> removeWaterOverlay, newVal -> removeWaterOverlay = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Fire Overlay When Resistant"))
                                .description(OptionDescription.of(Text.of("Shows the crosshair when in third person.")))
                                .binding(false, () -> removeFireOverlay, newVal -> removeFireOverlay = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(double.class)
                                .name(Text.literal("Change Fire Overlay Height"))
                                .description(OptionDescription.of(Text.of("Change the height of the fire overlay if your pack does not have low fire. May improve visibility. Set to 0.0 for default.")))
                                .binding(0.0, () -> fireOverlayHeight, newVal -> fireOverlayHeight = newVal)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(-0.5, 0.0)
                                        .step(0.01))
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Constant Vignette Darkness"))
                                .description(OptionDescription.of(Text.of("Apply a constant vignette regardless of sky light level.")))
                                .binding(false, () -> customVignetteDarkness, newVal -> customVignetteDarkness = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(double.class)
                                .name(Text.literal("Constant Vignette Darkness Value"))
                                .description(OptionDescription.of(Text.of("The value for constant vignette.")))
                                .binding(0.0, () -> customVignetteDarknessValue, newVal -> customVignetteDarknessValue = newVal)
                                .customController(opt -> new DoubleSliderController(opt, 0.0, 1.0, 0.1))
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}