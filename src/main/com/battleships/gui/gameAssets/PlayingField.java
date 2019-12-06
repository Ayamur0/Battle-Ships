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

    public static final int OWNFIELD = 0;
    public static final int OPPONENTFIELD = 1;

    private static final String playingfieldTexturePath = "PlayingField.png";
    private static final String highlightTexturePath = "cannonball.png";
    private static final float[] VERTICES = {-0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0};
    private static final int[] INDICES = {0, 1, 3, 3, 1, 2};
    private static final float[] TEXTURECOORDS = {0, 0, 0, 1, 1, 1, 1, 0};
    private static final float[] NORMALS = {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};
    private static final int MAXSIZE = 31;

    private ModelTexture texture;
    private Entity own;
    private Entity opponent;

    private Entity ball;
    private boolean cannonballFlying;
    private Cannonball cannonball;
    private ParticleSystemComplex fire;
    private Map<Integer, List<Vector3f>> burningFires = new HashMap<>();

    private Entity highlighter;
    private Vector3f currentPointedCell;

    private int size;
    private int textureOffset;
    private Vector3f ownPosition;
    private Vector3f opponentPosition;
    private Vector3f rotation;
    private float scale;

    /**
     * Create the two grids, the game is played on.
     * @param entities - List of entities the two grids should be saved to.
     * @param size - Count of rows/columns one grid should have.
     * @param loader - Loader needed to load models.
     */
    public PlayingField(List<Entity> entities, int size, Loader loader) {
        initializeGrids(loader, size);

        this.ball = loader.loadEntityfromOBJ("cannonball", "cannonball.png", 10, 1);

        initializeFireParticleSystem(loader);
        initializeHighlighter(loader, entities);
    }

    /**
     * Places a ship on the Grid.
     * @param entities - List of entities the ship should be saved to.
     * @param ship - {@link ShipManager} that handles placing a ship.
     * @param field - On which field the ship should be placed (OWNFIELD = 0 or OPPONENTFIELD = 1).
     * @param index - The index of the cell the back of the ship should be placed on. X and y of index are both integers.
     * @param shipSize - Size of the ship (between 2 and 5).
     * @param rotation - Direction the ship should be facing (one of the constant values in {@link ShipManager} for the directions)
     */
    public void placeShip(List<Entity> entities, ShipManager ship, int field, Vector2f index, int shipSize, int rotation) {
        Vector3f position = calculateShipPosition(field, index, shipSize, rotation);
        Vector3f degrees = new Vector3f(0, calculateShipRotation(rotation), 0);

        ship.placeShip(entities, shipSize, position, degrees, 1f);
    }

    /**
     * Create a flying cannonball.
     *
     * @param cannonballs - list of all cannonball entities that should be rendered on screen
     * @param origin      - the playing field from which the cannonball originates
     * @param destination - the destination cell on the other field
     */
    public void shoot(List<Entity> cannonballs, int origin, Vector2f destination) {
        //TODO shoot without animation
        //calculate index relative to center of field
        if (cannonballFlying) //TODO test this when clicking on field together with if its your turn
            return;

        if (/*call logic to test whether field has been shot already or not
            better: only call this function from logic if shooting is allowed*/false)
            return;

        Vector2f originIndex = origin == OWNFIELD ? new Vector2f(ownPosition.x, ownPosition.z) : new Vector2f(opponentPosition.x, opponentPosition.z);

        //calculate coordinate position of destination
        Vector2f  currentDestination = convertIndextoCoords(destination, origin == OWNFIELD ? OPPONENTFIELD : OWNFIELD);

        //create cannonball flying from origin to destination
        this.cannonball = new Cannonball(ball, currentDestination, originIndex, cannonballs.size());
        cannonballFlying = true;

        //add cannonball to rendered entities
        cannonballs.add(ball);
    }

    /**
     * Move the cannonball.
     * Needs to be called every frame while a cannonball is flying.
     *
     * @param cannonballs - List containing the cannonball
     */
    public void moveCannonball(List<Entity> cannonballs) {
        //TODO make list an entity because only one cannonball is there at a time
        if (!cannonballFlying)
            return;
        if (cannonball.update()) {
            cannonballs.remove(cannonball.getEntityIndex());
            cannonballHit();
        }
    }

    /**
     * Renders all fires, that are currently burning on ships.
     */
    public void renderFires() {
        for (int i : burningFires.keySet()) {
            for (Vector3f pos : burningFires.get(i)) {
                fire.generateParticles(pos);
            }
        }
    }

    /**
     * Highlights the cell at the intersection Point between the given Vector and the grid.
     * Used with mouse vector to highlight the cell the mouse is pointing at.
     * @param intersectionPoint - Vector that should intersect with grid to determine pointed cell.
     */
    public void highligtCell(Vector3f intersectionPoint) {
        Vector3f pointedCell = calculatePointedCell(intersectionPoint);
        if (pointedCell == null) {
            removeHighlighter();
            return;
        }
        setHighlighter(new Vector2f(pointedCell.x, pointedCell.y), (int) pointedCell.z);
    }

    /**
     * Calculates the cell on the grid that is intersected by the given vector.
     * @param intersectionPoint - vector that should intersect with grid.
     * @return - {@link Vector3f} with x = column index, y = row index and z = field that intersection is
     *             on (OWNFIELD or OPPONENTFIELD).
     */
    public Vector3f calculatePointedCell(Vector3f intersectionPoint) {
        return currentPointedCell = convertCoordsToIndex(intersectionPoint);
    }

    /**
     *
     * @return Entity of the players grid.
     */
    public Entity getOwn() {
        return own;
    }

    /**
     *
     * @return Emtity of the opponents grid.
     */
    public Entity getOpponent() {
        return opponent;
    }

    /**
     *
     * @return - The last cell that was calculated using the calculatePointedCell Method.
     */
    public Vector3f getCurrentPointedCell() {
        return currentPointedCell;
    }

    /**
     * Converts the rotation of a ship from the direction to degrees.
     * @param rotation - Direction the ship should be facing.
     * @return - Degrees the entity of the ship needs to be rotated upon the y axis, so it faces the given direction.
     */
    protected int calculateShipRotation(int rotation) {
        int degrees = 0;
        switch (rotation) {
            case ShipManager.NORTH:
                degrees = 0;
                break;
            case ShipManager.SOUTH:
                degrees = 180;
                break;
            case ShipManager.EAST:
                degrees = 270;
                break;
            case ShipManager.WEST:
                degrees = 90;
                break;
        }
        return degrees;
    }

    /**
     * Calculates the Coordinates the ship entity needs to have, to be placed on a given cell.
     * @param field - The field the ship should be placed on.
     * @param index - The index of the cell on the field the stern of the ship is on.
     * @param shipSize - The size of the ship (2-5)
     * @param rotation - The direction the ship should be facing (one of the constant values in {@link ShipManager} for the directions)
     * @return - The OpenGl Coordinates the entity of the ship needs to be placed at.
     */
    protected Vector3f calculateShipPosition(int field, Vector2f index, int shipSize, int rotation) {
        Vector3f position = new Vector3f();
        if (rotation == ShipManager.NORTH || rotation == ShipManager.SOUTH) {
            //move index so it's at center of ship instead of the cell the back part of the ship is on
            //upwards if ship is facing north, downwards if ship is facing south
            index.y += rotation == ShipManager.NORTH ? -(shipSize - 1) / 2f : (shipSize - 1) / 2f;
            Vector2f coords = convertIndextoCoords(index, field);

            position.x = coords.x;
            position.y -= 3;
            position.z = coords.y;
        }
        if (rotation == ShipManager.EAST || rotation == ShipManager.WEST) {
            //move index so it's at center of ship instead of back
            //right if ship is facing east, left is ship is facing west
            index.x += rotation == ShipManager.EAST ? (shipSize - 1) / 2f : -(shipSize - 1) / 2f;
            Vector2f coords = convertIndextoCoords(index, field);

            position.x = coords.x - 0.5f;
            position.y -= 3;
            position.z = coords.y - 0.5f;
        }
        return position;
    }

    /**
     * Places a fire or makes a water splash at the location where the cannonball has hit depending on
     * whether there was a ship or not. Disables flying cannonball.
     */
    private void cannonballHit() {
        //TODO integration with logic
        if (/*call logic to test if water or ship has been hit*/ true) {
            //get index of hit ship from logic
            int shipIndex = 0;
            if (burningFires.containsKey(shipIndex)) { //TODO test if ship has been destroyed (size of Vector List == size of ship)
                burningFires.get(shipIndex).add(new Vector3f(cannonball.getDestination().x, 0, cannonball.getDestination().y));
            } else {
                burningFires.put(shipIndex, new ArrayList<Vector3f>());
                burningFires.get(shipIndex).add(new Vector3f(cannonball.getDestination().x, 0, cannonball.getDestination().y));
            }
            cannonballFlying = false;
        } else //TODO water splash
            return;
    }

    /**
     * Convert the index of a cell to world coordinates of the center of that cell.
     * @param index - The index of the cell.
     * @param field - The field the cell is on.
     * @return - The world coordinates of the center of the cell at the index.
     */
    private Vector2f convertIndextoCoords(Vector2f index, int field) {
        float offset = size % 2 == 0 ? 0 : 0.5f;
        index.x -= size / 2f;
        index.y -= size / 2f;
        Entity usedField = field == OWNFIELD ? own : opponent;
        index.x = usedField.getPosition().x + (index.x + offset) * usedField.getScale() / (size + 1);
        index.y = usedField.getPosition().z + (index.y + offset) * usedField.getScale() / (size + 1);
        return index;
    }

    /**
     * Finds out which cell is at the given coordinates.
     * @param coords - The coordinates at which it should be calculated what cell is at those coordinates. The
     *               y-value of the coordinates does not matter.
     * @return - The index of the cell that is at the given coordinates, or {@code null} if there is no cell at the coordinates.
     */
    private Vector3f convertCoordsToIndex(Vector3f coords) {
        Vector3f result = new Vector3f();
        int field;

        if (coords.x > ownPosition.x - own.getScale() / 2 && coords.x < ownPosition.x + own.getScale() / 2 && coords.z > ownPosition.z - own.getScale() / 2 && coords.z < ownPosition.z + own.getScale() / 2) {
            field = OWNFIELD;
            result.x = coords.x - ownPosition.x + own.getScale() / 2;
            result.y = coords.z - ownPosition.z + own.getScale() / 2;
        } else if (coords.x > opponentPosition.x - opponent.getScale() / 2 && coords.x < opponentPosition.x + opponent.getScale() / 2 && coords.z > opponentPosition.z - opponent.getScale() / 2 && coords.z < opponentPosition.z + opponent.getScale() / 2) {
            field = OPPONENTFIELD;
            result.x = coords.x - opponentPosition.x + opponent.getScale() / 2;
            result.y = coords.z - opponentPosition.z + opponent.getScale() / 2;
        } else
            return null;

        int indexX = (int) (result.x / (scale / (size + 1)));
        int indexY = (int) (result.y / (scale / (size + 1)));

        result.x = indexX;
        result.y = indexY;
        result.z = field;

        return result;
    }

    /**
     * Removes the highlighter by moving it under the terrain.
     * Used when the mouse isn't pointing at a cell.
     */
    private void removeHighlighter() {
        highlighter.getPosition().y = -1000;
    }

    /**
     * Highlight the cell at the index, by setting the highlighter to it's position.
     * @param index - index of the cell that should be highlighted.
     * @param field - the field the cell that should be highlighted is on.
     */
    private void setHighlighter(Vector2f index, int field) {
        Vector2f coords = convertIndextoCoords(index, field);
        Vector3f position = new Vector3f(coords.x, -2.51f, coords.y);
        highlighter.setPosition(position);
    }

    /**
     * Initialize the entities for the grids, the game is played on.
     * One for the player and one for the opponent.
     * @param loader - Loader to load models.
     * @param size - Count of rows/columns one grid should have.
     */
    private void initializeGrids(Loader loader, int size) {
        this.size = size;
        this.rotation = new Vector3f();
        this.scale = 300f;
        this.texture = new ModelTexture(loader.loadTexture(playingfieldTexturePath));
        this.ownPosition = new Vector3f(350, -2.5f, -450);
        this.opponentPosition = new Vector3f(650, -2.5f, -450);
        this.textureOffset = (size + 1) / MAXSIZE;
        this.own = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), texture), 0, ownPosition, new Vector3f(), scale);
        own.setScale(300);
        own.getRotation().x -= 90;

        this.opponent = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), texture), 0, opponentPosition, new Vector3f(), scale);
        opponent.setScale(300);
        opponent.getRotation().x -= 90;
    }

    /**
     * Initialize the Entity that is used to highlight cells.
     * @param loader - Loader to load models.
     * @param entities - List of entities, the highlighter entity should be saved to.
     */
    private void initializeHighlighter(Loader loader, List<Entity> entities) {
        ModelTexture highlightTex = new ModelTexture(loader.loadTexture(highlightTexturePath));
        highlighter = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), highlightTex), 0, new Vector3f(), new Vector3f(), scale / (size + 1));
        highlighter.getRotation().x -= 90;
        entities.add(highlighter);
    }

    /**
     * Initialize the particle system used to create the fires after a ship has been hit.
     * @param loader - Loader to load textures.
     */
    private void initializeFireParticleSystem(Loader loader) {
        ParticleTexture fireTex = new ParticleTexture(loader.loadTexture("particles/fire.png"), 8, true);
        this.fire = new ParticleSystemComplex(fireTex, 20, 3.5f, -0.05f, 2f, 17);
        fire.setLifeError(0.3f);
        fire.setScaleError(0.3f);
        fire.setSpeedError(0.15f);
        fire.randomizeRotation();
        fire.setDirection(new Vector3f(0.1f, 1, 0.1f), -0.15f);
    }
}
