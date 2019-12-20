package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class MultiplayerButton extends MainMenuButton {

    private HostButton hostButton;
    private ClientButton clientButton;
    private BackButton backButton;

    private GuiTexture host;
    private GuiTexture client;
    private GuiTexture back;


    @Override
    protected void createButtons() {

    }

    @Override
    protected void createLabels() {

    }

    public MultiplayerButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        this.creatButtons();

        this.creatLabels();
    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(host,() -> hostButton);
        guiManager.createClickableGui(client,() -> clientButton);
        guiManager.createClickableGui(back,() -> backButton);

        TextMaster.clear();
        super.CreateTextLabels();
    }
    private void creatButtons(){
        hostButton = new HostButton(guiManager,loader);
        clientButton = new ClientButton(guiManager,loader);
        backButton = new BackButton(guiManager,loader,1);

        host = new GuiTexture(texture, standardButtonPos, buttonSize);
        client = new GuiTexture(texture,new Vector2f(host.getPositions().x, host.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(texture,new Vector2f(client.getPositions().x, client.getPositions().y+buttonGap),buttonSize);
    }
    private void creatLabels(){
        super.guiTexts.add(new GUIText("Host", 3f, font, new Vector2f(host.getPositions().x, host.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Client", 3f, font,new Vector2f(client.getPositions().x, client.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));


    }
}
