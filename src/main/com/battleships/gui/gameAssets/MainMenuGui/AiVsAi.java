package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.main.MenuTest;
import com.battleships.gui.renderingEngine.Loader;

public class AiVsAi extends MainMenuButton {
    //TODO
    public AiVsAi(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);
    }

    @Override
    protected void clickAction() {
        TextMaster.clear();
        MenuTest.GlobalGameState = 1;
        //TODO
    }
}
