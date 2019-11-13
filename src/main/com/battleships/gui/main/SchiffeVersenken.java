package com.battleships.gui.main;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.entities.Light;
import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.PlayingField;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiRenderer;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.particles.*;
import com.battleships.gui.postProcessing.Fbo;
import com.battleships.gui.postProcessing.PostProcessing;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.renderingEngine.OBJLoader;
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
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SchiffeVersenken {

    private static long window;

    public static void main(String[] args){

        WindowManager.initialize();

        window = WindowManager.getWindow();

        // *******************Main stuff initialization*******************

        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer(loader);

        // *******************GUI initialization*******************

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f,0.25f));
        guis.add(gui);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        GuiClickCallback guiClickCallback = new GuiClickCallback();
        guiClickCallback.addClickableGui(gui);

        // *******************TextInitialization*******************

        TextMaster.init(loader);

        FontType font = new FontType(loader.loadFontTexture("font/PixelDistance.png"), "PixelDistance");
        GUIText text = new GUIText("Testing text rendering!", 1, font, new Vector2f(0f,0.4f), 1f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        text.setColor(1,1,1);
        GUIText text2 = new GUIText("Text with outline!", 1, font, new Vector2f(0f,0.5f), 1f, true, 0.7f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        text2.setColor(1,1,1);
        GUIText text3 = new GUIText("Glowing Text!", 1, font, new Vector2f(0f,0.6f), 1f, true, 0.5f, 0.4f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        text3.setColor(1,1,1);
        GUIText text4 = new GUIText("Text with shadow!", 1, font, new Vector2f(0f,0.7f), 1f, true, 0.7f, 0.1f, new Vector3f(0.0f,0.0f,0.0f), new Vector2f(-0.006f, -0.006f));
        text4.setColor(1,1,1);

        // *******************Camera initialization*******************

        Camera camera = new Camera();

        // *******************Light initialization*******************

        Light light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));

        // *******************Terrain initialization*******************

        TerrainTexture texture0 = new TerrainTexture(loader.loadTexture("Water.jpg"));
        TerrainTexture texture1 = new TerrainTexture(loader.loadTexture("path.jpg"));
        TerrainTexture texture2 = new TerrainTexture(loader.loadTexture("Gravel.jpg"));
        TerrainTexture texture3 = new TerrainTexture(loader.loadTexture("Grass.jpg"));
        TerrainTexture texture4 = new TerrainTexture(loader.loadTexture("WetSand.jpg"));
        TerrainTexture texture5 = new TerrainTexture(loader.loadTexture("Sand.jpg"));

        TerrainTexturePack texturePack = new TerrainTexturePack(texture0, texture1, texture2, texture3, texture4, texture5);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("BlendMap.tga"));

        Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "HeightMap.jpg");
//        Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "HeightMap.jpg");

        // *******************Entity initialization*******************

        List<Entity> entities = new ArrayList<>();

        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern"), new ModelTexture(loader.loadTexture("FernAtlas.tga")));
        fern.getTexture().setNumberOfRows(2);
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setUseFakeLighting(true);

        List<Entity> ferns = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 500; i++){
            float x = random.nextFloat() * 800 - 400;
            float z = random.nextFloat() * -600;
            ferns.add(new Entity(fern, random.nextInt(4), new Vector3f(x, terrain.getHeightOfTerrain(x,z),z ), new Vector3f(), 0.6f));
        }

        entities.addAll(ferns);

        Entity ship = loader.loadEntityfromOBJ("test", "4ShipTex.tga", 10, 1);
        ship.setPosition(new Vector3f(0,0,-40));

        entities.add(ship);

        PlayingField playingField =  new PlayingField(30, loader);
        entities.add(playingField.getOwn());
        entities.add(playingField.getOpponent());
        entities.add(playingField.placeShip(0,0,0, loader));

        // *******************Water initialization*******************
        WaterFrameBuffers waterFbos = new WaterFrameBuffers();

        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFbos);
        List<WaterTile> waterTiles = new ArrayList<>();
        waterTiles.add(new WaterTile(400, -400, -3));


        // *******************Particle initialization*******************
        ParticleMaster.init(loader, renderer.getProjectionMatrix());
        ParticleTexture fire = new ParticleTexture(loader.loadTexture("particles/fire.png"), 8, true);
        ParticleSystemComplex system = new ParticleSystemComplex(fire,20, 3.5f, -0.05f, 2f, 17);
        system.setLifeError(0.3f);
        system.setScaleError(0.3f);
        system.setSpeedError(0.15f);
        system.randomizeRotation();
        system.setDirection(new Vector3f(0.1f,1, 0.1f), -0.15f);

        // *******************Post Processing initialization*******************

        Fbo fbo = new Fbo(WindowManager.getWidth(), WindowManager.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
        PostProcessing.init(loader);


        // *******************Callbacks initialization*******************

        WindowManager.setCallbacks(camera, guiClickCallback, waterFbos);

        MousePicker picker = new MousePicker(renderer.getProjectionMatrix(), camera);

        // ****************************************************
        // *******************Main Game Loop*******************
        // ****************************************************

        while (!GLFW.glfwWindowShouldClose(window)){
            camera.move(window, terrain);
            picker.update();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            //render reflection texture
            waterFbos.bindReflectionFrameBuffer();
            //move camera under water to get right reflection
            float distance = 2 * (camera.getPosition().y - waterTiles.get(0).getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, terrain, light, camera, new Vector4f(0, 1, 0, -waterTiles.get(0).getHeight() + 1f));
            //move camera back to normal
            camera.getPosition().y += distance;
            camera.invertPitch();

            //render refraction texture
            waterFbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities, terrain, light, camera, new Vector4f(0, -1, 0, waterTiles.get(0).getHeight()));
            waterFbos.unbindCurrentFrameBuffer();
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //not all drivers support disabling, if it doesn't work set clipPlane when rendering to screen high enough so nothing gets clipped

            system.generateParticles(new Vector3f());
//            new Particle(star, new Vector3f(camera.getPosition().x , camera.getPosition().y, camera.getPosition().z), new Vector3f(0, 30, 0), 1 ,4 ,0 ,1);
            ParticleMaster.update(camera);

            ship.getRotation().y += 0.1f;

//            for (Entity e : ferns)
//                renderer.processEntity(e);
//            renderer.processEntity(ship);
//            renderer.processTerrain(terrain);
//            renderer.processTerrain(terrain2);

//            fbo.bindFrameBuffer();

            renderer.renderScene(entities, terrain, light, camera, new Vector4f(0, -1, 0, 10000));
//            renderer.render(light,camera);
            ParticleMaster.renderParticles(camera, 1);

            waterRenderer.render(waterTiles, camera, light);

//            fbo.unbindFrameBuffer();
//            PostProcessing.doPostProcessing(fbo.getColorTexture());

            guiRenderer.render(guis);
            TextMaster.render();

            WindowManager.updateWindow();
            renderer.updateProjectionMatrix();
        }

        // *******************Clean up*******************

        waterFbos.cleanUp();
        waterShader.cleanUp();
        PostProcessing.cleanUp();
        fbo.cleanUp();
        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        //unload GLFW on window close
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
