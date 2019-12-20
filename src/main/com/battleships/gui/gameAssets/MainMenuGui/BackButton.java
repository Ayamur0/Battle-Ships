package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.renderingEngine.Loader;

public class BackButton extends MainMenuButton {
    private int prevMenu; //0=Main Menu, 1=Play Menu

    public BackButton(GuiManager guiManager, Loader loader, int prevMenu){
        super(guiManager,loader);
        this.prevMenu = prevMenu;
    }

    @Override
    protected void createButtons() {

    }

    @Override
    protected void createLabels() {

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
