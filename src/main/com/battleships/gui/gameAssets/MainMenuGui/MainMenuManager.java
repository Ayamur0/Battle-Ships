package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.water.WaterFrameBuffers;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

public class MainMenuManager {
    private static Menu menu;
    private static GuiManager guiManager;
    private static Loader loader;
    private static WaterFrameBuffers wFbo;

    private DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    public static void setMenu(Menu menu){
        MainMenuManager.menu = menu;
    }
    public static Menu getMenu(){return MainMenuManager.menu;}

    public MainMenuManager(GuiManager guiManager, Loader loader, WaterFrameBuffers wFbo) {
        MainMenuManager.guiManager = guiManager;
        MainMenuManager.loader = loader;
        MainMenuManager.wFbo = wFbo;
    }

    public static void LoadMainMenu(){
        menu = new MainMenu(guiManager,loader);

        WindowManager.setMainMenuCallbacks(GameManager.getMainMenuManager(), wFbo);
    }

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
}
