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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuTest {

    private static long window;

    public static void main(String[] args){

        WindowManager.initialize();

        window = WindowManager.getWindow();

        // *******************Main stuff initialization*******************

        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer(loader);

        // *******************GUI initialization*******************

        List<GuiTexture> guis = new ArrayList<>();
        //main menu
        Vector2f buttonSize = new Vector2f(0.14f,0.07f);
        Float buttonGap = 0.15f;
        GuiTexture play = new GuiTexture(loader.loadTexture("Brick.jpg"), new Vector2f(0.5f, 0.5f), buttonSize);
        GuiTexture options = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(play.getPositions().x,play.getPositions().y+buttonGap),buttonSize);
        GuiTexture exit = new GuiTexture(loader.loadTexture("Brick.jpg"),new Vector2f(options.getPositions().x,options.getPositions().y+buttonGap),buttonSize);
        guis.add(play);
        guis.add(options);
        guis.add(exit);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        GuiClickCallback guiClickCallback = new GuiClickCallback();
        guiClickCallback.addClickableGui(play);
        guiClickCallback.addClickableGui(options);
        guiClickCallback.addClickableGui(exit);

        // *******************TextInitialization*******************

        TextMaster.init(loader);

        FontType font = new FontType(loader.loadFontTexture("font/PixelDistance.png"), "PixelDistance");
        GUIText playText = new GUIText("Play", 1, font, new Vector2f(play.getPositions().x-play.getScale().x/2+0.01f,play.getPositions().y-play.getScale().y/2+0.01f), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        playText.setColor(1f,1f,1f);
        GUIText optionsText = new GUIText("Options", 1, font,new Vector2f(options.getPositions().x-options.getScale().x/2+0.01f,options.getPositions().y-options.getScale().y/2+0.01f), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        optionsText.setColor(1f,1f,1f);
        GUIText exitText = new GUIText("Exit", 1, font,new Vector2f(exit.getPositions().x-exit.getScale().x/2+0.01f,exit.getPositions().y-exit.getScale().y/2+0.01f), 0.12f, true, 0.0f, 0.1f, new Vector3f(1.0f,0.0f,0.0f), new Vector2f());
        exitText.setColor(1f,1f,1f);
        // *******************Camera initialization*******************

        Camera camera = new Camera();

        // *******************Light initialization*******************

        Light light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));

        // *******************Terrain initialization*******************


        // *******************Entity initialization*******************

        List<Entity> entities = new ArrayList<>();


        List<Entity> ferns = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 500; i++){
            float x = random.nextFloat() * 800 - 400;
            float z = random.nextFloat() * -600;
        }

        entities.addAll(ferns);

        // *******************Water initialization*******************
        WaterFrameBuffers waterFbos = new WaterFrameBuffers();

        WaterShader waterShader = new WaterShader();


        // *******************Particle initialization*******************
        ParticleMaster.init(loader, renderer.getProjectionMatrix());

        // *******************Post Processing initialization*******************

        Fbo fbo = new Fbo(WindowManager.getWidth(), WindowManager.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
        PostProcessing.init(loader);


        // *******************Callbacks initialization*******************

        WindowManager.setCallbacks(camera, guiClickCallback, waterFbos);

//        MousePicker picker = new MousePicker(renderer.getProjectionMatrix(), camera, terrain);

        // ****************************************************
        // *******************Main Game Loop*******************
        // ****************************************************

        while (!GLFW.glfwWindowShouldClose(window)){
//            picker.update();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            //render reflection texture
            waterFbos.bindReflectionFrameBuffer();
            //move camera under water to get right reflection
            camera.invertPitch();
            //move camera back to normal
            camera.invertPitch();

            //render refraction texture
            waterFbos.bindRefractionFrameBuffer();
            waterFbos.unbindCurrentFrameBuffer();
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //not all drivers support disabling, if it doesn't work set clipPlane when rendering to screen high enough so nothing gets clipped

//            new Particle(star, new Vector3f(camera.getPosition().x , camera.getPosition().y, camera.getPosition().z), new Vector3f(0, 30, 0), 1 ,4 ,0 ,1);
            ParticleMaster.update(camera);


            ParticleMaster.renderParticles(camera, 1);


            guiRenderer.render(guis);
            TextMaster.render();

            WindowManager.updateWindow();
            renderer.updateProjectionMatrix();

            double []xpos=new double[1];
            double []ypos   =new double[1];
            GLFW.glfwGetCursorPos(window,xpos,ypos);
            Point p = new Point((int)(xpos[0]),(int)(ypos[0]));
            //System.out.println(p);
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
