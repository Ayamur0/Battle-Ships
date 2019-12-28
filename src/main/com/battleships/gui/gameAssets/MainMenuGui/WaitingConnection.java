package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class WaitingConnection extends Menu{
    private GuiTexture background;
    private GuiTexture cancel;
    private boolean waiting;

    public WaitingConnection(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        background = new GuiTexture(super.texture,new Vector2f(0.5f,0.5f),new Vector2f(0.5f,0.5f));
        cancel = new GuiTexture(super.texture,new Vector2f(0.5f,0.7f),super.buttonSize);
        guiManager.createClickableGui(cancel,() -> this);

        Inits.getPermanentGuiElements().add(background);

    }
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(1), x, y)) {
            super.buttonClicked = 1;
            return true;
        }
        return false;
    }
    @Override
    protected void clickAction() {

    }
}
