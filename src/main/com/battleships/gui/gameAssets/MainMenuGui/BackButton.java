package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class BackButton extends MainMenuButton {
    private GuiTexture play;
    private GuiTexture options;
    private GuiTexture exit;

    private GUIText playText;
    private GUIText optionsText;
    private GUIText exitText;


    public BackButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);
        GuiTexture play = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.5f, 0.5f), buttonSize);
        GuiTexture options = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(play.getPositions().x,play.getPositions().y+buttonGap),buttonSize);
        GuiTexture exit = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(options.getPositions().x,options.getPositions().y+buttonGap),buttonSize);

        playText = new GUIText("Play", 1, font, new Vector2f(play.getPositions().x-play.getScale().x/2+0.01f,play.getPositions().y-play.getScale().y/2+0.01f), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        playText.setColor(1f,1f,1f);
        optionsText = new GUIText("Options", 1, font,new Vector2f(options.getPositions().x-options.getScale().x/2+0.01f,options.getPositions().y-options.getScale().y/2+0.01f), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        optionsText.setColor(1f,1f,1f);
        exitText = new GUIText("Exit", 1, font,new Vector2f(exit.getPositions().x-exit.getScale().x/2+0.01f,exit.getPositions().y-exit.getScale().y/2+0.01f), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        exitText.setColor(1f,1f,1f);

        TextMaster.clear();
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();
        TextMaster.clear();
        guiManager.createClickableGui(play,()->new PlayButton(guiManager,loader));
        guiManager.createClickableGui(options,()-> new OptionButton(guiManager,loader));
        guiManager.createClickableGui(exit,()->new ExitButton());
        TextMaster.loadText(playText);
        TextMaster.loadText(optionsText);
        TextMaster.loadText(exitText);
    }
}
