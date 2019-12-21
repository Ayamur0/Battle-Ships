package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class MainMenuButton extends GuiClickCallback {
    protected Vector2f buttonSize;
    protected Vector2f standardButtonPos;
    protected Float buttonGap;

    protected GuiManager guiManager;
    protected Loader loader;
    protected FontType font;

    protected int texture;
    protected Vector3f outlineColor;

    protected List<GuiTexture> buttons = new ArrayList<>();
    protected List<GUIText> guiTexts = new ArrayList<>();

    protected int buttonClicked;

    public MainMenuButton(GuiManager guiManager, Loader loader) {
        this.guiManager = guiManager;
        this.loader = loader;
        this.font = new FontType(loader.loadFontTexture("font/pirate.png"), "pirate");
        this.texture = loader.loadTexture("WoodButton.png");
        this.outlineColor = new Vector3f(1.0f,0.0f,0.0f);

        this.buttonSize = new Vector2f(0.14f,0.07f);
        this.standardButtonPos = new Vector2f(0.5f, 0.4f);
        this.buttonGap = 0.15f;
    }
    protected void CreateTextLabels(){
        SetTextColor();
        for (GUIText gui :guiTexts) {
            TextMaster.addText(gui);
        }
    }
    protected void SetTextColor(){
        for (GUIText gui :guiTexts) {
            gui.setColor(1f,1f,1f);
        }
    }
    protected void createClickable(){
        for (GuiTexture i:buttons){
            guiManager.createClickableGui(i,() -> this);
        }
    }
}
