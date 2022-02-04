package dev.tr7zw.cosmetizer.model.cube;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class CustomizableCubeListBuilder {

    private final List<CustomizableCube> cubes = Lists.newArrayList();
    private Map<String, float[]> uvData = new HashMap<>();
    private boolean mirror;

    public static CustomizableCubeListBuilder create() {
        return new CustomizableCubeListBuilder();
    }

    public CustomizableCubeListBuilder uvData(Map<String, float[]> data) {
        this.uvData = data;
        return this;
    }

    public CustomizableCubeListBuilder mirror(boolean bl) {
        this.mirror = bl;
        return this;
    }

    public List<CustomizableCube> getCubes() {
        return cubes;
    }

    public CustomizableCubeListBuilder addBox(float textureSizeX, float textureSizeY, float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
        this.cubes.add(new CustomizableCube(uvData, x, y, z, sizeX, sizeY, sizeZ, 0f, 0f, 0f,
                this.mirror, textureSizeX, textureSizeY));
        return this;
    }

}
