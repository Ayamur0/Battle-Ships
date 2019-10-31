package com.battleships.gui.engine.renderingEngine;

import com.battleships.gui.engine.entities.Camera;
import com.battleships.gui.engine.entities.Entity;
import com.battleships.gui.engine.entities.Light;
import com.battleships.gui.engine.models.TexturedModel;
import com.battleships.gui.engine.shaders.StaticShader;
import com.battleships.gui.engine.shaders.TerrainShader;
import com.battleships.gui.engine.terrains.Terrain;
import com.battleships.gui.game.main.SchiffeVersenken;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private static final float RED = 0.4f;
    private static final float GREEN = 0.5f;
    private static final float BLUE = 0.8f;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>(); //list all entities in one entry, that use the same texture
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(){
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public static void enableCulling(){
        //disable rendering of the backside of faces (inside of models)
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling(){
        //enable rendering of the backside of faces (inside of models){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    //load things needed for rendering and then render all entities
    public void render(Light light, Camera camera){
        prepare();
        shader.start();
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(RED,GREEN,BLUE);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        //clear stuff that has been rendered or it would be added another time next frame and render on top of each other
        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        //if a list with the texturedModel of the entity already exists in entities add entity to that list
        //else create new list in entities for the texturedModel of the entity
        if(batch != null){
            batch.add(entity);
        }else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void prepare(){
        //Clear window
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Only render pixel closest to camera
        GL11.glClearColor(RED, GREEN, BLUE, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    private void createProjectionMatrix(){
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(SchiffeVersenken.getWindow(), w, h);
        int width = w.get(0);
        int height = h.get(0);
        float aspectRatio = (float) width / (float) height;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix._m00(x_scale);
        projectionMatrix._m11(y_scale);
        projectionMatrix._m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix._m23(-1);
        projectionMatrix._m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix._m33(0);
//        projectionMatrix = new Matrix4f();
//        projectionMatrix.setPerspective((float)Math.toRadians(FOV), 800f / 600f, NEAR_PLANE, FAR_PLANE);
    }

}
