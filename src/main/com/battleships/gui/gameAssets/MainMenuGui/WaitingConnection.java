package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class WaitingConnection extends Menu{
    private GuiTexture background;
    private boolean waiting;

    public WaitingConnection(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        background = new GuiTexture(super.texture,new Vector2f(0.5f,0.5f),new Vector2f(0.5f,0.5f));
        buttons.add(new GuiTexture(super.texture,new Vector2f(0.5f,0.7f),super.buttonSize));

        super.guiTexts.add(new GUIText("Waiting for Connection",2.5f, font, new Vector2f(background.getPositions().x,background.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.createClickable();

        Inits.getPermanentGuiElements().add(background);

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

    }
}
