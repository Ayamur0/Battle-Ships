package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.guis.Slider;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class SingleplayerMenu extends MainMenuButton {
    private Slider slider;

    public Slider getSlider() {
        return slider;
    }

    public SingleplayerMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        guiManager.clearClickableGuis();

        TextMaster.clear();

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
    }

    private void createMenu(){
        super.CreateButtonTextures(1);

        slider = new Slider(loader.loadTexture("Brick.jpg"), loader.loadTexture("Brick.jpg"), 5, 30,
                15, new Vector2f(0.3f, 0.01f), new Vector2f(0.5f, 0.5f), guiManager, Inits.getPermanentGuiElements());
        super.guiTexts.add(new GUIText("Back",2.5f, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText(String.format("%d",slider.getValueAsInt()),2.5f, font, new Vector2f(0.5f,0.5f), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        return false;
    }
    public void RefreshSliderValue(){
        super.guiTexts.get(1).remove();
        super.guiTexts.get(1).setTextString(String.format("%d",slider.getValueAsInt()));
       TextMaster.loadText(super.guiTexts.get(1));
    }
    @Override
        protected void clickAction() {
            if (super.buttonClicked == 0){
                slider.remove();
                Inits.setStartMenu(new PlayMenu(guiManager,loader));
            }

    }
}
