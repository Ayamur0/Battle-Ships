package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.guis.Slider;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class InGameSettingsMenu extends Menu {
    private static final int FIGHT = 0;
    private static final int BACK = 1;
    protected int gameMode; //0 Singleplayer, 1 Multiplayer, 2 Ai VS Ai;

    protected Slider playingFieldSize;
    protected Slider difficulty1;
    protected Slider difficulty2; //needed for Ai VS Ai to remove the slider afterwords

    protected Vector2f sliderSize = new Vector2f(0.2f, 0.01f);

    public InGameSettingsMenu(GuiManager guiManager, Loader loader,int gameMode) {
        super(guiManager, loader);

        this.gameMode = gameMode;

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
    }
    public void RefreshSliderValue(){
        String difficultyName = "";

        super.guiTexts.get(1).remove();
        super.guiTexts.get(1).setTextString(String.format("%d", playingFieldSize.getValueAsInt()));


        switch (difficulty1.getValueAsInt()){
            case 1: difficultyName = "Easy";
                    break;
            case 2: difficultyName = "Normal";
                    break;
            case 3: difficultyName = "Hard";
                    break;
        }
        super.guiTexts.get(3).remove();
        super.guiTexts.get(3).setTextString(difficultyName);
        TextMaster.loadText(super.guiTexts.get(1));
        TextMaster.loadText(super.guiTexts.get(3));
    }
    public boolean isRunning(){
        if (difficulty2!=null)
            return (playingFieldSize.isRunning()||difficulty1.isRunning()||difficulty2.isRunning());
        else
            return (playingFieldSize.isRunning()||difficulty1.isRunning());
    }

    protected void createMenu() {

        playingFieldSize = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 5, 30,
                15, sliderSize, super.standardButtonPos, guiManager, GameManager.getGuis());

        difficulty1 = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 1, 3,
                2, sliderSize,new Vector2f(playingFieldSize.getPositions().x,playingFieldSize.getPositions().y+buttonGap), guiManager, GameManager.getGuis());

        super.guiTexts.add(new GUIText("Size",2.5f, font,new Vector2f(playingFieldSize.getPositions().x-0.14f,playingFieldSize.getPositions().y) , 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText(String.format("%d", playingFieldSize.getValueAsInt()),2.5f, font, new Vector2f(playingFieldSize.getPositions().x+0.16f,playingFieldSize.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Difficulty",2.5f, font, new Vector2f(difficulty1.getPositions().x-0.165f, difficulty1.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Normal",2.5f, font, new Vector2f(difficulty1.getPositions().x+0.16f, difficulty1.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        buttons.add(new GuiTexture(texture,new Vector2f(difficulty1.getPositions().x, difficulty1.getPositions().y+buttonGap),buttonSize));
        buttons.add(new GuiTexture(texture,new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y+buttonGap),buttonSize));
        GameManager.getGuis().addAll(buttons);

        super.guiTexts.add(new GUIText("Fight",2.5f, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back",2.5f, font, new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();

    }
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
    @Override
    protected void clickAction() {
        if(gameMode == 0){
            if (super.buttonClicked == FIGHT){
                super.clearMenu();
                playingFieldSize.remove();
                difficulty1.remove();
                //Inits.setGlobalGameState(1);
                //TODO set difficulty and size for offline game(need logic for that)
            }
            if (super.buttonClicked == BACK){
                super.clearMenu();
                playingFieldSize.remove();
                difficulty1.remove();
                MainMenuManager.setMenu(new PlayMenu(super.guiManager,super.loader));
            }
        }
        else if(gameMode == 1){
            if (super.buttonClicked == FIGHT){
                super.clearMenu();
                playingFieldSize.remove();
                difficulty1.remove();
                MainMenuManager.setMenu(new WaitingConnection(super.guiManager,super.loader));
                //Inits.setGlobalGameState(1);
                //TODO set difficulty and size for Multiplayer game(need logic for that)
                //TODO add Connection overlay
            }
            if (super.buttonClicked == BACK){
                super.clearMenu();
                playingFieldSize.remove();
                difficulty1.remove();
                MainMenuManager.setMenu(new MultiplayerMenu(super.guiManager,super.loader));
            }
        }
        else if(gameMode == 2){
            if (super.buttonClicked == FIGHT){
                super.clearMenu();
                playingFieldSize.remove();
                difficulty1.remove();
                difficulty2.remove();
                //Inits.setGlobalGameState(1);
                //TODO set difficulty and size for offline game(need logic for that)
                //TODO add Connection overlay
            }
            if (super.buttonClicked == BACK){
                super.clearMenu();
                playingFieldSize.remove();
                difficulty1.remove();
                difficulty2.remove();
                MainMenuManager.setMenu(new PlayMenu(super.guiManager,super.loader));
            }
        }
    }
}
