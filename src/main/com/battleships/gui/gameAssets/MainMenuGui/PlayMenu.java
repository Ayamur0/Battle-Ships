package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class PlayMenu extends Menu {

    public PlayMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
    }

    private void createMenu(){

        super.CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Ai VS Ai",2.5f, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Singleplayer", 2.5f, font, new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Multiplayer", 2.5f, font,new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(buttons.get(3).getPositions().x,buttons.get(3).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(1), x, y)) {
            super.buttonClicked = 1;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(2), x, y)) {
            super.buttonClicked = 2;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(3), x, y)) {
            super.buttonClicked = 3;
            return true;
        }
        return false;
    }

    @Override
    protected void clickAction() {
        if(buttonClicked == 0) {
            super.clearMenu();
            MainMenuManager.setMenu(new AiVsAiMenu(guiManager,loader));
            //TODO open ai vs ai Settings
        }
        if(buttonClicked == 1){
            super.clearMenu();
            MainMenuManager.setMenu(new InGameSettingsMenu(guiManager,loader,0));
        }
        if (super.buttonClicked == 2){
            super.clearMenu();
            MainMenuManager.setMenu(new MultiplayerMenu(guiManager,loader));
        }
        if (super.buttonClicked == 3){
            super.clearMenu();
            MainMenuManager.setMenu(new MainMenu(guiManager,loader));
        }
    }

}
