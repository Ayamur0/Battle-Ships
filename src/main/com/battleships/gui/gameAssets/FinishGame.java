package com.battleships.gui.gameAssets;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.logic.Stats;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FinishGame extends GuiClickCallback {

    /**
     * Vectors representing the color green.
     */
    private static Vector3f GREEN = new Vector3f(0,1,0);
    /**
     * Vectors representing the color red.
     */
    private static Vector3f RED = new Vector3f(1,0,0);
    /**
     * Vectors representing the color grey.
     */
    private static Vector3f GREY = new Vector3f(0.2f,0.2f,0.2f);
    /**
     * Vectors representing the color white.
     */
    private static Vector3f WHITE = new Vector3f(1,1,1);
    /**
     * Vectors representing the color black.
     */
    private static Vector3f BLACK = new Vector3f();

    /**
     * Creates the gui containing the stats of the game.
     * Gui changes depending on outcome of the game.
     * If user clicks on this gui he is returned to the main menu.
     * @param loader Loader to load texture.
     * @param guiManager GuiManager to handle clickFunction.
     * @param won {@code true} if the player has won.
     */
    public void finishGame(Loader loader, GuiManager guiManager, boolean won){
        GameManager.getLogic().advanceGamePhase();
        GuiTexture background = new GuiTexture(loader.loadTexture("EndScroll.png"), new Vector2f(0.5f,0.5f), new Vector2f(0.5f,0.8f));
        GameManager.getGuis().add(background);
        if(won) {
            TextMaster.addText(new GUIText("YOU WON!", 3, GameManager.getPirateFont(),
                    new Vector2f(0.5f, 0.16f), 0.5f, true, GREEN, 0.7f, 0.1f, GREY, new Vector2f()));
        }
        else {
            TextMaster.addText(new GUIText("YOU LOST!", 3, GameManager.getPirateFont(),
                    new Vector2f(0.5f, 0.16f), 0.5f, true, RED, 0.7f, 0.1f, GREY, new Vector2f()));
        }
        addStats(won);
        TextMaster.addText(new GUIText("Click to return to the main menu",2, GameManager.getPirateFont(),
                new Vector2f(0.5f,0.75f), 0.5f, true, WHITE, 0.7f, 0.1f, GREY,new Vector2f()));
        guiManager.createClickableGui(background, () -> this);
    }

    /**
     * Adds the texts containing the stats to the end screen.
     * @param won {@code true} if the player has won.
     */
    private void addStats(boolean won){
        Stats stats = GameManager.getLogic().getStats();
        stats.updateStats();
        addBlackText("Time played:", new Vector2f(0.625f, 0.35f));
        addBlackText(stats.getPlayTime()/60 + ":" + stats.getPlayTime() % 60, new Vector2f(0.825f, 0.35f));
        addBlackText("Rounds played:", new Vector2f(0.625f, 0.45f));
        addBlackText(""+stats.getRounds(), new Vector2f(0.825f, 0.45f));
        if(won){
            addBlackText("Ships Left alive:", new Vector2f(0.625f, 0.55f));
            addBlackText(stats.getShipsAlive()+"/"+stats.getMaxShips(), new Vector2f(0.825f, 0.55f));
        }
        else{
            addBlackText("Ships destroyed:", new Vector2f(0.625f, 0.55f));
            addBlackText(stats.getShipsDestroyed()+"/"+stats.getMaxShips(), new Vector2f(0.825f, 0.55f));
        }
        addBlackText("Accuracy", new Vector2f(0.625f, 0.65f));
        addBlackText(stats.getAccuracy()*100+"%", new Vector2f(0.825f, 0.65f));
    }

    /**
     * Adds a standard black text to the screen.
     * @param text Text that should be added.
     * @param pos Position tje text should be added at.
     */
    private void addBlackText(String text, Vector2f pos){
        TextMaster.addText(new GUIText(text,2, GameManager.getPirateFont(),
               pos, 0.5f, false, BLACK, 0.1f, 0.1f, GREY,new Vector2f()));
    }

    /**
     * Returns to the main menu if this is clicked.
     */
    @Override
    protected void clickAction() {
    }
}
