package dev.tr7zw.cosmetizer.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.tr7zw.cosmetizer.CosmetizerCore;
import dev.tr7zw.cosmetizer.loader.LocalProvider;
import dev.tr7zw.cosmetizer.model.Model;
import dev.tr7zw.cosmetizer.widget.CosmeticButton;
import dev.tr7zw.cosmetizer.widget.CosmeticButton.ModelData;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class ModelListScreen extends Screen {

    private Screen parent;
    private LocalProvider provider = new LocalProvider();
    
    
    public ModelListScreen(Screen parent) {
        super(new TranslatableComponent("cosmetizer.screen.modellist.title"));

    }
    
    @Override
    protected void init() {
        super.init();
        List<ModelData> models = new ArrayList<>();
        Iterator<String> ids = provider.getIds();
        while(ids.hasNext()) {
            try {
                String name = ids.next();
                Model model = CosmetizerCore.loader.loadModel(provider, name);
                ModelData data = new ModelData();
                data.name = name;
                data.model = model;
                if(model != null)
                    models.add(data);
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        updateButtons(models);
    }
    
    public void updateButtons(List<ModelData> models) {
        super.clearWidgets();
        int id = 0;
        int perRow = this.width / 120;
        for(ModelData model : models) {
            addRenderableWidget(new CosmeticButton((id%perRow) * (this.width / perRow) + 10, 30 + (id/perRow)*100, 100, 80, model, (event) -> {
                this.minecraft.setScreen(new LiveEditorScreen(this, model.name));
            }));
            id++;
        }
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

}
