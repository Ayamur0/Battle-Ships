package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class PlayButton extends MainMenuButton {
    private GuiTexture singleplayer;
    private GuiTexture multiplayer;
    private GuiTexture back;

    public PlayButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        singleplayer = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.2f, 0.2f), buttonSize);
        multiplayer = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(singleplayer.getPositions().x,singleplayer.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(multiplayer.getPositions().x,multiplayer.getPositions().y+buttonGap),buttonSize);

        super.guiTexts.add(new GUIText("Singleplayer", 2.5f, font, new Vector2f(singleplayer.getPositions().x,singleplayer.getPositions().y), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f()));
        super.guiTexts.add(new GUIText("Multiplayer", 2.5f, font,new Vector2f(multiplayer.getPositions().x,multiplayer.getPositions().y), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f()));

    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(singleplayer,() -> new SingleplayerButton(guiManager,loader));
        guiManager.createClickableGui(multiplayer,() -> new MultiplayerButton(guiManager,loader));
        guiManager.createClickableGui(back,() -> new BackButton(guiManager,loader));

        TextMaster.clear();
        super.CreateTextLabels();

    }
}
