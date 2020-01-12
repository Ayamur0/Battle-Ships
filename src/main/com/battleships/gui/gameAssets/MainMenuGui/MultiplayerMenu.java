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
 * Menu to choose if you host a game or connect to a game
 *
 * @author Sascha Mößle
 */
public class MultiplayerMenu extends Menu {
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

    public static String getFilename() {
        return filename;
    }

    private static String filename;

    /**
     * Creates the Multiplayer menu, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     */
    public MultiplayerMenu(GuiManager guiManager, Loader loader){
        super(guiManager, loader);

        this.createMenu();

        CreateTextLabels();
    }

    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    private void createMenu() {
        CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Load online", fontSize, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y-0.01f), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Game", fontSize, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y+0.03f), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));

        super.guiTexts.add(new GUIText("Host", fontSize, font, new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Client", fontSize, font,new Vector2f(buttons.get(2).getPositions().x, buttons.get(2).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", fontSize, font,new Vector2f(buttons.get(3).getPositions().x,buttons.get(3).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();

        GameManager.getGuis().addAll(buttons);
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
        if (super.buttonClicked == LOADONLINEGAME) {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);
            filename = fc.getName(fc.getSelectedFile());
            if (filename != null){
                filename.replace(".xml","");
                if(SaveFileManager.loadFromFile(filename) == null) {
                    GameManager.getSettings().setOnline(true);
                    MainMenuManager.setMenu(new WaitingConnection(guiManager, loader));
                }
            }
        }
        if (super.buttonClicked == HOST){
            super.clearMenu();
            MainMenuManager.setMenu(new InGameSettingsMenu(super.guiManager,super.loader,1));

        }
        if (super.buttonClicked == CLIENT){
            String ip = TinyFileDialogs.tinyfd_inputBox("Connect", "Enter ip Address", "");
            if (ip != null) {
                GameManager.getNetwork().start(false,ip);
                super.clearMenu();
                MainMenuManager.setMenu(new WaitingConnection(guiManager, loader));
            }
        }
        if (super.buttonClicked == BACK) {
            super.clearMenu();
            MainMenuManager.setMenu(new PlayMenu(guiManager,loader));
        }
    }
}
