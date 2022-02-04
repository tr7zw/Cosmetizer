package dev.tr7zw.cosmetizer.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import dev.tr7zw.cosmetizer.CosmetizerCore;
import dev.tr7zw.cosmetizer.model.Model;
import dev.tr7zw.cosmetizer.model.RenderContext;
import dev.tr7zw.cosmetizer.model.TextureProvider;
import dev.tr7zw.cosmetizer.model.cube.CustomizableCube;
import dev.tr7zw.cosmetizer.model.cube.CustomizableCubeListBuilder;
import dev.tr7zw.cosmetizer.model.cube.CustomizableModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class ModelLoaderV1 implements ModelLoader {

    private static final Gson GSON = new Gson();

    @Override
    public Model loadModel(ResourceProvider provider, String id) throws IOException {
        String json = provider.getModelData(id);
        String configJson = provider.getModelConfig(id);
        if (json == null) {
            return null;
        }
        ModelConfigV1 config = getConfig(configJson);
        if(config == null) {
            return null;
        }
        ModelData data = GSON.fromJson(json, ModelData.class);
        LoadedModel model = new LoadedModel();
        model.config = config;
        for (Entry<String, String> entry : data.textures.entrySet()) {
            TextureProvider texture = TextureLoader.INSTANCE.loadTexture(id, entry.getValue(), provider);
            if (texture == null) {
                throw new IOException("Unable to get required Texture " + id + " " + entry.getValue());
            }
            TextureSubSet subset = new TextureSubSet(texture);
            for (dev.tr7zw.cosmetizer.loader.ModelLoaderV1.ModelData.Element element : data.elements) {
                if (("#" + entry.getKey()).equals(element.faces.values().iterator().next().texture)) {
                    Map<String, float[]> uvData = new HashMap<>();
                    for(Entry<String, dev.tr7zw.cosmetizer.loader.ModelLoaderV1.ModelData.Element.Face> texData : element.faces.entrySet()) {
                        uvData.put(texData.getKey(), texData.getValue().uv);
                    }
                    List<CustomizableCube> cubes = CustomizableCubeListBuilder.create().uvData(uvData)
                            .addBox(data.texture_size[0], data.texture_size[1], element.from[0], element.from[1],
                                    element.from[2], element.to[0] - element.from[0], element.to[1] - element.from[1],
                                    element.to[2] - element.from[2])
                            .getCubes();
                    CustomizableModelPart part = new CustomizableModelPart(cubes, new HashMap<>());
                    if(element.rotation != null) {
                        part.setPiviot(element.rotation.origin[0], element.rotation.origin[1], element.rotation.origin[2]);
                        if("x".equals(element.rotation.axis)) {
                            part.xRot = element.rotation.angle*Mth.DEG_TO_RAD;
                        }
                        if("y".equals(element.rotation.axis)) {
                            part.yRot = element.rotation.angle*Mth.DEG_TO_RAD;
                        }
                        if("z".equals(element.rotation.axis)) {
                            part.zRot = element.rotation.angle*Mth.DEG_TO_RAD;
                        }
                    }
                    subset.models.add(part);
                }
            }
            if(!subset.models.isEmpty()) {
                model.modelData.add(subset);
            }
        }
        return model;
    }
    
    private ModelConfigV1 getConfig(String json) {
        if(json == null)return null;
        ModelVersion version = GSON.fromJson(json, ModelVersion.class);
        if(version.version == 1) {
            ModelConfigV1 config = GSON.fromJson(json, ModelConfigV1.class);
            if(config.offset.length != 3 || config.scale.length != 3 || config.bodyPart == null) {
                CosmetizerCore.LOGGER.warn("Config contains invalid values!");
                return null;
            }
            return config;
        }
        return null;
    }

    private static class LoadedModel implements Model {

        List<TextureSubSet> modelData = new ArrayList<>();
        ModelConfigV1 config;

        @Override
        public void render(RenderContext context, PoseStack poseStack, MultiBufferSource multiBufferSource, int light,
                int overlay) {
            poseStack.pushPose();
            // TODO: clean this up, it can be more modular
            BodyPart part = config.bodyPart;
            if(context.getPlayer() == null) {
                part = BodyPart.NONE;
            }
            switch(part) {
            case ARMS:
                poseStack.pushPose();
                context.getPlayerModel().leftArm.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, true);
                poseStack.popPose();
                context.getPlayerModel().rightArm.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, false);
                break;
            case BODY:
                context.getPlayerModel().body.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, false);
                break;
            case HEAD:
                context.getPlayerModel().head.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, false);
                break;
            case LEFT_ARM:
                context.getPlayerModel().leftArm.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, true);
                break;
            case LEFT_LEG:
                context.getPlayerModel().leftLeg.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, true);
                break;
            case LEGS:
                poseStack.pushPose();
                context.getPlayerModel().leftLeg.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, true);
                poseStack.popPose();
                context.getPlayerModel().rightLeg.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, false);
                break;
            case NONE:
                renderModel(context, poseStack, multiBufferSource, light, overlay, false);
                break;
            case RIGHT_ARM:
                context.getPlayerModel().rightArm.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, false);
                break;
            case RIGHT_LEG:
                context.getPlayerModel().rightLeg.translateAndRotate(poseStack);
                renderModel(context, poseStack, multiBufferSource, light, overlay, false);
                break;
            }
            poseStack.popPose();
        }
        
        private void renderModel(RenderContext context, PoseStack poseStack, MultiBufferSource multiBufferSource,
                int light, int overlay, boolean mirror) {
            poseStack.mulPose(Vector3f.ZP.rotation(Mth.PI));
            poseStack.mulPose(Vector3f.YP.rotation(Mth.PI * (config.rotateY/180f)));
            if(mirror)
                poseStack.scale(-1, 1, 1);
            //poseStack.translate(-8.3f/16f, -12f/16f, -8.5f/16f);
            poseStack.translate(config.offset[0]/16f, config.offset[1]/16f, config.offset[2]/16f);
            //poseStack.scale(1.05f, 1.05f, 1.05f);
            poseStack.scale(config.scale[0], config.scale[1], config.scale[2]);
            for(TextureSubSet set : modelData) {
                RenderType renderType = set.texture.getRender(context);
                VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
                for(CustomizableModelPart model : set.models) {
                    model.render(poseStack, vertexConsumer, light, overlay, mirror);
                }
            }
        }

    }

    private static class TextureSubSet {
        TextureProvider texture;
        List<CustomizableModelPart> models = new ArrayList<>();

        public TextureSubSet(TextureProvider texture) {
            this.texture = texture;
        }
    }

    private static class ModelData {
        int[] texture_size = new int[] {16, 16};
        Map<String, String> textures;
        List<Element> elements;

        private class Element {
            float[] from;
            float[] to;
            Rotation rotation;
            Map<String, Face> faces;

            private class Face {
                float[] uv;
                String texture;
            }
        }
        
        private class Rotation {
            float angle;
            String axis;
            float[] origin;
        }
    }
    
    private static class ModelVersion {
        int version;
    }
    
    private static class ModelConfigV1 {
        float[] offset = new float[] {0,0,0};
        float[] scale = new float[] {1,1,1};
        BodyPart bodyPart = BodyPart.NONE;
        float rotateY = 0;
    }

}
