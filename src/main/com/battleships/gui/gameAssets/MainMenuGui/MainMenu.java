package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import com.battleships.logic.SaveFileManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.UnsupportedEncodingException;

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
     * open the file explorer to chose the save file.
     */
    private JFileChooser fc;

    /**
     * Creates a new {@link JFileChooser}, the main menu, sets the color of the {@link GUIText} and creates the {@link GUIText}as labels on the Buttons.
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
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
    private void createMenu(){
        super.addBackground();

        super.CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Load", fontSize, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Play", fontSize, font,new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Options", fontSize, font,new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit", fontSize, font,new Vector2f(buttons.get(3).getPositions().x,buttons.get(3).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));

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
    protected boolean LoadGame(){
        try {
            FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)","xml");
            File test = new File(SaveFileManager.getJarPath()+"/SaveFiles/");
            fc.setCurrentDirectory(test);
            fc.setFileFilter(xmlfilter);

        }
        catch (UnsupportedEncodingException e){

        }
        fc.setDialogTitle("Select save file");
        fc.showOpenDialog(null);

        String s = fc.getName(fc.getSelectedFile());
        if (s!=null){
            String filename = s.replace(".xml","");
            if(SaveFileManager.loadFromFile(filename)==null){
                super.guiTexts.add(new GUIText("Error loading file", fontSize, font,new Vector2f(0.5f,0.3f), 0.3f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
                return false;
            }
            else{

            }
                return true;
        }
        else
            return false;
    }
    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if(buttonClicked == LOAD) {
            if (LoadGame()) {
                clearMenu();
                cleaBackgournd();
                GameManager.prepareGame();
            }
        }
        if(buttonClicked == PLAY){
            super.clearMenu();
            MainMenuManager.setMenu(new PlayMenu(guiManager,loader));
        }
        if (super.buttonClicked == OPTIONS){
            super.clearMenu();
            MainMenuManager.setMenu(new OptionMenu(guiManager,loader));
        }
        if(buttonClicked == EXIT){
            GLFW.glfwSetWindowShouldClose(WindowManager.getWindow(),true);
        }
    }

}
