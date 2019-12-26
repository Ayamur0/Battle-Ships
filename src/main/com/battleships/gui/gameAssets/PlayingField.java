package com.battleships.gui.gameAssets;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.audio.Source;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.testLogic.TestLogic;
import com.battleships.gui.models.ModelTexture;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.particles.ParticleSystemComplex;
import com.battleships.gui.particles.ParticleTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import com.battleships.gui.renderingEngine.OBJLoader;
import com.battleships.gui.window.WindowManager;
import org.apache.commons.math3.linear.*;
import org.joml.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that contains the {@link Entity} for the grids of the player, as well as the highlighted cell and the ships placed on the grids.
 * Contains all the visual functions related to grids, ships and shooting.
 *
 * @author Tim Staudenmaier
 */
public class PlayingField {

    /**
     * Grid related:
     *          OWNFIELD                - Constant indicating the grid of the player.
     *          OPPONENTFIELD           - Constant indicating the grid of the opponent.
     *          MAXSIZE                 - Constant for the max size of a grid (including one row/column for labels).
     *          own                     - Entity of the players grid
     *          opponent                - Entity of the opponents grid.
     *          size                    - Size of one grid (excluding row/column for labels).
     *          scale                   - Scale of one grid, gets smaller if the grid size is smaller.
     *          playingFieldTexturePath - Path of the texture for the grids.
     *          VERTICES, INDICES, TEXTURECOORDS, NORMALS
     *                                  - Data for creating rectangle vao on which the grid texture can be rendered.
     */
    public static final int OWNFIELD = 0;
    public static final int OPPONENTFIELD = 1;
    private static final int MAXSIZE = 31;
    private Entity own;
    private Entity opponent;
    private int size;
    private static float scale = 300;
    private static final String playingfieldTexturePath = "PlayingField.png";
    private static final float[] VERTICES = {-0.5f, 0.5f, 0, -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, 0.5f, 0.5f, 0};
    private static final int[] INDICES = {0, 1, 3, 3, 1, 2};
    private static final float[] TEXTURECOORDS = {0, 0, 0, 1, 1, 1, 1, 0};
    private static final float[] NORMALS = {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0};



    /**
     * Ship related:
     *          shipManager         - ShipManger that handles placing ships.
     *          ships               - Lists containing all ships currently on the grids. Needed for easy rendering.
     */
    private ShipManager shipManager;
    private List<Entity> ships;

    //TODO remove
    private boolean shipPlacingPhase;

    /**
     * Cannonball related:
     *          ball                - Entity for creating cannonballs.
     *          cannonballFlying    - {@code true} if there is already a cannonball flying
     *          cannonball          - The cannonball currently flying {@code null} if no cannonball is flying.
     *          fire                - ParticleSystem to indicate ships that were hit by having a fire burning on them.
     *          burningFires        - HashMap containing all ParticleSystems for each ship.
     *          burningFireSounds   - HashMap containing all the soundSources for each ships fires.
     *          animation           - if this is {@code true} then the cannonball animation is shown, else a hitMarker is immediately played without animation.
     *          cannon              - Source that can play the sound of a firing cannon.
     *          cannonSound         - SoundBuffer of the cannonSound
     *          waterSplash         - Source for playing either waterSplashSound or hitSound on cannonball impact
     *          waterSplashSound    - SoundBuffer for a waterSplash
     *          hitSound            - SoundBuffer for a sound of a cannonball hitting a ship.
     *          fireSound           - SoundBuffer for a soun of a burning fire.
     */
    private Entity ball;
    private boolean cannonballFlying;
    private Cannonball cannonball;
    private ParticleSystemComplex fire;
    private Map<Entity, List<Vector3f>> burningFires = new HashMap<>();
    private Map<Entity, List<Source>> burningFireSounds = new HashMap<>();
    private boolean animation = true;
    private Source cannon = new Source(1, 40, 500);
    private int cannonSound = AudioMaster.loadSound("Cannon");
    private Source waterSplash = new Source(1, 40, 500);
    private int waterSplashSound = AudioMaster.loadSound("WaterSplash2");
    private int hitSound = AudioMaster.loadSound("HitSoundShort");
    private int fireSound = AudioMaster.loadSound("fire");

