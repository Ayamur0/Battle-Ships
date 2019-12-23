package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.ingameGui.DisableSymbols;
import com.battleships.gui.gameAssets.ingameGui.ShipCounter;
import com.battleships.gui.gameAssets.testLogic.TestLogic;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.toolbox.MousePicker;
import com.battleships.gui.toolbox.ParallelTasks;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

public class GameManager {

    private DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    private static FontType pirateFont;

    private static GuiManager guiManager;
    private static PlayingField playingField;
    private static MousePicker mousePicker;
    private static ShipManager shipManager;
    private static ShipCounter shipCounter;
    private static DisableSymbols disableSymbols;
    private static Camera camera;

    public GameManager(Loader loader) {
        pirateFont = new FontType(loader.loadFontTexture("font/pirate.png"), "pirate");
    }

    public static void placeShip(Vector2i index, int size, int direction, int field){
        if(field == PlayingField.OPPONENTFIELD) {
            //TestLogic.opponent.placeShip(index.x, index.y, size, direction);
        }
        if(field == PlayingField.OWNFIELD){
            playingField.placeShip(index, size, direction);
            //TestLogic.own.placeShip(index.x, index.y, size, direction);
        }
    }

    /**
     * Shoot method, that passes the shoot command to the playingfield.
     * @param originField - Field the shot originates from (0 for own, 1 for opponent).
     * @param destinationIndex - Index of the field that should get shot.
     */
    public static void shoot(int originField, Vector2i destinationIndex){
        playingField.shoot(originField, destinationIndex);
    }

    /**
     * Passes the command to place a marker, at the specified index, to the playingfield.
     * @param shipHit - {@code true} if a ship was hit, so marker should be red. {@code false} if no ship was hit, so marker should be white.
     * @param index - Index where the marker should be placed.
     * @param field - Field the marker should be placed on (0 for own, 1 for opponent).
     */
    public static void placeMarker(boolean shipHit, Vector2i index, int field){
        playingField.placeMarker(shipHit, index, field);
    }

    /**
     * Finishes the game and shows endscreen.
     * @param won - {@code true} if the player has won, {@code false} else.
     */
    public static void finishGame(boolean won){
        //TODO
    }

    /**
     * Decrement the count, that keeps track of how many enemy ships of each size are alive.
     * @param size - Size of ship the count should be decremented for.
     */
    public static void decrementAliveShip(int size){
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
                Vector3i pointedCell = playingField.calculatePointedCell(cellIntersection);
                if (pointedCell != null) {
                    playingField.cellClicked(new Vector2i(pointedCell.x, pointedCell.y), pointedCell.z);
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
            if(key == GLFW.GLFW_KEY_R && action == GLFW.GLFW_PRESS){
                shipManager.rotateShip();
            }
            if(key == GLFW.GLFW_KEY_X && action == GLFW.GLFW_PRESS){
                toggleAnimations();
            }
            if(key == GLFW.GLFW_KEY_T && action == GLFW.GLFW_PRESS)
                camera.turnCamera();
        }
    };

    public static void toggleAnimations() {
        disableSymbols.toggleSymbol(DisableSymbols.ANIMATION);
        playingField.toggleShootingAnimation();
    }

    public static FontType getPirateFont() {
        return pirateFont;
    }

    public static PlayingField getPlayingField() {
        return playingField;
    }

    public static void setGuiManager(GuiManager guiManager2) {
        guiManager = guiManager2;
    }

    public static void setPlayingField(PlayingField playingField2) {
        playingField = playingField2;
        shipManager = playingField.getShipManager();
    }

    public static void setMousePicker(MousePicker mousePicker2) {
        mousePicker = mousePicker2;
    }

    public static void setShipCounter(ShipCounter shipCounter2) {
        shipCounter = shipCounter2;
    }

    public static void setDisableSymbols(DisableSymbols disableSymbols2) {
        disableSymbols = disableSymbols2;
    }

    public static void setCamera(Camera camera) {
        GameManager.camera = camera;
    }
}
