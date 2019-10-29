package Game.Assets;

import Engine.Rendering.Entities.CameraOld;
import Engine.Rendering.Entities.EntityOld;
import Engine.Rendering.Models.Mesh;
import Engine.Rendering.Models.ModelTexture;
import Engine.Rendering.Models.TextureLoader;
import Engine.Rendering.Shaders.GLSLProgram;
import Game.Main.SchiffeVersenken;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class PlayingField {

    private GLSLProgram shaderProgram;
    private Mesh mesh;
    private EntityOld playingField;
    private ModelTexture waterTex;
    private CameraOld camera;

    public PlayingField (int size){
        this.camera = SchiffeVersenken.getInstance().getCamera();
        this.shaderProgram = SchiffeVersenken.getInstance().getEntityShaderProgram();  //get shader program to be able to render
        mesh = new Mesh(new int[] {0, 1 ,3, 3, 1, 2},  //create 2d plane for playing field
                            new float[]{-0.5f, 0.5f, 0f,
                                        -0.5f, -0.5f, 0f,
                                         0.5f, -0.5f, 0f,
                                         0.5f, 0.5f, 0f},
                            new float[] {
                                         0, 0,
                                         0, 1,
                                         1, 1,
                                         1, 0});
//        mesh  = new Mesh(new int[] { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19, 19, 17, 18, 20, 21, 23, 23, 21, 22 }, new float[] { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,
//                -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
//
//                0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
//
//                -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
//
//                -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,
//
//                -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f }, new float[] { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0 });

        playingField = new EntityOld(mesh);  //create entity from 2d plane mesh
        waterTex = TextureLoader.loadTexture("Water.jpg");  //load playing field texture
    }

    public void render(){
        shaderProgram.setTexture("diffuseTexture", waterTex, 1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, waterTex.getID());
        shaderProgram.setMatrices(camera, playingField);

        playingField.getPosition().x = 3.5f;
        playingField.getPosition().y = -1.5f;
        playingField.getRotation().x = 90f;
        playingField.getRotation().y = 45f;
        playingField.getScale().x = 3;
        playingField.getScale().y = 3;
        playingField.getScale().z = 3;
        playingField.update();
        playingField.getMesh().render();
        GL13.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL11.glBegin(GL11.GL_LINES);
            GL11.glColor3f(255, 255, 255);
            GL11.glVertex3f(-0.5f, 0.5f, 0f);
            GL11.glVertex3f(-0.5f, -0.5f, 0f);
            GL11.glVertex3f(-0.5f, -0.5f, 0f);
            GL11.glVertex3f(0.5f, 0.5f, 0f);
        GL11.glEnd();
    }

}
