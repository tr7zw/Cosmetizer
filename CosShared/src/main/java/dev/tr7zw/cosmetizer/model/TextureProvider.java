package dev.tr7zw.cosmetizer.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

public interface TextureProvider {

    public RenderType getRender(RenderContext context);
    
    public static final TextureProvider PLAYERSKINCUTOUT = new TextureProvider() {
        
        @Override
        public RenderType getRender(RenderContext context) {
            if(context.getPlayer() != null) {
                return RenderType.entityCutout(context.getPlayer().getSkinTextureLocation());
            }
            return RenderType.entityCutout(DefaultPlayerSkin.getDefaultSkin());
        }
    };
    
    public static final TextureProvider PLAYERSKINTRANSLUCENT = new TextureProvider() {
        
        @Override
        public RenderType getRender(RenderContext context) {
            if(context.getPlayer() != null) {
                return RenderType.entityTranslucent(context.getPlayer().getSkinTextureLocation());
            }
            return RenderType.entityTranslucent(DefaultPlayerSkin.getDefaultSkin());
        }
    };
    
    public static final TextureProvider ENDPORTAL = new TextureProvider() {
        
        @Override
        public RenderType getRender(RenderContext context) {
            return RenderType.endPortal();
        }
    };
    
    public static final TextureProvider ENDGATEWAY = new TextureProvider() {
        
        @Override
        public RenderType getRender(RenderContext context) {
            return RenderType.endGateway();
        }
    };
    
    public static TextureProvider getCutout(ResourceLocation location) {
        return (context) -> {
            return RenderType.entityCutout(location);  
        };
    }
    
    public static TextureProvider getCutoutNoCull(ResourceLocation location) {
        return (context) -> {
            return RenderType.entityCutoutNoCull(location);  
        };
    }
    
}
