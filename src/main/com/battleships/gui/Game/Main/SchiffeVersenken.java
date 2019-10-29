package Game.Main;

import Engine.Game;
import Engine.Rendering.Entities.Camera;
import Engine.Rendering.Entities.CameraOld;
import Engine.Rendering.Entities.Entity;
import Engine.Rendering.Entities.EntityOld;
import Engine.Rendering.Models.Loader;
import Engine.Rendering.Models.*;
import Engine.Rendering.Shaders.GLSLProgram;
import Engine.Rendering.Shaders.StaticShader;
import Game.Assets.PlayingField;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class SchiffeVersenken extends Game {

    private static SchiffeVersenken instance;
    private Mesh mesh;
    private GLSLProgram basicShaderProgram;
    private GLSLProgram entityShaderProgram;
    private TextureLoader brickTexture;
    private EntityOld cube;
    private CameraOld camera;
    private PlayingField playingField;
//    private float yOffset = -0.5f; //for 2D objects

    public static void main(String[] args){
        instance = new SchiffeVersenken();
        instance.run();
    }

    //initialize resources then render resources in while
    @Override
    public void mainGameLoop(long window) {
//        camera = new CameraOld();



//        brickTexture = new TextureLoader("5Ship/5ShipTex.jpg");
////        mesh = new Mesh(new int[] {0, 1 ,3, 3, 1, 2},
////                            new float[]{-0.5f, 0.5f, 0f,
////                                        -0.5f, -0.5f, 0f,
////                                        0.5f, -0.5f, 0f,
////                                        0.5f, 0.5f, 0f},
////                            new float[] {
////                                        0, 0,
////                                        0, 1,
////                                        1, 1,
////                                        1, 0});
//
////        mesh  = new Mesh(new int[] { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19, 19, 17, 18, 20, 21, 23, 23, 21, 22 }, new float[] { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,
////                -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
////
////                0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
////
////                -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
////
////                -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,
////
////                -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f }, new float[] { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0 });
//
//        //add shader files to GLSL shader program for basic (2D) shaders
//        basicShaderProgram = new GLSLProgram("/Game/res/shaders/basic.vs", "/Game/res/shaders/basic.fs", "positions", "textureCoordinates");
////        //add uniform variables from shaders
//        basicShaderProgram.addUniformVariable("offset");
//        basicShaderProgram.addUniformVariable("diffuseTexture");
////
////        //add shader files to GLSL shader program for entity (3D) shaders
//        entityShaderProgram = new GLSLProgram("/Game/res/shaders/entity.vs", "/Game/res/shaders/entity.fs", "positions", "textureCoordinates");
////        //add variables that are used in shader files to uniform variables so its possible to pass them to shaders from program code
//        entityShaderProgram.addUniformVariable("diffuseTexture");
//        entityShaderProgram.addUniformVariable("transformationMatrix");
//        entityShaderProgram.addUniformVariable("viewMatrix");
//        entityShaderProgram.addUniformVariable("projectionMatrix");

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
//        mesh = OBJLoader.loadObjModel("5Ships");
//        cube = new EntityOld(mesh); //create entity with created mesh
//        playingField = new PlayingField(10);

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
        // code for 2D object
//        basicShaderProgram.enable();
//        basicShaderProgram.setVec3("offset", 0, yOffset, 0);
//        basicShaderProgram.setTexture("diffuseTexture", brickTexture, 0);
//        mesh.render();
//        yOffset += 0.001f;
//        cube.getPosition().x = 8f;
//        cube.getPosition().z = 1f;
//        cube.getPosition().y = -2;
//        cube.getScale().x = 0.1f;
//        cube.getScale().y = 0.1f;
//        cube.getScale().z = 0.1f;
//        cube.getRotation().y = 90f;
//        cube.update();
//        camera.getRotation().x = -20;
//        camera.getPosition().z = 1;
//
//
//        camera.update();
//
//        entityShaderProgram.enable();
//        entityShaderProgram.setTexture("diffuseTexture", brickTexture, 0);
//        entityShaderProgram.setMatrix("transformationMatrix", cube.getTransformationMatrix());
//        entityShaderProgram.setMatrix("viewMatrix", camera.getViewMatrix());
//        entityShaderProgram.setMatrix("projectionMatrix", camera.getProjectionMatrix());




//        cube.getMesh().render();

//        playingField.render();
    }

    //release resources
    @Override
    public void release() {
//        mesh.release(); 2D

        cube.getMesh().release();
        basicShaderProgram.release();
    }

    public GLSLProgram getEntityShaderProgram() {
        return entityShaderProgram;
    }

    public CameraOld getCamera() {
        return camera;
    }

    public static SchiffeVersenken getInstance() {
        return instance;
    }
}
