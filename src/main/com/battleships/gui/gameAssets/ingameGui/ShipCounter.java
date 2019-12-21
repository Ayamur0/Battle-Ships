package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.ShipManager;
import com.battleships.gui.gameAssets.TestLogic;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ShipCounter {

    private static final String texture = "ShipCounter.png";
    private static final Vector3f BLACK = new Vector3f();
    private static final Vector3f GREY = new Vector3f(0.2f,0.2f,0.2f);
    private static final Vector2f OUTLINEOFFSET = new Vector2f();

    private ShipManager shipManager;
    private GuiTexture gui;
    private List<GUIText> texts = new ArrayList<>();
    private GUIText[] counts = new GUIText[4];

    public ShipCounter (Loader loader, ShipManager shipManager, List<GuiTexture> guis){
        this.shipManager = shipManager;
        shipManager.setShipCounter(this);
        gui = new GuiTexture(loader.loadTexture(texture), new Vector2f(0.5f, 0.15f));
        guis.add(gui);
        texts.add(new GUIText("Enemy Ships Left", 3, GameManager.getPirateFont(), new Vector2f(0.5f,0.05f), 0.2f, true, GREY, 0, 0.1f, GREY, OUTLINEOFFSET));
        for(int i = 0; i < 4; i++)
            texts.add(new GUIText("" + TestLogic.getEnemyShipsLeft(i + 2), 3, GameManager.getPirateFont(), new Vector2f(0.315f + i * 0.122f ,0.26f), 0.2f, true, GREY, 0, 0.1f, GREY, OUTLINEOFFSET));
    }

    public void decrementCount(int shipSize){
        GUIText toChange = counts[shipSize - 2];
        toChange.remove();
        toChange.setTextString(""+TestLogic.getEnemyShipsLeft(shipSize));
        TextMaster.loadText(toChange);
    }

}
