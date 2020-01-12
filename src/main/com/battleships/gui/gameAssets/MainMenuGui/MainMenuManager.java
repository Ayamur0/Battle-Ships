package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.FinishGame;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.water.WaterFrameBuffers;
import com.battleships.gui.water.WaterTile;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.lang.reflect.GenericArrayType;
import java.nio.DoubleBuffer;

public class MainMenuManager {
    /**
     * Menu that handels all menus and their functions
     */
    private static Menu menu;
    /**
     * GuiManager that handles guis with click functions.
     */
    private static GuiManager guiManager;
    /**
     * Loader for loading models and textures.
     */
    private static Loader loader;
    /**
     * FrameBuffers that create the reflection and refraction texture for the {@link WaterTile}s.
     */
    private static WaterFrameBuffers wFbo;
    /**
     * For reading the x Mouse coordinates
     */
    private DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    /**
     * For reading the y Mouse coordinates
     */
    private DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    /**
     * Sets the current Menu
     * @param menu the menu that is shown
     */
    public static void setMenu(Menu menu){
        MainMenuManager.menu = menu;
    }

    /**
     * @return the current shown Menu from the game
     */
    public static Menu getMenu(){return MainMenuManager.menu;}

    /**
     * initializes all attributes for the menu
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     * @param wFbo Water Frame Buffers create the reflection and refraction texture for the {@link WaterTile}s
     */
    public MainMenuManager(GuiManager guiManager, Loader loader, WaterFrameBuffers wFbo) {
        MainMenuManager.guiManager = guiManager;
        MainMenuManager.loader = loader;
        MainMenuManager.wFbo = wFbo;
    }

    /**
     * Loads the Main menu for the game
     */
    public static void LoadMainMenu(){
        menu = new MainMenu(guiManager,loader);

        WindowManager.setMainMenuCallbacks(GameManager.getMainMenuManager(), wFbo);
    }

    /**
     * removes all ships
     */
    public void removeAllShips(){
        GameManager.removeAllShips();
    }

    /**
     * Clears everything from the ingame scene
     */
    public void clearAll(){
        removeAllShips();
        GameManager.removeIngameModelsFromScene();
        guiManager.clearClickableGuis();
        TextMaster.clear();
        GameManager.getGuis().clear();
    }

    /**
     * Clears the ingame textures and sets needed options for the menu
     */
    public void backToMainMenu(){
        clearAll();
        GameManager.getSettings().setAiLevelP(-1);
        GameManager.getSettings().setAiLevelO(-1);
        WindowManager.setMainMenuCallbacks(GameManager.getMainMenuManager(),wFbo);
        GameManager.getLogic().setGameState(GameManager.MENU);
        GridManager.setIsBackground(true);
        menu = new MainMenu(guiManager,loader);
    }
    /**
     * Function that gets called if the user left clicks anywhere in the game.
     * Gets the cursor position and tests all guis with a click action if they were
     * clicked on and if one was clicked executes the click action of that gui.
     */
    public GLFWMouseButtonCallback testClick = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {

            if(action != GLFW.GLFW_PRESS || button != GLFW.GLFW_MOUSE_BUTTON_1)
                return;
            GLFW.glfwGetCursorPos(window, x, y);
            x.rewind();
            y.rewind();

            float xpos = (float)x.get() / WindowManager.getWidth();
            float ypos = (float)y.get() / WindowManager.getHeight();

            x = BufferUtils.createDoubleBuffer(1);
            y = BufferUtils.createDoubleBuffer(1);

            guiManager.testGuiClick(xpos, ypos);
        }
    };
    /**
     * Function that gets called if the user presses any key on the keyboard.
     * Calls the corresponding function if a key that has a function was pressed.
     */
    public static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scanCode, int action, int mods) {
            if (key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS)
                WindowManager.setFullScreen(!WindowManager.isFullscreen());
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                if (GameManager.getLogic().getGameState() != GameManager.MENU && !ESCMenu.isActive())
                    MainMenuManager.setMenu(new ESCMenu(guiManager, loader));
                else if (ESCMenu.isActive())
                    ((ESCMenu) MainMenuManager.getMenu()).ClearESCMenu();
            }
        }
    };
}
