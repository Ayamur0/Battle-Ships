package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class SingleplayerButton extends MainMenuButton {

    private BackButton backButton;

    private GuiTexture back;


    public SingleplayerButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        this.createButtons();

        this.createLabels();
    }

    @Override
    protected void createButtons() {
        backButton = new BackButton(guiManager,loader,1);

        back = new GuiTexture(texture,new Vector2f(standardButtonPos),buttonSize);
    }

    @Override
    protected void createLabels() {
        super.guiTexts.add(new GUIText("Back", 3f, font, new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
    }

    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(back,() -> backButton);

        TextMaster.clear();
        super.CreateTextLabels();
    }
}
