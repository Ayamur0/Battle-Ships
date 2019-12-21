package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class DisableSymbols extends GuiClickCallback {

    public static final int ANIMATION = 0;
    public static final int SOUND = 1;

    private static final String textureAtlas = "DisableSymbols.png";
    private List<GuiTexture> currentSymbols = new ArrayList<>();
    private int buttonClicked;
    private GameManager gameManager;

    /**
     * Creates the gui-elements for the symbols that show whether sound or animations are enabled or disabled.
     * @param loader - Loader needed to load textures
     * @param guiManager - GuiManager that should handle the click function of these guis.
     * @param guis - List of guis that these should be added to. This list needs to be rendered so these guis show on screen.
     */
    public DisableSymbols (Loader loader, GuiManager guiManager, List<GuiTexture> guis, GameManager gameManager){
        this.gameManager = gameManager;
        gameManager.setDisableSymbols(this);
        int texture = loader.loadTexture(textureAtlas);
        GuiTexture animation = new GuiTexture(texture, new Vector2f(0.89f, 0.9f));
        animation.getScale().x /= 2;
        animation.getScale().y /= 2;
        animation.setRows(2);
        guiManager.createClickableGui(animation, () -> this);
        GuiTexture sound = new GuiTexture(texture, new Vector2f(animation.getPositions().x + animation.getScale().x + 0.02f, 0.9f));
        sound.getScale().x /= 2;
        sound.getScale().y /= 2;
        sound.setRows(2);
        sound.setOffsetY(1);
        guiManager.createClickableGui(sound, () -> this);
        currentSymbols.add(animation);
        currentSymbols.add(sound);
        guis.addAll(currentSymbols);
    }

    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(currentSymbols.get(0), x, y)) {
            buttonClicked = 0;
            return true;
        }
        if(super.isClickOnGui(currentSymbols.get(1), x, y)) {
            buttonClicked = 1;
            return true;
        }
        return false;
    }

    @Override
    protected void clickAction() {
        if(buttonClicked == 0) {
            gameManager.toggleAnimations();
        }
        if(buttonClicked == 1){}
            //TODO sound toggle
    }

    public void toggleSymbol(int symbol){
        currentSymbols.get(symbol).setOffsetX((currentSymbols.get(symbol).getOffsetX() * currentSymbols.get(symbol).getRows() + 1) % 2);
    }
}
