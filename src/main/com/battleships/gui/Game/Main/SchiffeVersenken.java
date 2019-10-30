package com.battleships.gui.Game.Main;

import com.battleships.gui.Engine.Game;
import com.battleships.gui.Engine.Rendering.Entities.Camera;
import com.battleships.gui.Engine.Rendering.Entities.Entity;
import com.battleships.gui.Engine.Rendering.Entities.Light;
import com.battleships.gui.Engine.Rendering.Models.*;
import com.battleships.gui.Engine.Rendering.RenderingEngine.Loader;
import com.battleships.gui.Engine.Rendering.RenderingEngine.MasterRenderer;
import com.battleships.gui.Engine.Rendering.RenderingEngine.Renderer;
import com.battleships.gui.Engine.Rendering.Shaders.StaticShader;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

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

        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-1), new Vector3f(0,0,0), new Vector3f(1,1,1));
        Light light = new Light(new Vector3f(5,0,5), new Vector3f(1,1,1));

        Camera camera = new Camera(window);

        Entity ship = loader.loadEntityfromOBJ("test", "4ShipTex.tga", 10, 1);
        ship.setPosition(new Vector3f(0,0,-40));

        while (!GLFW.glfwWindowShouldClose(window)){

            camera.move(window);
            renderer.processEntity(ship);
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
