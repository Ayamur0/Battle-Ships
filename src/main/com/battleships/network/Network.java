package com.battleships.network;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.Settings;
import org.joml.Vector2i;

public abstract class Network {
    private String shoot = "shoot ";
    private String size = "size ";
    private String confirmed = "confirmed ";
    private String answer = "answer ";
    private String save = "save ";
    private String load = "load ";

    private static GameManager gameManager;
    private static GridManager gridManager;




    private static Settings settings;

    /**
     * Die Methode schaut welcher String gesendet wurde und je nachdem wird die entsprechende Methode in der Logic aufgerufen.
     *
     * @param i Wenn i == 0 dann ist es der Server für i == 1 ist der Client gedacht.
     * @param text Der Input von Client oder User
     */
    public void whatKindOfStringIsThis(int i, String text){
        if(text.contains(shoot)){
            text = text.replace(shoot, "");
            String[] temp = text.split(" ");

            int row= Integer.parseInt(temp[0]);
            int col = Integer.parseInt(temp[1]);

            gameManager.shoot(gridManager.OPPONENTFIELD, new Vector2i(row+1, col+1));
        }else if(text.contains(size)){
            text = text.replace(size, "");

            settings = gameManager.getSettings();
            settings.setSize(Integer.parseInt(text));
        }else if(text.contains(confirmed)){
            text = text.replace(confirmed, "");
            if(text.equals("")) //TODO
                gameManager.getLogic().advanceGamePhase();
            //Ruft Methode in Logic auf
        }else if(text.contains(answer)){
            text = text.replace(answer, "");
            if(Integer.parseInt(text) == 0){

            }else if(Integer.parseInt(text) == 1){

            }else if(Integer.parseInt(text) == 2){

            }

        }else if(text.contains(save)){

        }else if(text.contains(load)){

        }else{
            //Fehler
        }
    }
}
