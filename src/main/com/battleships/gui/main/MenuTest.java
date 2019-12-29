package com.battleships.gui.main;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLXARBCreateContext;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.List;

public class MenuTest {

    public static void main(String[] args) {
        GameManager.init();
        Loader loader = new Loader();
        List<GuiTexture> guis = new ArrayList<>();
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        //GameManager.loadIngameScene();
        guis.add(new GuiTexture(loader.loadTexture("StartIcon.png"), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));
        while (!GLFW.glfwWindowShouldClose(WindowManager.getWindow())){
            if (GameManager.getLoading()){
                GLFW.glfwShowWindow(WindowManager.getWindow());
                guiRenderer.render(guis);
                WindowManager.updateWindow();
                GameManager.loadIngameScene();
                GameManager.getGuis().clear();
                GridManager.setIsBackground(true);
                WindowManager.clearCallbacks();
                MainMenuManager.LoadMainMenu();
                GLFW.glfwSetWindowAttrib(WindowManager.getWindow(), GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
                continue;
            }
            GameManager.updateSceneBlurred();


            WindowManager.updateWindow();

        }
        GameManager.cleanUpIngameScene();
    }
}
