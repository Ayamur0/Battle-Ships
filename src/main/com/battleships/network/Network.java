package com.battleships.network;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.OnlineGrid;
import com.battleships.logic.SaveFileManager;
import com.battleships.logic.Settings;
import org.joml.Vector2i;

public abstract class Network implements NetworkInterface{
    private String shoot = "shoot ";
    private String size = "size ";
    private String confirmed = "confirmed ";
    private String answer = "answer ";
    private String save = "save ";
    private String load = "load ";

    private boolean playerConfirm;
    private boolean opponentConfirm;

    private int lastShotX;
    private int lastShotY;

    private static GameManager gameManager;
    private static GridManager gridManager;





    /**
     * Die Methode schaut welcher String gesendet wurde und je nachdem wird die entsprechende Methode in der Logic aufgerufen.
     *
     * @param i Wenn i == 0 dann ist es der Server für i == 1 ist der Client gedacht.
     * @param text Der Input von Client oder User
     */
    public void executeStringFunction(int i, String text){
        if(text.contains(shoot)){
            text = text.replace(shoot, "");
            String[] temp = text.split(" ");

            int row= Integer.parseInt(temp[0]);
            int col = Integer.parseInt(temp[1]);

            GameManager.shoot(GridManager.OPPONENTFIELD, new Vector2i(row+1, col+1));
        }else if(text.contains(size)){
            text = text.replace(size, "");
            GameManager.getLogic().onlineMode(true);

            GameManager.getSettings().setSize(Integer.parseInt(text));
            GameManager.getSettings().setOnline(true);
        }else if(text.contains(confirmed)){
            text = text.replace(confirmed, "");
            if(text.equals(""))
               setOpponentConfirm();
            //Ruft Methode in Logic auf
        }else if(text.contains(answer)){
            text = text.replace(answer, "");
             if(Integer.parseInt(text) == 1){
                 if(GameManager.getLogic().getOpponentGrid() instanceof OnlineGrid)
                    ((OnlineGrid) GameManager.getLogic().getOpponentGrid()).processHit(lastShotX,lastShotY);
            }else if(Integer.parseInt(text) == 2){
                 if(GameManager.getLogic().getOpponentGrid() instanceof OnlineGrid)
                     ((OnlineGrid) GameManager.getLogic().getOpponentGrid()).processHitSunk(lastShotX,lastShotY);
            }

        }else if(text.contains(save)){
            text = text.replace(save, "");
            SaveFileManager.saveToFile(""+text);

        }else if(text.contains(load)){
            text = text.replace(load, "");
            SaveFileManager.loadFromFile(text);
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
