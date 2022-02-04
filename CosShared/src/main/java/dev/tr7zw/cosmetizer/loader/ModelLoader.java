package dev.tr7zw.cosmetizer.loader;

import java.io.IOException;

import dev.tr7zw.cosmetizer.model.Model;

public interface ModelLoader {

    public Model loadModel(ResourceProvider provider, String id) throws IOException;
    
}
