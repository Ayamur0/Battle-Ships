package com.battleships.gui.gameAssets.grids;

import com.battleships.gui.audio.Source;
import com.battleships.gui.entities.Entity;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.renderingEngine.MasterRenderer;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class handling all actions related to the grids.
 *
 * @author Tim Staudenmaier
 */
public class GridManager {

    /**
     * Constant indicating the grid of the player.
     */
    public static final int OWNFIELD = 0;
    /**
     * Constant indicating the grid of the opponent.
     */
    public static final int OPPONENTFIELD = 1;
    /**
     * Constant for the max size of a grid (including one row/column for labels).
     */
    private static final int MAXSIZE = 31;
    /**
     * Height of the grids in the world.
     */
    private static final float GRIDHEIGHT = -2.5f;
    /**
     * Position of the grid for the player in the world.
     */
    private static final Vector3f ownPosition = new Vector3f(350, GRIDHEIGHT, -450);
    /**
     * Position of the grid for the opponent in the world.
     */
    private static final Vector3f opponentPosition = new Vector3f(350, GRIDHEIGHT, -450);
    /**
     * Scale of one grid, gets smaller if the grid size is smaller.
     */
    private float scale = 300;

    /**
     * Grids in the world for the two players.
     */
    private GuiGrid ownGrid, opponentGrid;
    /**
     * Size of one grid (excluding row/column for labels).
     */
    private int size;
    /**
     * Highlighter indicating which cell is currently hovered by the mouse.
     */
    private Highlighter highlighter;
    /**
     * Cannonball needed for shooting.
     */
    private Cannonball cannonball;
    /**
     * Playable sounds during the game produced by the cannon and the impact of the cannonball.
     */
    private CannonSounds cannonSound;
    /**
     * Effect that gets played if a ship from the player got hit.
     */
    private Fire fire;
    /**
     * List containing all positions for fire effects grouped by the ships on which the fires are burning.
     */
    private Map<Entity, List<Vector3f>> burningFires = new HashMap<>();
    /**
     * List containing all sources for fire sounds grouped by the ships on which the fires are burning.
     */
    private Map<Entity, List<Source>> burningFireSounds = new HashMap<>();
    /**
     * {@code true} if the animation of the cannonball is shown.
     */
    private boolean animation = true;
    /**
     * {@code true} if ships are currently being placed.
     */
    private boolean shipPlacingPhase;

    /**
     * ShipManager that handles placing ships on the grids.
     */
    private ShipManager shipManager;
    /**
     * List containing the entities of all placed ships. Needed for rendering.
     */
    private List<Entity> ships = new ArrayList<>();
    /**
     * List containing all markers, needed for rendering.
     */
    private List<Entity> markers = new ArrayList<>();

    /**
     * {@code true} if these grids are only used as background for the main menu.
     */
    private static boolean isBackground;

    /**
     * Create a new GridManager.
     * Creates the two grids the game is played on, as well as all other entities needed for the game.
     * @param loader Loader to load models.
     * @param size Size on grid should have.
     */
    public GridManager(Loader loader, int size) {
        initializeGrids(loader, size);
        highlighter = new Highlighter(loader, scale / (size + 1), this);
        cannonball = new Cannonball(loader, this);
        fire = new Fire(loader);
        shipManager = new ShipManager(loader, this);
        cannonSound = new CannonSounds();
        GridMaths.setGridManager(this);
        Marker.createModels(loader);
    }

