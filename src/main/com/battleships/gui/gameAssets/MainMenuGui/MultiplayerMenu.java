package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class MultiplayerMenu extends Menu {
        public MultiplayerMenu(GuiManager guiManager, Loader loader){
        super(guiManager, loader);

        TextMaster.clear();

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
    }
    private void createMenu() {
        super.CreateButtonTextures(3);

        super.guiTexts.add(new GUIText("Host", 3f, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Client", 3f, font,new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 3, font,new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(1), x, y)) {
            super.buttonClicked = 1;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(2), x, y)) {
            super.buttonClicked = 2;
            return true;
        }

        return false;
    }
    @Override
    protected void clickAction() {
        System.out.println(buttonClicked);
        if (super.buttonClicked == 0){
            super.clearMenu();
            //TODO Set mode to multiplayer
            Inits.setStartMenu(new InGameSettingsMenu(super.guiManager,super.loader,1));
            //TODO Adding host game creation

        }
        if (super.buttonClicked == 1){
            String ip = TinyFileDialogs.tinyfd_inputBox("Connect", "Enter ip Address", "");
            //TODO Adding ip thingi
        }
        if (super.buttonClicked == 2) {
            super.clearMenu();
            Inits.setStartMenu(new PlayMenu(guiManager,loader));
        }
    }
}