    /**
     * Marker related:
     *          highlightTexturePath- Texture of the highlighter that indicates which cell the mouse cursor is hovering over.
     *          buoyModelOBJ        - path for the model of the markers.
     *          markers             - List containing all Entities of the markers, needed for rendering.
     *          highlighter         - Entity of the highlighter showing which cell currently is being hovered by the mouse cursor.
     *          currentPointedCell  - Vector containing the index of the currently hovered cell as x and y values and the constant indicating on which grid the cell is as z value.
     *          buoyModelWhite      - The texturedModel for water markers.
     *          buoyModelRed        - The texturedModel for ship markers (used on opponents grid where ships aren't visible).
     */
    //TODO hightlighter Tex
    private static final String highlightTexturePath = "cannonball.png";
    private static final String buoyModelOBJ = "buoy";
    private List<Entity> markers;
    private Entity highlighter;
    private Vector3i currentPointedCell;
    private TexturedModel buoyModelWhite;
    private TexturedModel buoyModelRed;

    /**
     * Create the two grids, the game is played on.
     * @param size - Count of rows/columns one grid should have.
     * @param loader - Loader needed to load models.
     */
    public PlayingField(int size, Loader loader) {
        this.ships = new ArrayList<>();
        this.markers = new ArrayList<>();
        initializeGrids(loader, size);

        this.ball = loader.loadEntityfromOBJ("cannonball", "cannonball.png", 10, 1);
        shipPlacingPhase = true;

        initializeFireParticleSystem(loader);
        initializeMarkers(loader);

        this.shipManager = new ShipManager(loader, this);
    }

    /**
     * Adds all entities related to this playingfield to the entity list in the renderer, so they get
     * rendered to the scene. Also renders all fires by creating particles emitted by the particle systems.
     * @param renderer - Renderer that the entities should be added to, this renderer needs to render the scene later.
     */
    public void render(MasterRenderer renderer){
        moveCannonball();
        highligtCell(GameManager.getPicker().getCurrentIntersectionPoint());
        if(cannonballFlying)
            cannonball.render(renderer);
        renderer.processEntityList(ships);
        renderer.processEntity(own);
        renderer.processEntity(opponent);
        renderer.processEntity(highlighter);
        renderer.processEntityList(markers);
        shipManager.renderCursorShip(renderer);
        for (Entity e : burningFires.keySet()) {
            for (Vector3f pos : burningFires.get(e)) {
                fire.generateParticles(pos);
            }
        }
    }

    /**
     * Function that determines what should happen when a cell is clicked depending on game phase.
     * Call when a cell is clicked.
     * @param index - Index of the cell that was clicked.
     * @param field - Field the cell that was clicked is on.
     */
    public void cellClicked(Vector2i index, int field){
        //TODO move to logic
        if(cannonballFlying)
            return;
        if(shipPlacingPhase){
            shipManager.placeCursorShip();
            return;
        }
        else
            shoot(field == OWNFIELD ? OPPONENTFIELD : OWNFIELD, index);
    }

    /**
     * Places a ship on the Grid of the player.
     * @param index - The index of the cell the back of the ship should be placed on. X and y of index are both integers.
     * @param shipSize - Size of the ship (between 2 and 5).
     * @param rotation - Direction the ship should be facing (one of the constant values in {@link ShipManager} for the directions)
     */
    public void placeShip(Vector2i index, int shipSize, int rotation) {
        Vector3f position = calculateShipPosition(OWNFIELD, index, shipSize, rotation);
        Vector3f degrees = new Vector3f(0, calculateShipRotation(rotation), 0);

        shipManager.placeShip(ships, shipSize, position, degrees, 1f);
    }

