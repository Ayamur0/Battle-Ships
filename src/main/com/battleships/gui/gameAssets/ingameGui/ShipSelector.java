package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

import java.util.List;

public class ShipSelector extends GuiClickCallback {

    public ShipSelector(Loader loader, GuiManager guiManager, List<GuiTexture> guis) {
        GuiTexture background = new GuiTexture(loader.loadTexture("IngameGuiShipSelectBackground.png"), new Vector2f(0.5f, 0));
        float space = 0.053125f;
        background.getPositions().y = 1 - background.getScale().y / 2;
        GuiTexture ship1 = new GuiTexture(loader.loadTexture("IngameGuiShipSelectShip1.png"), new Vector2f(0,background.getPositions().y - 0.0204f));
        ship1.getPositions().x = background.getPositions().x - 1.5f * space - 1.5f * ship1.getScale().x;
        GuiTexture ship2 = new GuiTexture(loader.loadTexture("IngameGuiShipSelectShip2.png"), new Vector2f(0,background.getPositions().y - 0.0204f));
        ship2.getPositions().x = background.getPositions().x - 0.5f * space - 0.5f * ship2.getScale().x;
        GuiTexture ship3 = new GuiTexture(loader.loadTexture("IngameGuiShipSelectShip3.png"), new Vector2f(0,background.getPositions().y - 0.0204f));
        ship3.getPositions().x = background.getPositions().x + 0.5f * space + 0.5f * ship3.getScale().x;
        GuiTexture ship4 = new GuiTexture(loader.loadTexture("IngameGuiShipSelectShip4.png"), new Vector2f(0,background.getPositions().y - 0.0204f));
        ship4.getPositions().x = background.getPositions().x + 1.5f * space + 1.5f * ship4.getScale().x;
        guis.add(background);
        guis.add(ship1);
        guis.add(ship2);
        guis.add(ship3);
        guis.add(ship4);
        guiManager.createClickableGui(ship1, () -> this);
        guiManager.createClickableGui(ship2, () -> this);
        guiManager.createClickableGui(ship3, () -> this);
        guiManager.createClickableGui(ship4, () -> this);
    }

    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y){
        return true;
    }

    @Override
    protected void clickAction(){

    }
}
