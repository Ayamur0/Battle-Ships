package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class MultiplayerButton extends MainMenuButton {
    private GuiTexture host;
    private GuiTexture client;
    private GuiTexture back;


    public MultiplayerButton(GuiManager guiManager, Loader loader){
        super(guiManager,loader);

        host = new GuiTexture(texture, standardButtonPos, buttonSize);
        client = new GuiTexture(texture,new Vector2f(host.getPositions().x, host.getPositions().y+buttonGap),buttonSize);
        back = new GuiTexture(texture,new Vector2f(client.getPositions().x, client.getPositions().y+buttonGap),buttonSize);

        super.guiTexts.add(new GUIText("Host", 3f, font, new Vector2f(host.getPositions().x, host.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Client", 3f, font,new Vector2f(client.getPositions().x, client.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true, 0.0f, 0.1f,outlineColor, new Vector2f()));

    }
    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(host,() -> new HostButton(guiManager,loader));
        guiManager.createClickableGui(client,() -> new ClientButton(guiManager,loader));
        guiManager.createClickableGui(back,() -> new BackButton(guiManager,loader));

        TextMaster.clear();
        super.CreateTextLabels();
    }
}
