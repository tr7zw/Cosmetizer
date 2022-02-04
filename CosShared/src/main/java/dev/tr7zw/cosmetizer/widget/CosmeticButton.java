package dev.tr7zw.cosmetizer.widget;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import dev.tr7zw.cosmetizer.model.Model;
import dev.tr7zw.cosmetizer.model.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

public class CosmeticButton extends Button {

    private ModelData modelData;
    
    public CosmeticButton(int x, int y, int width, int height, ModelData modelData, OnPress onPress) {
        super(x, y, width, height, new TextComponent(""), onPress);
        this.modelData = modelData;
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        fill(poseStack, x, y, x+width, y+height, this.isHovered ? 0xF0000000 : 0x80000000);
        renderModel(poseStack);
        @SuppressWarnings("resource")
        Font font = Minecraft.getInstance().font;
        int l = this.active ? 16777215 : 10526880;
        drawCenteredString(poseStack, font, new TextComponent(modelData.name), this.x + this.width / 2, this.y + (this.height - 8),
                l | Mth.ceil(this.alpha * 255.0F) << 24);
    }
    
    private void renderModel(PoseStack guiPoseStack) {
        int posx =  x;
        int posy = y;
        if(modelData.model == null)return;
        drawEntity(posx + width/2, posy+height/2, 30, 10, 10, modelData.model);
    }
    
    public static class ModelData {
        public String name;
        public Model model;
    }
    
    private void drawEntity(int i, int j, int k, float f, float g, Model model) {
        float h = (float) Math.atan((double) (f / 40.0F));
        float l = (float) Math.atan((double) (g / 40.0F));
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(i, j, 1050.0D);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack matrixStack = new PoseStack();
        matrixStack.translate(0.0D, 0.0D, 1000.0D);
        matrixStack.scale((float) k, (float) k, (float) k);
        matrixStack.mulPose(Vector3f.YP.rotation(Mth.PI* 2 * (System.currentTimeMillis()%10000/2500f)));
        matrixStack.mulPose(Vector3f.ZP.rotation(Mth.PI));
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion2 = Vector3f.XP.rotationDegrees(l * 20.0F);
        quaternion.mul(quaternion2);
        matrixStack.mulPose(quaternion);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion2.conj();
        entityRenderDispatcher.overrideCameraOrientation(quaternion2);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        // Mc renders the player in the inventory without delta, causing it to look
        // "laggy". Good luck unseeing this :)
        model.render(RenderContext.INSTANCE, matrixStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
        //entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0, matrixStack, bufferSource, 15728880);
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
    
}
