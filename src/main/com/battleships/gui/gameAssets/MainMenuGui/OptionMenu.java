package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class OptionMenu extends Menu {
    public OptionMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
    }

    private void createMenu(){
        super.CreateButtonTextures(1);

        super.guiTexts.add(new GUIText("Back",2.5f, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }

        return false;
    }

    @Override
    protected void clickAction() {
        if (super.buttonClicked == 0){
            super.clearMenu();
            MainMenuManager.setMenu(new PlayMenu(guiManager,loader));
        }

    }
}
