package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;

public class AiVsAiButton extends MainMenuButton {
    public AiVsAiButton(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);
    }

    @Override
    protected void clickAction() {
        TextMaster.clear();
        //Will be changed in final version
        //Inits.setGlobalGameState(1);
        //MenuTest.GlobalGameState = 1;
        //TODO start a Ai vs Ai game
        System.out.println("Ai VS Ai");
    }
}
