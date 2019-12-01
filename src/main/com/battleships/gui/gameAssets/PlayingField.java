package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.particles.ParticleSystemComplex;
import com.battleships.gui.particles.ParticleTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.apache.commons.math3.linear.*;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayingField {

    private static final int OWNFIELD = 0;
    private static final int OPPONENTFIELD = 1;

    private static final String playingfieldTexturePath = "PlayingField.png";
    private static final String highlightTexturePath = "cannonball.png";
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

    private Entity ball;
    private boolean cannonballFlying;
    private Cannonball cannonball;
    private ParticleSystemComplex fire;
    private Map<Integer, List<Vector3f>> burningFires = new HashMap<>();

    private Entity highlighter;

    private int size;
    private int textureOffset;
    private Vector3f ownPosition;
    private Vector3f opponentPosition;
    private Vector3f rotation;
    private float scale;


    public PlayingField(List<Entity> entities, int size, Loader loader) {
        this.size = size;
        this.rotation = new Vector3f();
        this.scale = 300f;
        this.texture = new ModelTexture(loader.loadTexture(playingfieldTexturePath));
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

        this.ball = loader.loadEntityfromOBJ("cannonball", "cannonball.png", 10,1);
        ParticleTexture fireTex = new ParticleTexture(loader.loadTexture("particles/fire.png"), 8, true);
        this.fire = new ParticleSystemComplex(fireTex,20, 3.5f, -0.05f, 2f, 17);
        fire.setLifeError(0.3f);
        fire.setScaleError(0.3f);
        fire.setSpeedError(0.15f);
        fire.randomizeRotation();
        fire.setDirection(new Vector3f(0.1f,1, 0.1f), -0.15f);

        ModelTexture highlightTex = new ModelTexture(loader.loadTexture(highlightTexturePath));
        highlighter = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), highlightTex), 0, new Vector3f(), new Vector3f(), scale / (size + 1));
        highlighter.getRotation().x -= 90;
        entities.add(highlighter);
    }

    public Entity getOwn() {
        return own;
    }

    public Entity getOpponent() {
        return opponent;
    }

    public void placeShip(List<Entity> entities, ShipManager ship, int field, Vector2f index, int shipSize, int rotation){
        //calculate index relative to center of field
        Vector2f relativeIndex = convertIndextoCoords(index);
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
            entities.get(shipIndex).getPosition().y -= 3;
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
            entities.get(shipIndex).getPosition().y -= 3;
            entities.get(shipIndex).getPosition().z = usedField.getPosition().z + relativeIndex.y * usedField.getScale() / (size + 1) - 0.5f;
            entities.get(shipIndex).getRotation().y += rotation == Ship.EAST ? 270 : 90;
        }
