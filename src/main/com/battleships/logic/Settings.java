package com.battleships.logic;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.gui.postProcessing.PostProcessing;
import com.battleships.gui.window.WindowManager;
import com.battleships.logic.AI.AIEasy;
import com.battleships.logic.AI.AIHard;
import com.battleships.logic.AI.AIMedium;
import com.thoughtworks.xstream.XStream;
import org.joml.Vector2i;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Class containing all currently set settings.
 */
public class Settings {

    /**
     * Constants representing ai levels.
     */
    public static final int EASY = 0, MEDIUM = 1, HARD = 2;

    /**
     * Constant for setting resolution of game to screen resolution.
     */
    public static final int SCREENRESOLUTION = -1;

    /**
     * Level the ai for the player in the current game use.
     */
    private int aiLevelP = -1;

    /**
     * Level the ai for the opponent in the current game use.
     */
    private int aiLevelO = -1;
    /**
     * Size the grid of the next game should have.
     */
    private int size = 12;
    /**
     * {@code true} if the game is played online, {@code false} if the game is played offline.
     */
    private boolean online = true;

    /**
     * {@code true} if sound is enabled, {@code false} else.
     */
    private boolean sound = true;

    /**
     * {@code true} if animations are enabled, {@code false} else.
     */
    private boolean animation;

    /**
     * Current resolution of the game.
     */
    private int resWidth, resHeight;

    /**
     * Creates new settings.
     * Tries to load existing settings.
     */
    public Settings() {
        loadSettings();
    }

    /**
     * @return {@code true} if the game is played online, {@code false} if the game is played offline.
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Set whether the game is played online or offline.
     * @param online {@code true} if the game is played online, {@code false} if the game is played offline.
     */
    public void setOnline(boolean online) {
        GameManager.getLogic().onlineMode(online);
        this.online = online;
    }

    /**
     * @return Level the AI of the player in the current game uses.
     */
    public int getAiLevelP() {
        return aiLevelP;
    }

    /**
     * Sets the level the AI for the player should use during the next game.
     * @param aiLevel Level the AI for the player in the next game should use.
     */
    public void setAiLevelP(int aiLevel) {
        this.aiLevelP = aiLevel;
        switch (aiLevel){
            case EASY: GameManager.getLogic().getTurnHandler().setPlayerAI(
                    new AIEasy(GridManager.OWNFIELD, GameManager.getLogic().getPlayerGrid().getSize(), GameManager.getLogic()));
            case MEDIUM: GameManager.getLogic().getTurnHandler().setPlayerAI(
                    new AIMedium(GridManager.OWNFIELD, GameManager.getLogic().getPlayerGrid().getSize(), GameManager.getLogic()));
            case HARD: GameManager.getLogic().getTurnHandler().setPlayerAI(
                    new AIHard(GridManager.OWNFIELD, GameManager.getLogic().getPlayerGrid().getSize(), GameManager.getLogic()));
        }
        if(aiLevel == -1)
            GameManager.getLogic().getTurnHandler().removePlayerAI();
    }

    /**
     * @return Level the AI of the opponent in the current game uses.
     */
    public int getAiLevelO() {
        return aiLevelO;
    }

    /**
     * Sets the level the AI for the opponent should use during the next game.
     * @param aiLevelO Level the AI for the opponent in the next game should use.
     */
    public void setAiLevelO(int aiLevelO) {
        this.aiLevelO = aiLevelO;
        switch (aiLevelO){
            case EASY: GameManager.getLogic().getTurnHandler().setPlayerAI(
                    new AIEasy(GridManager.OPPONENTFIELD, GameManager.getLogic().getPlayerGrid().getSize(), GameManager.getLogic()));
            case MEDIUM: GameManager.getLogic().getTurnHandler().setPlayerAI(
                    new AIMedium(GridManager.OPPONENTFIELD, GameManager.getLogic().getPlayerGrid().getSize(), GameManager.getLogic()));
            case HARD: GameManager.getLogic().getTurnHandler().setPlayerAI(
                    new AIHard(GridManager.OPPONENTFIELD, GameManager.getLogic().getPlayerGrid().getSize(), GameManager.getLogic()));
        }
        if(aiLevelO == -1)
            GameManager.getLogic().getTurnHandler().removePlayerAI();
    }

    /**
     * @return Size of the grids in the current game.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size for the grids in the next game.
     * @param size Size the grids should have in the next game.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return {@code true} if sound is enabled, {@code false} else.
     */
    public boolean isSound() {
        return sound;
    }

    /**
     * This value needs to be set, if sound is being enabled or disabled.
     * @param sound {@code true} if sound gets enabled, {@code false} if it gets disabled.
     */
    public void setSound(boolean sound) {
        this.sound = sound;
    }

    /**
     * @return {@code true} if animations are enabled, {@code false} else.
     */
    public boolean isAnimation() {
        return animation;
    }

    /**
     * This value needs to be set, if animations are being enabled or disabled.
     * @param animation {@code true} if animations get enabled, {@code false} if they get disabled.
     */
    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    /**
     * Changes the resolution of the game.
     * @param width Width of the new resolution in pixels or {@value SCREENRESOLUTION} for resolution of screen.
     * @param height Height of the new resolution in pixels or {@value SCREENRESOLUTION} for resolution of screen.
     */
    public void changeResolution(int width, int height){
        if(width == SCREENRESOLUTION || height == SCREENRESOLUTION){
            PostProcessing.changeResolution(WindowManager.getWidth(), WindowManager.getHeight());
            return;
        }
        PostProcessing.changeResolution(width, height);
    }

    /**
     * Loads the settings saved after exiting the game last time.
     * @return {@code true} if settings could be loaded {@code false} else.
     */
    public boolean loadSettings(){
        SettingsSaveFile saveFile = null;
        File settings;
        try {
            settings = new File(SaveFileManager.getJarPath() + "/Settings/settings.xml");
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            System.err.println("Error loading settings File!");
            return false;
        }
        if (settings.exists()) {
            XStream xstream = new XStream();
            xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
            xstream.alias("SettingsFile", SettingsSaveFile.class);
            try {
                saveFile = (SettingsSaveFile) xstream.fromXML(settings);
                System.out.println("Successfully loaded settings.xml!");
            } catch (Exception e) {
                System.err.println("Error while trying to load settings.xml!");
                e.printStackTrace(System.err);
                return false;
            }
        }
        else
            return false;
        sound = saveFile.isSound();
        animation = saveFile.isAnimation();
        resWidth = saveFile.getResWidth();
        resHeight = saveFile.getResHeight();
        return true;
    }

    /**
     * Saves the settings into a file, so they can be loaded when the game is started next time.
     * @return {@code true} if the settings were saved, {@code false} if an error occurred during saving.
     */
    public boolean saveSettings(){
        SettingsSaveFile saveFile = new SettingsSaveFile(sound, animation, resWidth, resHeight);
        XStream xstream = new XStream();
        xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
        xstream.autodetectAnnotations(true);
        try {
            File saveFolder = new File(SaveFileManager.getJarPath() + "/Settings/");
            if(!saveFolder.isDirectory()) {
                if(!saveFolder.mkdir()){
                    System.err.println("Error creating settings folder!");
                    return false;
                }
            }
            File saveXML = new File(saveFolder, "settings.xml");
            xstream.toXML(saveFile, new FileOutputStream(saveXML));
        } catch (Exception e) {
            System.err.println("Error while trying to safe settings to settings.xml!");
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }
}
