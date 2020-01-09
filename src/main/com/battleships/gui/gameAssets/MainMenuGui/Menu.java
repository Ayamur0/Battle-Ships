package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all functions and constants for the menus
 *
 * @author Sascha Mößle
 */
public abstract class Menu extends GuiClickCallback {
    /**
     * Standard size for all buttons
     */
    protected Vector2f buttonSize = new Vector2f(0.16f, 0.12f);
    /**
     * Standard position to start for all buttons
     */
    protected Vector2f standardButtonPos = new Vector2f(0.5f, 0.4f);
    /**
     * Standard gap between all buttons
     */
    protected Float buttonGap = 0.15f;
    /**
     * GuiManager that handles guis with click functions.
     */
    protected GuiManager guiManager;
    /**
     * Loader for loading models and textures.
     */
    protected Loader loader;
    /**
     * Main font used for all labels on buttons
     */
    protected FontType font;
    /**
     * Main button texture used in all menus
     */
    protected static int texture;
    /**
     * Texture behind the Buttons in the background
     */
    protected static int scrollBackground;
    /**
     * Main outline color used for the {@link GUIText}
     */
    protected Vector3f outlineColor = new Vector3f(1.0f, 0.0f, 0.0f);
    /**
     * List containing all {@link GuiTexture}s on the screen.
     */
    protected List<GuiTexture> buttons = new ArrayList<>();
    /**
     * List containing all {@link GUIText}s on the screen.
     */
    protected List<GUIText> guiTexts = new ArrayList<>();
    /**
     * Used to determine which button was the last one that was clicked.
     */
    protected int buttonClicked;

    /**
     * Loads all {@link GuiTexture} needed and the font for the {@link GUIText}
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     */
    public Menu(GuiManager guiManager, Loader loader) {
        this.guiManager = guiManager;
        this.loader = loader;
        if (GameManager.getPirateFont()==null)
            this.font = new FontType(loader.loadFontTexture("font/pirate.png"), "pirate");
        else
            this.font=GameManager.getPirateFont();
        if (texture == 0)
            texture = loader.loadTexture("WoodButton.png");
        if (scrollBackground == 0)
            scrollBackground = loader.loadTexture("scroll.png");
    }

    /**
     * creates the amount of {@link GuiTexture} needed.
     * @param anzahl how many button textures should be created
     */
    protected void CreateButtonTextures(int anzahl){
        GuiTexture scroll = new GuiTexture(scrollBackground,new Vector2f(0.5f,0.54f),new Vector2f(0.65f,1f));
        //TODO Change scale and position
        GameManager.getGuis().add(scroll);
        buttons.add(new GuiTexture(texture, standardButtonPos, buttonSize));
        for (int i = 0;i < anzahl-1; i++){
            buttons.add(new GuiTexture(texture,new Vector2f(buttons.get(i).getPositions().x,buttons.get(i).getPositions().y+buttonGap),buttonSize));
        }
        GameManager.getGuis().addAll(buttons);
    }

    /**
     * adds all labels the the {@link TextMaster} to render
     */
    protected void CreateTextLabels() {
        SetTextColor();
        for (GUIText gui : guiTexts) {
            TextMaster.addText(gui);
        }
    }

    /**
     * sets the color for the {@link GUIText}
     */
    protected void SetTextColor() {
        for (GUIText gui : guiTexts) {
            gui.setColor(1f, 1f, 1f);
        }
    }

    /**
     * makes the {@link GuiTexture} clickable
     */
    protected void createClickable() {
        for (GuiTexture i : buttons) {
            guiManager.createClickableGui(i, () -> this);
        }
    }

    /**
     * removes the {@link GuiTexture} and {@link GUIText} from the screen
     */
    protected void clearMenu(){
        for (GUIText gui : guiTexts){
            TextMaster.removeText(gui);
            gui.remove();
        }
        for (GuiTexture gui : buttons){
            guiManager.removeClickableGui(gui);
            GameManager.getGuis().remove(gui);
        }
    }
}
