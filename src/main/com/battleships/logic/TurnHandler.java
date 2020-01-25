package com.battleships.logic;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.logic.AI.AI;

/**
 * Class for handling turn order and ai turns during shooting phase.
 *
 * @author Tim Staudenmaier
 */
public class TurnHandler {

    /**
     * {@code true} if it's currently the players turn,
     * {@code false} else.
     */
    private boolean playerTurn = true;
    /**
     * {@code true} if the opponent is played by an AI, {@code false} else.
     */
    private boolean opponentAIUsed = false;
    /**
     * AI the opponent uses.
     * {@code null} if the opponent doesn't use an AI.
     */
    private AI opponentAI;
    /**
     * {@code true} if the player is played by an AI, {@code false} else.
     */
    private boolean playerAIUsed = false;
    /**
     * AI the player uses.
     * {@code null} if the player doesn't use an AI.
     */
    private AI playerAI;

    /**
     * Advances the turn order and executes AI turns if the next
     * turn is an AI.
     */
    public void advanceTurnOrder() {
        playerTurn = !playerTurn;
        makeAiTurns();
    }

    /**
     * All AI's used place their ships.
     */
    public void placeAiShips() {
        if (opponentAIUsed)
            opponentAI.placeShips();
        if (playerAIUsed)
            playerAI.placeShips();
        if (opponentAIUsed && playerAIUsed) {
            GameManager.getLogic().advanceGamePhase();
            makeAiTurns();
        }
    }

    /**
     * The AI whose turn it currently is makes that turn.
     * If it isn't the turn of an AI nothing happens.
     */
    public void makeAiTurns() {
        if (opponentAIUsed && !playerTurn)
            opponentAI.makeTurn();
        if (playerAIUsed && playerTurn)
            playerAI.makeTurn();
    }

    /**
     * @return {@code true} if it's currently the players turn, {@code false} else.
     */
    public boolean isPlayerTurn() {
        return playerTurn;
    }

    /**
     * Set playerTurn, needed if player should act second.
     *
     * @param playerTurn {@code true} if next turn should be the player, {@code false} else.
     */
    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * Set the AI the player should use if player uses an AI.
     *
     * @param ai AI the player should use.
     */
    public void setPlayerAI(AI ai) {
        playerAI = ai;
        playerAIUsed = true;
    }

    /**
     * Set the AI the opponent should use if opponent uses an AI.
     *
     * @param ai AI the opponent should use.
     */
    public void setOpponentAI(AI ai) {
        opponentAI = ai;
        opponentAIUsed = true;
    }

    /**
     * Removes the AI of the player, so the player isn't played by an AI.
     */
    public void removePlayerAI() {
        playerAIUsed = false;
    }

    /**
     * Removes the AI of the player, so the opponent isn't played by an AI.
     */
    public void removeOpponentAI() {
        opponentAIUsed = false;
    }

    /**
     * @return The AI that plays for the player. Only returns that AI, if the game is currently played online and
     * the player is played by an AI. Else this method will return null.
     */
    public AI getOnlineAI() {
        if (GameManager.getSettings().isOnline() && playerAIUsed) {
            return this.playerAI;
        }
        return null;
    }
}
