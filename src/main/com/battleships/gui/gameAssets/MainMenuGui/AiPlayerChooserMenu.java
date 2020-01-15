package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

/**
 * Overlay to choose the AI difficulty for yourself
 *
 * @author Sascha Mößle
 */
public class AiPlayerChooserMenu extends Menu {
    /**
     * Constant value for Easy button
     */
    private static final int EASY = 0;
    /**
     * Constant value for Medium button
     */
    private static final int MEDIUM = 1;
    /**
     * Constant value for Hard button
     */
    private static final int HARD = 2;
    /**
     * Constant value for Back button
     */
    private static final int BACK = 3;

    /**
     * Creates the menu to choose your AI difficulty when you press Play as AI, sets the color of the {@link GUIText} and creates the {@link GUIText} on the Buttons.
     *
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader     Loader needed to load textures
     */
    public AiPlayerChooserMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        createMenu();

        CreateTextLabels();

        createClickable();
    }

    /**
     * Creates the {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    protected void createMenu() {
        super.CreateButtonTextures(4);

        super.guiTexts.add(new GUIText("Easy", 2.5f, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.guiTexts.add(new GUIText("Medium", 2.5f, font, new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Hard", 2.5f, font, new Vector2f(buttons.get(2).getPositions().x, buttons.get(2).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 2.5f, font, new Vector2f(buttons.get(3).getPositions().x, buttons.get(3).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

    }

    /**
     * Tests if the click was on one of the {@link GuiTexture} in the menu
     *
     * @param gui The gui to test for if the click was on it.
     * @param x   xPos of the click (left of screen = 0, right of screen = 1)
     * @param y   yPos of the click (top of screen = 0, bottom of screen = 1)
     * @return {@code true} if the click was on one of the button textures, {@code false} else.
     */
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if (super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        if (super.isClickOnGui(super.buttons.get(1), x, y)) {
            super.buttonClicked = 1;
            return true;
        }
        if (super.isClickOnGui(super.buttons.get(2), x, y)) {
            super.buttonClicked = 2;
            return true;
        }
        if (super.isClickOnGui(super.buttons.get(3), x, y)) {
            super.buttonClicked = 3;
            return true;
        }
        return false;
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if(buttonClicked == BACK){
            clearMenu();
            new ESCMenu(guiManager, loader);
            return;
        }
        ESCMenu.setIsPlayerAI(true);
        ESCMenu.setActive(false);
        clearMenu();
        cleaBackgournd();
        GameManager.getSettings().setAiLevelP(buttonClicked);
        GameManager.getLogic().getTurnHandler().makeAiTurns();

//        if (buttonClicked == EASY) {
//            ESCMenu.setIsPlayerAI(true);
//            ESCMenu.setActive(false);
//            clearMenu();
//            cleaBackgournd();
//            GameManager.getSettings().setAiLevelP(EASY);
//        }
//        if (buttonClicked == MEDIUM) {
//            ESCMenu.setIsPlayerAI(true);
//            ESCMenu.setActive(false);
//
//            clearMenu();
//            cleaBackgournd();
//            GameManager.getSettings().setAiLevelP(MEDIUM);
//        }
//        if (buttonClicked == HARD) {
//            ESCMenu.setIsPlayerAI(true);
//            ESCMenu.setActive(false);
//            clearMenu();
//            cleaBackgournd();
//            GameManager.getSettings().setAiLevelP(HARD);
//        }
//        if (buttonClicked == BACK) {
//            clearMenu();
//            new ESCMenu(guiManager, loader);
//        }
//
    }
}