    /**
     * Places a marker that indicates that a field has been shot. Is used anytime a cel is shot that doesn't
     * contain a ship owned by the player. In that case the marker would be the fire.
     * @param ship - {@code true} if marker should indicate a ship was hit, {@code false} for water.
     * @param index - Index at which the marker should be placed.
     * @param field - Field on which the marker should be placed (0 for OWNFIELD, 1 for OPPONENTFIELD).
     */
    public void placeMarker(boolean ship, Vector2i index, int field){
        Vector2f coords = convertIndextoCoords(new Vector2f(index), field);
        Entity buoy;
        if(ship)
            buoy = new Entity(buoyModelRed, new Vector3f(coords.x, -3, coords.y), new Vector3f(), 1);
        else
            buoy = new Entity(buoyModelWhite, new Vector3f(coords.x, -3, coords.y), new Vector3f(), 1);
        markers.add(buoy);
    }

    /**
     * Create a flying cannonball that shoots the specified cell.
     * @param origin      - the playing field from which the cannonball originates
     * @param destinationCell - the destination cell on the other field
     */
    public void shoot(int origin, Vector2i destinationCell) {
        if(!animation){
            playHitEffect(convertIndextoCoords(new Vector2f(destinationCell), origin == OWNFIELD ? OPPONENTFIELD : OWNFIELD));
            return;
        }

        if(origin == OWNFIELD)
            cannon.setPosition(own.getPosition().x, own.getPosition().y, own.getPosition().z);
        else
            cannon.setPosition(opponent.getPosition().x, opponent.getPosition().y, opponent.getPosition().z);
        cannon.play(cannonSound);
        //calculate index relative to center of field
        if (cannonballFlying) //TODO test this when clicking on field together with if its your turn
            return;

        if (/*call logic to test whether field has been shot already or not
            better: only call this function from logic if shooting is allowed*/false)
            return;

        Vector2f originIndex = origin == OWNFIELD ? new Vector2f(own.getPosition().x, own.getPosition().z) : new Vector2f(opponent.getPosition().x, opponent.getPosition().z);

        //calculate coordinate position of destination
        Vector2f  destinationCoords = convertIndextoCoords(new Vector2f(destinationCell), origin == OWNFIELD ? OPPONENTFIELD : OWNFIELD);

        //create cannonball flying from origin to destination
        this.cannonball = new Cannonball(ball, destinationCoords, originIndex, destinationCell, origin == OWNFIELD ? OPPONENTFIELD : OWNFIELD);
        cannonballFlying = true;

    }

    /**
     * Move the cannonball.
     * Needs to be called every frame while a cannonball is flying.
     *
     */
    public void moveCannonball() {
        if (!cannonballFlying)
            return;
        if (cannonball.update()) {
            cannonballHit();
        }
    }

    /**
     * Highlights the cell at the intersection Point between the given Vector and the grid.
     * Used with mouse vector to highlight the cell the mouse is pointing at.
     * @param intersectionPoint - Vector that should intersect with grid to determine pointed cell.
     */
    public void highligtCell(Vector3f intersectionPoint) {
        Vector3i pointedCell = calculatePointedCell(intersectionPoint);
        if (pointedCell == null) {
            removeHighlighter();
            return;
        }
        setHighlighter(new Vector2i(pointedCell.x, pointedCell.y), (int) pointedCell.z);
    }

    /**
     * Calculates the cell on the grid that is intersected by the given vector.
     * @param intersectionPoint - vector that should intersect with grid.
     * @return - {@link Vector3f} with x = column index, y = row index and z = field that intersection is
     *             on (OWNFIELD or OPPONENTFIELD).
     */
    public Vector3i calculatePointedCell(Vector3f intersectionPoint) {
        return currentPointedCell = convertCoordsToIndex(intersectionPoint);
    }

    /**
     * Set phase to be shipPlacing phase, so clicking on cells places ships or
     * firing phase, so clicking on cell shoots a cannonball.
     */
    public void setShipPlacingPhase(boolean shipPlacingPhase) {
        this.shipPlacingPhase = shipPlacingPhase;
    }

    /**
     * Toggles whether a shooting animation is shown or not.
     */
    public void toggleShootingAnimation(){
        animation = !animation;
    }

