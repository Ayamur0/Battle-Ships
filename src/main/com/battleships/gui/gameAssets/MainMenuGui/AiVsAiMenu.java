package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.guis.Slider;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

/**
 * Setting menu for the Ai VS Ai game mode
 *
 * @author Sascha Mößle
 */
public class AiVsAiMenu extends InGameSettingsMenu {
    /**
     * Creates the Ai Vs Ai menu
     *
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader     Loader needed to load textures
     */
    public AiVsAiMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader, 2);
    }

    /**
     * Creates the {@link Slider}, adds {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    @Override
    protected void createMenu() {
        super.playingFieldSize = new Slider(loader.loadTexture("Slider.png"), loader.loadTexture("WoodenSlider.jpg"), 5, 30,
                15, super.sliderSize, new Vector2f(super.standardButtonPos.x, super.standardButtonPos.y), guiManager, GameManager.getGuis());
        super.difficulty1 = new Slider(loader.loadTexture("Slider.png"), loader.loadTexture("WoodenSlider.jpg"), EASY, HARD,
                MEDIUM, super.sliderSize, new Vector2f(playingFieldSize.getPositions().x, playingFieldSize.getPositions().y + buttonGap), guiManager, GameManager.getGuis());
        super.difficulty2 = new Slider(loader.loadTexture("Slider.png"), loader.loadTexture("WoodenSlider.jpg"), EASY, HARD,
                MEDIUM, super.sliderSize, new Vector2f(difficulty1.getPositions().x, difficulty1.getPositions().y + buttonGap), guiManager, GameManager.getGuis());

        super.guiTexts.add(new GUIText("Size: " + playingFieldSize.getValueAsInt(), fontSize, font, new Vector2f(playingFieldSize.getPositions().x, playingFieldSize.getPositions().y - 0.06f), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Difficulty AI 1: Normal", fontSize, font, new Vector2f(difficulty1.getPositions().x, difficulty1.getPositions().y - 0.06f), 0.3f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Difficulty AI 2: Normal", fontSize, font, new Vector2f(difficulty2.getPositions().x, difficulty2.getPositions().y - 0.06f), 0.3f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        buttons.add(new GuiTexture(buttonTexture, new Vector2f(0.58f, 0.83f), buttonSize));
        buttons.add(new GuiTexture(buttonTexture, new Vector2f(new Vector2f(0.42f, 0.83f)), buttonSize));

        super.guiTexts.add(new GUIText("Begin", 2.5f, font, new Vector2f(buttons.get(0).getPositions().x, buttons.get(0).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", 2.5f, font, new Vector2f(buttons.get(1).getPositions().x, buttons.get(1).getPositions().y), 0.12f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        GameManager.getGuis().addAll(buttons);
        super.createClickable();
    }

    /**
     * Refreshes the {@link GUIText} that show the Value from the {@link Slider} of the Sliders in the current Menu
     */
    @Override
    public void RefreshSliderValue() {
        String difficultyName = "";

        super.guiTexts.get(0).remove();
        super.guiTexts.get(0).setTextString("Size: " + playingFieldSize.getValueAsInt());


        switch (difficulty1.getValueAsInt()) {
            case EASY:
                difficultyName = "Easy";
                break;
            case MEDIUM:
                difficultyName = "Normal";
                break;
            case HARD:
                difficultyName = "Hard";
                break;
        }
        super.guiTexts.get(1).remove();
        super.guiTexts.get(1).setTextString("Difficulty AI 1: " + difficultyName);
        switch (difficulty2.getValueAsInt()) {
            case EASY:
                difficultyName = "Easy";
                break;
            case MEDIUM:
                difficultyName = "Normal";
                break;
            case HARD:
                difficultyName = "Hard";
                break;
        }
        super.guiTexts.get(2).remove();
        super.guiTexts.get(2).setTextString("Difficulty AI 2: " + difficultyName);
        TextMaster.loadText(super.guiTexts.get(0));
        TextMaster.loadText(super.guiTexts.get(1));
        TextMaster.loadText(super.guiTexts.get(2));
    }
}
