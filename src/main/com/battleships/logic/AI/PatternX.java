package com.battleships.logic.AI;

import org.joml.Vector2i;

import java.util.Random;

/**
 * Pattern that shoots in a X form on the grid.
 * The X can be started from any point and in any direction.
 *
 * @author Tim Staudenmaier
 */
public class PatternX implements Pattern {

    /**
     * Directions the diagonal lines can start in.
     */
    private static final int DOWNLEFT = 0, UPLEFT = 1, DOWNRIGHT = 2, UPRIGHT = 3;

    /**
     * X value of the last index.
     */
    private int lastX;
    /**
     * Y value of the last index.
     */
    private int lastY;
    /**
     * Size of the grid this pattern is used on.
     */
    private int size;
    /**
     * Direction in the current diagonal line has.
     */
    private int direction;
    /**
     * Amount of diagonal lines completed (after 2 this pattern stops).
     */
    private int rowscomplete;

    /**
     * Creates a new X-Pattern.
     *
     * @param size Size of the grid this pattern is used on.
     */
    public PatternX(int size) {
        this.size = size;
    }

    /**
     * @return The next index for this pattern.
     */
    @Override
    public Vector2i nextIndex() {
        switch (direction) {
            case DOWNRIGHT:
                lastX++;
                lastY++;
                break;
            case DOWNLEFT:
                lastX--;
                lastY++;
                break;
            case UPLEFT:
                lastX--;
                lastY--;
                break;
            case UPRIGHT:
                lastX++;
                lastY--;
                break;
        }
        if (lastX == size + 1 || lastX == 0) {
            rowscomplete++;
            if (rowscomplete == 2)
                return null;
            direction = chooseNextDirection();
            switch (direction) {
                case DOWNRIGHT:
                    lastX = 1;
                    lastY = 1;
                    break;
                case DOWNLEFT:
                    lastX = size;
                    lastY = 1;
                    break;
                case UPLEFT:
                    lastX = size;
                    lastY = size;
                    break;
                case UPRIGHT:
                    lastX = 1;
                    lastY = size;
                    break;
            }
        }
        return new Vector2i(lastX, lastY);
    }

    /**
     * @return A random direction the next diagonal line can start with.
     */
    private int chooseNextDirection() {
        Random r = new Random();
        int next = r.nextInt(1);
        if (direction == DOWNRIGHT || direction == UPLEFT) {
            return next == 0 ? DOWNLEFT : UPRIGHT;
        } else {
            return next == 0 ? DOWNRIGHT : UPLEFT;
        }
    }

    /**
     * @return The first index for this pattern.
     */
    @Override
    public Vector2i firstIndex() {
        Random r = new Random();
        lastX = r.nextInt(4);
        lastX = 1;
        switch (lastX) {
            case 0:
                lastX = 1;
                lastY = 1;
                direction = DOWNRIGHT;
                return new Vector2i(lastX, lastY);
            case 1:
                lastX = size;
                lastY = 1;
                direction = DOWNLEFT;
                return new Vector2i(lastX, lastY);
            case 2:
                lastX = 1;
                lastY = size;
                direction = UPRIGHT;
                return new Vector2i(lastX, lastY);
            case 3:
                lastX = size;
                lastY = size;
                direction = UPLEFT;
                return new Vector2i(lastX, lastY);
            default:
                lastX = 1;
                lastY = 1;
                direction = DOWNRIGHT;
                return new Vector2i(1, 1);
        }
    }
}
