package com.battleships.gui.gameAssets;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.entities.Light;
import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.MainMenuGui.ESCMenu;
import com.battleships.gui.gameAssets.MainMenuGui.InGameSettingsMenu;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.gameAssets.grids.ShipManager;
import com.battleships.gui.gameAssets.ingameGui.DisableSymbols;
import com.battleships.gui.gameAssets.ingameGui.ShipCounter;
import com.battleships.gui.gameAssets.ingameGui.ShipSelector;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.particles.ParticleMaster;
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
import com.battleships.logic.LogicManager;
import com.battleships.logic.Settings;
import com.battleships.network.NetworkManager;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLXAMDGPUAssociation;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class that controls the game from the gui side.
 * Contains all functions needed by the logic to control the gui for the game.
 * 
 * @author Tim Staudenmaier
 */
public class GameManager {

    /**
     * Constants indicating the different game states the gui can be in.
     */
    public static final int MENU = 0, SHIPLACING = 1, SHOOTING = 2;

    /**
     * DoubleBuffers used to read position of the mouse from GLFW.
     */
    private static DoubleBuffer x = BufferUtils.createDoubleBuffer(1), y = BufferUtils.createDoubleBuffer(1);

    /**
     * Indicates the current game state of the gui.
     */
    private static int gameState;

    /**
     * Indicates if game is loading
     */
    private static boolean loading;

    /**
     * Main font used in this game
     */
    private static FontType pirateFont;
    /**
     * Loader for loading models and textures.
     */
    private static Loader loader;
    /**
     * GuiManager that handles guis with click functions.
     */
    private static GuiManager guiManager;
    /**
     * GridManager that handles the grids the game is played on.
     */
    private static GridManager gridManager;
    /**
     * MousePicker to get the position of the mouse cursor in the 3D world.
     */
    private static MousePicker mousePicker;
    /**
     * ShipManager that handles placing the ships.
     */
    private static ShipManager shipManager;
    /**
     * ShipCounter gui that is used during shooting phase.
     */
    private static ShipCounter shipCounter;
    /**
     * ShipSelector gui that is used during ship placing phase.
     */
    private static ShipSelector shipSelector;
    /**
     * Symbols that indicate whether sound and animations are currently
     * enabled or disabled.
     */
    private static DisableSymbols disableSymbols;
    /**
     * Renderer that handles rendering of entities, terrains ans skyboxes.
     */
    private static MasterRenderer renderer;
    /**
     * Camera through which the scene is viewed.
     */
    private static Camera camera;
    /**
     * List containing all {@link GuiTexture}s on the screen.
     */
    private static List<GuiTexture> guis = new ArrayList<>();
    /**
     * Light that lights the scene (sun).
     */
    private static Light light;
    /**
     * Terrain the game is played on.
     */
    private static Terrain terrain;
    /**
     * List containing all entities currently in the scene.
     */
    private static List<Entity> entities = new ArrayList<>();
    /**
     * List that contains all {@link WaterTile}s of the scene.
     */
    private static List<WaterTile> waterTiles = new ArrayList<>();
    /**
     * Renderer for rendering the {@link WaterTile}s.
     */
    private static WaterRenderer waterRenderer;
    /**
     * FrameBuffers that create the reflection and refraction texture for the {@link WaterTile}s.
     */
    private static WaterFrameBuffers waterFbos;
    /**
     * Renderer for rendering all {@link GuiTexture}s.
     */
    private static GuiRenderer guiRenderer;
    /**
     * Shader used by the {@link WaterRenderer}.
     */
    private static WaterShader waterShader;

    /**
     * Manager for loading and handling main menu.
     */
    private static MainMenuManager mainMenuManager;

    /**
     * Frame buffer for applying the blur effect to the scene
     * during main menu.
     */
    private static Fbo blur;

    /**
     * LogicManager that handles all logic functions.
     */
    private static LogicManager logic;

    /**
     * Contains all settings currently set for the game.
     */
    private static Settings settings;

    /**
     * {@code true} if the cannonball has reached it's destination.
     * Gets read in the next frame and is then reset after executing functions needed after the cannonball has hit.
     */
    private static boolean cannonballDone;

