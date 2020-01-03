package com.battleships.gui.main;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.testLogic.TestLogic;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class SchiffeVersenken {

    private static long window;

    public static void main(String[] args){

        // *******************Main stuff initialization*******************


        TestLogic.init(20);
        GameManager.init();
        GameManager.loadIngameScene();
        WindowManager.destroyLoadingScreen();

        // *******************GUI initialization*******************


        // *******************Camera initialization*******************



        // *******************Light initialization*******************


        // *******************Terrain initialization*******************


        // *******************Entity initialization*******************


//        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern"), new ModelTexture(loader.loadTexture("FernAtlas.tga")));
//        fern.getTexture().setNumberOfRows(2);
//        fern.getTexture().setHasTransparency(true);
//        fern.getTexture().setUseFakeLighting(true);

//        List<Entity> ferns = new ArrayList<>();
//        Random random = new Random();
//        for(int i = 0; i < 500; i++){
//            float x = random.nextFloat() * 800 - 400;
//            float z = random.nextFloat() * -600;
//            ferns.add(new Entity(fern, random.nextInt(4), new Vector3f(x, terrain.getHeightOfTerrain(x,z),z ), new Vector3f(), 0.6f));
//        }
//
//        entities.addAll(ferns);

        Vector3f cellIntersection;

        // *******************Water initialization*******************


        // *******************Particle initialization*******************

        // *******************Post Processing initialization*******************

       // Fbo fbo = new Fbo(WindowManager.getWidth(), WindowManager.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
       // PostProcessing.init(loader);

        // *******************Callbacks initialization*******************




        // *******************Audio initialization*******************


        // ****************************************************
        // *******************Main Game Loop*******************
        // ****************************************************

        while (!GLFW.glfwWindowShouldClose(WindowManager.getWindow())){


//            fbo.unbindFrameBuffer();
//            PostProcessing.doPostProcessing(fbo.getColorTexture());
            GameManager.updateScene();


            WindowManager.updateWindow();

        }

        // *******************Clean up*******************

        GameManager.cleanUpIngameScene();
        //unload GLFW on window close
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
