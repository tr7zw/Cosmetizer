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
import net.minecraft.world.phys.Vec3;

public class LiveEditorScreen extends Screen {

    private Screen parent;
    public static PlayerFeatureRenderer modelRenderer = null;
    private String modelName;
    private LocalProvider provider = new LocalProvider();
    private String lastModelString = null;
    private String lastConfigString = null;
    private long cooldown = 0;
    private int rotationX = 0;
    private int rotationY = 0;
    
    public LiveEditorScreen(Screen parent, String modelName) {
        super(new TranslatableComponent("cosmetizer.screen.liveeditor.title"));
        this.modelName = modelName;
        this.parent = parent;
    }
    
    @Override
    public void onClose() {
        if(modelRenderer != null) {
            modelRenderer.model = null;
        }
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(PoseStack poseStack, int xMouse, int yMouse, float delta) {
        //renderBackground(poseStack, 0);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(poseStack, xMouse, yMouse, delta);
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
        int y = minecraft.getWindow().getGuiScaledHeight()/2;
        int size = (int) (60f * (minecraft.getWindow().getGuiScaledHeight() / 200f));
        // Prevent the model from clipping into the back of the gui^^
        //lookY = Math.min(lookY, 10);
        drawEntity(x, y, size, rotationX, rotationY, this.minecraft.player, delta);
        drawCenteredString(poseStack, this.font, new TranslatableComponent("cosmetizer.screen.liveeditor.hint"), this.width / 2, this.height - 20, 16777215);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        System.out.println(i);
        if(i == 263) { //left
            rotationX--;
        }
        if(i == 262) { //right
            rotationX++;
        }
        if(i == 264) { //down
            rotationY--;
        }
        if(i == 265) { //up
            rotationY++;
        }
        return super.keyPressed(i, j, k);
    }

    // Modified version from InventoryScreen
    private void drawEntity(int x, int y, int size, float lookX, float lookY, LivingEntity livingEntity, float delta) {
        float rotationModifyer = 3;
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate(x, y, 1050.0D);
        System.out.println("Rotation " + (lookY * rotationModifyer) + " " + lookX);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack matrixStack = new PoseStack();
        matrixStack.translate(0.0D, 1, 1000.0D);
        matrixStack.scale((float) size, (float) size, (float) size);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion2 = Vector3f.XP.rotationDegrees(lookY * rotationModifyer);
        quaternion.mul(quaternion2);
        matrixStack.mulPose(quaternion);
        matrixStack.translate(0.0D, -1, 0D);
        float yBodyRot = livingEntity.yBodyRot;
        float yRot = livingEntity.getYRot();
        float yRotO = livingEntity.yRotO;
        float yBodyRotO = livingEntity.yBodyRotO;
        float xRot = livingEntity.getXRot();
        float xRotO = livingEntity.xRotO;
        float yHeadRotO = livingEntity.yHeadRotO;
        float yHeadRot = livingEntity.yHeadRot;
        Vec3 vel = livingEntity.getDeltaMovement();
        livingEntity.yBodyRot = 180.0F + (lookX*rotationModifyer);
        livingEntity.setYRot(180.0F + (lookX*rotationModifyer));
        livingEntity.yBodyRotO = livingEntity.yBodyRot;
        livingEntity.yRotO = livingEntity.getYRot();
        livingEntity.setDeltaMovement(Vec3.ZERO);
        livingEntity.setXRot(0);
        livingEntity.xRotO = livingEntity.getXRot();
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion2.conj();
        entityRenderDispatcher.overrideCameraOrientation(quaternion2);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        // Mc renders the player in the inventory without delta, causing it to look
        // "laggy". Good luck unseeing this :)
        entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixStack, bufferSource, 15728880);
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = yBodyRot;
        livingEntity.yBodyRotO = yBodyRotO;
        livingEntity.setYRot(yRot);
        livingEntity.yRotO = yRotO;
        livingEntity.setXRot(xRot);
        livingEntity.xRotO = xRotO;
        livingEntity.yHeadRotO = yHeadRotO;
        livingEntity.yHeadRot = yHeadRot;
        livingEntity.setDeltaMovement(vel);
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
    
}
