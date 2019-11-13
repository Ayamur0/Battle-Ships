package com.battleships.gui.water;

import com.battleships.gui.entities.Camera;
import com.battleships.gui.entities.Light;
import com.battleships.gui.models.RawModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.toolbox.Maths;
import com.battleships.gui.window.WindowManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class WaterRenderer {

    private static final String DUDV_MAP = "waterDUDV2.png"; //TODO find best texture and tiling size
    private static final String NORMAL_MAP = "normalMap2.png";
    private static final float WAVE_SPEED = 0.01f;

    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;

    private float moveFactor = 0;

    private int dudvTexture;
    private int normalMap;

    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        dudvTexture = loader.loadTexture(DUDV_MAP);
        normalMap = loader.loadTexture(NORMAL_MAP);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }

    public void render(List<WaterTile> water, Camera camera, Light sun) {
        prepareRender(camera, sun);
        for (WaterTile tile : water) {
            Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), new Vector3f(), WaterTile.TILE_SIZE);
            shader.loadModelMatrix(modelMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }

    private void prepareRender(Camera camera, Light sun){
        shader.start();
        shader.loadViewMatrix(camera);
        shader.loadProjectionMatrix(MasterRenderer.getProjectionMatrix());
        moveFactor += WAVE_SPEED * WindowManager.getDeltaTime();
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        shader.loadLight(sun);
        shader.loadPlanes(MasterRenderer.getNearPlane(), MasterRenderer.getFarPlane());
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void unbind(){
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
}
