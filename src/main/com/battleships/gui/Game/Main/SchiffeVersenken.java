package com.battleships.gui.Game.Main;

import com.battleships.gui.Engine.Game;
import com.battleships.gui.Engine.Rendering.Entities.Camera;
import com.battleships.gui.Engine.Rendering.Entities.Entity;
import com.battleships.gui.Engine.Rendering.Models.*;
import com.battleships.gui.Engine.Rendering.Shaders.StaticShader;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class SchiffeVersenken extends Game {

    private static SchiffeVersenken instance;
//    private float yOffset = -0.5f; //for 2D objects

    public static void main(String[] args){
        instance = new SchiffeVersenken();
        instance.run();
    }

    //initialize resources then render resources in while
    @Override
    public void mainGameLoop(long window) {


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
        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);
        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        ModelTexture texture = TextureLoader.loadTexture("Brick.jpg");
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Entity entity = new Entity(texturedModel, new Vector3f(-1,0,0), new Vector3f(0,0,0), new Vector3f(0,0,0));

        Camera camera = new Camera(window);

        while (!GLFW.glfwWindowShouldClose(window)){

            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            renderer.render(entity,shader);
            shader.stop();

            GLFW.glfwSwapBuffers(window);

            //allow mouse and keyboard inputs
            GLFW.glfwPollEvents();
        }
        loader.cleanUp();
        shader.cleanUp();
    }

    //release resources
    @Override
    public void release() {
//        mesh.release(); 2D
    }

    public static SchiffeVersenken getInstance() {
        return instance;
    }
}
