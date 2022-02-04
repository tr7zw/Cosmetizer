package dev.tr7zw.cosmetizer.loader;

import java.io.IOException;
import java.util.Iterator;

import com.mojang.blaze3d.platform.NativeImage;

public interface ResourceProvider {

    public Iterator<String> getIds();
    
    public String getModelData(String id) throws IOException;
    
    public String getModelConfig(String id) throws IOException;
    
    public NativeImage getTexture(String id, String textureId) throws IOException;
    
}
