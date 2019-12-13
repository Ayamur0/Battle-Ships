package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class MainMenu extends MainMenuButton {

    private PlayButton playButton;
    private OptionButton optionButton;
    private ExitButton exitButton;

    private GuiTexture play;
    private GuiTexture options;
    private GuiTexture exit;



    public MainMenu(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        this.creatButtons();

        this.creatLabels();

        TextMaster.clear();
        super.CreateTextLabels();
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(play,() -> playButton);
        guiManager.createClickableGui(options,() -> optionButton);
        guiManager.createClickableGui(exit,() -> exitButton);

        TextMaster.clear();
        super.CreateTextLabels();
    }

    private void creatButtons(){

        playButton = new PlayButton(guiManager,loader);
        optionButton = new OptionButton(guiManager,loader);
        exitButton = new ExitButton();

        play = new GuiTexture(texture, standardButtonPos, buttonSize);
        options = new GuiTexture(texture,new Vector2f(play.getPositions().x,play.getPositions().y+buttonGap),buttonSize);
        exit = new GuiTexture(texture,new Vector2f(options.getPositions().x,options.getPositions().y+buttonGap),buttonSize);

        guiManager.createClickableGui(play,()-> playButton);
        guiManager.createClickableGui(options,()-> optionButton);
        guiManager.createClickableGui(exit,()-> exitButton);
    }

    private void creatLabels(){
        super.guiTexts.add(new GUIText("Play", 3, font, new Vector2f(play.getPositions().x,play.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Options", 3, font,new Vector2f(options.getPositions().x,options.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit", 3, font,new Vector2f(exit.getPositions().x,exit.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));


    }
}
