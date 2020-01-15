package com.battleships.gui.main;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import com.battleships.logic.LogicManager;
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

        while (!GLFW.glfwWindowShouldClose(WindowManager.getWindow())){
            if (GameManager.getLoading()){

                WindowManager.createLoadingScreen();

                GameManager.loadIngameScene();
                GameManager.getGuis().clear();
                GridManager.setIsBackground(true);
                WindowManager.clearCallbacks();
                MainMenuManager.LoadMainMenu();

                WindowManager.destroyLoadingScreen();
                continue;
            }
            if (GameManager.getLogic().getGameState()==GameManager.MENU)
                GameManager.updateSceneBlurred();
            else
                GameManager.updateScene();
            WindowManager.updateWindow();

        }
        GameManager.cleanUpIngameScene();
        GameManager.getSettings().saveSettings();
    }
}
