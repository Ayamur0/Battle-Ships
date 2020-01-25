package com.battleships.logic.AI;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import org.joml.Vector2i;

import java.util.Random;

/**
 * Pattern that shoots randomly on the grid.
 *
 * @author Tim Staudenmaier
 */
public class PatternRandom implements Pattern {

    /**
     * Size of the grid this pattern is used on.
     */
    private int size;
    /**
     * Team this pattern is used for.
     */
    private int team;
    /**
     * Random number generator for generating random indices.
     */
    private Random random = new Random();

    /**
     * Creates a new random pattern.
     *
     * @param size Size of the grid this pattern is used on.
     * @param team Team this grid is used for.
     */
    public PatternRandom(int size, int team) {
        this.size = size;
        this.team = team;
    }

    /**
     * @return The next index for this pattern.
     */
    @Override
    public Vector2i nextIndex() {
        int x = random.nextInt(size) + 1;
        int y = random.nextInt(size) + 1;
        while (GameManager.getLogic().hasBeenShot(x, y, team == GridManager.OWNFIELD ? GridManager.OPPONENTFIELD : GridManager.OWNFIELD)) {
            y += x / size;
            y %= size + 1;
            if (y == 0)
                y += 1;
            x += 1;
            x %= size + 1;
            if (x == 0)
                x += 1;
        }
        return new Vector2i(x, y);
    }

    /**
     * @return The first index for this pattern.
     */
    @Override
    public Vector2i firstIndex() {
        return nextIndex();
    }
}
