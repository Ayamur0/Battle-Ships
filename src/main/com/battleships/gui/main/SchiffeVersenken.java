package com.battleships.gui.main;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.audio.Source;
import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.entities.Light;
import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.PlayingField;
import com.battleships.gui.gameAssets.ShipManager;
import com.battleships.gui.gameAssets.ingameGui.DisableSymbols;
import com.battleships.gui.gameAssets.ingameGui.ShipCounter;
import com.battleships.gui.gameAssets.ingameGui.ShipSelector;
import com.battleships.gui.gameAssets.testLogic.TestLogic;
import com.battleships.gui.guis.*;
import com.battleships.gui.particles.*;
import com.battleships.gui.postProcessing.Fbo;
import com.battleships.gui.postProcessing.PostProcessing;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.terrains.TerrainTexture;
import com.battleships.gui.terrains.TerrainTexturePack;
import com.battleships.gui.toolbox.MousePicker;
import com.battleships.gui.water.WaterFrameBuffers;
import com.battleships.gui.water.WaterRenderer;
import com.battleships.gui.water.WaterShader;
import com.battleships.gui.water.WaterTile;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.util.ArrayList;
import java.util.List;

public class SchiffeVersenken {

    private static long window;

    public static void main(String[] args){

        // *******************Main stuff initialization*******************


        TestLogic.init(20);
        GameManager.init();
        GameManager.loadIngameScene();

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