    /**
     *
     * @return - The last cell that was calculated using the calculatePointedCell Method.
     */
    public Vector3i getCurrentPointedCell() {
        return currentPointedCell;
    }

    /**
     *
     * @return - The {@link ShipManager} of this PlayingField.
     */
    public ShipManager getShipManager() {
        return shipManager;
    }

    /**
     *
     * @return - The amount of columns/rows on one playingField.
     */
    public int getSize() {
        return size;
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
     * @param indexInt - The index of the cell on the field the stern of the ship is on.
     * @param shipSize - The size of the ship (2-5)
     * @param rotation - The direction the ship should be facing (one of the constant values in {@link ShipManager} for the directions)
     * @return - The OpenGl Coordinates the entity of the ship needs to be placed at.
     */
    protected Vector3f calculateShipPosition(int field, Vector2i indexInt, int shipSize, int rotation) {
        Vector3f position = new Vector3f();
        Vector2f indexFloat = new Vector2f(indexInt);
        if (rotation == ShipManager.NORTH || rotation == ShipManager.SOUTH) {
            //move index so it's at center of ship instead of the cell the back part of the ship is on
            //upwards if ship is facing north, downwards if ship is facing south
            indexFloat.y += rotation == ShipManager.NORTH ? -(shipSize - 1) / 2f : (shipSize - 1) / 2f;
            Vector2f coords = convertIndextoCoords(indexFloat, field);

            position.x = coords.x;
            position.y -= 3;
            position.z = coords.y;
        }
        if (rotation == ShipManager.EAST || rotation == ShipManager.WEST) {
            //move index so it's at center of ship instead of back
            //right if ship is facing east, left is ship is facing west
            indexFloat.x += rotation == ShipManager.EAST ? (shipSize - 1) / 2f : -(shipSize - 1) / 2f;
            Vector2f coords = convertIndextoCoords(indexFloat, field);

            position.x = coords.x - 0.5f;
            position.y -= 3;
            position.z = coords.y - 0.5f;
        }
        return position;
    }

    /**
     * Disables and removes flying cannonball. And plays effect at hit location, or marks the hit location.
     */
    private void cannonballHit() {
        //TODO integration with logic
        boolean shipHit = false;
        if(cannonball.getDestinationField() == OWNFIELD) {
            playHitEffect(cannonball.getDestination());
            if(!shipHit) {
                placeMarker(shipHit, cannonball.getDestinationCell(), cannonball.getDestinationField());
                waterSplash.setPosition(cannonball.getDestination().x, -3, cannonball.getDestination().y);
                waterSplash.play(hitSound);
            }
        }
        else if(cannonball.getDestinationField() == OPPONENTFIELD || !shipHit) {
            //TODO play water effect
            waterSplash.setPosition(cannonball.getDestination().x, -3, cannonball.getDestination().y);
            waterSplash.play(waterSplashSound);
            placeMarker(shipHit, cannonball.getDestinationCell(), cannonball.getDestinationField());
        }
        cannonballFlying = false;
        cannonball = null;
    }

    /**
     * Places a fire or makes a water splash at the location where the cannonball has hit depending on
     * whether there was a ship or not.
     */
    private void playHitEffect(Vector2f location){
        Entity ship = own; //TODO get ship from logic
        Source sound = new Source(1, 10, 300);
        sound.setPosition(location.x, 0, location.y);
        sound.setLooping(true);
        sound.play(fireSound);
        if (burningFires.containsKey(ship)) { //TODO test if ship has been destroyed (size of Vector List == size of ship) Logic?
            burningFires.get(ship).add(new Vector3f(location.x, 0, location.y));
            burningFireSounds.get(ship).add(sound);
        } else {
            burningFires.put(ship, new ArrayList<>());
            burningFires.get(ship).add(new Vector3f(location.x, 0, location.y));
            burningFireSounds.put(ship, new ArrayList<>());
            burningFireSounds.get(ship).add(sound);
        }
    }

    /**
     * Convert the index of a cell to world coordinates of the center of that cell.
     * @param i - The index of the cell.
     * @param field - The field the cell is on.
     * @return - The world coordinates of the center of the cell at the index.
     */
    private Vector2f convertIndextoCoords(Vector2f i, int field) {
        float offset = size % 2 == 0 ? 0 : 0.5f;
        Vector2f index = new Vector2f(i.x, i.y);
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
    private Vector3i convertCoordsToIndex(Vector3f coords) {
        Vector3f result = new Vector3f();
        int field;

        if (coords.x > own.getPosition().x - own.getScale() / 2 + own.getScale() / (size + 1) &&
                coords.x < own.getPosition().x + own.getScale() / 2 &&
                coords.z > own.getPosition().z - own.getScale() / 2 + own.getScale() / (size + 1) &&
                coords.z < own.getPosition().z + own.getScale() / 2) {
            field = OWNFIELD;
            result.x = coords.x - own.getPosition().x + own.getScale() / 2;
            result.y = coords.z - own.getPosition().z + own.getScale() / 2;
        } else if (coords.x > opponent.getPosition().x - opponent.getScale() / 2 + own.getScale() / (size + 1) &&
                coords.x < opponent.getPosition().x + opponent.getScale() / 2 &&
                coords.z > opponent.getPosition().z - opponent.getScale() / 2  + own.getScale() / (size + 1) &&
                coords.z < opponent.getPosition().z + opponent.getScale() / 2) {
            field = OPPONENTFIELD;
            result.x = coords.x - opponent.getPosition().x + opponent.getScale() / 2;
            result.y = coords.z - opponent.getPosition().z + opponent.getScale() / 2;
        } else
            return null;

        int indexX = (int) (result.x / (scale / (size + 1)));
        int indexY = (int) (result.y / (scale / (size + 1)));

        return new Vector3i(indexX, indexY, field);
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
    private void setHighlighter(Vector2i index, int field) {
        Vector2f coords = convertIndextoCoords(new Vector2f(index), field);
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
        ModelTexture texture = new ModelTexture(loader.loadTexture(playingfieldTexturePath));
        texture.setNumberOfRows((float)MAXSIZE / (size + 1));
        scale *= (size + 1) / (float)MAXSIZE;
        Vector3f ownPosition = new Vector3f(350, -2.5f, -450);
        Vector3f opponentPosition = new Vector3f(350 + scale + 5, -2.5f, -450);
        this.own = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), texture), 0, ownPosition, new Vector3f(), scale);
        own.getRotation().x -= 90;

        this.opponent = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), texture), 0, opponentPosition, new Vector3f(), scale);
        opponent.getRotation().x -= 90;
    }

    /**
     * Initialize the Entity that is used to highlight cells and the model for the buoyModels marking shot cells.
     * @param loader - Loader to load models.
     */
    private void initializeMarkers(Loader loader) {
        ModelTexture highlightTex = new ModelTexture(loader.loadTexture(highlightTexturePath));
        highlighter = new Entity(new TexturedModel(loader.loadToVAO(VERTICES, TEXTURECOORDS, NORMALS, INDICES), highlightTex), new Vector3f(), new Vector3f(), scale / (size + 1));
        highlighter.getRotation().x -= 90;

        ModelTexture white = new ModelTexture(loader.loadTexture("white.png"));
        ModelTexture red = new ModelTexture(loader.loadTexture("red.png"));
        buoyModelWhite = new TexturedModel(OBJLoader.loadObjModel(buoyModelOBJ), white);
        buoyModelRed = new TexturedModel(OBJLoader.loadObjModel(buoyModelOBJ), red);
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

    /**
     *
     * @return - Entity for the gird of the player.
     */
    public Entity getOwn() {
        return own;
    }

    /**
     *
     * @return - Entity for the grid of the opponent.
     */
    public Entity getOpponent() {
        return opponent;
    }

    /**
     *
     * @return - Maximum Size of one grid (including one row / column for labels)
     */
    public static int getMAXSIZE() {
        return MAXSIZE;
    }

    /**
     *
     * @return - The scale (diameter) of one playing field in world coordinates.
     */
    public static float getScale() {
        return scale;
    }
}
