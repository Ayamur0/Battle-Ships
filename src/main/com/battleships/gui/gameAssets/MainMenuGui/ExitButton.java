package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.glfw.GLFW;

public class ExitButton extends GuiClickCallback {
    @Override
    protected void clickAction() {
        GLFW.glfwSetWindowShouldClose(WindowManager.getWindow(),true);
    }
}
