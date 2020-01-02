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

public abstract class Menu extends GuiClickCallback {
    protected Vector2f buttonSize;
    protected Vector2f standardButtonPos;
    protected Float buttonGap;

    protected GuiManager guiManager;
    protected Loader loader;
    protected FontType font;

    protected static int texture;
    protected static int scrollBackground;
    protected Vector3f outlineColor;

    protected List<GuiTexture> buttons = new ArrayList<>();
    protected List<GUIText> guiTexts = new ArrayList<>();

    protected int buttonClicked;

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
            scrollBackground = loader.loadTexture("paperBackground.png");
        this.outlineColor = new Vector3f(1.0f, 0.0f, 0.0f);

        this.buttonSize = new Vector2f(0.16f, 0.12f);
        this.standardButtonPos = new Vector2f(0.5f, 0.4f);
        this.buttonGap = 0.15f;
    }

    protected void CreateButtonTextures(int anzahl){
        GuiTexture test = new GuiTexture(scrollBackground,new Vector2f(0.5f,0.6f),new Vector2f(0.3f,0.75f));
        //TODO Change scale and position
        GameManager.getGuis().add(test);
        buttons.add(new GuiTexture(texture, standardButtonPos, buttonSize));
        for (int i = 0;i < anzahl-1; i++){
            buttons.add(new GuiTexture(texture,new Vector2f(buttons.get(i).getPositions().x,buttons.get(i).getPositions().y+buttonGap),buttonSize));
        }
        GameManager.getGuis().addAll(buttons);
    }

    protected void CreateTextLabels() {
        SetTextColor();
        for (GUIText gui : guiTexts) {
            TextMaster.addText(gui);
        }
    }

    protected void SetTextColor() {
        for (GUIText gui : guiTexts) {
            gui.setColor(1f, 1f, 1f);
        }
    }

    protected void createClickable() {
        for (GuiTexture i : buttons) {
            guiManager.createClickableGui(i, () -> this);
        }
    }
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
