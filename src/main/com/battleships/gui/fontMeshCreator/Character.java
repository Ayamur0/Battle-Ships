package com.battleships.gui.fontMeshCreator;

public class Character {

    private int id;
    private double xTextureCoord;
    private double yTextureCoord;
    private double xMaxTextureCoord;
    private double yMaxTextureCoord;
    private double xOffset;
    private double yOffset;
    private double sizeX;
    private double sizeY;
    private double xAdvance;

    /**
     * @param id:
     *          - ASCII value of the character
     * @param xTextureCoord:
     *          - x texCoord for the top left corner of character in the texture atlas
     * @param yTextureCoord:
     *          - y texCoord for the top left corner of character in the texture atlas
     * @param xTexSize:
     *          - width of character in the texture atlas
     * @param yTexSize:
     *          - height of character in the texture atlas
     * @param xOffset:
     *          - x distance from the cursor to the left side of the character's quad
     * @param yOffset:
     *          - y distance from the cursor to the top edge of the character's quad
     * @param sizeX:
     *          - width of the character's quad in screen space
     * @param sizeY:
     *          - height of the character's quad in screen space
     * @param xAdvance:
     *          - how far in pixels the cursor needs to advance after adding this character
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

    protected int getId() {
        return id;
    }

    protected double getxTextureCoord() {
        return xTextureCoord;
    }

    protected double getyTextureCoord() {
        return yTextureCoord;
    }

    protected double getXMaxTextureCoord() {
        return xMaxTextureCoord;
    }

    protected double getYMaxTextureCoord() {
        return yMaxTextureCoord;
    }

    protected double getxOffset() {
        return xOffset;
    }

    protected double getyOffset() {
        return yOffset;
    }

    protected double getSizeX() {
        return sizeX;
    }

    protected double getSizeY() {
        return sizeY;
    }

    protected double getxAdvance() {
        return xAdvance;
    }
}