//        ship.placeShip(entities, 5, new Vector3f(350 + 10 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450), new Vector3f(), 0.65f);
//        ship.placeShip(entities, 4, new Vector3f(350 + 11 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450 + 300f / 31 * 0.5f), new Vector3f(), 1f);
//        ship.placeShip(entities, 3, new Vector3f(350 + 12 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450), new Vector3f(), 1f);
//        ship.placeShip(entities, 2, new Vector3f(350 + 13 * 300f / 31 - 0.5f ,ownPosition.y + 0.5f,-450 + 300f / 31 * 0.5f) , new Vector3f(), 1f);
        //ownPosition.x + 300 / index * 0.5f,ownPosition.y,ownPosition.z + 300 / index * 0.5f
    }

    /**
     * Create a flying cannonball.
     * @param cannonballs - list of all cannonball entities that should be rendered on screen
     * @param origin - the playing field from which the cannonball originates
     * @param destination - the destination cell on the other field
     */
    public void shoot(List<Entity> cannonballs, int origin, Vector2f destination){
        //TODO shoot without animation
        //calculate index relative to center of field
        if(cannonballFlying) //TODO test this when clicking on field together with if its your turn
            return;

        if(/*call logic to test whether field has been shot already or not
            better: only call this function from logic if shooting is allowed*/false)
            return;

        Vector2f currentDestination = new Vector2f();

        Vector2f originIndex = origin == OWNFIELD ? new Vector2f(ownPosition.x , ownPosition.z) : new Vector2f(opponentPosition.x, opponentPosition.z);

        //calculate position of destination relative to center
        currentDestination = convertIndextoCoords(destination);
        //calculate position of destination as absolute coordinates
        currentDestination.x += origin == OWNFIELD ? opponent.getPosition().x : own.getPosition().x;
        currentDestination.y += origin == OWNFIELD ? opponent.getPosition().z : own.getPosition().z;

        //create cannonball flying from origin to destination
        this.cannonball = new Cannonball(ball, currentDestination, originIndex, cannonballs.size());
        cannonballFlying = true;

        //add cannonball to rendered entities
        cannonballs.add(ball);
    }

    /**
     * Move the cannonball.
     * Needs to be called every frame while a cannonball is flying.
     * @param cannonballs - List containing the cannonball
     */
    public void moveCannonball(List<Entity> cannonballs){
        //TODO make list an entity because only one cannonball is there at a time
        if(!cannonballFlying)
            return;
        if(cannonball.update()) {
            cannonballs.remove(cannonball.getEntityIndex());
            cannonballHit();
        }
    }

    public void cannonballHit(){
        //TODO integration with logic
        if(/*call logic to test if water or ship has been hit*/ true){
            //get index of hit ship from logic
            int shipIndex = 0;
            if (burningFires.containsKey(shipIndex)){ //TODO test if ship has been destroyed (size of Vector List == size of ship)
                burningFires.get(shipIndex).add(new Vector3f(cannonball.getDestination().x, 0, cannonball.getDestination().y));
            }
            else{
                burningFires.put(shipIndex, new ArrayList<Vector3f>());
                burningFires.get(shipIndex).add(new Vector3f(cannonball.getDestination().x, 0, cannonball.getDestination().y));
            }
            cannonballFlying = false;
        }
        else //TODO water splash
            return;
    }

    public void renderFires(){
        for(int i : burningFires.keySet()){
            for(Vector3f pos : burningFires.get(i)){
                fire.generateParticles(pos);
            }
        }
    }

    public void highligtCell(Vector3f intersectionPoint){

        Vector3f pointedCell = getPointedCell(intersectionPoint);
        if(pointedCell == null){
            removeHighlighter();
            return;
        }
        setHighlighter(new Vector2f(pointedCell.x, pointedCell.y), (int)pointedCell.z);
    }

    private Vector3f getPointedCell(Vector3f intersectionPoint){
        return convertCoordsToIndex(intersectionPoint);
    }

    private Vector2f convertIndextoCoords(Vector2f index){
        return new Vector2f(index.x - size / 2f, index.y - size / 2f);
    }

    private Vector3f convertCoordsToIndex(Vector3f coords){
        Vector3f result = new Vector3f();
        int field;
        float cellSize;

        if(coords.x > ownPosition.x - own.getScale() && coords.x < ownPosition.x + own.getScale() && coords.z > ownPosition.z - own.getScale() && coords.z < ownPosition.z + own.getScale()){
            field = OWNFIELD;
            result.x = coords.x - ownPosition.x + own.getScale() / 2;
            result.y = coords.z - ownPosition.z + own.getScale() / 2;
            cellSize = own.getScale() / size;
        }

        else if(coords.x > opponentPosition.x - opponent.getScale() && coords.x < opponentPosition.x + opponent.getScale() && coords.z > opponentPosition.z - opponent.getScale() && coords.z < opponentPosition.z + opponent.getScale()) {
            field = OPPONENTFIELD;
            result.x = coords.x - opponentPosition.x + opponent.getScale() / 2;
            result.y = coords.z - opponentPosition.z + opponent.getScale() / 2;
            cellSize = opponent.getScale() / size;
        }
        else
            return null;

        int indexX = (int)(result.x / (scale / (size + 1)));
        int indexY = (int)(result.y / (scale / (size + 1)));

        result.x = indexX;
        result.y = indexY;
        result.z = field;

        return result;
    }

    private void removeHighlighter(){
        highlighter.getPosition().y = -1000;
    }

    private void setHighlighter(Vector2f index, int field){
        Entity usedField = field == OWNFIELD ? own : opponent;
        float offset = size % 2 == 0 ? 0 : 0.5f;
        Vector2f coords = convertIndextoCoords(index);
        Vector3f position = new Vector3f((coords.x + offset) * usedField.getScale() / (size + 1) + usedField.getPosition().x, -2.51f, (coords.y + offset) * usedField.getScale() / (size + 1) + usedField.getPosition().z);
        System.out.println(position.x + " " + position.y + " " + position.z);
        highlighter.setPosition(position);
    }
}
