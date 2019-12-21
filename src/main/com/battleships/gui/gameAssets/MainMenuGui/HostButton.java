package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.renderingEngine.Loader;

public class HostButton extends MainMenuButton {
    public HostButton(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);
    }

    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        //TODO creat a online game


        TextMaster.clear();
        super.CreateTextLabels();
    }
}
