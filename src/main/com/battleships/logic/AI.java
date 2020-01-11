package com.battleships.logic;

/**
 * Interface for different AI's.
 *
 * @author Tim Staudenmaier
 */
public interface AI {

    /**
     * AI makes their next turn.
     */
    void makeTurn();

    /**
     * AI places it's ships.
     */
    void placeShips();
}
