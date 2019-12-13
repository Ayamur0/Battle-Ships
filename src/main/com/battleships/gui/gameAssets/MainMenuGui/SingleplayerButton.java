package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class SingleplayerButton extends MainMenuButton {

    private PVsAiButton pVsAiButton;
    private AiVsAiButton aiVsAiButton;
    private BackButton backButton;

    private GuiTexture pVsAi;
    private GuiTexture aiVsAi;
    private GuiTexture back;


    public SingleplayerButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        this.creatButtons();

        this.creatLabels();
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(pVsAi,() -> pVsAiButton);
        guiManager.createClickableGui(aiVsAi,() -> aiVsAiButton);
        guiManager.createClickableGui(back,() -> backButton);

        TextMaster.clear();
        super.CreateTextLabels();
    }
    private void creatButtons(){
        pVsAiButton = new PVsAiButton(guiManager,loader);
        aiVsAiButton = new AiVsAiButton(guiManager,loader);
        backButton = new BackButton(guiManager,loader,1);

        pVsAi = new GuiTexture(texture, standardButtonPos, buttonSize);
        aiVsAi = new GuiTexture(texture,new Vector2f(pVsAi.getPositions().x,pVsAi.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(texture,new Vector2f(aiVsAi.getPositions().x,aiVsAi.getPositions().y+buttonGap),buttonSize);
    }
    private void creatLabels(){
        super.guiTexts.add(new GUIText("Player VS AI", 2f, font, new Vector2f(pVsAi.getPositions().x,pVsAi.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("AI VS AI", 2.4f, font, new Vector2f(aiVsAi.getPositions().x,aiVsAi.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3f, font, new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));

    }
}
