package com.battleships.logic.AI;

import org.joml.Vector2i;

import java.util.Random;

/**
 * Pattern that shoots in a chess pattern.
 * Shoots every second cell on the grid, so it can find every ship.
 *
 * @author Tim Staudenmaier
 */
public class PatternChess implements Pattern {

    /**
     * Colors on a chess board this pattern always shoots at one of the colors.
     */
    private static final int BLACK = 0, WHITE = 1;
    /**
     * Corners in which this pattern can start.
     */
    private static final int DOWNLEFT = 0, UPLEFT = 1, DOWNRIGHT = 2, UPRIGHT = 3;

    /**
     * Color this pattern is shooting at.
     */
    private int color;

    /**
     * Corner in which this pattern started.
     */
    private int start;
    /**
     * Size of the grid this pattern is used on.
     */
    private int size;
    /**
     * Value that needs to be added as offset when the pattern moves into the next row,
     * so it stays on the same color.
     * This offset is used if the last cell with this patterns color was on the second outermost column.
     */
    private int addX1;
    /**
     * Value that needs to be added as offset when the pattern moves into the next row,
     * so it stays on the same color.
     * This offset is used if the last cell with this patterns color was on the outermost column.
     */
    private int addX2;
    /**
     * Last index this grid returned.
     */
    private Vector2i lastIndex;

    /**
     * Creates a new chess pattern.
     *
     * @param size Size of the grid this pattern is used on.
     */
    public PatternChess(int size) {
        this.size = size;
        Random r = new Random();
        start = r.nextInt(4);
        //start = 1;
        color = r.nextInt(2);
        //color = BLACK;
        if (size % 2 == 0) {
            addX1 = -1;
            addX2 = 1;
        } else
            addX1 = addX2 = 0;
    }

    /**
     * @return The next index for this pattern.
     */
    @Override
    public Vector2i nextIndex() {
        if (start == DOWNLEFT || start == UPLEFT) {
            lastIndex.x -= 2;
            if (lastIndex.x < 1) {
                lastIndex.x += (size + (lastIndex.x == 0 ? addX1 : addX2));
                lastIndex.y += start == DOWNLEFT ? 1 : -1;
            }
            if (lastIndex.y > size || lastIndex.y < 1)
                return null;
        }
        if (start == DOWNRIGHT || start == UPRIGHT) {
            lastIndex.x += 2;
            if (lastIndex.x > size) {
                lastIndex.x -= (size + (lastIndex.x == size + 1 ? addX1 : addX2));
                lastIndex.y += start == DOWNRIGHT ? 1 : -1;
            }
            if (lastIndex.y > size || lastIndex.y < 1)
                return null;
        }
        return lastIndex;
    }

    /**
     * @return The first index for this pattern.
     */
    @Override
    public Vector2i firstIndex() {
        switch (start) {
            case DOWNLEFT:
                lastIndex = new Vector2i(size, 1);
                break;
            case DOWNRIGHT:
                lastIndex = new Vector2i(1, 1);
                break;
            case UPLEFT:
                lastIndex = new Vector2i(size, size);
                break;
            case UPRIGHT:
                lastIndex = new Vector2i(1, size);
                break;
        }
        if ((lastIndex.x + lastIndex.y) % 2 != 1 && color == WHITE) {
            if (start == UPRIGHT)
                lastIndex.x += 1;
            if (start == DOWNLEFT)
                lastIndex.x -= 1;
        }
        if ((lastIndex.x + lastIndex.y) % 2 != 0 && color == BLACK) {
            if (start == DOWNRIGHT)
                lastIndex.x += 1;
            if (start == UPLEFT)
                lastIndex.x -= 1;
        }
        return lastIndex;
    }
}
