package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class BackButton extends MainMenuButton {
    private GuiTexture play;
    private GuiTexture options;
    private GuiTexture exit;

    public BackButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        play = new GuiTexture(texture, new Vector2f(0.5f, 0.5f), buttonSize);
        options = new GuiTexture(texture,new Vector2f(play.getPositions().x,play.getPositions().y+buttonGap),buttonSize);
        exit = new GuiTexture(texture,new Vector2f(options.getPositions().x,options.getPositions().y+buttonGap),buttonSize);

        super.guiTexts.add(new GUIText("Play", 3, font, new Vector2f(play.getPositions().x,play.getPositions().y), 0.12f, true, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Options", 3, font,new Vector2f(options.getPositions().x,options.getPositions().y), 0.12f, true, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit", 3, font,new Vector2f(exit.getPositions().x,exit.getPositions().y), 0.12f, true, 0.0f, 0.1f, outlineColor, new Vector2f()));

    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(play,()->new PlayButton(guiManager,loader));
        guiManager.createClickableGui(options,()-> new OptionButton(guiManager,loader));
        guiManager.createClickableGui(exit,()->new ExitButton());

        TextMaster.clear();
        super.CreateTextLabels();
    }
            }
