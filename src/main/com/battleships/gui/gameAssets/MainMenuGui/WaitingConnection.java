package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.logic.SaveFile;
import com.battleships.logic.SaveFileManager;
import org.joml.Vector2f;

/**
 * Overlay if you are waiting for an connection
 *
 * @author Sascha Mößle
 */
public class WaitingConnection extends Menu {
    /**
     * Constant value for Cancel button
     */
    private static final int CANCEL = 0;

    /**
     * {@code true} when the opponent has connected
     */
    private boolean opponentConnected;
    /**
     * stores the save File if the multiplayer game is from a save File
     */
    private SaveFile saveFile;

    /**
     * Creates the Multiplayer menu, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     *
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader     Loader needed to load textures
     */
    public WaitingConnection(GuiManager guiManager, Loader loader, SaveFile saveFile) {
        super(guiManager, loader);
        this.saveFile = saveFile;

        this.createMenu();

        super.CreateTextLabels();
    }

    /**
     * @return The boolean indicating if the opponent has connected
     */
    public boolean isOpponentConnected() {
        return opponentConnected;
    }

    /**
     * setts if the opponent is connect or not
     *
     * @param opponentConnected indicates if opponent is connected
     */
    public void setOpponentConnected(boolean opponentConnected) {
        this.opponentConnected = opponentConnected;
    }

    /**
     * Creates the {@link GUIText} as labels, adds {@link GuiTexture} and makes them clickable
     */
    private void createMenu() {
        super.addBackground();
        buttons.add(new GuiTexture(buttonTexture, new Vector2f(0.5f, 0.7f), super.buttonSize));
        super.guiTexts.add(new GUIText("Waiting for Connection", fontSize, font, new Vector2f(0.5f, 0.5f), 0.5f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Cancel", fontSize, font, new Vector2f(buttons.get(0).getPositions()), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.createClickable();

        GameManager.getGuis().add(buttons.get(0));
    }

    /**
     * Setts all settings needed to start a Multiplayer session
     */
    public void startMultiplayerGame() {
        opponentConnected = false;
        if (saveFile != null) {
            GameManager.prepareGame();
            GameManager.getNetwork().sendLoad(MainMenuManager.getMenu().getFileName());
            SaveFileManager.loadSaveFile(saveFile);
        } else {
            GameManager.resizeGrid();
            GameManager.getNetwork().sendSize(GameManager.getSettings().getSize());
            GameManager.getLogic().advanceGamePhase();
            GameManager.getLogic().getTurnHandler().setPlayerTurn(false);
        }
        clearMenu();
        cleaBackgournd();
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if (buttonClicked == CANCEL) {
            super.clearMenu();
            GameManager.getSettings().setOnline(false);
            GameManager.getNetwork().stopConnectionSearch();
            MainMenuManager.setMenu(new MultiplayerMenu(guiManager, loader));
        }
    }
}
