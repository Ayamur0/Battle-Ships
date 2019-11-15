package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector3f;

import java.util.List;

public class PlayingField {

    private static final int OWNFIELD = 0;
    private static final int OPPONENTFIELD = 1;

    private static final String texturePath = "PlayingField.png";
    private static final float[] VERTICES = {-0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0};
    private static final int[] INDICES = {0,1,3,3,1,2};
    private static final float[] TEXTURECOORDS = {0,0,0,1,1,1,1,0};
    private static final float[] NORMALS = {0,1,0,0,1,0,0,1,0,0,1,0};
    private static final String objPath = "playingField";
    private static final int MAXSIZE = 31;

    private ModelTexture texture;
    private Entity own;
    private Entity opponent;
    private int vaos;

    private int size;
    private int textureOffset;
    private Vector3f ownPosition;
    private Vector3f opponentPosition;
    private Vector3f rotation;
    private float scale;


    public PlayingField(int size, Loader loader) {
        this.size = size + 1;
        this.rotation = new Vector3f();
        this.scale = 1f;
        this.texture = new ModelTexture(loader.loadTexture(texturePath));
        this.ownPosition = new Vector3f(350, -2.5f, -450);
        this.opponentPosition = new Vector3f(650, -2.5f, -450);
        this.textureOffset = size / MAXSIZE;
//        this.own = new Entity(new TexturedModel(OBJLoader.loadObjModel(objPath), texture), 0, ownPosition, rotation, scale);
        this.own = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), texture), 0, ownPosition, new Vector3f(), scale);
        own.setScale(300);
//        own.getRotation().z -= 90;
        own.getRotation().x -= 90;
//        own.getRotation().y -= 90;
//        own.getRotation().z -= 90;


        this.opponent = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), texture), 0, opponentPosition, new Vector3f(), scale);
        opponent.setScale(300);
//        opponent.getRotation().z -= 90;
        opponent.getRotation().x -= 90;
//        opponent.getRotation().y -= 90;
    }

    public Entity getOwn() {
        return own;
    }

    public Entity getOpponent() {
        return opponent;
    }

    public void placeShip(List<Entity> entities, ShipManager ship, int field, float index, int shipSize){
        ship.placeShip(entities, shipSize, new Vector3f(ownPosition.x - 150 + 150/31f + 150/31f * 0.5f ,ownPosition.y + 0.5f,ownPosition.z - 150 + 150/31f + 150/31f * 0.5f / index * 0.5f), new Vector3f(), 1);
        //ownPosition.x + 300 / index * 0.5f,ownPosition.y,ownPosition.z + 300 / index * 0.5f
    }
}
