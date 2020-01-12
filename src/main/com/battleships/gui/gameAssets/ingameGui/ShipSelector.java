package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.gameAssets.grids.ShipManager;
import com.battleships.gui.window.WindowManager;
import com.battleships.logic.ShipAmountLoader;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Overlay during the ship placing phase of the game. Contains all {@link GuiTexture}s and their functions needed for this UI.
 *
 * @author Tim Staudenmaier
 */
public class ShipSelector extends GuiClickCallback {

    /**
     * ID's for the clickable buttons, besides the ship buttons.
     */
    private static final int DELETE = 6, RANDOM = 7, CONFIRM = 8;

    /**
     * Color of all {@link GUIText}s on this UI.
     */
    private static final Vector3f BLACK = new Vector3f();
    /**
     * Color of the outline of all {@link GUIText}s oon this UI.
     */
    private static final Vector2f OUTLINEOFFSET = new Vector2f();

    /**
     * Array containing the GuiTextures for all clickable elements of this UI.
     */
    private GuiTexture[] buttons = new GuiTexture[7];
    /**
     * Gui of the background behind the clickable buttons.
     */
    private GuiTexture background;
    /**
     * List containing all texts on this UI.
     */
    private List<GUIText> shipCountTexts = new ArrayList<>();
    /**
     * Array containing how many ships of each size are left to place.
     */
    private int[] shipCounts;
    /**
     * Used to determine which button was the last one that was clicked.
     * ButtonNumbers correspond to ship sizes.
     */
    private int buttonNumber;
    /**
     * ShipManager that handles the placing of the selected ships.
     */
    private ShipManager shipManager;

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
     * Ship counts for the current grid size, so counts that show ships left to place can be reset to values of beginning.
     */
    private int[] standardShipCounts;

    /**
     * Create the gui used for ship selecting.
     * @param loader Loader needed to load textures.
     * @param guiManager GuiManager needed to link click functions to the gui elements.
     * @param shipManager ShipManager of the current game, needed for the click functions.
     * @param guis List of guis this gui should be saved to, this list needs to be rendered later to show this gui
     *             on screen.
     */
    public ShipSelector(Loader loader, GuiManager guiManager, ShipManager shipManager, List<GuiTexture> guis) {
        this.guiManager = guiManager;
        this.guis = guis;
        this.shipManager = shipManager;
        shipManager.setShipSelector(this);
        background = new GuiTexture(loader.loadTexture("IngameGuiShipSelectBackground.png"), new Vector2f(0.5f, 0));
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
        GuiTexture delete = new GuiTexture(loader.loadTexture("deleteIcon.png"), new Vector2f(0.08f,0.9f), new Vector2f(0.05f, 0.09f));
        GuiTexture random = new GuiTexture(loader.loadTexture("randomizeIcon.png"), new Vector2f(0.08f,0.78f), new Vector2f(0.05f,0.09f));
        GuiTexture confirm = new GuiTexture(loader.loadTexture("confirmIcon.png"), new Vector2f(0.08f,0.66f), new Vector2f(0.05f,0.09f));
        guis.add(background);
        buttons[0] = ship1;
        buttons[1] = ship2;
        buttons[2] = ship3;
        buttons[3] = ship4;
        buttons[4] = delete;
        buttons[5] = random;
        buttons[6] = confirm;
        for(GuiTexture g : buttons){
            guis.add(g);
            guiManager.createClickableGui(g, () -> this);
        }

        standardShipCounts = ShipAmountLoader.getShipAmounts(shipManager.getGridSize());
        if(standardShipCounts == null){
            System.err.println("Something went wrong calculating the ship amounts!");
            return;
        }
        shipCounts = new int[standardShipCounts.length];
        System.arraycopy(standardShipCounts, 0, shipCounts, 0, standardShipCounts.length);

        for(int i = 0; i < 4; i++)
            shipCountTexts.add(new GUIText(shipCounts[i] + " Left", 2, GameManager.getPirateFont(), new Vector2f(buttons[i].getPositions().x, buttons[i].getPositions().y + buttons[i].getScale().y / 2 + 0.05f), buttons[i].getScale().x, true, BLACK, 0, 0.1f,BLACK, OUTLINEOFFSET));
}

