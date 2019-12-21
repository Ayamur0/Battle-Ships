package com.battleships.gui.gameAssets;

import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.ingameGui.DisableSymbols;
import com.battleships.gui.gameAssets.ingameGui.ShipCounter;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.toolbox.MousePicker;
import com.battleships.gui.toolbox.ParallelTasks;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

public class GameManager {

    private DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    private static FontType pirateFont;

    private GuiManager guiManager;
    private PlayingField playingField;
    private MousePicker mousePicker;
    private ShipManager shipManager;
    private ShipCounter shipCounter;
    private DisableSymbols disableSymbols;

    public GameManager(Loader loader) {
        pirateFont = new FontType(loader.loadFontTexture("font/pirate.png"), "pirate");
    }

    /**
     * Shoot method, that passes the shoot command to the playingfield.
     * @param originField - Field the shot originates from (0 for own, 1 for opponent).
     * @param destinationIndex - Index of the field that should get shot.
     */
    public void shoot(int originField, Vector2f destinationIndex){
        playingField.shoot(originField, destinationIndex);
    }

    /**
     * Passes the command to place a marker, at the specified index, to the playingfield.
     * @param shipHit - {@code true} if a ship was hit, so marker should be red. {@code false} if no ship was hit, so marker should be white.
     * @param index - Index where the marker should be placed.
     * @param field - Field the marker should be placed on (0 for own, 1 for opponent).
     */
    public void placeMarker(boolean shipHit, Vector2f index, int field){
        playingField.placeMarker(shipHit, index, field);
    }

    /**
     * Finishes the game and shows endscreen.
     * @param won - {@code true} if the player has won, {@code false} else.
     */
    public void finishGame(boolean won){
        //TODO
    }

    /**
     * Decrement the count, that keeps track of how many enemy ships of each size are alive.
     * @param size - Size of ship the count should be decremented for.
     */
    public void decrementAliveShip(int size){
        shipCounter.decrementCount(size);
    }

    /**
     * Function that gets called if the user left clicks anywhere in the game.
     * Gets the cursor position and tests all guis with a click action if they were
     * clicked on and if one was clicked executes the click action of that gui.
     */
    public GLFWMouseButtonCallback testClick = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {

            if(action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_2){
                shipManager.removeCursorShip();
                return;
            }
            if(action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_1) {

                GLFW.glfwGetCursorPos(window, x, y);
                x.rewind();
                y.rewind();

                float xpos = (float) x.get() / WindowManager.getWidth();
                float ypos = (float) y.get() / WindowManager.getHeight();

                x.clear();
                y.clear();

                if (guiManager.testGuiClick(xpos, ypos))
                    return;

                Vector3f cellIntersection = mousePicker.getCurrentIntersectionPoint();
                Vector3f pointedCell = playingField.calculatePointedCell(cellIntersection);
                if (pointedCell != null) {
                    playingField.cellClicked(new Vector2f(pointedCell.x, pointedCell.y), (int) pointedCell.z);
                }
            }
        }
    };

    public GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scanCode, int action, int mods) {
            if(key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS)
                WindowManager.setFullScreen(!WindowManager.isFullscreen());
            if(key == GLFW.GLFW_KEY_F && action == GLFW.GLFW_PRESS)
                playingField.swapShipPlacingPhase();
            if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS){
                shipManager.rotateShip();
            }
            if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS){
                toggleAnimations();
            }
        }
    };

    public void toggleAnimations() {
        disableSymbols.toggleSymbol(DisableSymbols.ANIMATION);
        playingField.toggleShootingAnimation();
    }

    public static FontType getPirateFont() {
        return pirateFont;
    }

    public void setGuiManager(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    public void setPlayingField(PlayingField playingField) {
        this.playingField = playingField;
        shipManager = playingField.getShipManager();
    }

    public void setMousePicker(MousePicker mousePicker) {
        this.mousePicker = mousePicker;
    }

    public void setShipCounter(ShipCounter shipCounter) {
        this.shipCounter = shipCounter;
    }

    public void setDisableSymbols(DisableSymbols disableSymbols) {
        this.disableSymbols = disableSymbols;
    }
}
