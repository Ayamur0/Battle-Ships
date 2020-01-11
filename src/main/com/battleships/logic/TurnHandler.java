package com.battleships.logic;

public class TurnHandler {

    private boolean playerTurn = true;
    private boolean opponentAIUsed = false;
    private AI opponentAI;
    private boolean playerAIUsed = false;
    private AI playerAI;

    public void advanceTurnOrder(){
        playerTurn = !playerTurn;
        makeAiTurns();
    }

    public void placeAiShips(){
        if(opponentAIUsed)
            opponentAI.placeShips();
        if(playerAIUsed)
            playerAI.placeShips();
    }

    public void makeAiTurns(){
        if(opponentAIUsed && !playerTurn)
            opponentAI.makeTurn();
        if(playerAIUsed && playerTurn)
            playerAI.makeTurn();
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerAI(AI ai){
        playerAI = ai;
        playerAIUsed = true;
    }

    public void setOpponentAI(AI ai){
        opponentAI = ai;
        opponentAIUsed = true;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void removePlayerAI(){
        playerAIUsed = false;
    }

    public void removeOpponentAI(){
        opponentAIUsed = false;
    }
}
