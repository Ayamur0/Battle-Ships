package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.ShipManager;
import com.battleships.gui.gameAssets.TestLogic;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ShipCounter extends GuiClickCallback implements Runnable{

    private static final String texture = "ShipCounter.png";
    private static final String hider = "ShipCounterHider.png";
    private static final Vector3f BLACK = new Vector3f();
    private static final Vector3f GREY = new Vector3f(0.2f,0.2f,0.2f);
    private static final Vector2f OUTLINEOFFSET = new Vector2f();

    private boolean visible = true;
    private GuiTexture gui;
    private List<GUIText> texts = new ArrayList<>();
    private GUIText[] counts = new GUIText[4];

    /**
     * Creates the gui, that shows the amount of enemy ships left.
     * @param loader - Loader to load textures.
     * @param guiManager - Manager to handle gui's with click action
     * @param guis - List of guis, this one should be added to. This list needs to be
     *               passed to a renderer, for this gui to show on the screen.
     */
    public ShipCounter (Loader loader, GuiManager guiManager, List<GuiTexture> guis, GameManager gameManager){
        gameManager.setShipCounter(this);
        gui = new GuiTexture(loader.loadTexture(texture), new Vector2f(0.5f, 0.15f));
        guis.add(gui);
        GuiTexture hideButton = new GuiTexture(loader.loadTexture(hider), new Vector2f(gui.getPositions().x + gui.getScale().x / 2 + 0.02f, 0.05f));
        hideButton.getScale().x /= 2;
        hideButton.getScale().y /= 2;
        guis.add(hideButton);
        texts.add(new GUIText("Enemy Ships Left", 3, GameManager.getPirateFont(), new Vector2f(0.5f,0.05f), 0.2f, true, GREY, 0, 0.1f, GREY, OUTLINEOFFSET));
        for(int i = 0; i < 4; i++)
            texts.add(new GUIText("" + TestLogic.getEnemyShipsLeft(i + 2), 3, GameManager.getPirateFont(), new Vector2f(0.315f + i * 0.122f ,0.265f), 0.2f, true, GREY, 0, 0.1f, GREY, OUTLINEOFFSET));
        guiManager.createClickableGui(hideButton, () -> this);
    }

    /**
     * Decrement the count of alive ships, for a specific size.
     * @param shipSize - shipSize for which the count of the ships left should be decremented.
     */
    public void decrementCount(int shipSize){
        GUIText toChange = counts[shipSize - 2];
        toChange.remove();
        toChange.setTextString(""+TestLogic.getEnemyShipsLeft(shipSize));
        TextMaster.loadText(toChange);
    }

    /**
     * Action, that gets called, when the gui (in this case the button to hide this gui) is clicked.
     * This action moves the gui offscreen, or onscreen.
     */
    @Override
    protected void clickAction() {
        Thread t = new Thread(this);
        t.start(); //TODO more parallel tasks
    }

    /**
     * Moves the gui to be visible onscreen, if it was offscreen before or moves it offscreen to not be visible, if it was visible before.
     * The movement is executed in a separate thread.
     */
    public void run(){
        if(visible) {
            while (gui.getPositions().y > -0.2f){
                gui.getPositions().y -= 0.01f;
                for(GUIText t : texts){
                    t.getPosition().y -= 0.01f;
                }
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            visible = false;
        }
        else{
            while (gui.getPositions().y < 0.15f){
                gui.getPositions().y += 0.01f;
                for(GUIText t : texts){
                    t.getPosition().y += 0.01f;
                }
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            visible = true;
        }
    }
}
