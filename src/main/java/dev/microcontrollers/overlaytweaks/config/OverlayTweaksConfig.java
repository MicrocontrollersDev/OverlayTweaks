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

    // Water and Fire

    @ConfigEntry public boolean removeWaterOverlay = true;
    @ConfigEntry public boolean removeWaterFov = true;
    @ConfigEntry public boolean removeFireOverlay = true;
    @ConfigEntry public double fireOverlayHeight = 0.0;
    @ConfigEntry public float customFireOverlayOpacity = 100F;

    // Held Items

    @ConfigEntry public float customShieldHeight = 0F;
    @ConfigEntry public float customShieldOpacity = 100F;

    // Screens and HUD

    @ConfigEntry public boolean shouldMoveHotbar = true;
    @ConfigEntry public boolean hideCrosshairInContainers = true;
    @ConfigEntry public boolean showCrosshairInPerspective = false;
    @ConfigEntry public boolean removeCrosshairBlending = false;
    @ConfigEntry public float containerOpacity = (208/255f) * 100f;

    // Effects

    @ConfigEntry public boolean cleanerNightVision = true;
    @ConfigEntry public boolean removeElderGuardianJumpscare = false;
    @ConfigEntry public boolean customVignetteDarkness = false;
    @ConfigEntry public float customVignetteDarknessValue = 0F;

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, ((defaults, config, builder) -> builder
                .title(Text.literal("Overlay Tweaks"))

                // Water and Fire

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Water and Fire"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Water Overlay"))
                                .description(OptionDescription.of(Text.of("Removes the underwater overlay when in water.")))
                                .binding(defaults.removeWaterOverlay, () -> config.removeWaterOverlay, newVal -> config.removeWaterOverlay = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Underwater FOV Change"))
                                .description(OptionDescription.of(Text.of("Stops the FOV from changing when you go underwater.")))
                                .binding(defaults.removeWaterFov, () -> config.removeWaterFov, newVal -> config.removeWaterFov = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Fire Overlay When Resistant"))
                                .description(OptionDescription.of(Text.of("Removes the fire overlay when you are resistant to fire, such as when you have fire resistance or are in creative mode.")))
                                .binding(defaults.removeFireOverlay, () -> config.removeFireOverlay, newVal -> config.removeFireOverlay = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(double.class)
                                .name(Text.literal("Fire Overlay Height"))
                                .description(OptionDescription.of(Text.of("Change the height of the fire overlay if your pack does not have low fire. May improve visibility. Set to 0.0 for default vanilla.")))
                                .binding(0.0, () -> config.fireOverlayHeight, newVal -> config.fireOverlayHeight = newVal)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(-0.5, 0.0)
                                        .step(0.01))
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Fire Overlay Opacity"))
                                .description(OptionDescription.of(Text.of("The value for fire overlay opacity.")))
                                .binding(100F, () -> config.customFireOverlayOpacity, newVal -> config.customFireOverlayOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .build())

                // Held Items

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Held Items"))
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Shield Height"))
                                .description(OptionDescription.of(Text.of("The value for shield height. Set to 0.0 for default vanilla.")))
                                .binding(0F, () -> config.customShieldHeight, newVal -> config.customShieldHeight = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.2f", value)))
                                        .range(-0.5F, 0F)
                                        .step(0.01F))
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Shield Opacity"))
                                .description(OptionDescription.of(Text.of("The value for shield opacity.")))
                                .binding(100F, () -> config.customShieldOpacity, newVal -> config.customShieldOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .build())

                // Screens and HUD

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Screens and HUD"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Move Hotbar Up"))
                                .description(OptionDescription.of(Text.of("Moves the hotbar up by 2 pixels to show the entire hotbar on the screen with no cutoff. This may cause chat to interfere with hearts or armor status on smaller screens or GUI scales. Use the mod \"Chat Patches\" on Modrinth to move chat up.")))
                                .binding(defaults.shouldMoveHotbar, () -> config.shouldMoveHotbar, newVal -> config.shouldMoveHotbar = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Hide Crosshair in Containers"))
                                .description(OptionDescription.of(Text.of("Hides crosshair when a container is opened. Great for containers with translucent backgrounds.")))
                                .binding(defaults.hideCrosshairInContainers, () -> config.hideCrosshairInContainers, newVal -> config.hideCrosshairInContainers = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Show Crosshair in Third Person"))
                                .description(OptionDescription.of(Text.of("Shows the crosshair when in third person.")))
                                .binding(defaults.showCrosshairInPerspective, () -> config.showCrosshairInPerspective, newVal -> config.showCrosshairInPerspective = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Crosshair Blending"))
                                .description(OptionDescription.of(Text.of("Removes color blending on the crosshair, making it always white.")))
                                .binding(defaults.removeCrosshairBlending, () -> config.removeCrosshairBlending, newVal -> config.removeCrosshairBlending = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Container Background Opacity"))
                                .description(OptionDescription.of(Text.of("Set the transparency of the container background. Set to 0 to make it completely transparent.")))
                                .binding((208/255f) * 100f, () -> config.containerOpacity, newVal -> config.containerOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .build())

                // Effects

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Effects"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Cleaner Night Vision"))
                                .description(OptionDescription.of(Text.of("Makes the night vision loss a gradual effect instead of an on and off flicker.")))
                                .binding(defaults.cleanerNightVision, () -> config.cleanerNightVision, newVal -> config.cleanerNightVision = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Constant Vignette Darkness"))
                                .description(OptionDescription.of(Text.of("Apply a constant vignette regardless of sky light level.")))
                                .binding(defaults.customVignetteDarkness, () -> config.customVignetteDarkness, newVal -> config.customVignetteDarkness = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Constant Vignette Darkness Value"))
                                .description(OptionDescription.of(Text.of("The value for constant vignette.")))
                                .binding(0F, () -> config.customVignetteDarknessValue, newVal -> config.customVignetteDarknessValue = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}