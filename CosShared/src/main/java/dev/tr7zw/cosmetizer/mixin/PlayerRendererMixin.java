package dev.tr7zw.cosmetizer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.cosmetizer.CosmetizerCore;
import dev.tr7zw.cosmetizer.loader.LocalProvider;
import dev.tr7zw.cosmetizer.renderlayer.PlayerFeatureRenderer;
import dev.tr7zw.cosmetizer.screen.LiveEditorScreen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin
        extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onCreate(CallbackInfo info) {
        try {
            this.addLayer(new PlayerFeatureRenderer(this, CosmetizerCore.loader.loadModel(new LocalProvider(), "backpack_creeper")));
            this.addLayer(new PlayerFeatureRenderer(this, CosmetizerCore.loader.loadModel(new LocalProvider(), "amethyst_crown")));
            CosmetizerCore.loader.loadModel(new LocalProvider(), "infinity_gauntlet");
            LiveEditorScreen.modelRenderer = new PlayerFeatureRenderer(this, null);
            this.addLayer(LiveEditorScreen.modelRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
