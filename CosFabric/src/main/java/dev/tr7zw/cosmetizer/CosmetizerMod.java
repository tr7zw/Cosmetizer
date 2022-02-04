package dev.tr7zw.cosmetizer;

import net.fabricmc.api.ModInitializer;

public class CosmetizerMod extends CosmetizerCore implements ModInitializer {

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		init();
	}
}