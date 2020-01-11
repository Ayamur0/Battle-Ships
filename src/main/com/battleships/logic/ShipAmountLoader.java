package com.battleships.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class to load the amounts of ships that need to be placed on specific grid sizes.
 * Loads these values from a predetermined table.
 *
 * @author Tim Staudenmaier
 */
public class ShipAmountLoader {

    /**
     * Reads amount of ships needed for specified size from file.
     * @param gridSize Size of grid for which the ship amounts should be read.
     * @return Array containing ship amounts for each size ordered from small ships to large ships
     */
    public static int[] getShipAmounts(int gridSize){
        if(gridSize < 5 || gridSize > 30){
            System.err.println("Grid size has to be between 5 and 30 was " + gridSize + "!");
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(ShipAmountLoader.class.getResourceAsStream("/resources/schiffstabelle.csv")));
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
            return new int[] {Integer.parseInt(sizes[3]), Integer.parseInt(sizes[2]),Integer.parseInt(sizes[1]),Integer.parseInt(sizes[0])};
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't read shipTable file!");
            return null;
        }
    }
}
