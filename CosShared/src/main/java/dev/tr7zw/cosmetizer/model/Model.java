package dev.tr7zw.cosmetizer.model;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;

public interface Model {

    public void render(RenderContext context, PoseStack poseStack, MultiBufferSource multiBufferSource,  int light, int overlay);
    
}
