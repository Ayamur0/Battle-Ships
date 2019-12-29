package com.battleships.gui.main;

import com.battleships.gui.gameAssets.MainMenuGui.Background;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.glfw.GLFW;

public class MenuTest {

    public static void main(String[] args) {
        Inits inits = new Inits();
        inits.init();
        Background background = new Background();
        while (!GLFW.glfwWindowShouldClose(WindowManager.getWindow())) {
            background.renderBackground();
            Inits.render();
            WindowManager.updateWindow();

        }
        Inits.cleanUp();
    }
}
