package com.battleships.gui.gameAssets;

import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.toolbox.MousePicker;
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

    public GameManager(Loader loader) {
        pirateFont = new FontType(loader.loadFontTexture("font/pirate.png"), "pirate");
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

    private void toggleAnimations() {
        //TODO make gui that shows that animations are disabled
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
}
