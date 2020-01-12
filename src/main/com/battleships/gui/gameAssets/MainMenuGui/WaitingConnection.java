package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

import java.lang.reflect.GenericArrayType;

/**
 * Overlay if you are waiting for an connection
 *
 * @author Sascha Mößle
 */
public class WaitingConnection extends Menu{
    /**
     * Constant value for Cancel button
     */
    private static final int CANCEL = 0;

    /**
     * Indicates if the game is loaded from a file
     */
    private boolean fromFile;
    /**
     * Creates the Multiplayer menu, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     */
    public WaitingConnection(GuiManager guiManager, Loader loader,boolean fromFile) {
        super(guiManager, loader);
        this.fromFile = fromFile;

        this.createMenu();

        super.CreateTextLabels();
    }

    private void createMenu() {
        super.addBackground();
        buttons.add(new GuiTexture(buttonTexture,new Vector2f(0.5f,0.7f),super.buttonSize));
        super.guiTexts.add(new GUIText("Waiting for Connection",fontSize, font, new Vector2f(0.5f,0.5f), 0.5f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Cancel",fontSize, font, new Vector2f(buttons.get(0).getPositions()), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();

        GameManager.getGuis().add(buttons.get(0));
    }
    public void startMultiplayerGame(){
        if (fromFile){
            GameManager.getNetwork().sendLoad(MultiplayerMenu.getFilename());

        }
        else{
            GameManager.resizeGrid();
            GameManager.getNetwork().sendSize(GameManager.getSettings().getSize());
            GameManager.getLogic().advanceGamePhase();
        }

        clearMenu();
        cleaBackgournd();

    }


    /**
     * Tests if the click was on the cancel button
     * @param gui The gui to test for if the click was on it.
     * @param x xPos of the click (left of screen = 0, right of screen = 1)
     * @param y yPos of the click (top of screen = 0, bottom of screen = 1)
     * @return {@code true} if the click was on one of the button textures, {@code false} else.
     */
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        return false;
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if(buttonClicked == CANCEL) {
            super.clearMenu();
            GameManager.getNetwork().stopConnectionSearch();
            MainMenuManager.setMenu(new MultiplayerMenu(guiManager,loader));
        }
    }
}
