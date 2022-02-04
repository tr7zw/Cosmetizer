package dev.tr7zw.cosmetizer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.cosmetizer.screen.LiveEditorScreen;
import dev.tr7zw.cosmetizer.screen.ModelListScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {

    protected PauseScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "createPauseMenu", at = @At("RETURN"))
    private void createPauseMenu(CallbackInfo info) {
        addRenderableWidget(new Button(this.width / 2 - 102, this.height -72, 98, 20,
                new TranslatableComponent("cosmetizer.screen.liveeditor.title"),
                button -> this.minecraft.setScreen(new ModelListScreen(this))));
    }

}
