package dev.microcontrollers.overlaytweaks;

import dev.microcontrollers.overlaytweaks.config.OverlayTweaksConfig;
import net.fabricmc.api.ModInitializer;

public class OverlayTweaks implements ModInitializer {
	@Override
	public void onInitialize() {
		OverlayTweaksConfig.CONFIG.load();
	}

}