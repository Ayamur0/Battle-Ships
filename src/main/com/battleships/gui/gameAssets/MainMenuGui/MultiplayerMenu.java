package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

/**
 * Menu to choose if you host a game or connect to a game
 *
 * @author Sascha Mößle
 */
public class MultiplayerMenu extends Menu {
    /**
     * Constant value for host button
     */
    private static final int HOST = 0;
    /**
     * Constant value for client button
     */
    private static final int CLIENT = 1;
    /**
     * Constant value for back button
     */
    private static final int BACK = 2;

    /**
     * Creates the Multiplayer menu, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     */
    public MultiplayerMenu(GuiManager guiManager, Loader loader){
        super(guiManager, loader);

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
    }

    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    private void createMenu() {
        super.CreateButtonTextures(3);

        super.guiTexts.add(new GUIText("Host", 3f, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Client", 3f, font,new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }

    /**
     * Tests if the click was on one of the {@link GuiTexture} in the menu
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
        if(super.isClickOnGui(super.buttons.get(1), x, y)) {
            super.buttonClicked = 1;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(2), x, y)) {
            super.buttonClicked = 2;
            return true;
        }

        return false;
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if (super.buttonClicked == HOST){
            super.clearMenu();
            //TODO Set mode to multiplayer
            MainMenuManager.setMenu(new InGameSettingsMenu(super.guiManager,super.loader,1));
            //TODO Adding host game creation

        }
        if (super.buttonClicked == CLIENT){
            String ip = TinyFileDialogs.tinyfd_inputBox("Connect", "Enter ip Address", "");
            //TODO Adding ip thingi (need logic or network for that
        }
        if (super.buttonClicked == BACK) {
            super.clearMenu();
            MainMenuManager.setMenu(new PlayMenu(guiManager,loader));
        }
    }
}
