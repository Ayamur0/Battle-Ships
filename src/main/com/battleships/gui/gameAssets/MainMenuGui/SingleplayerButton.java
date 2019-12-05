package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class SingleplayerButton extends MainMenuButton {
    private GuiTexture singleplayer;
    private GuiTexture multiplayer;
    private GuiTexture back;


    public SingleplayerButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);
        singleplayer = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.5f, 0.5f), buttonSize);
        multiplayer = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(singleplayer.getPositions().x,singleplayer.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(multiplayer.getPositions().x,multiplayer.getPositions().y+buttonGap),buttonSize);
    }
    @Override
    protected void clickAction() {

    }
}
