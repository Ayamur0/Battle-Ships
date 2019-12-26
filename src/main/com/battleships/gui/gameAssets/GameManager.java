package com.battleships.gui.gameAssets;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.entities.Light;
import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.MainMenuGui.ESCMenu;
import com.battleships.gui.gameAssets.ingameGui.DisableSymbols;
import com.battleships.gui.gameAssets.ingameGui.ShipCounter;
import com.battleships.gui.gameAssets.ingameGui.ShipSelector;
import com.battleships.gui.gameAssets.testLogic.TestLogic;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.Inits;
import com.battleships.gui.particles.ParticleMaster;
import com.battleships.gui.postProcessing.PostProcessing;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.terrains.TerrainTexture;
import com.battleships.gui.terrains.TerrainTexturePack;
import com.battleships.gui.toolbox.MousePicker;
import com.battleships.gui.toolbox.ParallelTasks;
import com.battleships.gui.water.WaterFrameBuffers;
import com.battleships.gui.water.WaterRenderer;
import com.battleships.gui.water.WaterShader;
import com.battleships.gui.water.WaterTile;
import com.battleships.gui.window.WindowManager;
import com.sun.org.apache.xml.internal.security.Init;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    public static final int MENU = 0;
    public static final int SHIPLACING = 1;
    public static final int SHOOTING = 2;

    private static DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    private static DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    private static int gameState;

    private static FontType pirateFont;
    private static Loader loader;
    private static GuiManager guiManager;
    private static PlayingField playingField;
    private static MousePicker mousePicker;
    private static ShipManager shipManager;
    private static ShipCounter shipCounter;
    private static ShipSelector shipSelector;
    private static DisableSymbols disableSymbols;
    private static MasterRenderer renderer;
    private static Camera camera;
    private static List<GuiTexture> guis = new ArrayList<>();
    private static Light light;
    private static Terrain terrain;
    private static List<Entity> entities = new ArrayList<>();
    private static List<WaterTile> waterTiles = new ArrayList<>();
    private static WaterRenderer waterRenderer;
    private static WaterFrameBuffers waterFbos;
    private static GuiRenderer guiRenderer;
    private static WaterShader waterShader;

    public static void init() {
        WindowManager.initialize();
        loader = new Loader();
        pirateFont = new FontType(loader.loadFontTexture("font/pirate.png"), "pirate");
        renderer = new MasterRenderer(loader);
        TextMaster.init(loader);
        guiManager = new GuiManager();
        guiRenderer = new GuiRenderer(loader);
        AudioMaster.init();
        waterFbos = new WaterFrameBuffers();
        waterShader = new WaterShader();
        waterRenderer = new WaterRenderer(loader, waterShader, MasterRenderer.getProjectionMatrix(), waterFbos);
        ParticleMaster.init(loader, MasterRenderer.getProjectionMatrix());

    }

    public static void placeShip(Vector2i index, int size, int direction, int field){
        if(field == PlayingField.OPPONENTFIELD) {
            //TestLogic.opponent.placeShip(index.x, index.y, size, direction);
        }
        if(field == PlayingField.OWNFIELD){
            playingField.placeShip(index, size, direction);
            //TestLogic.own.placeShip(index.x, index.y, size, direction);
        }
    }

    public static void startShipPlacementPhase(){
        if(shipCounter != null)
            shipCounter.remove();
        playingField.setShipPlacingPhase(true);
        shipSelector = new ShipSelector(loader, guiManager, shipManager, guis);
    }

    public static void startPlayPhase(){
        playingField.setShipPlacingPhase(false);
        if(shipSelector != null)
            shipSelector.remove();
        shipCounter = new ShipCounter(loader, guiManager, guis);
    }

    public static void loadIngameScene(){
        disableSymbols = new DisableSymbols(loader, guiManager, guis);
        camera = new Camera();
        mousePicker = new MousePicker(camera, MasterRenderer.getProjectionMatrix(), terrain);
        TerrainTexture texture1 = new TerrainTexture(loader.loadTexture("path.jpg"));
        TerrainTexture texture2 = new TerrainTexture(loader.loadTexture("Gravel.jpg"));
        TerrainTexture texture3 = new TerrainTexture(loader.loadTexture("Grass.jpg"));
        TerrainTexture texture4 = new TerrainTexture(loader.loadTexture("WetSand.jpg"));
        TerrainTexture texture5 = new TerrainTexture(loader.loadTexture("Sand.jpg"));

        TerrainTexturePack texturePack = new TerrainTexturePack(texture1, texture2, texture3, texture4, texture5);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("BlendMapLarge.tga")); //TODO change blendMap to remove water texture

        terrain = new Terrain(-0.25f,-0.75f, loader, texturePack, blendMap, "HeightMapLarge.jpg");

        light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));

        playingField =  new PlayingField(30, loader);
        shipManager = playingField.getShipManager();

        waterTiles.add(new WaterTile(0, 0, -3));
        waterTiles.add(new WaterTile(800, 0, -3));
        waterTiles.add(new WaterTile(0, -800, -3));
        waterTiles.add(new WaterTile(800, -800, -3));

        camera.setStandardPos();
        WindowManager.setCallbacks(camera, waterFbos);
    }

    public static void renderWater(){
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

        //render reflection texture
        waterFbos.bindReflectionFrameBuffer();
        //move camera under water to get right reflection
        float distance = 2 * (camera.getPosition().y - waterTiles.get(0).getHeight());
        camera.getPosition().y -= distance;
        camera.invertPitch();
        renderer.renderScene(entities, terrain, light, camera, new Vector4f(0, 1, 0, -waterTiles.get(0).getHeight() + 1f));
        ParticleMaster.renderParticles(camera, 1);
        //move camera back to normal
        camera.getPosition().y += distance;
        camera.invertPitch();

        //render refraction texture
        waterFbos.bindRefractionFrameBuffer();
        renderer.renderScene(entities, terrain, light, camera, new Vector4f(0, -1, 0, waterTiles.get(0).getHeight()));
        waterFbos.unbindCurrentFrameBuffer();
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //not all drivers support disabling, if it doesn't work set clipPlane when rendering to screen high enough so nothing gets clipped
        waterRenderer.render(waterTiles, camera, light);
    }

    public static void renderEntities(){
        playingField.render(renderer);
        shipManager.moveCursorShip();
        renderer.renderScene(entities, terrain, light, camera, new Vector4f(0, 0, 0, 0));
    }

    public static void renderParticles(){
        ParticleMaster.update(camera);
        ParticleMaster.renderParticles(camera, 1);
    }

    public static void updateScene(){
        camera.move(terrain);
        mousePicker.update();
        AudioMaster.setListenerData(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, camera.getPitch(), camera.getYaw());
        renderEntities();
        renderWater();
        renderParticles();
        guiRenderer.render(guis);
        TextMaster.render();
        renderer.updateProjectionMatrix();
    }

    public static void cleanUpIngameScene(){
        if(shipCounter != null)
            shipCounter.remove();
        disableSymbols.remove();
        AudioMaster.cleanUp();
        waterFbos.cleanUp();
        waterShader.cleanUp();
        //PostProcessing.cleanUp();
        //fbo.cleanUp();
        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
    }

    /**
     * Shoot method, that passes the shoot command to the playingfield.
     * @param originField - Field the shot originates from (0 for own, 1 for opponent).
     * @param destinationIndex - Index of the field that should get shot.
     */
    public static void shoot(int originField, Vector2i destinationIndex){
        playingField.shoot(originField, destinationIndex);
    }

    /**
     * Passes the command to place a marker, at the specified index, to the playingfield.
     * @param shipHit - {@code true} if a ship was hit, so marker should be red. {@code false} if no ship was hit, so marker should be white.
     * @param index - Index where the marker should be placed.
     * @param field - Field the marker should be placed on (0 for own, 1 for opponent).
     */
    public static void placeMarker(boolean shipHit, Vector2i index, int field){
        playingField.placeMarker(shipHit, index, field);
    }

    /**
     * Finishes the game and shows endscreen.
     * @param won - {@code true} if the player has won, {@code false} else.
     */
    public static void finishGame(boolean won){
        //TODO
    }

    /**
     * Decrement the count, that keeps track of how many enemy ships of each size are alive.
     * @param size - Size of ship the count should be decremented for.
     */
    public static void decrementAliveShip(int size){
        shipCounter.decrementCount(size);
    }

    /**
     * Function that gets called if the user left clicks anywhere in the game.
     * Gets the cursor position and tests all guis with a click action if they were
     * clicked on and if one was clicked executes the click action of that gui.
     */
    public static GLFWMouseButtonCallback testClick = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {

            if(action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_2){
                shipManager.removeCursorShip();
                return;
            }
            if(action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_1) {

                GLFW.glfwGetCursorPos(window, x, y);
                x.rewind();
                y.rewind();

                float xpos = (float) x.get() / WindowManager.getWidth();
                float ypos = (float) y.get() / WindowManager.getHeight();

                x.clear();
                y.clear();

                if (guiManager.testGuiClick(xpos, ypos))
                    return;

                Vector3f cellIntersection = mousePicker.getCurrentIntersectionPoint();
                Vector3i pointedCell = playingField.calculatePointedCell(cellIntersection);
                if (pointedCell != null) {
                    playingField.cellClicked(new Vector2i(pointedCell.x, pointedCell.y), pointedCell.z);
                }
            }
        }
    };

    public static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scanCode, int action, int mods) {
            if(key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS)
                WindowManager.setFullScreen(!WindowManager.isFullscreen());
            if(key == GLFW.GLFW_KEY_F && action == GLFW.GLFW_PRESS)
                startShipPlacementPhase();
            if(key == GLFW.GLFW_KEY_G && action == GLFW.GLFW_PRESS)
                startPlayPhase();
            if(key == GLFW.GLFW_KEY_R && action == GLFW.GLFW_PRESS){
                shipManager.rotateShip();
            }
            if(key == GLFW.GLFW_KEY_X && action == GLFW.GLFW_PRESS){
                toggleAnimations();
            }
            if(key == GLFW.GLFW_KEY_T && action == GLFW.GLFW_PRESS)
                camera.turnCamera();
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS)
                Inits.setStartMenu(new ESCMenu(guiManager,Inits.getLoader()));
        }
    };

    public static void toggleAnimations() {
        playingField.toggleShootingAnimation();
    }

    public static FontType getPirateFont() {
        return pirateFont;
    }

    public static PlayingField getPlayingField() {
        return playingField;
    }

    public static List<GuiTexture> getGuis() {
        return guis;
    }

    public static MousePicker getPicker() {
        return mousePicker;
    }
}
