package com.battleships.gui.water;

/**
 * A single WaterTile in the world. Needs to be passed to a {@link WaterRenderer} to show on screen.
 *
 * @author Tim Staudenmaier
 */
public class WaterTile {

    /**
     * Size of one tile in world coordinates.
     */
    public static final float TILE_SIZE = 400;

    /**
     * Height at which this tile is placed.
     */
    private float height;
    /**
     * x and z coordinates of the center of this tile.
     */
    private float x, z;

    /**
     * Create a new WaterTile.
     *
     * @param centerX Center xCoord of the water tile (world coordinates)
     * @param centerZ Center zCoord of the water tile (world coordinates)
     * @param height  Height of the water tile (world coordinates)
     */
    public WaterTile(float centerX, float centerZ, float height) {
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
    }

    /**
     * @return Height of the water tile (world coordinates)
     */
    public float getHeight() {
        return height;
    }

    /**
     * @return Center xCoord of the water tile (world coordinates)
     */
    public float getX() {
        return x;
    }

    /**
     * @return Center zCoord of the water tile (world coordinates)
     */
    public float getZ() {
        return z;
    }
}
