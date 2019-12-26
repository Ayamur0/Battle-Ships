package com.battleships.gui.main;

import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.MainMenuGui.InGameSettingsMenu;
import com.battleships.gui.particles.ParticleMaster;
import com.battleships.gui.postProcessing.PostProcessing;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;


public class MenuTest {

    public static void main(String[] args){
        Inits inits = new Inits();
        inits.initMenu();
        // ****************************************************
        // *******************Main Game Loop*******************
        // ****************************************************
        //String s = TinyFileDialogs.tinyfd_inputBox("test", "test","");
        while (!GLFW.glfwWindowShouldClose(Inits.getWindow())){
//            picker.update();
            if(Inits.getGlobalGameState() == 0) {
                GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

                //render refraction texture
                GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //not all drivers support disabling, if it doesn't work set clipPlane when rendering to screen high enough so nothing gets clipped

//            new Particle(star, new Vector3f(camera.getPosition().x , camera.getPosition().y, camera.getPosition().z), new Vector3f(0, 30, 0), 1 ,4 ,0 ,1);

                if(Inits.getStartMenu() instanceof InGameSettingsMenu){
                    if (((InGameSettingsMenu) Inits.getStartMenu()).getPlayingFieldSize().isRunning() || ((InGameSettingsMenu) Inits.getStartMenu()).getDifficulty().isRunning())  {
                        ((InGameSettingsMenu) Inits.getStartMenu()).RefreshSliderValue();
                    }
                }

                inits.getGuiRenderer().render(inits.getPermanentGuiElements());
                inits.getGuiManager().renderClickableGuis(inits.getGuiRenderer());
                TextMaster.render();

                WindowManager.updateWindow();
                inits.getRenderer().updateProjectionMatrix();

            }
            else if(Inits.getGlobalGameState() == 1){
                if (!inits.isGameInitDone()){
                    inits.initGame();
                }
                inits.getCamera().move(inits.getTerrain());
                inits.getPicker().update();
                ParticleMaster.update(inits.getCamera());

                Vector3f terrainPoint = inits.getPicker().getCurrentTerrainPoint();
                if(terrainPoint != null) {
//                entities.get(3).setPosition(new Vector3f(terrainPoint.x, terrainPoint.y < -3 ? -3 : terrainPoint.y, terrainPoint.z));
//                System.out.println(terrainPoint.x + " " + terrainPoint.z);
                }
                GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

                //render reflection texture
                inits.getWaterFbos().bindReflectionFrameBuffer();
                //move camera under water to get right reflection
                float distance = 2 * (inits.getCamera().getPosition().y - inits.getWaterTiles().get(0).getHeight());
                inits.getCamera().getPosition().y -= distance;
                inits.getCamera().invertPitch();
                inits.getRenderer().renderScene(inits.getEntities(), inits.getTerrain(), inits.getLight(), inits.getCamera(), new Vector4f(0, 1, 0, -inits.getWaterTiles().get(0).getHeight() + 1f));
                ParticleMaster.renderParticles(inits.getCamera(), 1);
                //move camera back to normal
                inits.getCamera().getPosition().y += distance;
                inits.getCamera().invertPitch();

                //render refraction texture
                inits.getWaterFbos().bindRefractionFrameBuffer();
                inits.getRenderer().renderScene(inits.getEntities(), inits.getTerrain(), inits.getLight(), inits.getCamera(), new Vector4f(0, -1, 0, inits.getWaterTiles().get(0).getHeight()));
                inits.getWaterFbos().unbindCurrentFrameBuffer();
                GL11.glDisable(GL30.GL_CLIP_DISTANCE0); //not all drivers support disabling, if it doesn't work set clipPlane when rendering to screen high enough so nothing gets clipped

                inits.getSystem().generateParticles(new Vector3f());
                inits.getPlayingField().render(inits.getRenderer());
//            new Particle(star, new Vector3f(camera.getPosition().x , camera.getPosition().y, camera.getPosition().z), new Vector3f(0, 30, 0), 1 ,4 ,0 ,1);


                inits.getShip().getRotation().y += 0.1f;
                inits.getPlayingField().moveCannonball();
                inits.setCellIntersection(inits.getPicker().getCurrentIntersectionPoint());
                inits.getPlayingField().highligtCell(inits.getCellIntersection());

                inits.getShips().moveCursorShip();

                inits.getRenderer().renderScene(inits.getEntities(), inits.getTerrain(), inits.getLight(), inits.getCamera(), new Vector4f(0, 0, 0, 0));


                inits.getWaterRenderer().render(inits.getWaterTiles(), inits.getCamera(), inits.getLight());

                ParticleMaster.renderParticles(inits.getCamera(), 1);

//            fbo.unbindFrameBuffer();
//            PostProcessing.doPostProcessing(fbo.getColorTexture());

                inits.getGuiRenderer().render(inits.getPermanentGuiElements());
                TextMaster.render();

                WindowManager.updateWindow();
                inits.getRenderer().updateProjectionMatrix();
            }
        }

        // *******************Clean up*******************

        PostProcessing.cleanUp();
        inits.getFbo().cleanUp();
        TextMaster.cleanUp();
        inits.getGuiRenderer().cleanUp();
        inits.getRenderer().cleanUp();
        inits.getLoader().cleanUp();
        //unload GLFW on window close
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
