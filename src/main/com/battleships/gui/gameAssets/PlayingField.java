package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
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
        this.size = size;
        this.rotation = new Vector3f();
        this.scale = 1f;
        this.texture = new ModelTexture(loader.loadTexture(texturePath));
        this.ownPosition = new Vector3f(350, -2.5f, -450);
        this.opponentPosition = new Vector3f(650, -2.5f, -450);
        this.textureOffset = (size + 1) / MAXSIZE;
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

    public void placeShip(List<Entity> entities, ShipManager ship, int field, Vector2f index, int shipSize, int rotation){
        //calculate index relative to center of field
        Vector2f relativeIndex = new Vector2f(index.x - size / 2f, index.y - size / 2f);
        Entity usedField = field == OWNFIELD ? own : opponent;
//        ship.placeShip(entities, shipSize, new Vector3f(ownPosition.x - 150 + 150/31f + 150/31f * 0.5f ,ownPosition.y + 0.5f,ownPosition.z - 150 + 150/31f + 150/31f * 0.5f / index * 0.5f), new Vector3f(), 1);
        if(rotation == Ship.NORTH || rotation == Ship.SOUTH) {
            //move index so it's at center of ship instead of back
            //upwards if ship is facing north, downwards if ship is facing south
            relativeIndex.y += rotation == Ship.NORTH ? -(shipSize - 1) / 2f : (shipSize - 1) / 2f;
            //create new ship without position and rotation
            //save index to set position and rotation
            //set scale to 1 for all ships, except biggest ship needs to be 0.65, size 3 should be ~ 0.9 //TODO change models
            int shipIndex = ship.placeShip(entities, shipSize, new Vector3f(), new Vector3f(), shipSize == 5 ? 0.65f : 1f);
            //calculate x(y) position for ship
            //start at center of field
            //calculate width(height) of one column(row) with scale / size
            //add relativeIndex times the width(height) of one column(row)
            //add offset of -0.5 so all ships are centered
            //size + 1 because of row and column containing letters and numbers
            entities.get(shipIndex).getPosition().x = usedField.getPosition().x + relativeIndex.x * usedField.getScale() / (size + 1) ;
            entities.get(shipIndex).getPosition().z = usedField.getPosition().z + relativeIndex.y * usedField.getScale() / (size + 1) - 0.5f;
            entities.get(shipIndex).getRotation().y += rotation == Ship.NORTH ? 0 : 180;
        }
        if(rotation == Ship.EAST || rotation == Ship.WEST) {
            //move index so it's at center of ship instead of back
            //right if ship is facing east, left is ship is facing west
            relativeIndex.x += rotation == Ship.EAST ?(shipSize - 1) / 2f : -(shipSize - 1) / 2f;
            //create new ship without position and rotation
            //save index to set position and rotation
            int shipIndex = ship.placeShip(entities, shipSize, new Vector3f(), new Vector3f(), 1f);
            //calculate x(y) position for ship
            //start at center of field
            //calculate width(height) of one column(row) with scale / size
            //add relativeIndex times the width(height) of one column(row)
            //add offset of -0.5 so all ships are centered
            //size + 1 because of row and column containing letters and numbers
            entities.get(shipIndex).getPosition().x = usedField.getPosition().x + relativeIndex.x * usedField.getScale() / (size + 1) - 0.5f;
            entities.get(shipIndex).getPosition().z = usedField.getPosition().z + relativeIndex.y * usedField.getScale() / (size + 1) - 0.5f;
            entities.get(shipIndex).getRotation().y += rotation == Ship.EAST ? 270 : 90;
        }
//        ship.placeShip(entities, 5, new Vector3f(350 + 10 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450), new Vector3f(), 0.65f);
//        ship.placeShip(entities, 4, new Vector3f(350 + 11 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450 + 300f / 31 * 0.5f), new Vector3f(), 1f);
//        ship.placeShip(entities, 3, new Vector3f(350 + 12 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450), new Vector3f(), 1f);
//        ship.placeShip(entities, 2, new Vector3f(350 + 13 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450 + 300f / 31 * 0.5f) , new Vector3f(), 1f);
        //ownPosition.x + 300 / index * 0.5f,ownPosition.y,ownPosition.z + 300 / index * 0.5f
    }
}
