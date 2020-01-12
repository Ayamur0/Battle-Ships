package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.guis.Slider;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.logic.LogicManager;
import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import org.joml.Vector2f;

/**
 * Contains all functions needed for an settings menu
 *
 * @author Sascha Mößle
 */
public class InGameSettingsMenu extends Menu {
    /**
     * Constant value for start button
     */
    private static final int START = 0;
    /**
     * Constant value for back button
     */
    private static final int BACK = 1;
    private static final int SP = 0;
    private static final int MP = 1;
    private static final int AIVSAI = 2;
    /**
     * The offset used too set the {@link GUIText} above the {@link Slider}
     */
    private float sliderOffset = 0.04f;
    /**
     * Indicates if the settings are for Singleplayer, Multiplayer or Ai VS Ai
     */
    protected int gameMode; //0 Singleplayer, 1 Multiplayer, 2 Ai VS Ai;

    /**
     * Slider for the playing field size
     */
    protected Slider playingFieldSize;
    /**
     * Slider for the ai difficulty
     */
    protected Slider difficulty1;
    /**
     * Slider for the second ai difficulty only needed if game mode Ai VS Ai is chosen
     */
    protected Slider difficulty2; //needed for Ai VS Ai to remove the slider afterwords

    /**
     * Size for the Sliders
     */
    protected Vector2f sliderSize = new Vector2f(0.2f, 0.01f);

    /**
     * Creates the {@link Slider}, adds and changes the color of the {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     * @param gameMode needed to indicate what game mode it is.
     */
    public InGameSettingsMenu(GuiManager guiManager, Loader loader,int gameMode) {
        super(guiManager, loader);

        this.gameMode = gameMode;

        this.createMenu();

        CreateTextLabels();

    }

    /**
     * Refreshes the {@link GUIText} that show the Value of the {@link Slider} in the current Menu
     */
    public void RefreshSliderValue(){
        String difficultyName = "";

        super.guiTexts.get(0).remove();
        super.guiTexts.get(0).setTextString("Size: "+playingFieldSize.getValueAsInt());
        if (gameMode == SP || gameMode == AIVSAI){
            switch (difficulty1.getValueAsInt()){
                case 1: difficultyName = "Easy";
                    break;
                case 2: difficultyName = "Normal";
                    break;
                case 3: difficultyName = "Hard";
                    break;
            }
            super.guiTexts.get(1).remove();
            super.guiTexts.get(1).setTextString("Difficulty: "+difficultyName);
            TextMaster.loadText(super.guiTexts.get(1));
        }
        TextMaster.loadText(super.guiTexts.get(0));

    }

    /**
     * Indicates if one of the {@link Slider} is moving
     * @return {@code true} if one of the sliders is moving {@code false} else
     */
    public boolean isRunning(){
        if (gameMode == SP)
            return (playingFieldSize.isRunning()||difficulty1.isRunning());
        else if (gameMode == MP)
            return (playingFieldSize.isRunning());
        else
            return (playingFieldSize.isRunning()||difficulty1.isRunning()||difficulty2.isRunning());
    }

    /**
     * Creates the {@link Slider}, adds {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    protected void createMenu() {

        playingFieldSize = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 5, 30,
                15, sliderSize, super.standardButtonPos, guiManager, GameManager.getGuis());
        super.guiTexts.add(new GUIText("Size: "+playingFieldSize.getValueAsInt(),fontSize, font,new Vector2f(playingFieldSize.getPositions().x,playingFieldSize.getPositions().y-0.04f) , 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));


        if (gameMode == SP) {
            difficulty1 = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 1, 3,
                    2, sliderSize, new Vector2f(playingFieldSize.getPositions().x, playingFieldSize.getPositions().y + buttonGap), guiManager, GameManager.getGuis());
            super.guiTexts.add(new GUIText("Difficulty: Normal",fontSize, font, new Vector2f(difficulty1.getPositions().x, difficulty1.getPositions().y-0.04f), 0.4f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        }

        buttons.add(new GuiTexture(buttonTexture,new Vector2f(standardButtonPos.x,standardButtonPos.y+2*buttonGap),buttonSize));
        buttons.add(new GuiTexture(buttonTexture,new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y+buttonGap),buttonSize));
        GameManager.getGuis().addAll(buttons);

        super.guiTexts.add(new GUIText("Begin",fontSize, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back",fontSize, font, new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();

    }

    /**
     * Tests if the click was on one of the Textures in the menu
     * @param gui The gui to test for if the click was on it.
     * @param x xPos of the click (left of screen = 0, right of screen = 1)
     * @param y yPos of the click (top of screen = 0, bottom of screen = 1)
     * @return {@code true} if the click was on one of the button textures, {@code false} else.
     */
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(1), x, y)) {
            super.buttonClicked = 1;
            return true;
        }
        return false;
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
            if (super.buttonClicked == START){
                super.cleaBackgournd();
                super.clearMenu();
                GameManager.getSettings().setSize(playingFieldSize.getValueAsInt());
                GameManager.resizeGrid();
                playingFieldSize.remove();

                if (gameMode == SP || gameMode == AIVSAI){
                    GameManager.getSettings().setAiLevelO(difficulty1.getValueAsInt());
                    difficulty1.remove();
                    if (gameMode == AIVSAI){
                        GameManager.getSettings().setAiLevelP(difficulty2.getValueAsInt());
                        difficulty2.remove();
                    }
                    GameManager.getLogic().advanceGamePhase();
                }
                else{
                    GameManager.getSettings().setOnline(true);
                    GameManager.getNetwork().start(true,null);
                    MainMenuManager.setMenu(new WaitingConnection(guiManager,loader,false));
                    ((WaitingConnection)MainMenuManager.getMenu()).setServer(true);
                }
            }
            if (super.buttonClicked == BACK){
                super.clearMenu();
                playingFieldSize.remove();
                if (difficulty1!=null)
                    difficulty1.remove();
                if (difficulty2 != null)
                    difficulty2.remove();
                if (gameMode == SP || gameMode == AIVSAI)
                    MainMenuManager.setMenu(new PlayMenu(super.guiManager,super.loader));
                else
                    MainMenuManager.setMenu(new MultiplayerMenu(super.guiManager,super.loader));
            }
        }
    }
