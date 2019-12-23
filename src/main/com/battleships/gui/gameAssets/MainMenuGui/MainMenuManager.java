package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

public class MainMenuManager {

    private DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    private GuiManager guiManager;

    public MainMenuManager(GuiManager guiManager) {
        this.guiManager = guiManager;
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
