package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.postProcessing.Fbo;
import com.battleships.gui.postProcessing.PostProcessing;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class MainMenuManager {
    private static Menu menu;
    private static GuiManager guiManager;
    private static Loader loader;

    private DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    public static void setMenu(Menu menu){
        MainMenuManager.menu = menu;
    }

    public MainMenuManager(GuiManager guiManager, Loader loader) {
        this.guiManager = guiManager;
        this.loader = loader;
    }

    public static void LoadMainMenu(){
        menu = new MainMenu(guiManager,loader);

        WindowManager.setMainMenuCallbacks(GameManager.getMainMenuManager());
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
