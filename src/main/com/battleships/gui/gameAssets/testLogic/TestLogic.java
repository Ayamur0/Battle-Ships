package com.battleships.gui.gameAssets.testLogic;

import com.battleships.gui.fontMeshCreator.MetaFile;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.PlayingField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestLogic {

    //public static Grid own;
    //public static Grid opponent;

    public static void main(String[] args){
        getShipAmounts(10);
    }

    public static void init(int size) {
        //own = new Grid(size, PlayingField.OWNFIELD);
        //opponent = new Grid(size, PlayingField.OPPONENTFIELD);
    }

    public static int getEnemyShipsLeft(int size){
        return 10;
    }

    public static int[] getShipAmounts(int gridSize){
        if(gridSize < 5 || gridSize > 30){
            System.err.println("Grid size has to be between 5 and 30 was " + gridSize + "!");
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(TestLogic.class.getResourceAsStream("/resources/schiffstabelle.csv")));
        for(int linesToRead = gridSize - 5; linesToRead > 0; linesToRead--) {
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Couldn't read shipTable file!");
                return null;
            }
        }
        try {
            String line = reader.readLine();
            String[] sizes = line.split(";");
            return new int[] {Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]),Integer.parseInt(sizes[2]),Integer.parseInt(sizes[3])};
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't read shipTable file!");
            return null;
        }
    }
}
