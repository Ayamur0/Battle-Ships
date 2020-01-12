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
    protected Vector2f standardButtonPos = new Vector2f(0.5f, 0.38f);
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
     * Main font size for {@link GUIText}
     */
    protected float fontSize = 2.5f;
    /**
     * Main button texture used in all menus
     */
    protected static int buttonTexture;
    /**
     * Texture behind the Buttons in the background
     */
    protected static int scrollBackground;
    /**
     * List containing all {@link GuiTexture}s behind the buttons
     */
    protected static List<GuiTexture> backgounds = new ArrayList<>();
    /**
     * Texture above all buttons
     */
    protected static int icon;
    /**
     * Main outline color used for the {@link GUIText}
     */
    protected Vector3f outlineColor = new Vector3f(0.63f, 0.63f, 0.63f);
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
        if (buttonTexture == 0)
            buttonTexture = loader.loadTexture("WoodButton.png");
        if (scrollBackground == 0)
            scrollBackground = loader.loadTexture("scroll.png");
        if (icon == 0)
            icon = loader.loadTexture("StartIcon.png");
        if (backgounds.size()==0)
            addBackground();
    }

    /**
     * creates the amount of {@link GuiTexture} needed.
     * @param anzahl how many button textures should be created
     */
    protected void CreateButtonTextures(int anzahl){
        buttons.add(new GuiTexture(buttonTexture, standardButtonPos, buttonSize));
        for (int i = 0;i < anzahl-1; i++){
            buttons.add(new GuiTexture(buttonTexture,new Vector2f(buttons.get(i).getPositions().x,buttons.get(i).getPositions().y+buttonGap),buttonSize));
        }
        GameManager.getGuis().addAll(buttons);
    }

    /**
     * adds a {@link GuiTexture} behind the buttons as background
     */
    protected void addBackground(){
         backgounds.add(new GuiTexture(scrollBackground, new Vector2f(0.5f, 0.5f), new Vector2f(0.36f, 1f)));
         backgounds.add(new GuiTexture(icon, new Vector2f(0.5f, 0.175f), new Vector2f(0.3f, 0.3f)));
         GameManager.getGuis().addAll(backgounds);
    }

    /**
     * clears all {@link GuiTexture} that are no buttons at the game Begin
     */
    protected void cleaBackgournd(){
        GameManager.getGuis().removeAll(backgounds);
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
            gui.setColor(0.25f, 0.25f, 0.25f);
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
        for (GUIText text : guiTexts){
            TextMaster.removeText(text);
            text.remove();
        }
        for (GuiTexture gui : buttons){
            guiManager.removeClickableGui(gui);
            GameManager.getGuis().remove(gui);
        }

    }
}
