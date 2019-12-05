package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class MainMenu extends GuiClickCallback {
    private Vector2f buttonSize = new Vector2f(0.14f,0.07f);
    private Float buttonGap = 0.15f;

    private GuiManager guiManager;
    private Loader loader;

    private GuiTexture play;
    private GuiTexture options;
    private GuiTexture exit;


    public MainMenu(GuiManager guiManager, Loader loader){
        this.guiManager=guiManager;
        this.loader=loader;
        GuiTexture play = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.5f, 0.5f), buttonSize);
        GuiTexture options = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(play.getPositions().x,play.getPositions().y+buttonGap),buttonSize);
        GuiTexture exit = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(options.getPositions().x,options.getPositions().y+buttonGap),buttonSize);

        guiManager.createClickableGui(play,()->new PlayButton(guiManager,loader));
        guiManager.createClickableGui(options,()-> new OptionButton(guiManager,loader));
        guiManager.createClickableGui(exit,()->new ExitButton(guiManager,loader));
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();
        /*guiManager.createClickableGui(play,()->new PlayButton(guiManager,loader));
        guiManager.createClickableGui(options,()-> new OptionButton(guiManager,loader));
        guiManager.createClickableGui(exit,()->new ExitButton(guiManager,loader));

         */
    }
}
