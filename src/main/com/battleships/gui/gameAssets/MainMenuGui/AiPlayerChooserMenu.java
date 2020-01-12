package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class AiPlayerChooserMenu extends Menu {
    private static final int EASY = 0;
    private static final int MEDIUM = 1;
    private static final int HARD = 2;
    private static final int BACK = 3;
    public AiPlayerChooserMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        createMenu();

        CreateTextLabels();

        createClickable();
    }


    protected void createMenu(){
        super.CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Easy",2.5f, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.guiTexts.add(new GUIText("Medium",2.5f, font, new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Hard",2.5f, font, new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back",2.5f, font, new Vector2f(buttons.get(3).getPositions().x,buttons.get(3).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

    }
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
        if(super.isClickOnGui(super.buttons.get(2), x, y)) {
            super.buttonClicked = 2;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(3), x, y)) {
            super.buttonClicked = 3;
            return true;
        }
        return false;
    }
    @Override
    protected void clickAction() {
        if (buttonClicked == EASY){
            ESCMenu.setActive(false);
            clearMenu();
            cleaBackgournd();
            GameManager.getSettings().setAiLevelP(EASY);
        }
        if (buttonClicked == MEDIUM){
            ESCMenu.setActive(false);

            clearMenu();
            cleaBackgournd();
            GameManager.getSettings().setAiLevelP(MEDIUM);
        }
        if (buttonClicked == HARD){
            ESCMenu.setActive(false);
            clearMenu();
            cleaBackgournd();
            GameManager.getSettings().setAiLevelP(HARD);
        }
        if (buttonClicked == BACK){
            clearMenu();
            new ESCMenu(guiManager,loader);
        }

    }
}
