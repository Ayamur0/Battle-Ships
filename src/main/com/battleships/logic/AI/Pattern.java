package com.battleships.logic.AI;

import org.joml.Vector2i;

/**
 * Methods each pattern needs to have.
 * Patterns return indices on the grid in a order that forms a specific pattern.
 *
 * @author Tim Staudenmaier
 */
public interface Pattern {

    /**
     * @return The next index for this pattern.
     */
    Vector2i nextIndex();

    /**
     * @return The first index for this pattern.
     */
    Vector2i firstIndex();
}
