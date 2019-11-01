package com.battleships.gui.main;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.entities.Light;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.renderingEngine.OBJLoader;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.terrains.TerrainTexture;
import com.battleships.gui.terrains.TerrainTexturePack;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SchiffeVersenken {

    private static long window;

    public static void main(String[] args){

        WindowManager.initialize();

        window = WindowManager.getWindow();

    //initialize resources then render resources in while

        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer(loader);

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f,0.25f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        Light light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));

        TerrainTexture texture0 = new TerrainTexture(loader.loadTexture("Water.jpg"));
        TerrainTexture texture1 = new TerrainTexture(loader.loadTexture("path.jpg"));
        TerrainTexture texture2 = new TerrainTexture(loader.loadTexture("Gravel.jpg"));
        TerrainTexture texture3 = new TerrainTexture(loader.loadTexture("Grass.jpg"));
        TerrainTexture texture4 = new TerrainTexture(loader.loadTexture("WetSand.jpg"));
        TerrainTexture texture5 = new TerrainTexture(loader.loadTexture("Sand.jpg"));

        TerrainTexturePack texturePack = new TerrainTexturePack(texture0, texture1, texture2, texture3, texture4, texture5);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("BlendMap.tga"));

        Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "HeightMap.jpg");
//        Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "HeightMap.jpg");

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern"), new ModelTexture(loader.loadTexture("FernAtlas.tga")));
        fern.getTexture().setNumberOfRows(2);
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setUseFakeLighting(true);

        List<Entity> ferns = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 500; i++){
            float x = random.nextFloat() * 800 - 400;
            float z = random.nextFloat() * -600;
            ferns.add(new Entity(fern, random.nextInt(4), new Vector3f(x, terrain.getHeightOfTerrain(x,z),z ), new Vector3f(), 0.6f));
        }

        Camera camera = new Camera();

        Entity ship = loader.loadEntityfromOBJ("test", "4ShipTex.tga", 10, 1);
        ship.setPosition(new Vector3f(0,0,-40));

        GuiClickCallback guiClickCallback = new GuiClickCallback();
        guiClickCallback.addClickableGui(gui);
        WindowManager.setCallbacks(camera, guiClickCallback);

        while (!GLFW.glfwWindowShouldClose(window)){
            camera.move(window, terrain);

            for (Entity e : ferns)
                renderer.processEntity(e);
            renderer.processEntity(ship);
            renderer.processTerrain(terrain);
//            renderer.processTerrain(terrain2);


            renderer.render(light,camera);

            guiRenderer.render(guis);

            ship.getRotation().y += 0.1f;

            WindowManager.updateWindow();
        }

        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        //unload GLFW on window close
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