    /**
     * {@code true} if the last cannonball has hit a ship.
     */
    private static boolean cannonballHit;

    /**
     * Class that handles network related stuff
     */
    private static NetworkManager network;

    /**
     * Initialize the GameManager and all needed components.
     * Needs to be called when the game is started.
     */
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
        loading = true;
        blur = new Fbo(WindowManager.getWidth(), WindowManager.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
        PostProcessing.init(loader);
        mainMenuManager = new MainMenuManager(guiManager,loader,waterFbos);
        settings = new Settings();
        logic = new LogicManager();
        network = new NetworkManager();
    }

    /**
     * GridManager places a ship.
     * @param index Index of the cell the stern of the ship is on.
     * @param size Size of the ship.
     * @param direction Direction the ship is facing (constants in {@link ShipManager}).
     * @param field ID of the grid the ship is placed on.
     */
    public static void placeShip(Vector2i index, int size, int direction, int field){
        if(logic.canShipBePlaced(index.x,index.y,size,direction,field)) {
            shipSelector.decrementCount(size);
            logic.placeShip(index.x, index.y, size, direction, gridManager.placeShip(index, size, direction, field), field);
        }
    }

    /**
     * Removes all ships on the players grid.
     */
    public static void removeAllShips(){
        if(shipSelector != null)
            shipSelector.resetCount();
        gridManager.removeAllShips();
        if(gridManager.getShips().size() > 0)
            logic.removeAllShips();
    }

    /**
     * Starts the ship placing phase.
     * Creates gui needed for that phase and destroys unneeded guis.
     */
    public static void startShipPlacementPhase(){
        GridManager.setIsBackground(false);
        WindowManager.setCallbacks(camera, waterFbos);
        disableSymbols = new DisableSymbols(loader, guiManager, guis);
        if(shipCounter != null)
            shipCounter.remove();
        shipSelector = new ShipSelector(loader, guiManager, shipManager, guis);
    }

    /**
     * Starts the shooting phase.
     * Creates gui needed for that phase and destroys unneeded guis.
     */
    public static void startPlayPhase(){
        if(shipSelector != null)
            shipSelector.remove();
        shipCounter = new ShipCounter(loader, guiManager, guis);
    }

    /**
     * Loads the main scene the game is played in.
     * This scene mainly consists of a terrain, water, a light and a camera.
     */
    public static void loadIngameScene(){
        clearScene();
        disableSymbols = new DisableSymbols(loader, guiManager, guis);
        camera = new Camera();
        mousePicker = new MousePicker(camera, MasterRenderer.getProjectionMatrix(), terrain);
        TerrainTexture texture1 = new TerrainTexture(loader.loadTexture("path.jpg"));
        TerrainTexture texture2 = new TerrainTexture(loader.loadTexture("Gravel.jpg"));
        TerrainTexture texture3 = new TerrainTexture(loader.loadTexture("Grass.jpg"));
        TerrainTexture texture4 = new TerrainTexture(loader.loadTexture("WetSand.jpg"));
        TerrainTexture texture5 = new TerrainTexture(loader.loadTexture("Sand.jpg"));

        TerrainTexturePack texturePack = new TerrainTexturePack(texture1, texture2, texture3, texture4, texture5);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("BlendMapLarge.tga"));

        terrain = new Terrain(-0.25f,-0.75f, loader, texturePack, blendMap, "HeightMapLarge.jpg");

