package com.battleships.gui.fontMeshCreator;

/**
 * One character of a font, containing all the data needed to write this letter to the screen.
 *
 * @author Tim Staudenmaier
 */
public class Character {

    /**
     * ASCII Code
     */
    private int id;
    /**
     * x texCoord for the top left corner of character in the texture atlas
     */
    private double xTextureCoord;
    /**
     * y texCoord for the top left corner of character in the texture atlas
     */
    private double yTextureCoord;
    /**
     * x texCoord for the bottom right corner of character in the texture atlas
     */
    private double xMaxTextureCoord;
    /**
     * y texCoord for the bottom right corner of character in the texture atlas
     */
    private double yMaxTextureCoord;
    /**
     * x distance from the cursor to the left side of the character's quad
     */
    private double xOffset;
    /**
     * y distance from the cursor to the top edge of the character's quad
     */
    private double yOffset;
    /**
     * width of the character's quad in screen space
     */
    private double sizeX;
    /**
     * height of the character's quad in screen space
     */
    private double sizeY;
    /**
     * how far in pixels the cursor needs to advance after adding this character
     */
    private double xAdvance;

    /**
     * @param id            ASCII value of the character
     * @param xTextureCoord x texCoord for the top left corner of character in the texture atlas
     * @param yTextureCoord y texCoord for the top left corner of character in the texture atlas
     * @param xTexSize      width of character in the texture atlas
     * @param yTexSize      height of character in the texture atlas
     * @param xOffset       x distance from the cursor to the left side of the character's quad
     * @param yOffset       y distance from the cursor to the top edge of the character's quad
     * @param sizeX         width of the character's quad in screen space
     * @param sizeY         height of the character's quad in screen space
     * @param xAdvance      how far in pixels the cursor needs to advance after adding this character
     */
    protected Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize,
                        double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
        this.id = id;
        this.xTextureCoord = xTextureCoord;
        this.yTextureCoord = yTextureCoord;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xMaxTextureCoord = xTexSize + xTextureCoord;
        this.yMaxTextureCoord = yTexSize + yTextureCoord;
        this.xAdvance = xAdvance;
    }

    /**
     * @return ASCII value of the character
     */
    protected int getId() {
        return id;
    }

    /**
     * @return x texCoord for the top left corner of character in the texture atlas.
     */
    protected double getxTextureCoord() {
        return xTextureCoord;
    }

    /**
     * @return y texCoord for the top left corner of character in the texture atlas.
     */
    protected double getyTextureCoord() {
        return yTextureCoord;
    }

    /**
     * @return x texCoord for the bootom right corner of character in the texture atlas.
     */
    protected double getXMaxTextureCoord() {
        return xMaxTextureCoord;
    }

    /**
     * @return y texCoord for the bootom right corner of character in the texture atlas.
     */
    protected double getYMaxTextureCoord() {
        return yMaxTextureCoord;
    }

    /**
     * @return x distance from the cursor to the left side of the quad the character is rendered on.
     */
    protected double getxOffset() {
        return xOffset;
    }

    /**
     * @return y distance from the cursor to the left side of the quad the character is rendered on.
     */
    protected double getyOffset() {
        return yOffset;
    }

    /**
     * @return width of the character's quad in screen space.
     */
    protected double getSizeX() {
        return sizeX;
    }

    /**
     * @return height of the character's quad in screen space.
     */
    protected double getSizeY() {
        return sizeY;
    }

    /**
     * @return how far in pixels the cursor needs to advance after adding this character.
     */
    protected double getxAdvance() {
        return xAdvance;
    }
}
