package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.renderingEngine.Loader;

public class BackButton extends MainMenuButton {
    public BackButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        if(prevMenu==0){
            new MainMenu(guiManager,loader).clickAction();
        }
        else if(prevMenu==1){
            new PlayButton(guiManager,loader).clickAction();
        }
    }
}
