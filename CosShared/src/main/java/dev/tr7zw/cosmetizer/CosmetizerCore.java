package dev.tr7zw.cosmetizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.tr7zw.cosmetizer.loader.ModelLoader;
import dev.tr7zw.cosmetizer.loader.ModelLoaderV1;

public abstract class CosmetizerCore {

    public static final Logger LOGGER = LogManager.getLogger("Cosmetizer");
    public static ModelLoader loader = new ModelLoaderV1();
    
    public void init() {
        LOGGER.info("Shared setup...");

    }
    
}
