package dev.tr7zw.cosmetizer.loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.platform.NativeImage;

import dev.tr7zw.cosmetizer.model.TextureProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class TextureLoader {

    public static final TextureLoader INSTANCE = new TextureLoader();
    
    public final Map<String, TextureProvider> specialTextures = new HashMap<>();
    private final Map<ResourceLocation, TextureProvider> loadedTextures = new HashMap<>();
    
    public TextureLoader() {
        specialTextures.put("special_player", TextureProvider.PLAYERSKINCUTOUT);
        specialTextures.put("special_player_transparent", TextureProvider.PLAYERSKINTRANSLUCENT);
        specialTextures.put("special_endportal", TextureProvider.ENDPORTAL);
        specialTextures.put("special_endgateway", TextureProvider.ENDGATEWAY);
    }
    
    public TextureProvider loadTexture(String id, String textureId, ResourceProvider provider) throws IOException {
        if(specialTextures.containsKey(textureId)) {
            return specialTextures.get(textureId);
        }
        ResourceLocation resourceKey = new ResourceLocation("cosmetizer", (id + "-" + textureId).toLowerCase());
        if(loadedTextures.containsKey(resourceKey)) {
            return loadedTextures.get(resourceKey);
        }
        NativeImage image = provider.getTexture(id, textureId);
        if(image == null) {
            throw new IOException("Unable to load texture " + resourceKey);
        }
        Minecraft.getInstance().getTextureManager().register(resourceKey, new DynamicTexture(image));
        TextureProvider texture = TextureProvider.getCutoutNoCull(resourceKey);
        loadedTextures.put(resourceKey, texture);
        return texture;
    }
    
}
