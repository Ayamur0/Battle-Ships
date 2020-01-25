package com.battleships.gui.terrains;

import com.battleships.gui.models.RawModel;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.toolbox.Maths;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A terrain that can be placed in the world.
 * Needs to be passed to a {@link com.battleships.gui.renderingEngine.TerrainRenderer} to be visible on screen.
 *
 * @author Tim Staudenmaier
 */
public class Terrain {

    /**
     * Size of one terrain in world coordinates.
     */
    private static final float SIZE = 1600;
    /**
     * Maximum height a terrain can reach (world coordinates).
     */
    private static final float MAX_HEIGHT = 40;
    /**
     * Maximum color a pixel on the terrain can have (white).
     */
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

    /**
     * X position of the middle of the terrain.
     */
    private float x;
    /**
     * Z position of the middle of the terrain.
     */
    private float z;
    /**
     * Model of the terrain.
     */
    private RawModel model;
    /**
     * TexturePack this terrain uses.
     */
    private TerrainTexturePack texturePack;
    /**
     * BlendMap for mapping the textures onto the terrain.
     */
    private TerrainTexture blendMap;

    /**
     * Height of each pixel of the terrain.
     */
    private float[][] heights;

    /**
     * Create a new terrain.
     * The world is split into different grids on which terrains can be placed.
     * Grid indices increase in the same direction as the x and z values in the world do.
     *
     * @param gridX       Index of the grid on the x axis this terrain should be placed in.
     * @param gridZ       Index of the grid on the z axis this terrain should be placed in.
     * @param loader      Loader to load the model.
     * @param texturePack TexturePack this terrain should use.
     * @param blendMap    BlendMap texture for mapping the textures onto this terrain.
     * @param heightMap   Path to the heightMap image that specifies the heights of this terrain.
     *                    The darker a pixel on this image is, the lower the terrain will be at that place.
     */
    public Terrain(float gridX, float gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
        this.blendMap = blendMap;
        this.texturePack = texturePack;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }

    /**
     * @return x position of this terrains middle in the world.
     */
    public float getX() {
        return x;
    }

    /**
     * @return z position of this terrains middle in the world.
     */
    public float getZ() {
        return z;
    }

    /**
     * @return The model of this terrain.
     */
    public RawModel getModel() {
        return model;
    }

    /**
     * @return The texturePack this terrain uses.
     */
    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    /**
     * @return The BlendMap texture of this terrain.
     */
    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    /**
     * The height of this terrain at specific world coordinates.
     *
     * @param worldX x coordinate
     * @param worldZ z coordinate
     * @return Height of the terrain at the passed coordinates.
     */
    public float getHeightOfTerrain(float worldX, float worldZ) {
        //convert world position to terrain position
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;

        //get coordinates of grid player is in
        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        //stop if grid is outside terrain
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }

        //get coordinates in grid (between 0 and 1)
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;

        //each square (grid) the terrain is made out of, is made of 2 triangles
        //calculate in which triangle player is
        //calculate height of the triangle at the players position using barycentric interpolation
        if (xCoord <= (1 - zCoord)) {
            answer = Maths
                    .baryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = Maths
                    .baryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return answer;
    }

    /**
     * Generate the model for this terrain from it's heightMap.
     *
     * @param loader    Loader to load the model.
     * @param heightMap Path to the image containing the height map of this terrain.
     * @return The model this terrain needs to use to match the height map.
     */
    private RawModel generateTerrain(Loader loader, String heightMap) {

        //load height map
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResource("/com/battleships/gui/res/textures/" + heightMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get vertex count from image size in pixels
        int VERTEX_COUNT = image.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        //total count of vertices
        int count = VERTEX_COUNT * VERTEX_COUNT;

        //arrays for vertices, their normals and their textureCoords as well as the indices
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];

        //double loop to loop through every vertex
        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                //store vertex Coordinates (x,y,z)
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j, i, image);
                //also store height in 2D array for fast access for collision detection
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;

                //store normal vector of vertex in array
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

                //store textureCoords of vertex in array
                textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);

                //continue with next vertex
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    /**
     * Calculate in which direction the normal vector needs to face at a specific point on the terrain.
     *
     * @param x     x coordinate of the pixel on the image of the point he normal should be calculated for.
     * @param y     y coordinate of the pixel on the image of the point he normal should be calculated for.
     * @param image Image containing the height map of this terrain.
     * @return The normal vector the terrain needs to have at the passed pixel.
     */
    private Vector3f calculateNormal(int x, int y, BufferedImage image) {
        //calculate height of 4 Pixels around the pixel the normal is calculated for
        float heightL = getHeight(x - 1, y, image);
        float heightR = getHeight(x + 1, y, image);
        float heightD = getHeight(x, y - 1, image);
        float heightU = getHeight(x, y + 1, image);
        //calculate normal relative to near pixels
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();
        return normal;
    }

    /**
     * Converts a pixel on the height map image into an actual height value depending on how dark the pixel is.
     * The darker the lower the terrain.
     *
     * @param x     x coordinate of the pixel on the image the height should be calculated for.
     * @param y     y coordinate of the pixel on the image the height should be calculated for.
     * @param image image containing the height map of this terrain.
     * @return The height the terrain needs to have at the the passed pixel.
     */
    private float getHeight(int x, int y, BufferedImage image) {
        if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()) {
            return 0;
        }
        float height = image.getRGB(x, y);
        height += MAX_PIXEL_COLOR / 2f;
        height /= MAX_PIXEL_COLOR / 2f;
        height *= MAX_HEIGHT;
        return height;
    }
}
