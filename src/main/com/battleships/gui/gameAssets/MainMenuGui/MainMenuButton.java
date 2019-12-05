package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class MainMenuButton extends GuiClickCallback {
    protected Vector2f buttonSize = new Vector2f(0.14f,0.07f);
    protected Float buttonGap = 0.15f;

    protected GuiManager guiManager;
    protected Loader loader;
    protected FontType font;

    protected List<GUIText> guiTexts = new ArrayList<>();

    public MainMenuButton(GuiManager guiManager, Loader loader) {
        this.guiManager = guiManager;
        this.loader = loader;
        this.font = new FontType(loader.loadFontTexture("font/PixelDistance.png"), "PixelDistance");
    }
    protected void CreateTextLabels(){
        SetTextColor();
        for (GUIText gui :guiTexts) {
            TextMaster.loadText(gui);
        }
    }
    protected void SetTextColor(){
        for (GUIText gui :guiTexts) {
            gui.setColor(1f,1f,1f);
        }
    }
}
