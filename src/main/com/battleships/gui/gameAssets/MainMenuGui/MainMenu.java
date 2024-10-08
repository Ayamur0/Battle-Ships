package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;

/**
 * The main menu for the game
 *
 * @author Sascha Mößle
 */
public class MainMenu extends Menu {
    /**
     * Constant value for load button
     */
    private static final int LOAD = 0;
    /**
     * Constant value for play button
     */
    private static final int PLAY = 1;
    /**
     * Constant value for options button
     */
    private static final int OPTIONS = 2;
    /**
     * Constant value for exit button
     */
    private static final int EXIT = 3;

    /**
     * Creates a new {@link JFileChooser}, the main menu, sets the color of the {@link GUIText} and creates the {@link GUIText}as labels on the Buttons.
     *
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader     Loader needed to load textures
     */
    public MainMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        fc = new JFileChooser();

        this.createMenu();

        CreateTextLabels();
    }

    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    private void createMenu() {
        super.addBackground();

        super.CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Load", fontSize, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Play", fontSize, font, new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Options", fontSize, font, new Vector2f(buttons.get(2).getPositions().x, buttons.get(2).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit", fontSize, font, new Vector2f(buttons.get(3).getPositions().x, buttons.get(3).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.createClickable();
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if (buttonClicked == LOAD) {
            openLoadGameDialog();
        }
        if (buttonClicked == PLAY) {
            super.clearMenu();
            MainMenuManager.setMenu(new PlayMenu(guiManager, loader));
        }
        if (super.buttonClicked == OPTIONS) {

            super.clearMenu();
            MainMenuManager.setMenu(new OptionMenu(guiManager, loader));
        }
        if (buttonClicked == EXIT) {
            GLFW.glfwSetWindowShouldClose(WindowManager.getWindow(), true);
        }
    }
}
