package com.battleships.gui.main;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class MenuTest {
    private static class loadIngame implements Runnable{
        public loadIngame() {
            Thread t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            GameManager.loadIngameScene();

        }
    }

    public static void main(String[] args) {
        GameManager.init();
        Loader loader = new Loader();
        List<GuiTexture> guis = new ArrayList<>();
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        //new loadIngame();
        GameManager.loadIngameScene();
        guis.add(new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f)));
        GameManager.getGuis().clear();
        MainMenuManager.LoadMainMenu();
        while (!GLFW.glfwWindowShouldClose(WindowManager.getWindow())){
            if (GameManager.getLoading()){
                guiRenderer.render(guis);
                continue;
            }

            GameManager.updateScene();


            WindowManager.updateWindow();

        }
        GameManager.cleanUpIngameScene();
    }
}
