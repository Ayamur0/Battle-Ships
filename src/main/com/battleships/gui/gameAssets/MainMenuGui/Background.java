package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Light;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.terrains.Terrain;
import com.battleships.gui.terrains.TerrainTexture;
import com.battleships.gui.terrains.TerrainTexturePack;
import com.battleships.gui.water.WaterFrameBuffers;
import com.battleships.gui.water.WaterRenderer;
import com.battleships.gui.water.WaterShader;
import com.battleships.gui.water.WaterTile;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Background {
    private MasterRenderer renderer;
    private Loader loader;
    private GuiManager guiManager;
    private GuiRenderer guiRenderer;
    private WaterFrameBuffers waterFbos;
    private WaterShader waterShader;
    private WaterRenderer waterRenderer;
    private Camera camera;
    private Terrain terrain;
    private Light light;
    private GridManager gridManager;
    private List<WaterTile> waterTiles;

    public Background(){
        loader = Inits.getLoader();
        renderer = Inits.getRenderer();
        guiManager = Inits.getGuiManager();
        guiRenderer = Inits.getGuiRenderer();

        waterTiles = new ArrayList<>();

        waterFbos = new WaterFrameBuffers();
        waterShader = new WaterShader();
        waterRenderer = new WaterRenderer(loader,waterShader,MasterRenderer.getProjectionMatrix(), waterFbos);

        loadMenuBackground();
    }
    private void loadMenuBackground(){
        camera = new Camera();
        TerrainTexture texture1 = new TerrainTexture(loader.loadTexture("path.jpg"));
        TerrainTexture texture2 = new TerrainTexture(loader.loadTexture("Gravel.jpg"));
        TerrainTexture texture3 = new TerrainTexture(loader.loadTexture("Grass.jpg"));
        TerrainTexture texture4 = new TerrainTexture(loader.loadTexture("WetSand.jpg"));
        TerrainTexture texture5 = new TerrainTexture(loader.loadTexture("Sand.jpg"));

        TerrainTexturePack texturePack = new TerrainTexturePack(texture1, texture2, texture3, texture4, texture5);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("BlendMapLarge.tga")); //TODO change blendMap to remove water texture

        terrain = new Terrain(-0.25f,-0.75f, loader, texturePack, blendMap, "HeightMapLarge.jpg");

        light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));

        gridManager = new GridManager(loader,15);

        waterTiles.add(new WaterTile(0, 0, -3));
        waterTiles.add(new WaterTile(800, 0, -3));
        waterTiles.add(new WaterTile(0, -800, -3));
        waterTiles.add(new WaterTile(800, -800, -3));

        camera.setStandardPos();
        WindowManager.setCallbacks(camera, waterFbos);
    }
    public void renderBackground(){
        camera.move(terrain);
        AudioMaster.setListenerData(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, camera.getPitch(), camera.getYaw());
        GameManager.renderEntities();
        GameManager.renderWater();
        GameManager.renderParticles();
        guiRenderer.render(Inits.getPermanentGuiElements());
        TextMaster.render();
        renderer.updateProjectionMatrix();
    }
}
