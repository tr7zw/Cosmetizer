package dev.tr7zw.cosmetizer.screen;

import java.io.IOException;

import com.google.common.base.Objects;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import dev.tr7zw.cosmetizer.CosmetizerCore;
import dev.tr7zw.cosmetizer.loader.LocalProvider;
import dev.tr7zw.cosmetizer.renderlayer.PlayerFeatureRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;

public class LiveEditorScreen extends Screen {

    private Screen parent;
    public static PlayerFeatureRenderer modelRenderer = null;
    private String modelName;
    private LocalProvider provider = new LocalProvider();
    private String lastModelString = null;
    private String lastConfigString = null;
    private long cooldown = 0;
    
    public LiveEditorScreen(Screen parent, String modelName) {
        super(new TranslatableComponent("cosmetizer.screen.liveeditor.title"));
        this.modelName = modelName;
    }
    
    @Override
    public void onClose() {
        if(modelRenderer != null) {
            modelRenderer.model = null;
        }
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(PoseStack poseStack, int xMouse, int yMouse, float f) {
        //renderBackground(poseStack, 0);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(poseStack, xMouse, yMouse, f);
        if (this.minecraft.level == null) return;
        if(modelName == null || modelRenderer == null) {
            onClose();
            return;
        }
        
        try {
            if(cooldown < System.currentTimeMillis()) {
                cooldown = System.currentTimeMillis() + 1000;
                String mString = provider.getModelData(modelName);
                String cString = provider.getModelConfig(modelName);
                System.out.println("Update: " + mString + " " + cString + " " + modelRenderer.model);
                if(true){//(!(!Objects.equal(lastConfigString, cString) || !Objects.equal(lastModelString, mString))) {
                    lastConfigString = cString;
                    lastModelString = mString;
                    modelRenderer.model = CosmetizerCore.loader.loadModel(provider, modelName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int x = minecraft.getWindow().getGuiScaledWidth()/2;
        int y = minecraft.getWindow().getGuiScaledHeight()-45;
        int size = (int) (60f * (minecraft.getWindow().getGuiScaledHeight() / 200f));
        int lookX = x - xMouse;
        int lookY = y - 80 - yMouse;
        // Prevent the model from clipping into the back of the gui^^
        //lookY = Math.min(lookY, 10);
        InventoryScreen.renderEntityInInventory(x, y, size, lookX, lookY, this.minecraft.player);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        System.out.println(i);
        return super.keyPressed(i, j, k);
    }

  /*  private void renderEntityInInventory(int x, int y, int size, float lookX, float lookY, LivingEntity livingEntity) {
        float h = (float) Math.atan((lookX / 40.0F));
        float l = (float) Math.atan((lookY / 40.0F));
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(x, y, 1050.0D);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        poseStack2.translate(0.0D, 0.0D, 1000.0D);
        poseStack2.scale(size, size, size);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion2 = Vector3f.XP.rotationDegrees(l * 20.0F);
        quaternion.mul(quaternion2);
        poseStack2.mulPose(quaternion);
        float m = livingEntity.yBodyRot;
        float n = livingEntity.getYRot();
        float o = livingEntity.getXRot();
        float p = livingEntity.yHeadRotO;
        float q = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + h * 20.0F;
        livingEntity.setYRot(180.0F + h * 40.0F);
        livingEntity.setXRot(-l * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion2.conj();
        entityRenderDispatcher.overrideCameraOrientation(quaternion2);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F,
                poseStack, bufferSource, 15728880));
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = m;
        livingEntity.setYRot(n);
        livingEntity.setXRot(o);
        livingEntity.yHeadRotO = p;
        livingEntity.yHeadRot = q;
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }*/
    
}
