package com.battleships.gui.main;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.entities.Light;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenu;
import com.battleships.gui.gameAssets.PlayingField;
import com.battleships.gui.gameAssets.ShipManager;
import com.battleships.gui.gameAssets.ingameGui.ShipSelector;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.particles.ParticleMaster;
import com.battleships.gui.particles.ParticleSystemComplex;
import com.battleships.gui.particles.ParticleTexture;
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

import java.util.ArrayList;
import java.util.List;

public class Inits {
    public static int GlobalGameState = 0; //0 = Main Menu, 1 = InGame

    private static long window;

    private boolean menuInitDone;
    private boolean gameInitDone;

    private Loader loader;
    private MasterRenderer renderer;
    private Fbo fbo;

    private GuiManager guiManager;
    private List<GuiTexture> permanentGuiElements;
    private MainMenu startMenu;
    private GuiRenderer guiRenderer;


    private MainMenuManager mainMenuManager;
    private GameManager gameManager;

    private ShipManager ships;
    private ShipSelector shipSelector;

    private Camera camera;
    private Light light;

    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private Terrain terrain;

    private List<Entity> entities;
    private Entity ship;

    private PlayingField playingField;

    private Vector3f cellIntersection;
    private Vector3f pointedCell;

    private WaterFrameBuffers waterFbos;
    private WaterShader waterShader;
    private WaterRenderer waterRenderer;
    private List<WaterTile> waterTiles;

    private ParticleTexture fire;

    private ParticleSystemComplex system;

    private MousePicker picker;

    public Loader getLoader() {
        return loader;
    }

    public MasterRenderer getRenderer() {
        return renderer;
    }

    public Fbo getFbo() {
        return fbo;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public List<GuiTexture> getPermanentGuiElements() {
        return permanentGuiElements;
    }

    public MainMenu getStartMenu() {
        return startMenu;
    }

    public GuiRenderer getGuiRenderer() {
        return guiRenderer;
    }

    public static int getGlobalGameState() {
        return GlobalGameState;
    }

    public static long getWindow() {
        return window;
    }

    public boolean isMenuInitDone() {
        return menuInitDone;
    }

    public boolean isGameInitDone() {
        return gameInitDone;
    }

    public ShipManager getShips() {
        return ships;
    }

    public ShipSelector getShipSelector() {
        return shipSelector;
    }

    public Camera getCamera() {
        return camera;
    }

    public Light getLight() {
        return light;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public PlayingField getPlayingField() {
        return playingField;
    }

    public Vector3f getCellIntersection() {
        return cellIntersection;
    }

    public Vector3f getPointedCell() {
        return pointedCell;
    }

    public WaterFrameBuffers getWaterFbos() {
        return waterFbos;
    }

    public WaterShader getWaterShader() {
        return waterShader;
    }

    public WaterRenderer getWaterRenderer() {
        return waterRenderer;
    }

    public List<WaterTile> getWaterTiles() {
        return waterTiles;
    }

    public ParticleTexture getFire() {
        return fire;
    }

    public ParticleSystemComplex getSystem() {
        return system;
    }

    public MousePicker getPicker() {
        return picker;
    }

    public void setCellIntersection(Vector3f cellIntersection) {
        this.cellIntersection = cellIntersection;
    }

    public void setPointedCell(Vector3f pointedCell) {
        this.pointedCell = pointedCell;
    }

    public void initMenu(){

        WindowManager.initialize();

        window = WindowManager.getWindow();

        // *******************Main stuff initialization*******************

        loader = new Loader();
        renderer = new MasterRenderer(loader);
        TextMaster.init(loader);
        gameManager = new GameManager(loader);

        // *******************GUI initialization*******************
        guiManager = new GuiManager(gameManager);
        permanentGuiElements = new ArrayList<>();

        startMenu = new MainMenu(guiManager,loader);
        guiRenderer = new GuiRenderer(loader);

        // *******************Post Processing initialization*******************
        fbo = new Fbo(WindowManager.getWidth(), WindowManager.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
        PostProcessing.init(loader);

        // *******************Callbacks initialization*******************
        mainMenuManager = new MainMenuManager(guiManager);

        WindowManager.setMainMenuCallbacks(mainMenuManager);

        menuInitDone = true;
    }

    public Entity getShip() {
        return ship;
    }

    public static void setGlobalGameState(int globalGameState) {
        GlobalGameState = globalGameState;
    }

    public void initGame(){

        // *******************Camera initialization*******************

        camera = new Camera();
        camera.getPosition().x = 350f;
        camera.getPosition().y = 100f;
        camera.getPosition().z = -450f;

        // *******************Light initialization*******************

        light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));

        // *******************Terrain initialization*******************

        TerrainTexture texture0 = new TerrainTexture(loader.loadTexture("Water.jpg"));
        TerrainTexture texture1 = new TerrainTexture(loader.loadTexture("path.jpg"));
        TerrainTexture texture2 = new TerrainTexture(loader.loadTexture("Gravel.jpg"));
        TerrainTexture texture3 = new TerrainTexture(loader.loadTexture("Grass.jpg"));
        TerrainTexture texture4 = new TerrainTexture(loader.loadTexture("WetSand.jpg"));
        TerrainTexture texture5 = new TerrainTexture(loader.loadTexture("Sand.jpg"));

        texturePack = new TerrainTexturePack(texture0, texture1, texture2, texture3, texture4, texture5);

        blendMap = new TerrainTexture(loader.loadTexture("BlendMap.tga")); //TODO change blendMap to remove water texture

        terrain = new Terrain(0,-1, loader, texturePack, blendMap, "HeightMap.jpg");
//        Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "HeightMap.jpg");

        // *******************Entity initialization*******************

        entities = new ArrayList<>();

        ship = loader.loadEntityfromOBJ("ship4", "ship4.tga", 10, 1);
        ship.setPosition(new Vector3f(0,0,-40));

        entities.add(ship);

        playingField =  new PlayingField(30, loader, gameManager);
        ships = playingField.getShipManager();
        shipSelector = new ShipSelector(loader, guiManager, ships, permanentGuiElements);
//        ShipManager ships = new ShipManager(loader);
        playingField.placeShip( 0, new Vector2f(15,15),4, 0);
        playingField.placeShip(0, new Vector2f(3,3),3, 1);
        playingField.shoot(1, new Vector2f(15,15));
//        playingField.placeShip(entities, ships, 0, 7,2);
//        playingField.placeShip(entities, ships, 0, 9,3);
//        playingField.placeShip(entities, ships, 0, 11,5);

        // *******************Water initialization*******************
        waterFbos = new WaterFrameBuffers();

        waterShader = new WaterShader();
        waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFbos);
        waterTiles = new ArrayList<>();
        waterTiles.add(new WaterTile(400, -400, -3));


        // *******************Particle initialization*******************
        ParticleMaster.init(loader, renderer.getProjectionMatrix());
        fire = new ParticleTexture(loader.loadTexture("particles/fire.png"), 8, true);
        system = new ParticleSystemComplex(fire,20, 3.5f, -0.05f, 2f, 17);
        system.setLifeError(0.3f);
        system.setScaleError(0.3f);
        system.setSpeedError(0.15f);
        system.randomizeRotation();
        system.setDirection(new Vector3f(0.1f,1, 0.1f), -0.15f);

        // *******************Callbacks initialization*******************

        picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain, gameManager);

        gameManager = new GameManager(loader);
        WindowManager.setCallbacks(camera, gameManager, waterFbos);


        gameInitDone = true;

    }
}