    /**
     * Adds all entities related to this GridManager to the entity list in the renderer, so they get
     * rendered to the scene. Also renders all fires by creating particles emitted by the particle systems.
     * @param renderer Renderer that the entities should be added to, this renderer needs to render the scene later.
     */
    public void render(MasterRenderer renderer){
        if(!isBackground) {
            highlighter.highligtCell(GameManager.getPicker().getCurrentIntersectionPoint());
        }
        renderer.processEntity(cannonball);
        renderer.processEntityList(ships);
        renderer.processEntity(ownGrid);
        renderer.processEntity(opponentGrid);
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
     * @param index Index of the cell that was clicked.
     * @param field Field the cell that was clicked is on.
     */
    public void cellClicked(Vector2i index, int field){
        //TODO move to logic
        if(shipPlacingPhase){
            shipManager.placeCursorShip();
        }
        else
            shoot(field == OWNFIELD ? OPPONENTFIELD : OWNFIELD, index);
    }

    /**
     * Places a ship on the Grid of the player.
     * @param index The index of the cell the back part of the ship should be placed on.
     * @param shipSize Size of the ship (between 2 and 5).
     * @param rotation Direction the ship should be facing (one of the constant values in {@link ShipManager} for the directions)
     */
    public void placeShip(Vector2i index, int shipSize, int rotation) {
        Vector3f position = GridMaths.calculateShipPosition(ownGrid, index, shipSize, rotation);
        Vector3f degrees = new Vector3f(0, GridMaths.calculateShipRotation(rotation), 0);

        shipManager.placeShip(ships, shipSize, position, degrees, 1f);
    }

    /**
     * Create a flying cannonball that shoots the specified cell.
     * @param origin the playing field from which the cannonball originates
     * @param destinationCell the destination cell on the other field
     */
    public void shoot(int origin, Vector2i destinationCell) {
        if(cannonball.isFlying())
            return;

        //TODO remove sound if no animation?
        if(origin == OWNFIELD)
            cannonSound.playSound(ownGrid.getPosition(), CannonSounds.CANNONSOUND);
        else
            cannonSound.playSound(opponentGrid.getPosition(), CannonSounds.CANNONSOUND);

        //calculate index relative to center of field
        //TODO test for turn

        if (/*call logic to test whether field has been shot already or not
            better: only call this function from logic if shooting is allowed*/false)
            return;

        Vector2f originIndex = origin == OWNFIELD ? new Vector2f(ownGrid.getPosition().x, ownGrid.getPosition().z) : new Vector2f(opponentGrid.getPosition().x, opponentGrid.getPosition().z);

        //calculate coordinate position of destination
        Vector2f  destinationCoords = GridMaths.convertIndextoCoords(new Vector2f(destinationCell), origin == OWNFIELD ? opponentGrid : ownGrid);

        //create cannonball flying from origin to destination
        cannonball.start(destinationCoords, originIndex, destinationCell, origin == OWNFIELD ? OPPONENTFIELD : OWNFIELD);
    }

    /**
     * Places a fire at the location.
     * @param location Location at which the fire should be placed (world coordinates x,z)
     */
    protected void playFireEffect(Vector2f location){
        Entity ship = ownGrid; //TODO get ship from logic
        Source sound = fire.createFireSound(location);
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
     * Places a marker that indicates that a field has been shot. Is used anytime a cel is shot that doesn't
     * contain a ship owned by the player. In that case the marker would be the fire.
     * @param ship {@code true} if marker should indicate a ship was hit, {@code false} for water.
     * @param index Index at which the marker should be placed.
     * @param gridID GridID of grid on which the marker should be placed (0 for OWNFIELD, 1 for OPPONENTFIELD).
     */
    public void placeMarker(boolean ship, Vector2i index, int gridID){
        GuiGrid grid = getGridByID(gridID);
        if(grid == null)
            return;
        markers.add(new Marker(ship ? 1 : 0, new Vector2f(index.x, index.y), grid));
    }

    /**
     * Initialize the {@link GuiGrid}s of this GridManager.
     * One for the player and one for the opponent.
     * @param loader Loader to load models.
     * @param size Count of rows/columns one grid should have.
     */
    private void initializeGrids(Loader loader, int size) {
        this.size = size;
        scale *= (size + 1) / (float)MAXSIZE;
        opponentPosition.x = 350 + scale + 5;
        this.ownGrid = new GuiGrid(loader, ownPosition, new Vector3f(-90,0,0), scale, (float)MAXSIZE / (size + 1));
        this.opponentGrid = new GuiGrid(loader, opponentPosition, new Vector3f(-90,0,0), scale, (float)MAXSIZE / (size + 1));
    }

    /**
     * Play a sound using the {@link CannonSounds} class.
     * @param pos Position at which the sound should play (world coordinates x,y,z).
     * @param type Type of the sound (one of the constants in {@link CannonSounds}).
     */
    public void playSound(Vector3f pos, int type){
        cannonSound.playSound(pos, type);
    }


    /**
     * @return Height of the grids in the world.
     */
    public static float getGRIDHEIGHT() {
        return GRIDHEIGHT;
    }

    /**
     * Returns the corresponding GuiGrid for the passed constant.
     * @param id Constant for the grid.
     * @return The grid corresponding to that constant. {@code null} if there is no grid for that constant.
     */
    public GuiGrid getGridByID(int id){
        if(id == OWNFIELD)
            return ownGrid;
        if(id == OPPONENTFIELD)
            return opponentGrid;
        return null;
    }

    /**
     * @return GuiGrid of the player.
     */
    public GuiGrid getOwnGrid() {
        return ownGrid;
    }

    /**
     * @return GuiGrid of the opponent.
     */
    public GuiGrid getOpponentGrid() {
        return opponentGrid;
    }

    /**
     * @return The scale of one grid in the world.
     */
    public float getScale() {
        return scale;
    }

    /**
     * @return The amount of rows on one grid (excluding one row for labels).
     */
    public int getSize() {
        return size;
    }

    /**
     * @return {@code true} if the cannonball animation is currently played.
     */
    public boolean isAnimation() {
        return animation;
    }

    /**
     * @return Index of the cell that is currently pointed at.
     * x and y values of the vector are the index of the cell.
     * z value is the constant for the grid on which the pointed cell is.
     */
    public Vector3i getCurrentPointedCell(){
        return highlighter.getCurrentPointedCell();
    }

    /**
     * Set the current game phase.
     * @param shipPlacingPhase {@code true} if the current game phase should be set to place ships, {@code false} if game phase should be set to shooting.
     */
    public void setShipPlacingPhase(boolean shipPlacingPhase) {
        this.shipPlacingPhase = shipPlacingPhase;
    }

    /**
     * @return The ShipManager that handles placing ships.
     */
    public ShipManager getShipManager() {
        return shipManager;
    }

    /**
     * Toggles whether a shooting animation is shown or not.
     */
    public void toggleShootingAnimation(){
        animation = !animation;
    }

    /**
     * @return max size of a grid (including one row/column for labels).
     */
    public static int getMAXSIZE() {
        return MAXSIZE;
    }

    /**
     * Set if these grids are only used as a background.
     * @param isBackground {@code true} if these grids are only used as a background.
     */
    public static void setIsBackground(boolean isBackground) {
        GridManager.isBackground = isBackground;
    }
}
