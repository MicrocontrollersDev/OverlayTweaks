package dev.microcontrollers.overlaytweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class OverlayTweaksConfig {
    public static final ConfigClassHandler<OverlayTweaksConfig> CONFIG = ConfigClassHandler.createBuilder(OverlayTweaksConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("overlaytweaks.json"))
                    .build())
            .build();

    // Screens

    @SerialEntry public float containerOpacity = (208/255F) * 100F;
    @SerialEntry public float containerTextureOpacity = 100F;
    @SerialEntry public int containerSize = 1;
    @SerialEntry public float tabPlayerListOpacity = (32/255F) * 100F;
    @SerialEntry public float moveTabDown = 10F;
    @SerialEntry public boolean moveTabBelowBossBars = false;
    @SerialEntry public boolean showPingInTab = false;
    @SerialEntry public boolean scalePingDisplay = false;
    @SerialEntry public boolean hideFalsePing = false;
    @SerialEntry public boolean disableScreenBobbing = false;
    @SerialEntry public boolean disableHandBobbing = false;
    @SerialEntry public boolean disableMapBobbing = true;
    @SerialEntry public boolean disableScreenDamage = false;
    @SerialEntry public boolean disableHandDamage = false;

    // HUD

    @SerialEntry public float customShieldHeight = 0F;
    @SerialEntry public float customShieldOpacity = 100F;
    @SerialEntry public boolean colorShieldCooldown = false;
    @SerialEntry public boolean removeWaterOverlay = true;
    @SerialEntry public boolean removeWaterFov = true;
    @SerialEntry public boolean removeFireOverlay = true;
    @SerialEntry public double fireOverlayHeight = 0.0;
    @SerialEntry public float customFireOverlayOpacity = 100F;
    @SerialEntry public int moveHotbarBy = 2;
    @SerialEntry public boolean removeItemTooltip = false;
    @SerialEntry public boolean hotbarEnchantmentGlance = false;
    @SerialEntry public boolean hotbarDamageGlance = false;
    @SerialEntry public boolean hideScoreboardInDebug = false;
    @SerialEntry public boolean classicDebugStyle = false;
    @SerialEntry public boolean disableTitles = false;
    @SerialEntry public float titleScale = 100F;
    @SerialEntry public boolean autoTitleScale = true;
    @SerialEntry public float titleOpacity = 100F;
    @SerialEntry public boolean removeAllToasts = false;
    @SerialEntry public boolean removeTutorialToasts = true;

    // Crosshair

    @SerialEntry public boolean hideCrosshairInContainers = true;
    @SerialEntry public boolean showCrosshairInPerspective = false;
    @SerialEntry public boolean showCrosshairInSpectator = false;
    @SerialEntry public boolean removeCrosshairBlending = false;
    @SerialEntry public float crosshairOpacity = 100F;
    @SerialEntry public boolean useNormalCrosshair = false;
    @SerialEntry public boolean useDebugCrosshair = false;
    @SerialEntry public boolean fixDebugCooldown = true;
    @SerialEntry public int debugCrosshairSize = 10;

    // Effects

    @SerialEntry public boolean potionGlint = false;
    @SerialEntry public boolean staticItems = false;
    @SerialEntry public boolean unstackedItems = false;
    @SerialEntry public boolean cleanerNightVision = true;
    @SerialEntry public boolean cleanerSkyDarkness = true;
    @SerialEntry public boolean removeElderGuardianJumpscare = false;
    @SerialEntry public float horseOpacity = 100F;
    @SerialEntry public float pigOpacity = 100F;
    @SerialEntry public float pumpkinOpacity = 100F;
    @SerialEntry public boolean customVignetteDarkness = false;
    @SerialEntry public float customVignetteDarknessValue = 0F;

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("Overlay Tweaks"))

                // Screens

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Screens"))

                        // Containers

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Containers"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Container Background Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the transparency of container backgrounds. Set to 0 to make it completely transparent.")))
                                        .binding((208/255F) * 100F, () -> config.containerOpacity, newVal -> config.containerOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Container Texture Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the transparency of container textures. Set to 0 to make it completely transparent.")))
                                        .binding(100F, () -> config.containerTextureOpacity, newVal -> config.containerTextureOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(int.class)
                                        .name(Text.literal("Container Scale"))
                                        .description(OptionDescription.of(Text.of("Change container scaling. Some values may make containers bigger than your screen. Set to 1 to use the default.")))
                                        .binding(2, () -> config.containerSize, newVal -> config.containerSize = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%01d", value) + "x"))
                                                .range(1, 4)
                                                .step(1))
                                        .build())
                                .build())

                        // Tab List

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Tab List"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Tab Player List Background Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the transparency of the player list in tab. Set to 0 to make it completely transparent.")))
                                        .binding((32/255F) * 100F, () -> config.tabPlayerListOpacity, newVal -> config.tabPlayerListOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Move Tab List Down"))
                                        .description(OptionDescription.of(Text.of("Moves the tab list down by the specified number of units. Helps prevent tab from overlapping with a bossbar.")))
                                        .binding(10F, () -> config.moveTabDown, newVal -> config.moveTabDown = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value)))
                                                .range(0F, 60F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Move Tab List Below Bossbars"))
                                        .description(OptionDescription.of(Text.of("Moves the tab list below all bossbars. This will take priority over the \"Move Tab List Down\" setting.")))
                                        .binding(defaults.moveTabBelowBossBars, () -> config.moveTabBelowBossBars, newVal -> config.moveTabBelowBossBars = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Show Numerical Ping"))
                                        .description(OptionDescription.of(Text.of("Replace the ping icon with a numerical value.")))
                                        .binding(defaults.showPingInTab, () -> config.showPingInTab, newVal -> config.showPingInTab = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Scale Numerical Ping"))
                                        .description(OptionDescription.of(Text.of("Scales the ping display to make it smaller.")))
                                        .binding(defaults.scalePingDisplay, () -> config.scalePingDisplay, newVal -> config.scalePingDisplay = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Hide Fake Ping"))
                                        .description(OptionDescription.of(Text.of("Some servers force a ping of 0 or 1 or very high numbers to hide players ping. This will hide the number from being displayed as it is useless.")))
                                        .binding(defaults.hideFalsePing, () -> config.hideFalsePing, newVal -> config.hideFalsePing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Shake

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Screen Shake"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Screen Bobbing"))
                                        .description(OptionDescription.of(Text.of("Disables the screen shake when moving.")))
                                        .binding(defaults.disableScreenBobbing, () -> config.disableScreenBobbing, newVal -> config.disableScreenBobbing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Hand Bobbing"))
                                        .description(OptionDescription.of(Text.of("Disables the hand shake when moving.")))
                                        .binding(defaults.disableHandBobbing, () -> config.disableHandBobbing, newVal -> config.disableHandBobbing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Map Bobbing"))
                                        .description(OptionDescription.of(Text.of("Disables the hand shake when holding a map. Does nothing if \"Disable Hand Bobbing\" is turned on.")))
                                        .binding(defaults.disableMapBobbing, () -> config.disableMapBobbing, newVal -> config.disableMapBobbing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Screen Damage Tilt"))
                                        .description(OptionDescription.of(Text.of("Disables the screen shake when taking damage.")))
                                        .binding(defaults.disableScreenDamage, () -> config.disableScreenDamage, newVal -> config.disableScreenDamage = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Hand Damage Tilt"))
                                        .description(OptionDescription.of(Text.of("Disables the hand shake when taking damage.")))
                                        .binding(defaults.disableHandDamage, () -> config.disableHandDamage, newVal -> config.disableHandDamage = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())

                // HUD

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("HUD"))

                        // Shield

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Shield"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Shield Height"))
                                        .description(OptionDescription.of(Text.of("The value for shield height.")))
                                        .binding(0F, () -> config.customShieldHeight, newVal -> config.customShieldHeight = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.2f", value)))
                                                .range(-0.5F, 0F)
                                                .step(0.01F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Shield Opacity"))
                                        .description(OptionDescription.of(Text.of("The value for shield opacity. Currently does not work for shields with banner designs.")))
                                        .binding(100F, () -> config.customShieldOpacity, newVal -> config.customShieldOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Color Shield Cooldown"))
                                        .description(OptionDescription.of(Text.of("Adds a color to your shield depending on the cooldown remaining")))
                                        .binding(defaults.colorShieldCooldown, () -> config.colorShieldCooldown, newVal -> config.colorShieldCooldown = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Water

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Water"))
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
                                .build())

                        // Fire

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Fire"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Fire Overlay When Resistant"))
                                        .description(OptionDescription.of(Text.of("Removes the fire overlay when you are resistant to fire, such as when you have fire resistance or are in creative mode.")))
                                        .binding(defaults.removeFireOverlay, () -> config.removeFireOverlay, newVal -> config.removeFireOverlay = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(double.class)
                                        .name(Text.literal("Fire Overlay Height"))
                                        .description(OptionDescription.of(Text.of("Change the height of the fire overlay if your pack does not have low fire. May improve visibility.")))
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

                        // Hotbar

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Hotbar"))
                                .option(Option.createBuilder(int.class)
                                        .name(Text.literal("Move Hotbar Up"))
                                        .description(OptionDescription.of(Text.of("Move the hotbar up by the specified amount.")))
                                        .binding(2, () -> config.moveHotbarBy, newVal -> config.moveHotbarBy = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(0, 5)
                                                .step(1))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Held Item Name Tooltip"))
                                        .description(OptionDescription.of(Text.of("Removes the tooltip above the hotbar when holding an item.")))
                                        .binding(defaults.removeItemTooltip, () -> config.removeItemTooltip, newVal -> config.removeItemTooltip = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Enchantment Glance"))
                                        .description(OptionDescription.of(Text.of("Shows the enchantments of a held item above the hotbar. This will move the item name tooltip higher if it is not disabled.")))
                                        .binding(defaults.hotbarEnchantmentGlance, () -> config.hotbarEnchantmentGlance, newVal -> config.hotbarEnchantmentGlance = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Damage Glance"))
                                        .description(OptionDescription.of(Text.of("Shows the attack damage of a held item above the hotbar.")))
                                        .binding(defaults.hotbarDamageGlance, () -> config.hotbarDamageGlance, newVal -> config.hotbarDamageGlance = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // F3 Debug

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Debug"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Hide Scoreboard In Debug Menu"))
                                        .description(OptionDescription.of(Text.of("Removes the scoreboard when you have the F3 menu open.")))
                                        .binding(defaults.hideScoreboardInDebug, () -> config.hideScoreboardInDebug, newVal -> config.hideScoreboardInDebug = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Classic Debug Style"))
                                        .description(OptionDescription.of(Text.of("Removes the background of the F3 text and renders the text with a shadow instead.")))
                                        .binding(defaults.classicDebugStyle, () -> config.classicDebugStyle, newVal -> config.classicDebugStyle = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Titles

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Titles"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Titles"))
                                        .description(OptionDescription.of(Text.of("Remove titles entirely.")))
                                        .binding(defaults.disableTitles, () -> config.disableTitles, newVal -> config.disableTitles = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Title Scale"))
                                        .description(OptionDescription.of(Text.of("Set the scale for titles.")))
                                        .binding(100F, () -> config.titleScale, newVal -> config.titleScale = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Automatically Scale Titles"))
                                        .description(OptionDescription.of(Text.of("Scale titles automatically if they go past the edges of your screen.")))
                                        .binding(defaults.autoTitleScale, () -> config.autoTitleScale, newVal -> config.autoTitleScale = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Title Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the opacity for titles.")))
                                        .binding(100F, () -> config.titleOpacity, newVal -> config.titleOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())

                        // Toasts

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Toasts"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove All Toasts"))
                                        .description(OptionDescription.of(Text.of("Prevents all toasts from rendering.")))
                                        .binding(defaults.removeAllToasts, () -> config.removeAllToasts, newVal -> config.removeAllToasts = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Tutorial Toasts"))
                                        .description(OptionDescription.of(Text.of("Removes only tutorial toasts when entering a world in a new instance. Does nothing if \"Remove all Toasts\" is enabled.")))
                                        .binding(defaults.removeTutorialToasts, () -> config.removeTutorialToasts, newVal -> config.removeTutorialToasts = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())

                // Crosshair

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Crosshair"))

                        // General

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("General"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Hide in Containers"))
                                        .description(OptionDescription.of(Text.of("Hides crosshair when a container is opened. Great for containers with translucent backgrounds.")))
                                        .binding(defaults.hideCrosshairInContainers, () -> config.hideCrosshairInContainers, newVal -> config.hideCrosshairInContainers = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Show in Third Person"))
                                        .description(OptionDescription.of(Text.of("Shows the crosshair when in third person.")))
                                        .binding(defaults.showCrosshairInPerspective, () -> config.showCrosshairInPerspective, newVal -> config.showCrosshairInPerspective = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Always Show in Spectator"))
                                        .description(OptionDescription.of(Text.of("Always show the crosshair when in spectator mode.")))
                                        .binding(defaults.showCrosshairInSpectator, () -> config.showCrosshairInSpectator, newVal -> config.showCrosshairInSpectator = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Blending"))
                                        .description(OptionDescription.of(Text.of("Removes color blending on the crosshair, making it always white.")))
                                        .binding(defaults.removeCrosshairBlending, () -> config.removeCrosshairBlending, newVal -> config.removeCrosshairBlending = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Crosshair Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the crosshair when \"Remove Crosshair Blending\" is enabled.")))
                                        .binding(100F, () -> config.crosshairOpacity, newVal -> config.crosshairOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())

                        // Debug Crosshair

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Debug"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Ignore Debug Crosshair"))
                                        .description(OptionDescription.of(Text.of("Uses the normal crosshair instead of the debug crosshair when F3 is open. Takes priority over \"Always Use Debug Mode\".")))
                                        .binding(defaults.useNormalCrosshair, () -> config.useNormalCrosshair, newVal -> config.useNormalCrosshair = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Always Use Debug Crosshair"))
                                        .description(OptionDescription.of(Text.of("Uses the debug crosshair instead of the normal crosshair regardless of when F3 is open. Requires \"Ignore Debug Mode\" to be disabled.")))
                                        .binding(defaults.useDebugCrosshair, () -> config.useDebugCrosshair, newVal -> config.useDebugCrosshair = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Fix Debug Crosshair Attack Indicator"))
                                        .description(OptionDescription.of(Text.of("Due to an oversight, the debug crosshair does not have an attack cooldown indicator. This feature adds it back.")))
                                        .binding(defaults.fixDebugCooldown, () -> config.fixDebugCooldown, newVal -> config.fixDebugCooldown = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(int.class)
                                        .name(Text.literal("Debug Crosshair Size"))
                                        .description(OptionDescription.of(Text.of("Changes the size of the crosshair.")))
                                        .binding(10, () -> config.debugCrosshairSize, newVal -> config.debugCrosshairSize = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(0, 20)
                                                .step(1))
                                        .build())
                                .build())
                        .build())

                // Effects

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Effects"))

                        // Item

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Items"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Potion Glint"))
                                        .description(OptionDescription.of(Text.of("Add the potion glint that was removed in 1.19.4.")))
                                        .binding(defaults.potionGlint, () -> config.potionGlint, newVal -> config.potionGlint = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Static Items"))
                                        .description(OptionDescription.of(Text.of("Remove the bobbing of items dropped on the ground.")))
                                        .binding(defaults.staticItems, () -> config.staticItems, newVal -> config.staticItems = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Unstacked Items"))
                                        .description(OptionDescription.of(Text.of("Always render one item per stack of dropped items.")))
                                        .binding(defaults.unstackedItems, () -> config.unstackedItems, newVal -> config.unstackedItems = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // World

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("World"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Cleaner Night Vision Decay"))
                                        .description(OptionDescription.of(Text.of("Makes the night vision loss a gradual effect instead of an on and off flicker.")))
                                        .binding(defaults.cleanerNightVision, () -> config.cleanerNightVision, newVal -> config.cleanerNightVision = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Cleaner Sky Void Darkness"))
                                        .description(OptionDescription.of(Text.of("Lowers the required height for the sky to turn black before getting closer to the void. This prevents the sky turning black at a high Y value.")))
                                        .binding(defaults.cleanerSkyDarkness, () -> config.cleanerSkyDarkness, newVal -> config.cleanerSkyDarkness = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Elder Guardian Jumpscare"))
                                        .description(OptionDescription.of(Text.of("Removes the elder guardian particle effect from showing on your screen.")))
                                        .binding(defaults.removeElderGuardianJumpscare, () -> config.removeElderGuardianJumpscare, newVal -> config.removeElderGuardianJumpscare = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Ridden Horse Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the horse you are currently riding.")))
                                        .binding(100F, () -> config.horseOpacity, newVal -> config.horseOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Ridden Pig Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the pig you are currently riding.")))
                                        .binding(100F, () -> config.pigOpacity, newVal -> config.pigOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())

                        // Full Screen Effects

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Full Screen Effects"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Pumpkin Overlay Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the pumpkin overlay when wearing a pumpkin.")))
                                        .binding(100F, () -> config.pumpkinOpacity, newVal -> config.pumpkinOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Constant Vignette Darkness"))
                                        .description(OptionDescription.of(Text.of("Apply a constant vignette regardless of sky light level.")))
                                        .binding(defaults.customVignetteDarkness, () -> config.customVignetteDarkness, newVal -> config.customVignetteDarkness = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Custom Vignette Darkness Value"))
                                        .description(OptionDescription.of(Text.of("The value for constant vignette.")))
                                        .binding(0F, () -> config.customVignetteDarknessValue, newVal -> config.customVignetteDarknessValue = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())
                        .build())
        )).generateScreen(parent);
    }

    public static void load() {
        OverlayTweaksConfig.CONFIG.serializer().load();
    }
}