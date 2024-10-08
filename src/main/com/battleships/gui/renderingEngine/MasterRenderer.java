package com.battleships.gui.renderingEngine;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.entities.Light;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.shaders.StaticShader;
import com.battleships.gui.shaders.TerrainShader;
import com.battleships.gui.skybox.SkyboxRenderer;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.window.WindowManager;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Renderer that controls the main renderers needed for rendering a scene {@link EntityRenderer}, {@link TerrainRenderer} and {@link SkyboxRenderer}.
 * Can render a whole scene using these renderers.
 *
 * @author Tim Staudenmaier.
 */
public class MasterRenderer {

    /**
     * FOV the camera viewing the scene should have (degrees).
     */
    private static final float FOV = 70;
    /**
     * Everything closer to the camera than this plane doesn't get rendered.
     */
    private static final float NEAR_PLANE = 0.1f;
    /**
     * Everything further aways from the camera than this plane doesn't get rendered.
     */
    private static final float FAR_PLANE = 1200;

    /**
     * Color of the void as rgb values (gets rendered if nothing else is at that spot).
     */
    private static final float RED = 0.4f, GREEN = 0.5f, BLUE = 0.8f;

    /**
     * Current projection matrix of the window.
     */
    private static Matrix4f projectionMatrix;

    /**
     * Shader for rendering Entities.
     */
    private StaticShader shader = new StaticShader();
    /**
     * Renderer for rendering entities.
     */
    private EntityRenderer renderer;

    /**
     * Renderer for rendering terrains.
     */
    private TerrainRenderer terrainRenderer;
    /**
     * Shader for rendering terrains.
     */
    private TerrainShader terrainShader = new TerrainShader();

    /**
     * Map containing all Entities in the world, grouped by their TexturedModels.
     */
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    /**
     * List containing all terrains in the world.
     */
    private List<Terrain> terrains = new ArrayList<>();

    /**
     * Renderer for rendering the skybox.
     */
    private SkyboxRenderer skyboxRenderer;

    /**
     * Create new MasterRenderer, that is capable of rendering entities, terrains, skyboxes and lights.
     *
     * @param loader Loader to pass to pass to skyboxRenderer to load it's CubeMap
     */
    public MasterRenderer(Loader loader) {
        enableCulling();
        updateProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    /**
     * Disables the rendering of the backsides of faces, so the inside of
     * models doesn't get rendered to save performance.
     */
    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Enable the rendering of the backside of faces, so the inside of models
     * has texture, needs more performance.
     */
    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * @return Distance of the nearPlane from the point from which the scene is rendered (camera), everything in front of
     * this plane doesn't get rendered.
     */
    public static float getNearPlane() {
        return NEAR_PLANE;
    }

    /**
     * @return Distance of the farPlane from the point from which the scene is rendered (camera), everything behind of
     * this plane doesn't get rendered.
     */
    public static float getFarPlane() {
        return FAR_PLANE;
    }

    /**
     * @return The current projection Matrix.
     */
    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Add all elements of a scene consisting of many entities, one terrain, one light, one camera and a clipPlane
     * to the List/Hashmaps so they get passed to the corresponding renderers. After that call the render method
     * to actually render the scene.
     *
     * @param entities  All entities that should be added before rendering the scene.
     * @param terrain   The terrain that should be added before rendering the scene.
     * @param light     The light that lights the scene.
     * @param camera    The camera, from which the scene should be rendered.
     * @param clipPlane Plane after which nothing should be rendered. x,y,z and w are the A, B, C and D of a plane
     *                  equation (Ax + By + Cz + D = 0), so x,y and z are the normal of the plane and D ist he signed distance
     *                  from the origin.
     */
    public void renderScene(List<Entity> entities, Terrain terrain, Light light, Camera camera, Vector4f clipPlane) {
        processTerrain(terrain);
        processEntityList(entities);
        render(light, camera, clipPlane);
    }

    /**
     * Start shaders and load everything needed to them, then render all currently saved entities and terrains to the
     * screen using the light and camera, as well as removing everything behind the clipPlane.
     *
     * @param light     Light that lights the scene.
     * @param camera    Camera from which the scene should be rendered. (Picture on screen is what camera sees)
     * @param clipPlane Plane after which nothing gets rendered anymore.
     */
    public void render(Light light, Camera camera, Vector4f clipPlane) {
        prepare();
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxRenderer.render(camera);
        //clear stuff that has been rendered or it would be added another time next frame and render on top of each other
        terrains.clear();
        entities.clear();
    }

    /**
     * Add a terrain to the list of terrain that should be rendered, next time render() is called.
     *
     * @param terrain Terrain to be added to the List of rendered terrains.
     */
    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    /**
     * Add a List of entities to the HashMap of entities that get rendered when render() is called.
     *
     * @param entities List of entities that should be added.
     */
    public void processEntityList(List<Entity> entities) {
        for (Entity e : entities)
            processEntity(e);
    }

    /**
     * Add one entity to the HashMap of entities that get rendered when render() is called.
     *
     * @param entity Entity that should be added.
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        //if a list with the texturedModel of the entity already exists in entities add entity to that list
        //else create new list in entities for the texturedModel of the entity
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    /**
     * Set some OpenGl related settings needed before 3D objects can be rendered.
     */
    public void prepare() {
        //Clear window
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Only render pixel closest to camera
        GL11.glClearColor(RED, GREEN, BLUE, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * CleanUp all the shaders and renderers, call this method on program exit.
     */
    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
        skyboxRenderer.cleanUp();
    }

    /**
     * Update the projection matrix to current window settings.
     * This method needs to be called whenever the size of the window is changed.
     */
    public void updateProjectionMatrix() {
        projectionMatrix = new Matrix4f();
        if (GameManager.getSettings() != null && GameManager.getSettings().getResHeight() == -1 && GameManager.getSettings().getResWidth() == -1)
            projectionMatrix.setPerspective((float) Math.toRadians(FOV), (float) GameManager.getSettings().getResWidth() / GameManager.getSettings().getResHeight(), NEAR_PLANE, FAR_PLANE);
        projectionMatrix.setPerspective((float) Math.toRadians(FOV), (float) WindowManager.getWidth() / WindowManager.getHeight(), NEAR_PLANE, FAR_PLANE);
    }
}
