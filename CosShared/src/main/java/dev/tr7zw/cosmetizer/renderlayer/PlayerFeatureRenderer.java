package dev.tr7zw.cosmetizer.renderlayer;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.cosmetizer.model.Model;
import dev.tr7zw.cosmetizer.model.RenderContext;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class PlayerFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public Model model;
    
    public PlayerFeatureRenderer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, Model model) {
        super(renderLayerParent);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light,
            AbstractClientPlayer paramT, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
            float paramFloat5, float paramFloat6) {
        RenderContext.INSTANCE.setPlayer(paramT);
        RenderContext.INSTANCE.setPlayerModel(getParentModel());
        if(model != null)
            model.render(RenderContext.INSTANCE, poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY);
        RenderContext.INSTANCE.dispose();
    }

}
