package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.logic.ShipAmountLoader;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Overlay during the shooting phase of the game. Contains all {@link GuiTexture}s and their functions needed for this UI.
 *
 * @author Tim Staudenmaier
 */
public class ShipCounter extends GuiClickCallback implements Runnable{

    /**
     * Texture of the main UI.
     */
    private static final String texture = "ShipCounter.png";
    /**
     * Texture for the hide button of the UI.
     */
    private static final String hider = "ShipCounterHider.png";
    /**
     * Color of all {@link GUIText}s on this UI.
     */
    private static final Vector3f GREY = new Vector3f(0.2f,0.2f,0.2f);
    /**
     *  Color of the outline all {@link GUIText}s have on this UI.
     */
    private static final Vector2f OUTLINEOFFSET = new Vector2f();

    /**
     * {@code true} if the shipCounter is currently visible, {@code false} if it's hidden.
     */
    private boolean visible = true;
    /**
     * GuiTexture for the Counter.
     */
    private GuiTexture gui;
    /**
     * GuiTexture for the hideButton that hides the gui
     */
    private GuiTexture hideButton;
    /**
     * List containing all the texts on the ShipCounter.
     * (Remaining ship counts and label).
     */
    private List<GUIText> texts = new ArrayList<>();
    /**
     * Array containing how many of each ship size are left on the opponents grid.
     */
    //TODO get from logic
    private GUIText[] counts = new GUIText[4];

    /**
     * {@link GuiManager} handling the click functions of this UI.
     */
    private GuiManager guiManager;
    /**
     * List of guis these two symbols should get added to, this list needs to be passed to
     * a {@link com.battleships.gui.guis.GuiRenderer} for these symbols to appear on screen.
     */
    private List<GuiTexture> guis;

    /**
     * Creates the gui, that shows the amount of enemy ships left.
     * @param loader Loader to load textures.
     * @param guiManager Manager to handle gui's with click action
     * @param guis List of guis, this one should be added to. This list needs to be
     *               passed to a renderer, for this gui to show on the screen.
     */
    public ShipCounter (Loader loader, GuiManager guiManager, List<GuiTexture> guis){
        this.guiManager = guiManager;
        this.guis = guis;
        gui = new GuiTexture(loader.loadTexture(texture), new Vector2f(0.5f, 0.15f));
        guis.add(gui);
        hideButton = new GuiTexture(loader.loadTexture(hider), new Vector2f(gui.getPositions().x + gui.getScale().x / 2 + 0.02f, 0.05f));
        hideButton.getScale().x /= 2;
        hideButton.getScale().y /= 2;
        guis.add(hideButton);
        texts.add(new GUIText("Enemy Ships Left", 3, GameManager.getPirateFont(), new Vector2f(0.5f,0.05f), 0.2f, true, GREY, 0, 0.1f, GREY, OUTLINEOFFSET));
        for(int i = 0; i < 4; i++)
            texts.add(new GUIText("" + GameManager.getLogic().getEnemyShipsLeft()[i], 3, GameManager.getPirateFont(), new Vector2f(0.315f + i * 0.122f ,0.265f), 0.2f, true, GREY, 0, 0.1f, GREY, OUTLINEOFFSET));
        guiManager.createClickableGui(hideButton, () -> this);
    }

    /**
     * Update the count of alive ships.
     */
    public void updateCount(){
        for(int i = 1; i < texts.size(); i++) {
            texts.get(i).remove();
            texts.get(i).setTextString(""+ GameManager.getLogic().getEnemyShipsLeft()[i-1]);
            TextMaster.loadText(texts.get(i));
        }
    }

    /**
     * Action, that gets called, when the gui (in this case the button to hide this gui) is clicked.
     * This action moves the gui offscreen, or onscreen.
     */
    @Override
    protected void clickAction() {
        Thread t = new Thread(this);
        t.start();
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

    /**
     * Removes the shipCounter gui from the screen.
     */
    public void remove(){
        guiManager.removeClickableGui(gui);
        guis.remove(gui);
        guis.remove(hideButton);
        for(GUIText t : texts)
            t.remove();
    }
}
