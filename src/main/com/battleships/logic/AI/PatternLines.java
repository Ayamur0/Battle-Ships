package com.battleships.logic.AI;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Pattern that shoots in diagonal lines across the grid.
 * These lines have a random distance (between 2 and 4) and a random direction (of the 4 possible diagonal directions).
 *
 * @author Tim Staudenmaier
 */
public class PatternLines implements Pattern {

    /**
     * Directions in which the lines can go.
     */
    private static final int DOWNLEFT = 0, DOWNRIGHT = 1;
    /**
     * Points on the grid at which the AI can start the first line.
     */
    private static final int RIGHT = 0, MIDDLERIGHT = 1, MIDDLELEFT = 2, LEFT = 3;

    /**
     * Distance between two lines in cells.
     */
    private int distance;
    /**
     * Direction each line has.
     */
    private int direction;
    /**
     * Point at which the AI has started.
     */
    private int startPoint;
    /**
     * Cells at which each line has started.
     */
    private List<Vector2i> lineStarts = new ArrayList<>();
    /**
     * Size of the grid this pattern is used on.
     */
    private int size;
    /**
     * Last index this pattern calculated.
     */
    private Vector2i lastIndex;

    /**
     * Creates a new Pattern that returns indices forming diagonal lines.
     *
     * @param size Size of the grid this pattern is used on.
     */
    public PatternLines(int size) {
        this.size = size;
        Random r = new Random();
        distance = r.nextInt(3) + 2;
        direction = r.nextInt(2);
        startPoint = r.nextInt(4);
        calculateLineStarts();
    }

    /**
     * Calculates the start cells for all lines.
     */
    private void calculateLineStarts() {
        List<Vector2i> right = new ArrayList<>();
        List<Vector2i> left = new ArrayList<>();
        if (direction == DOWNLEFT) {
            int x = size, y = 1;
            while (y <= size) {
                right.add(new Vector2i(x, y));
                y += distance + 1;
            }
            y = 1;
            while (x > 0) {
                left.add(new Vector2i(x, y));
                x -= distance + 1;
            }
        }
        if (direction == DOWNRIGHT) {
            int x = 1, y = 1;
            while (x <= size) {
                right.add(new Vector2i(x, y));
                x += distance + 1;
            }
            x = 1;
            while (y <= size) {
                left.add(new Vector2i(x, y));
                y += distance + 1;
            }
        }
        switch (startPoint) {
            case RIGHT:
                Collections.reverse(right);
                lineStarts.addAll(right);
                lineStarts.addAll(left);
                break;
            case MIDDLERIGHT:
                lineStarts.addAll(right);
                lineStarts.addAll(left);
                break;
            case MIDDLELEFT:
                lineStarts.addAll(left);
                lineStarts.addAll(right);
            case LEFT:
                Collections.reverse(left);
                lineStarts.addAll(left);
                lineStarts.addAll(right);
        }
    }

    /**
     * @return The next index for this pattern.
     */
    @Override
    public Vector2i nextIndex() {
        if (direction == DOWNLEFT) {
            lastIndex.x -= 1;
            lastIndex.y += 1;

            if (lastIndex.x < 1 || lastIndex.y > size) {
                if (lineStarts.size() == 0)
                    return null;
                lastIndex = lineStarts.get(0);
                lineStarts.remove(0);
            }
        }
        if (direction == DOWNRIGHT) {
            lastIndex.x += 1;
            lastIndex.y += 1;

            if (lastIndex.x > size || lastIndex.y > size) {
                if (lineStarts.size() == 0)
                    return null;
                lastIndex = lineStarts.get(0);
                lineStarts.remove(0);
            }
        }
        return lastIndex;
    }

    /**
     * @return The first index for this pattern.
     */
    @Override
    public Vector2i firstIndex() {
        lastIndex = lineStarts.get(0);
        lineStarts.remove(0);
        return lastIndex;
    }
}
