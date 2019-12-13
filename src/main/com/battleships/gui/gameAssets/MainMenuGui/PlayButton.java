package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class PlayButton extends MainMenuButton {
    private SingleplayerButton singleplayerButton;
    private MultiplayerButton multiplayerButton;
    private BackButton backButton;

    private GuiTexture singleplayer;
    private GuiTexture multiplayer;
    private GuiTexture back;

    public PlayButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        this.creatButtons();

        this.creatLabels();
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(singleplayer,() -> singleplayerButton);
        guiManager.createClickableGui(multiplayer,() -> multiplayerButton);
        guiManager.createClickableGui(back,() -> backButton);

        TextMaster.clear();
        super.CreateTextLabels();
    }
    private void creatButtons(){
        singleplayerButton = new SingleplayerButton(guiManager,loader);
        multiplayerButton = new MultiplayerButton(guiManager,loader);
        backButton = new BackButton(guiManager,loader,0);

        singleplayer = new GuiTexture(texture, standardButtonPos, buttonSize);
        multiplayer = new GuiTexture(texture,new Vector2f(singleplayer.getPositions().x,singleplayer.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(texture,new Vector2f(multiplayer.getPositions().x,multiplayer.getPositions().y+buttonGap),buttonSize);
    }
    private void creatLabels(){
        super.guiTexts.add(new GUIText("Singleplayer", 2.5f, font, new Vector2f(singleplayer.getPositions().x,singleplayer.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Multiplayer", 2.5f, font,new Vector2f(multiplayer.getPositions().x,multiplayer.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));

    }
}
