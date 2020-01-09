package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
/**
 * Contains the buttons to chose what game mode you want to play
 *
 * @author Sascha Mößle
 */
public class PlayMenu extends Menu {
    /**
     * Constant value for ai vs ai button
     */
    private static final int AIVSAI = 0;
    /**
     * Constant value for Singleplayer button
     */
    private static final int SINGLEPLAYER = 1;
    /**
     * Constant value for Multiplayer button
     */
    private static final int MULTIPLAYER = 2;
    /**
     * Constant value for back button
     */
    private static final int BACK = 3;

    /**
     * Creates the menu, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     */
    public PlayMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
    }
    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    private void createMenu(){

        super.CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Ai VS Ai",2.5f, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Singleplayer", 2.5f, font, new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Multiplayer", 2.5f, font,new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(buttons.get(3).getPositions().x,buttons.get(3).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }
    /**
     * Tests if the click was on one of the {@link GuiTexture} in the menu
     * @param gui The gui to test for if the click was on it.
     * @param x xPos of the click (left of screen = 0, right of screen = 1)
     * @param y yPos of the click (top of screen = 0, bottom of screen = 1)
     * @return {@code true} if the click was on one of the button textures, {@code false} else.
     */
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
    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if(buttonClicked == AIVSAI) {
            super.clearMenu();
            MainMenuManager.setMenu(new AiVsAiMenu(guiManager,loader));
            //TODO open ai vs ai Settings
        }
        if(buttonClicked == SINGLEPLAYER){
            super.clearMenu();
            MainMenuManager.setMenu(new InGameSettingsMenu(guiManager,loader,0));
        }
        if (super.buttonClicked == MULTIPLAYER){
            super.clearMenu();
            MainMenuManager.setMenu(new MultiplayerMenu(guiManager,loader));
        }
        if (super.buttonClicked == BACK){
            super.clearMenu();
            MainMenuManager.setMenu(new MainMenu(guiManager,loader));
        }
    }

}
