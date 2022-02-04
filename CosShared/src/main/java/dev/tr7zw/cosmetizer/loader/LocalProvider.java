package dev.tr7zw.cosmetizer.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import com.mojang.blaze3d.platform.NativeImage;

import dev.tr7zw.cosmetizer.CosmetizerCore;

public class LocalProvider implements ResourceProvider {

    private File folder = new File("models");
    
    @Override
    public Iterator<String> getIds() {
        if(!folder.exists()) {
            folder.mkdirs();
        }
        File[] files = folder.listFiles();
        return new Iterator<String>() {

            int offset = 0;
            String next = null;
            
            @Override
            public boolean hasNext() {
                while(offset < files.length) {
                    if(files[offset].exists() && files[offset].isDirectory()) {
                        File dir = files[offset];
                        File json = new File(dir, dir.getName() + ".json");
                        offset++;
                        if(json.exists()) {
                            next = dir.getName();
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public String next() {
                return next;
            }
        };
    }

    @Override
    public String getModelData(String id) throws IOException {
        CosmetizerCore.LOGGER.debug("Models folder: " + folder.getAbsolutePath());
        File targetFolder = new File(folder, id);
        File json = new File(targetFolder, id + ".json");
        if(json.exists()) {
            return new String(Files.readAllBytes(json.toPath()));
        }
        return null;
    }

    @Override
    public NativeImage getTexture(String id, String textureId) throws IOException {
        File targetFolder = new File(folder, id);
        File imageFile = new File(targetFolder, textureId + ".png");
        if(!imageFile.exists()){
            return null;
        }
        return NativeImage.read(new FileInputStream(imageFile));
    }

    @Override
    public String getModelConfig(String id) throws IOException {
        CosmetizerCore.LOGGER.debug("Models folder: " + folder.getAbsolutePath());
        File targetFolder = new File(folder, id);
        File json = new File(targetFolder, id + "-config.json");
        if(json.exists()) {
            return new String(Files.readAllBytes(json.toPath()));
        }
        return null;
    }

}
