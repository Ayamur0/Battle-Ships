package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.logic.SaveFileManager;
import org.joml.Vector2f;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.swing.*;

/**
 * Overlay if ESC was pressed in the game
 *
 * @author Sascha Mößle
 */
public class ESCMenu extends Menu {
    /**
     * Constant value for save button
     */
    private static final int SAVE = 0;
    /**
     * Constant value for resume button
     */
    private static final int RESUME = 1;
    /**
     * Constant value for Play as AI button
     */
    private static final int PLAYAI = 2;
    /**
     * Constant value for exit button
     */
    private static final int EXIT = 3;
    /**
     * Indicates if the {@link ESCMenu} is active or not
     */
    private static boolean active;
    /**
     * Indicates if the player is a AI
     */
    private static boolean isPlayerAI;
    /**
     * @return The boolean indicating if the player is a AI
     */
    public static boolean isIsPlayerAI() {
        return isPlayerAI;
    }

    /**
     * @return The boolean indicating if the {@link ESCMenu} is active
     */
    public static boolean isActive() {
        return active;
    }

    /**
     * Sets the variable that indicates if the {@link ESCMenu} is active
     *
     * @param active indicates if the {@link ESCMenu} is active
     */
    public static void setActive(boolean active) {
        ESCMenu.active = active;
    }

    /**
     * Sets the variable that indicates if the player is a AI
     *
     * @param isPlayerAI indicates if the player is a AI
     */
    public static void setIsPlayerAI(boolean isPlayerAI) {
        ESCMenu.isPlayerAI = isPlayerAI;
    }

    /**
     * Creates the menu when you press ESC, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     *
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader     Loader needed to load textures
     */
    public ESCMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        active = true;

        if(GameManager.getShipCounter() != null)
            GameManager.getShipCounter().hide();
        if (GameManager.getShipSelector() != null)
            GameManager.getShipSelector().hide();

        super.addBackground();

        this.createMenu();

        CreateTextLabels();

    }

    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    private void createMenu() {

        super.CreateButtonTextures(4);

        GameManager.getGuis().addAll(buttons);


        super.guiTexts.add(new GUIText("Save", fontSize, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Resume", fontSize, font, new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        if (isPlayerAI)
            super.guiTexts.add(new GUIText("Play yourself", fontSize, font, new Vector2f(buttons.get(2).getPositions().x, buttons.get(2).getPositions().y), 0.2f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        else
            super.guiTexts.add(new GUIText("Play as AI", fontSize, font, new Vector2f(buttons.get(2).getPositions().x, buttons.get(2).getPositions().y), 0.2f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit", fontSize, font, new Vector2f(buttons.get(3).getPositions().x, buttons.get(3).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.createClickable();
    }
    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if (buttonClicked == SAVE) {
            long time = System.currentTimeMillis();
            if (!GameManager.getSettings().isOnline()) {
                new Thread(new TextInput("Save", "Enter file name")).start();
            } else {
                if (SaveFileManager.saveToFile(String.valueOf(time))) {
                    GameManager.getNetwork().closeConnection();
                    GameManager.getNetwork().sendSave(String.valueOf(time));
                    GameManager.getMainMenuManager().backToMainMenu();
                } else
                    JOptionPane.showMessageDialog(null,"Error saving file","Save Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        if (buttonClicked == RESUME) {
            if(GameManager.getShipCounter() != null)
                GameManager.getShipCounter().hide();
            if (GameManager.getShipSelector() != null)
                GameManager.getShipSelector().hide();
            active = false;
            super.clearMenu();
            super.cleaBackgournd();
        }
        if (super.buttonClicked == PLAYAI) {
            if (GameManager.getLogic().getGameState()==GameManager.SHIPLACING){
                JOptionPane.showMessageDialog(null,"AI not avaiable in ship placing phase.\nPlease place your ships first","Playing as AI",JOptionPane.ERROR_MESSAGE);
            }
            else if (isIsPlayerAI()) {
                active=false;
                GameManager.getSettings().setAiLevelP(-1);
                isPlayerAI = false;
                if(GameManager.getShipCounter() != null)
                    GameManager.getShipCounter().hide();
                if (GameManager.getShipSelector() != null)
                    GameManager.getShipSelector().hide();
                super.clearMenu();
                super.cleaBackgournd();
            } else {
                super.clearMenu();
                MainMenuManager.setMenu(new AiPlayerChooserMenu(guiManager, loader));
            }
        }
        if (buttonClicked == EXIT) {
            active = false;
            super.clearMenu();
            if (GameManager.getSettings().isOnline())
                GameManager.getNetwork().closeConnection();
            GameManager.getLogic().setGameState(GameManager.MENU);
            GameManager.getMainMenuManager().backToMainMenu();
        }
    }

    /**
     * Processes a entered filename by the user by using the last Input made through a {@link TinyFileDialogs}.
     */
    public void processInput() {
        if (SaveFileManager.saveToFile(userInput))
            GameManager.getMainMenuManager().backToMainMenu();
        else
            JOptionPane.showMessageDialog(null,"Error saving file","Save Error",JOptionPane.ERROR_MESSAGE);
        userInputMade = false;
    }
}
