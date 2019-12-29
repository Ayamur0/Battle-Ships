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

public class AiVsAiMenu extends InGameSettingsMenu {
    public AiVsAiMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader,2);
    }
    @Override
    protected void createMenu(){

        super.playingFieldSize = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 5, 30,
                15, super.sliderSize, new Vector2f(super.standardButtonPos.x,super.standardButtonPos.y-0.15f), guiManager, GameManager.getGuis());
        super.difficulty1 = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 1, 3,
                2, super.sliderSize,new Vector2f(playingFieldSize.getPositions().x,playingFieldSize.getPositions().y+buttonGap), guiManager, GameManager.getGuis());
        super.difficulty2 = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 1, 3,
                2, super.sliderSize,new Vector2f(difficulty1.getPositions().x, difficulty1.getPositions().y+buttonGap), guiManager, GameManager.getGuis());

        super.guiTexts.add(new GUIText("Size",2.5f, font,new Vector2f(playingFieldSize.getPositions().x-0.14f,playingFieldSize.getPositions().y) , 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText(String.format("%d", playingFieldSize.getValueAsInt()),2.5f, font, new Vector2f(playingFieldSize.getPositions().x+0.16f,playingFieldSize.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Difficulty AI 1",2.5f, font, new Vector2f(difficulty1.getPositions().x-0.185f, difficulty1.getPositions().y), 0.16f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Normal",2.5f, font, new Vector2f(difficulty1.getPositions().x+0.16f, difficulty1.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Difficulty AI 2",2.5f, font, new Vector2f(difficulty2.getPositions().x-0.185f,difficulty2.getPositions().y), 0.16f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Normal",2.5f, font, new Vector2f(difficulty2.getPositions().x+0.16f,difficulty2.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        buttons.add(new GuiTexture(texture,new Vector2f(difficulty2.getPositions().x,difficulty2.getPositions().y+buttonGap),buttonSize));
        buttons.add(new GuiTexture(texture,new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y+buttonGap),buttonSize));

        super.guiTexts.add(new GUIText("Fight",2.5f, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back",2.5f, font, new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }
    @Override
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
        switch (difficulty2.getValueAsInt()){
            case 1: difficultyName = "Easy";
                break;
            case 2: difficultyName = "Normal";
                break;
            case 3: difficultyName = "Hard";
                break;
        }
        super.guiTexts.get(5).remove();
        super.guiTexts.get(5).setTextString(difficultyName);
        TextMaster.loadText(super.guiTexts.get(1));
        TextMaster.loadText(super.guiTexts.get(3));
        TextMaster.loadText(super.guiTexts.get(5));
    }
}