        light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));

        gridManager =  new GridManager(loader, settings.getSize());
        shipManager = gridManager.getShipManager();

        waterTiles.add(new WaterTile(0, 0, -3));
        waterTiles.add(new WaterTile(800, 0, -3));
        waterTiles.add(new WaterTile(0, -800, -3));
        waterTiles.add(new WaterTile(800, -800, -3));

        camera.setStandardPos();
        WindowManager.setCallbacks(camera, waterFbos);
        loading = false;
    }

    /**
     * Changes the size of the grids to the size in the settings.
     */
    public static void resizeGrid(){
        gridManager.resizeGrid(loader,settings.getSize());
        camera.setStandardPos();
    }

    /**
     * Removes everything from the scene.
     */
    public static void clearScene(){
        guiManager.clearClickableGuis();
        waterTiles.clear();
        entities.clear();
        terrain = null;
        guis.clear();
        camera = null;
        mousePicker = null;
        gridManager = null;
        shipManager = null;
        light = null;
    }

    /**
     * Renders all the {@link WaterTile}s of the scene.
     */
    public static void prepareWater(){
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

    /**
     * Renders all the {@link Entity} of the scene.
     */
    public static void renderEntities(){
        gridManager.render(renderer);
        shipManager.moveCursorShip();
        renderer.renderScene(entities, terrain, light, camera, new Vector4f(0, 0, 0, 0));
    }

    /**
     * Renders all the {@link com.battleships.gui.particles.Particle}s of the scene.
     */
    public static void renderParticles(){
        ParticleMaster.update(camera);
        ParticleMaster.renderParticles(camera, 1);
    }

    /**
     * Updates everything in the scene and then renders everything.
     * This render method blurs the scene, so it can be used as a background image.
     */
    public static void updateSceneBlurred(){
        blur.updateSize();
        camera.move(terrain);
        camera.addYaw(0.1f);
        camera.scrollCallback.invoke(0,0,-0.05f);
        mousePicker.update();
        AudioMaster.setListenerData(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, camera.getPitch(), camera.getYaw());
        blur.bindFrameBuffer();
        renderEntities();
        blur.unbindFrameBuffer();
        prepareWater();
        if (MainMenuManager.getMenu() instanceof InGameSettingsMenu){
            if (((InGameSettingsMenu) MainMenuManager.getMenu()).isRunning()){
                ((InGameSettingsMenu) MainMenuManager.getMenu()).RefreshSliderValue();
            }
        }
        blur.bindFrameBuffer();
        waterRenderer.render(waterTiles, camera, light);
        renderParticles();
        blur.unbindFrameBuffer();
        renderer.updateProjectionMatrix();
        PostProcessing.doPostProcessing(blur.getColorTexture());
        guiRenderer.render(guis);
        TextMaster.render();
        if(settings.isOnline())
            network.execute();
    }

    /**
     * Updates everything in the scene and then renders everything.
     */
    public static void updateScene(){
        if(GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_L) == GLFW.GLFW_PRESS)
            logic.advanceTurn();
        camera.move(terrain);
        mousePicker.update();
        AudioMaster.setListenerData(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, camera.getPitch(), camera.getYaw());
        if(cannonballDone) {
            cannonballDone = false;
            if(cannonballHit)
                logic.repeatTurn();
            else
                logic.advanceTurn();
        }
        renderEntities();
        prepareWater();
        waterRenderer.render(waterTiles, camera, light);
        renderParticles();
        guiRenderer.render(guis);
        TextMaster.render();
        renderer.updateProjectionMatrix();
        if(settings.isOnline())
            network.execute();
    }

    /**
     * Cleans up everything that was used in the ingame scene.
     * Needs to be called when the ingame scene isn't needed anymore.
     */
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
     * Shoot method, that passes the shoot command to the {@link GridManager}.
     * @param originField ID of the grid the shot originates from.
     * @param destinationIndex Index of the cell that should get shot.
     *
     * @return true if the shot can be made (no ball is currently flying and cell hasn't already been shot), false else.
     */
    public static boolean shoot(int originField, Vector2i destinationIndex){
        if(originField == GridManager.OWNFIELD && !logic.isPlayerTurn() || originField == GridManager.OPPONENTFIELD && logic.isPlayerTurn())
            return false;
        if(settings.isOnline() && logic.getTurnHandler().isPlayerTurn()){
            network.sendShoot(destinationIndex.x, destinationIndex.y);
            return true;
        }
        return gridManager.shoot(originField, destinationIndex);
    }

    /**
     * Method to process answer of shots made through internet connection.
     * @param shipHit {@code true} if the shot hit a ship, {@code false} else.
     */
    public static void processShootAnswer(boolean shipHit){
        gridManager.getCannonball().cannonballHit2(shipHit);
    }

    /**
     * Function to tell GameManager that cannonball has reached it's destination.
     * Needed to exit the second thread in which the cannonball was moved, so OpenGL function can be used.
     * Only used if animations are enabled.
     * @param shipHit {@code true} if the cannonball has hit a ship, {@code false} else.
     */
    public static void cannonballHit(boolean shipHit){
        cannonballDone = true;
        cannonballHit = shipHit;
    }

    /**
     * Passes the command to place a marker, at the specified index, to the {@link GridManager}.
     * @param shipHit {@code true} if a ship was hit, so marker should be red. {@code false} if no ship was hit, so marker should be white.
     * @param index Index where the marker should be placed.
     * @param field ID of the grid the marker should be placed on.
     */
    public static void placeMarker(boolean shipHit, Vector2i index, int field){
        gridManager.placeMarker(shipHit, index, field);
    }

    /**
     * Finishes the game and shows endscreen.
     * @param won {@code true} if the player has won, {@code false} else.
     */
    public static void finishGame(boolean won){
        if(shipCounter != null)
            shipCounter.remove();
        FinishGame f = new FinishGame();
        f.finishGame(loader, guiManager, won);
    }

    /**
     * Updates the counts, that keep track of how many enemy ships of each size are alive.
     */
    public static void updateAliveShip(){
        shipCounter.updateCount();
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

                Vector3i pointedCell = gridManager.getCurrentPointedCell();
                if (pointedCell != null) {
                    gridManager.cellClicked(new Vector2i(pointedCell.x, pointedCell.y), pointedCell.z);
                }
            }
        }
    };

    /**
     * Function that gets called if the user presses any key on the keyboard.
     * Calls the corresponding function if a key that has a function was pressed.
     */
    public static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scanCode, int action, int mods) {
            if(key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS)
                WindowManager.setFullScreen(!WindowManager.isFullscreen());
            if(key == GLFW.GLFW_KEY_F && action == GLFW.GLFW_PRESS)
                logic.advanceGamePhase();
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
                if (GameManager.getLogic().getGameState()!=GameManager.MENU)
                    MainMenuManager.setMenu(new ESCMenu(guiManager,loader));
            if(key == GLFW.GLFW_KEY_K && action == GLFW.GLFW_PRESS) {
                FinishGame f = new FinishGame();
                f.finishGame(loader, guiManager, false);
            }
            if(key == GLFW.GLFW_KEY_L && action == GLFW.GLFW_PRESS) {
                logic.advanceTurn();
            }
            if(key == GLFW.GLFW_KEY_O&& action == GLFW.GLFW_PRESS) {
                logic.placeRandomShips(0);
            }
        }
    };

    /**
     * Toggle the animations of the game and the symbol indicating
     * if animation are on or off.
     */
    public static void toggleAnimations() {
        gridManager.toggleShootingAnimation();
        disableSymbols.toggleSymbol(DisableSymbols.ANIMATION);
    }

    /**
     * @return The main font this game uses.
     */
    public static FontType getPirateFont() {
        return pirateFont;
    }

    /**
     * @return The GridManager of this game.
     */
    public static GridManager getGridManager() {
        return gridManager;
    }

    /**
     * @return All guis currently on the screen.
     */
    public static List<GuiTexture> getGuis() {
        return guis;
    }

    /**
     * @return The mouse picker of the game.
     */
    public static MousePicker getPicker() {
        return mousePicker;
    }

    /**
     * @return The main menu manager handling the main menu.
     */
    public static MainMenuManager getMainMenuManager() {
        return mainMenuManager;
    }

    /**
     * @return The boolean indicating if game is loading
     */
    public static boolean getLoading(){
        return loading;
    }

    /**
     * @return LogicManager this game uses.
     */
    public static LogicManager getLogic() {
        return logic;
    }

    /**
     * @return Class containing all settings with their current values.
     */
    public static Settings getSettings() {
        return settings;
    }

    /**
     * @return The Network manager of this game.
     */
    public static NetworkManager getNetwork() {
        return network;
    }
}
