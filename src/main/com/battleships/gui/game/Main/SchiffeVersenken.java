package com.battleships.gui.game.main;

import com.battleships.gui.engine.Game;
import com.battleships.gui.engine.entities.Camera;
import com.battleships.gui.engine.entities.Entity;
import com.battleships.gui.engine.entities.Light;
import com.battleships.gui.engine.models.ModelTexture;
import com.battleships.gui.engine.models.RawModel;
import com.battleships.gui.engine.models.TexturedModel;
import com.battleships.gui.engine.renderingEngine.Loader;
import com.battleships.gui.engine.renderingEngine.MasterRenderer;
import com.battleships.gui.engine.renderingEngine.OBJLoader;
import com.battleships.gui.engine.terrains.Terrain;
import com.battleships.gui.engine.terrains.TerrainTexture;
import com.battleships.gui.engine.terrains.TerrainTexturePack;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SchiffeVersenken extends Game {

    private static SchiffeVersenken instance;
    private static long window;
//    private float yOffset = -0.5f; //for 2D objects

    public static void main(String[] args){
        instance = new SchiffeVersenken();
        instance.run();
    }

    //initialize resources then render resources in while
    @Override
    public void mainGameLoop(long window) {
        this.window = window;

        float[] vertices = {
                -0.5f, 0.5f, 0f,    //V0
                -0.5f, -0.5f, 0f,   //V1
                0.5f, -0.5f, 0f,    //V2
                0.5f, 0.5f, 0f,     //V3
        };


        int[] indices = {
                0,1,3, //Top left triangle (V0,V1,V3)
                3,1,2  //Bottom right triangle (V3,V1,V2)
        };

        float[] textureCoords = {
                0,0, //V0 (0,0 is top left corner of texture) -> map to vertex V0
                0,1, //V1  (bottom left corner of texture) -> map to vertex V1
                1,1, //V2
                1,0 //V3
        };

        float[] normals = {
                -0.5f, 0.5f, 0f,    //V0
                -0.5f, -0.5f, 0f,   //V1
                0.5f, -0.5f, 0f,    //V2
                0.5f, 0.5f, 0f,     //V3
        };
        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer();
        RawModel model = loader.loadToVAO(vertices, textureCoords, normals, indices);
//        ModelTexture texture = Loader.loadTexture("Brick.jpg");
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("Brick.jpg")));

        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-1), new Vector3f(0,0,0), 1);
        Light light = new Light(new Vector3f(5,5,5), new Vector3f(1,1,1));

        TerrainTexture texture0 = new TerrainTexture(loader.loadTexture("Water.jpg"));
        TerrainTexture texture1 = new TerrainTexture(loader.loadTexture("path.jpg"));
        TerrainTexture texture2 = new TerrainTexture(loader.loadTexture("Gravel.jpg"));
        TerrainTexture texture3 = new TerrainTexture(loader.loadTexture("Grass.jpg"));
        TerrainTexture texture4 = new TerrainTexture(loader.loadTexture("WetSand.jpg"));
        TerrainTexture texture5 = new TerrainTexture(loader.loadTexture("Sand.jpg"));

        TerrainTexturePack texturePack = new TerrainTexturePack(texture0, texture1, texture2, texture3, texture4, texture5);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("BlendMap.tga"));

        Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap);

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern"), new ModelTexture(loader.loadTexture("fern.tga")));
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setUseFakeLighting(true);

        List<Entity> ferns = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 500; i++){
            ferns.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), new Vector3f(), 0.6f));
        }

        Camera camera = new Camera(window);

        Entity ship = loader.loadEntityfromOBJ("test", "4ShipTex.tga", 10, 1);
        ship.setPosition(new Vector3f(0,0,-40));

        while (!GLFW.glfwWindowShouldClose(window)){
            camera.move(window);

            for (Entity e : ferns)
                renderer.processEntity(e);
            renderer.processEntity(ship);
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);


            renderer.render(light,camera);

            ship.getRotation().y += 0.1f;



            GLFW.glfwSwapBuffers(window);
            //allow mouse and keyboard inputs
            GLFW.glfwPollEvents();
        }
        renderer.cleanUp();
        loader.cleanUp();
    }

    //release resources
    @Override
    public void release() {
//        mesh.release(); 2D
    }

    public static SchiffeVersenken getInstance() {
        return instance;
    }

    public static long getWindow(){return window;}
}