    /**
     * Function that gets called to test if a click was on this gui.
     * This gui has 4 individual buttons, so depending on which button was pressed, the buttonNumber
     * attribute gets set to the value for that button, so the clickAction() method knows, which button was clicked.
     * @param gui not used in this override, because the gui textures are saved in buttons[] array, so should be null.
     * @param x x position of the click in screen coordinates.
     * @param y y position of the click in screen coordinates.
     * @return {@code true} if the click was on one of the buttons of this gui, {@code false} else.
     */
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y){
        for(int i = 0; i < buttons.length; i++) {
//            if (buttons[i].getPositions().x - 0.5f * buttons[i].getScale().x <= x && buttons[i].getPositions().x + 0.5f *
//                    buttons[i].getScale().x >= x && buttons[i].getPositions().y - 0.5f * buttons[i].getScale().y <= y &&
//                    buttons[i].getPositions().y + 0.5f * buttons[i].getScale().y >= y) {
            if(super.isClickOnGui(buttons[i],x,y)){
                this.buttonNumber = i + 2;
                return true;
            }
        }
        return false;
    }

    /**
     * Gets called if one of the 4 buttons on this gui was clicked.
     * Adds a ship to the cursor and decrements the amount of ships left for that size.
     * The size of the ship changes from 2 5 depending on which button was clicked.
     */
    @Override
    protected void clickAction(){
        if(buttonNumber < 6 && shipCounts[buttonNumber - 2] > 0) {
            shipManager.stickShipToCursor(buttonNumber);
            return;
        }
        switch (buttonNumber){
            case DELETE: shipManager.removeCursorShip(); GameManager.removeAllShips(); return;
            case RANDOM: shipManager.removeCursorShip(); GameManager.removeAllShips(); GameManager.getLogic().placeRandomShips(GridManager.OWNFIELD); return;
            case CONFIRM:
                if(IntStream.of(shipCounts).sum() > 0)
                    return;
                if(!GameManager.getSettings().isOnline())
                    GameManager.getLogic().advanceGamePhase();
                else
                    GameManager.getNetwork().sendConfirm();
        }
    }

    /**
     * Change the text under the button for that ship size, so it shows that you can place one
     * ship less or one more of that size than before.
     * @param increment {@code true} if the count should be incremented, {@code false} if it should be decremented
     */
    public void changeCount(int shipSize, boolean increment){
        GUIText dummy = shipCountTexts.get(shipSize - 2);
        dummy.remove();
        if(increment)
            shipCounts[shipSize - 2]++;
        else
            shipCounts[shipSize - 2]--;
        dummy.setTextString(shipCounts[shipSize - 2] + " Left");
        //TextMaster.loadText(new GUIText(shipCounts[0] + " Left", 2, dummy.getFont(), dummy.getPosition(), dummy.getLineMaxSize(), true, 0, 0.1f,BLACK, OUTLINEOFFSET));
        TextMaster.loadText(dummy);
    }

    /**
     * Decrements the amount remaining of the ships with the given size.
     * @param shipSize Size of ships the count should be decremented for.
     */
    public void decrementCount(int shipSize){
        changeCount(shipSize, false);
    }

    /**
     * Increments the amount remaining of the ships with the given size.
     * @param shipSize Size of ships the count should be incremented for.
     */
    public void incrementCount(int shipSize){
        changeCount(shipSize, true);
    }

    /**
     * Removes the gui elements of the ship selector.
     */
    public void remove(){
        guis.remove(background);
        for(GuiTexture t : buttons){
            guis.remove(t);
            guiManager.removeClickableGui(t);
        }
        for(GUIText t : shipCountTexts)
            t.remove();
    }

    /**
     * Resets counts for ships left to full values (values from the beginning before any ship was placed).
     */
    public void resetCount(){
        for(int i = 0; i < 4; i++){
            shipCountTexts.get(i).remove();
            System.arraycopy(standardShipCounts, 0, shipCounts, 0, standardShipCounts.length);
            shipCountTexts.get(i).setTextString(shipCounts[i] + " Left");
            TextMaster.loadText(shipCountTexts.get(i));
        }
    }
}
