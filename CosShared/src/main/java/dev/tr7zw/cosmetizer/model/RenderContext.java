package dev.tr7zw.cosmetizer.model;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public interface RenderContext {

    public void dispose();
    
    public AbstractClientPlayer getPlayer();
    
    public void setPlayer(AbstractClientPlayer player);
    
    public PlayerModel<AbstractClientPlayer> getPlayerModel();
    
    public void setPlayerModel(PlayerModel<AbstractClientPlayer> model);
    
    public static final RenderContext INSTANCE = new RenderContext() {
        
        private AbstractClientPlayer player = null;
        private PlayerModel<AbstractClientPlayer> playerModel = null;
        
        @Override
        public void setPlayer(AbstractClientPlayer player) {
            this.player = player;
        }
        
        @Override
        public AbstractClientPlayer getPlayer() {
            return player;
        }
        
        @Override
        public void dispose() {
            player = null;
            playerModel = null;
        }

        @Override
        public PlayerModel<AbstractClientPlayer> getPlayerModel() {
            return playerModel;
        }

        @Override
        public void setPlayerModel(PlayerModel<AbstractClientPlayer> model) {
            this.playerModel = model;
        }
    };
    
}
