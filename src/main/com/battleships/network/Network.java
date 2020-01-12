package com.battleships.network;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.OnlineGrid;
import com.battleships.logic.SaveFileManager;
import com.battleships.logic.Settings;
import org.joml.Intersectiond;
import org.joml.Vector2i;

public abstract class Network implements NetworkInterface{

    private static final int NONE = -1, SHOOT = 0, CONFIRM = 1, SAVE = 2, LOAD = 3, SIZE = 4;

    private String shoot = "shoot ";
    private String size = "size ";
    private String confirmed = "confirmed";
    private String answer = "answer ";
    private String save = "save ";
    private String load = "load ";

    private boolean playerConfirm;
    private boolean opponentConfirm;

    private int row;
    private int col;
    private String ID;

    private int action;

    private int lastShotX;
    private int lastShotY;

    public void execute(){
        switch (action){
            case NONE: break;
            case SHOOT: GameManager.shoot(GridManager.OPPONENTFIELD, new Vector2i(row+1, col+1)); break;
            case CONFIRM: setOpponentConfirm(); break;
            case SAVE: SaveFileManager.saveToFile(ID); break;
            case LOAD: SaveFileManager.loadFromFile(ID); GameManager.getMainMenuManager().clearAll(); GameManager.resizeGrid(); GameManager.getLogic().advanceGamePhase(); break;
            case SIZE: GameManager.getMainMenuManager().clearAll(); GameManager.resizeGrid(); GameManager.getLogic().advanceGamePhase(); break;
        }
        action = NONE;
    }


    /**
     * Die Methode schaut welcher String gesendet wurde und je nachdem wird die entsprechende Methode in der Logic aufgerufen.
     *
     * @param i Wenn i == 0 dann ist es der Server für i == 1 ist der Client gedacht.
     * @param text Der Input von Client oder User
     */
    public void setStringFunction(int i, String text){
        if(text.contains(shoot)){
            text = text.replace(shoot, "");
            String[] temp = text.split(" ");

            row= Integer.parseInt(temp[0]);
            col = Integer.parseInt(temp[1]);

            action = SHOOT;
        }else if(text.contains(size)){
            text = text.replace(size, "");
            GameManager.getLogic().onlineMode(true);

            GameManager.getSettings().setSize(Integer.parseInt(text));
            GameManager.getSettings().setOnline(true);
            action = SIZE;
        }else if(text.contains(confirmed)){
            text = text.replace(confirmed, "");
            if(text.equals(""))
               action = CONFIRM;
        }else if(text.contains(answer)){
            text = text.replace(answer, "");
            if(Integer.parseInt(text) == 0)
                GameManager.processShootAnswer(false);
             if(Integer.parseInt(text) == 1){
                 GameManager.processShootAnswer(true);
                 if(GameManager.getLogic().getOpponentGrid() instanceof OnlineGrid)
                    ((OnlineGrid) GameManager.getLogic().getOpponentGrid()).processHit(lastShotX,lastShotY);
            }else if(Integer.parseInt(text) == 2){
                 GameManager.processShootAnswer(true);
                 if(GameManager.getLogic().getOpponentGrid() instanceof OnlineGrid)
                     ((OnlineGrid) GameManager.getLogic().getOpponentGrid()).processHitSunk(lastShotX,lastShotY);
            }

        }else if(text.contains(save)){
            text = text.replace(save, "");
            ID = text;

        }else if(text.contains(load)){
            text = text.replace(load, "");
            ID = text;
        }else{
            System.err.println("Received faulty message from Network!");
        }
    }

    public void setOpponentConfirm(){
        opponentConfirm = true;
        if(playerConfirm){
            GameManager.getLogic().advanceGamePhase();
            opponentConfirm = false;
            playerConfirm = false;
        }
    }

    public void setPlayerConfirm(){
        playerConfirm = true;
        if(opponentConfirm){
            GameManager.getLogic().advanceGamePhase();
            opponentConfirm = false;
            playerConfirm = false;
        }
    }

    public void setLastShot(int x, int y){
        lastShotX = x;
        lastShotY = y;
    }
}
