package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.swing.*;

/**
 * Menu to choose if you host a game or connect to a game
 *
 * @author Sascha Mößle
 */
public class MultiplayerMenu extends Menu {
    /**
     * Constant value for Load online game button
     */
    private static final int LOADONLINEGAME = 0;
    /**
     * Constant value for host button
     */
    private static final int HOST = 1;
    /**
     * Constant value for client button
     */
    private static final int CLIENT = 2;
    /**
     * Constant value for back button
     */
    private static final int BACK = 3;

    /**
     * Creates the Multiplayer menu, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     *
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader     Loader needed to load textures
     */
    public MultiplayerMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        this.createMenu();

        CreateTextLabels();
    }

    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    private void createMenu() {
        CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Load online", fontSize, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y - 0.01f), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Game", fontSize, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y + 0.03f), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.guiTexts.add(new GUIText("Host", fontSize, font, new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Client", fontSize, font, new Vector2f(buttons.get(2).getPositions().x, buttons.get(2).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", fontSize, font, new Vector2f(buttons.get(3).getPositions().x, buttons.get(3).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.createClickable();

        GameManager.getGuis().addAll(buttons);
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if (super.buttonClicked == LOADONLINEGAME) {
            GameManager.getSettings().setOnline(true);
            openLoadGameDialog();
        }
        if (super.buttonClicked == HOST) {
            super.clearMenu();
            MainMenuManager.setMenu(new InGameSettingsMenu(super.guiManager, super.loader, 1));

        }
        if (super.buttonClicked == CLIENT) {
            new Thread(new TextInput("Connect", "Enter ip Address")).start();
        }
        if (super.buttonClicked == BACK) {
            super.clearMenu();
            MainMenuManager.setMenu(new PlayMenu(guiManager, loader));
        }
    }

    /**
     * Processes a entered IP by the user by using the last Input made through a {@link TinyFileDialogs}.
     */
    public void processInput() {
        if (userInput != null) {
            if (!GameManager.getNetwork().start(false, userInput))
                new Thread(new ErrorMessage("Error connecting to opponent", "Connection Error")).start();
            super.clearMenu();
            MainMenuManager.setMenu(new MultiplayerMenu(guiManager, loader));
        }
        userInputMade = false;
    }
}
