package com.battleships.gui.main;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.InGameSettingsMenu;
import com.battleships.gui.gameAssets.MainMenuGui.Menu;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenu;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.postProcessing.Fbo;
import com.battleships.gui.postProcessing.PostProcessing;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.window.WindowManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class Inits {
    private static boolean menuInitDone;

    private static Loader loader = new Loader();
    private static MasterRenderer renderer;
    private static Fbo fbo;

    private static GuiManager guiManager;
    private static List<GuiTexture> permanentGuiElements;



    private static Menu startMenu;
    private static GuiRenderer guiRenderer;


    private static MainMenuManager mainMenuManager;
    private static GameManager gameManager;

    public static void setStartMenu(Menu startMenu) {
        Inits.startMenu = startMenu;
    }
    public static Loader getLoader() {
        return loader;
    }

    public static MasterRenderer getRenderer() {
        return renderer;
    }

    public static Fbo getFbo() {
        return fbo;
    }

    public static GuiManager getGuiManager() {
        return guiManager;
    }

    public static List<GuiTexture> getPermanentGuiElements() {
        return permanentGuiElements;
    }

    public static Menu getStartMenu() {
        return startMenu;
    }

    public static GuiRenderer getGuiRenderer() {
        return guiRenderer;
    }

    public static void init(){

        WindowManager.initialize();
        // *******************Main stuff initialization*******************
        loader = new Loader();
        renderer = new MasterRenderer(loader);
        TextMaster.init(loader);

        // *******************GUI initialization*******************
        guiManager = new GuiManager();
        permanentGuiElements = new ArrayList<>();

        startMenu = new MainMenu(guiManager,loader);
        guiRenderer = new GuiRenderer(loader);

        // *******************Post Processing initialization*******************
        fbo = new Fbo(WindowManager.getWidth(), WindowManager.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
        PostProcessing.init(loader);

        // *******************Callbacks initialization*******************
        mainMenuManager = new MainMenuManager(guiManager,loader);

        WindowManager.setMainMenuCallbacks(mainMenuManager, null);

        menuInitDone = true;
    }
    public static void render(){
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

        //render refraction texture
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //not all drivers support disabling, if it doesn't work set clipPlane when rendering to screen high enough so nothing gets clipped
        //background.render();
//            new Particle(star, new Vector3f(camera.getPosition().x , camera.getPosition().y, camera.getPosition().z), new Vector3f(0, 30, 0), 1 ,4 ,0 ,1);

        if (Inits.getStartMenu() instanceof InGameSettingsMenu) {
            if (((InGameSettingsMenu) Inits.getStartMenu()).isRunning())
                ((InGameSettingsMenu) Inits.getStartMenu()).RefreshSliderValue();
        }

        guiRenderer.render(permanentGuiElements);
        guiManager.renderClickableGuis(guiRenderer);
        TextMaster.render();
        renderer.updateProjectionMatrix();
    }

    public static void cleanUp(){
        fbo.cleanUp();
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        //unload GLFW on window close
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
