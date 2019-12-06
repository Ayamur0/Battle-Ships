package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.renderingEngine.Loader;

public class ClientButton extends MainMenuButton {
    public ClientButton(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);
    }

    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        //TODO


        TextMaster.clear();
        super.CreateTextLabels();
    }
}
