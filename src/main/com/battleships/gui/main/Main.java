package com.battleships.gui.main;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.glfw.GLFW;

public class Main {

    public static void main(String[] args) {
        GameManager.init();

        while (!GLFW.glfwWindowShouldClose(WindowManager.getWindow())) {
            if (GameManager.getLoading()) {

                WindowManager.createLoadingScreen();

                GameManager.loadIngameScene();
                GameManager.getGuis().clear();
                GridManager.setIsBackground(true);
                WindowManager.clearCallbacks();
                MainMenuManager.LoadMainMenu();

                WindowManager.destroyLoadingScreen();
                continue;
            }
            if (GameManager.getLogic().getGameState() == GameManager.MENU)
                GameManager.updateSceneBlurred();
            else
                GameManager.updateScene();
            WindowManager.updateWindow();

        }
        GameManager.cleanUpIngameScene();
        GameManager.getSettings().saveSettings();
    }
}
