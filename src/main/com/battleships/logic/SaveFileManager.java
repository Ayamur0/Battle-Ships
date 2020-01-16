package com.battleships.logic;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class that handles saving and loading files.
 * Uses .xml Files for saving and loading.
 *
 * @author Tim Staudenmaier
 */
public class SaveFileManager {

    /**
     * Creates a new {@link SaveFile} with the specified name and tries to save it
     * to the saveFiles folder located in the same folder as the jar is.
     * Files are saved as .xml Files.
     * @param name Name of the file the game should be saved in (without suffix).
     * @return {@code true} if the file was saved, {@code false} if an error occurred.
     */
    public static boolean saveToFile(String name){
        SaveFile saveFile = new SaveFile();
        XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        xstream.autodetectAnnotations(true);
        try {
            File saveFolder = new File(getJarPath() + "/SaveFiles/");
            if(!saveFolder.isDirectory()) {
                if(!saveFolder.mkdir()){
                    System.err.println("Error creating save game folder!");
                    return false;
                }
            }
            File saveXML = new File(saveFolder, name+".xml");
            int i = 1;
            while(saveXML.exists()){
                saveXML = new File(saveFolder, name + "(" + i + ").xml");
                i++;
            }
            xstream.toXML(saveFile, new FileOutputStream(saveXML));
        } catch (Exception e) {
            System.err.println("Error while trying to safe game to saveGame.xml!");
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    /**
     * Load the {@link SaveFile} class from a saved .xml file.
     * @param fileName Name of the saved file that should be loaded.
     * @return The loaded {@link SaveFile} if it worked, else {@code null}.
     */
    public static SaveFile loadFromFile(String fileName){
        SaveFile saveFile = null;
        File saveGame;
        try {
            saveGame = new File(getJarPath() + "/SaveFiles/" + fileName + ".xml");
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            System.err.println("Error loading saveFile!");
            return null;
        }
        if (saveGame.exists()) {
            XStream xstream = new XStream();
            xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
            xstream.alias("SaveFile", SaveFile.class);
            try {
                saveFile = (SaveFile) xstream.fromXML(saveGame);
                System.out.println("Successfully loaded saveGame.xml!");
            } catch (Exception e) {
                System.err.println("Error while trying to load saveGame.xml!");
                e.printStackTrace(System.err);
                return null;
            }
        } else {
            System.err.println("File doesn't exists!");
        }
        return saveFile;
    }

    /**
     * @return The absolute Path the jar is located at.
     * @throws UnsupportedEncodingException If jar Path can't be read.
     */
    public static String getJarPath() throws UnsupportedEncodingException {
        URL url = SaveFileManager.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
        return new File(jarPath).getParentFile().getPath();
    }

    /**
     * Loads the data from a {@link SaveFile} into logic and gui so a game can be started from it.
     * Only call this after initializing the ingame scene, so all models are loaded!
     * @param saveFile SaveFile the data should be loaded from.
     */
    public static void loadSaveFile(SaveFile saveFile){
        GameManager.setSettings(saveFile.getSettings());
        GameManager.getSettings().setOnline(false);
        LogicManager logic = GameManager.getLogic();
        GridManager gridManager = GameManager.getGridManager();

        logic.setOpponentGrid(saveFile.getOpponentGrid());
        logic.setPlayerGrid(saveFile.getPlayerGrid());
        logic.setStats(saveFile.getStats());
        logic.getStats().restartTime();
        logic.setTurnHandler(saveFile.getTurnHandler());
        logic.setGameState(saveFile.getGameState());

        if(saveFile.getGameState() == GameManager.SHIPLACING) {
            GameManager.getShipSelector().setShipCounts(saveFile.getShipsLeftPlacing());
            GameManager.getShipSelector().updateCounts();
        }
        gridManager.setShips(saveFile.getShips());
        gridManager.setMarkers(saveFile.getMarkers());
        gridManager.setBurningFires(saveFile.getBurningFires());
        gridManager.setBurningFireSounds(saveFile.getBurningFireSounds());
        GameManager.resizeGrid();
    }
}
