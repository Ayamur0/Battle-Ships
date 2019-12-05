package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class PlayButton extends GuiClickCallback {
    private Vector2f buttonSize = new Vector2f(0.14f,0.07f);
    private Float buttonGap = 0.15f;

    private GuiManager guiManager;
    private Loader loader;

    private GuiTexture singleplayer;
    private GuiTexture multiplayer;
    private GuiTexture back;


    public PlayButton(GuiManager guiManager, Loader loader){
        this.guiManager=guiManager;
        this.loader=loader;

        singleplayer = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.2f, 0.2f), buttonSize);
        multiplayer = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(singleplayer.getPositions().x,singleplayer.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(multiplayer.getPositions().x,multiplayer.getPositions().y+buttonGap),buttonSize);
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();
        System.out.println("Play");
        guiManager.createClickableGui(singleplayer,() -> new SingleplayerButton(guiManager,loader));
        guiManager.createClickableGui(multiplayer,() -> new MultiplayerButton(guiManager,loader));
        guiManager.createClickableGui(back,() -> new ExitButton(guiManager,loader));
    }
}
