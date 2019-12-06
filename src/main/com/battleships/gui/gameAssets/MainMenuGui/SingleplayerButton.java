package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class SingleplayerButton extends MainMenuButton {
    private GuiTexture pVsAi;
    private GuiTexture aiVsAi;
    private GuiTexture back;


    public SingleplayerButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        pVsAi = new GuiTexture(texture, standardButtonPos, buttonSize);
        aiVsAi = new GuiTexture(texture,new Vector2f(pVsAi.getPositions().x,pVsAi.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(texture,new Vector2f(aiVsAi.getPositions().x,aiVsAi.getPositions().y+buttonGap),buttonSize);

        super.guiTexts.add(new GUIText("Player VS AI", 2f, font, new Vector2f(pVsAi.getPositions().x,pVsAi.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("AI VS AI", 2.4f, font, new Vector2f(aiVsAi.getPositions().x,aiVsAi.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3f, font, new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        prevMenu = 1;

        guiManager.createClickableGui(pVsAi,() -> new PVsAi(guiManager,loader));
        guiManager.createClickableGui(aiVsAi,() -> new AiVsAi(guiManager,loader));
        guiManager.createClickableGui(back,() -> new BackButton(guiManager,loader));

        TextMaster.clear();
        super.CreateTextLabels();
    }
}
